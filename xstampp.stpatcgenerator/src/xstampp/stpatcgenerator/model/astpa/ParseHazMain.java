package xstampp.stpatcgenerator.model.astpa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.xml.sax.SAXException;

import xstampp.astpa.model.DataModelController;

public class ParseHazMain {

	public static void main(String[] args) {
		try {
			URL schemaFile;
			String file = null;
			for (String arg : args) {
				if (arg.endsWith(".hazx") || arg.endsWith(".haz")) {
					file = arg;
				}
			}
			if (file == null) {
				return;
			}
			schemaFile = ParseHazMain.class.getResource("/hazschema.xsd"); //$NON-NLS-1$
			File xmlFile = new File(file);
			if (!xmlFile.exists()) {
				return;
			}
			BufferedReader reader = new BufferedReader(new FileReader(xmlFile));
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
			Source xmlSource = new StreamSource(stream, xmlFile.toURI()
					.toASCIIString());

			SchemaFactory schemaFactory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(schemaFile);

			Validator validator = schema.newValidator();
			validator.validate(xmlSource);
			System.setProperty(
					"com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize", "true"); //$NON-NLS-1$ //$NON-NLS-2$
			JAXBContext context = JAXBContext
					.newInstance(DataModelController.class);

			Unmarshaller um = context.createUnmarshaller();
			StringReader stringReader = new StringReader(buffer.toString());

			// DataModelController with all informations stored in the haz/hazx
			// file
//			DataModelController dataModel = (DataModelController) um
//					.unmarshal(stringReader);
//			dataModel.getCausalComponents();
//			for (ProcessModelValue value : dataModel.fetchProcessComponents()) {
//				System.out.print(value.getController() + " | ");
//				System.out.print(value.getPM() + " | ");
//				System.out.print(value.getPMV() + " | ");
//				System.out.println(value.getValueText());
//			}
//			for (RefinedSafetyRule entry : dataModel.getAllRefinedRules()) {
//				System.out.println("RSR1." + entry.getNumber());
//				System.out.println("  - Critical Combinations: "
//						+ entry.getCriticalCombies());
//				System.out.println("  - LTL: " + entry.getLtlProperty());
//				System.out.println("  - Rule: " + entry.getSafetyRule());
//				System.out.println("  - Refined unsafe control action: "
//						+ entry.getRefinedUCA());
//				System.out.println("  - Refined safety constraint: "
//						+ entry.getRefinedSafetyConstraint());
//			}
			reader.close();
			System.getProperties().remove(
					"com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize"); //$NON-NLS-1$

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

}
