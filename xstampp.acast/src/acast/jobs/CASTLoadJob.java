package acast.jobs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.Observable;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import messages.Messages;

import org.apache.commons.lang3.StringEscapeUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.xml.sax.SAXException;

import xstampp.model.IDataModel;
import xstampp.util.AbstractLoadJob;
import acast.controller.Controller;

public class CASTLoadJob extends AbstractLoadJob {

	public CASTLoadJob() {
		super();
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Loading ACAST...", 2);

		try {
			// validate the file
			URL schemaFile = this.getClass().getResource("/accschema.xsd"); //$NON-NLS-1$
			BufferedReader reader = new BufferedReader(new FileReader(
					this.getFile()));
			StringBuffer buffer = new StringBuffer();
			String line;

			while ((line = reader.readLine()) != null) {
				if (line.contains("<") || line.contains(">")) { //$NON-NLS-1$ //$NON-NLS-2$
					// gt and lt are replaced by null chars for the time of
					// unescaping
					line = line.replace(">", "\0\0"); //$NON-NLS-1$ //$NON-NLS-2$
					line = line.replace("<", "\0"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				line = StringEscapeUtils.unescapeHtml4(line);
				// now all gt/lt signs left after the unescaping are not part of
				// the xml
				// syntax and get back escaped to assure the correct xml parsing
				line = line.replace("&", "&amp;"); //$NON-NLS-1$ //$NON-NLS-2$
				line = line.replace(">", "&gt;"); //$NON-NLS-1$ //$NON-NLS-2$
				line = line.replace("<", "&lt;"); //$NON-NLS-1$ //$NON-NLS-2$

				line = line.replace("\0\0", ">"); //$NON-NLS-1$ //$NON-NLS-2$
				line = line.replace("\0", "<"); //$NON-NLS-1$ //$NON-NLS-2$
				buffer.append(line);
				buffer.append("\n"); //$NON-NLS-1$
			}
			reader.close();

			Reader stream = new StringReader(buffer.toString());
			Source xmlFile = new StreamSource(stream, getFile().toURI()
					.toASCIIString());

			SchemaFactory schemaFactory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(schemaFile);

			Validator validator = schema.newValidator();
			monitor.worked(1);
			validator.validate(xmlFile);
			System.setProperty(
					"com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize", "true"); //$NON-NLS-1$ //$NON-NLS-2$
			JAXBContext context = JAXBContext.newInstance(Controller.class);

			Unmarshaller um = context.createUnmarshaller();
			StringReader stringReader = new StringReader(buffer.toString());
			this.setController((IDataModel) um.unmarshal(stringReader));
			reader.close();
			monitor.worked(2);
			monitor.done();
			System.getProperties().remove(
					"com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize"); //$NON-NLS-1$

		} catch (SAXException e) {
			this.getLog().error(e.getMessage(), e);
			addErrorMsg(String.format(Messages.InvalidSchemaFile,
					"accschema.xsd")); //$NON-NLS-2$
			return Status.CANCEL_STATUS;
		} catch (IOException e) {
			this.getLog().error(e.getMessage(), e);
			return Status.CANCEL_STATUS;
		} catch (JAXBException e) {
			e.printStackTrace();
			addErrorMsg(String.format(Messages.ThisHazFileIsInvalid, getFile()
					.getName()));
			return Status.CANCEL_STATUS;

		}
		return Status.OK_STATUS;
	}

	@Override
	protected Observable getModelObserver() {
		// TODO Auto-generated method stub
		return null;
	}

}
