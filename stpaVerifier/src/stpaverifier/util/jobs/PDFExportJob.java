package stpaverifier.util.jobs;

import java.net.URL;
import java.util.Observable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import stpaverifier.controller.model.STPAVerifierController;
import stpaverifier.model.ExportInformation;
import xstampp.util.JAXBExportJob;

public class PDFExportJob extends JAXBExportJob {

	private STPAVerifierController controller;

	public PDFExportJob(String name,String path,STPAVerifierController controller, String xslName) {
		super(name, path, xslName);
		this.controller = controller;
	}

	@Override
	protected Observable getModelObserver() {
		return controller;
	}

	@Override
	protected Object getModel() {
		return controller.getExportModel();
	}

	@Override
	protected JAXBContext getModelContent() throws JAXBException {
		return JAXBContext.newInstance(ExportInformation.class);
	}

	@Override
	protected Transformer getxslTransformer(String resource) {
		URL xslUrl = this.getClass().getResource(resource);

		if (xslUrl == null) {
			return null;
		}
		try {
			StreamSource transformXSLSource = new StreamSource(xslUrl.openStream());
			TransformerFactory transfact = TransformerFactory.newInstance();
			transfact.setURIResolver(new URIResolver() {
				@Override
				public Source resolve(String href, String base) {
					return new StreamSource(this.getClass().getClassLoader()
							.getResourceAsStream("/" + href)); //$NON-NLS-1$
				}
			});
			return transfact.newTransformer(transformXSLSource);
		} catch (Exception e) {
			return null;
		}
	}

}
