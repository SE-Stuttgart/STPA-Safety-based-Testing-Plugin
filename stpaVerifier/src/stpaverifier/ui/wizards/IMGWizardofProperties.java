package stpaverifier.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import stpaverifier.Activator;
import stpaverifier.util.jobs.PDFExportJob;
import xstampp.ui.wizards.TableExportPage;
import xstampp.util.JAXBExportJob;

public class IMGWizardofProperties extends STPAVerifierExportWizard  {

	public static final String ID ="stpaVerifier.exportWizard.csv.counterexample";
	
	public IMGWizardofProperties() {
		setWindowTitle("Export the Properties Image");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		super.init(workbench, selection);
		
		TableExportPage page = new TableExportPage(new String[]{"*.png"},"Properties Image",Activator.PLUGIN_ID);
		page.setNameSuggestion("Properties_Image_");
		page.setNeedProject(false);
		page.setShowDecorateCSButton(false);
		setExportPage(page);
	}

	@Override
	public boolean performFinish() {
		if(super.performFinish()){
			JAXBExportJob job = new PDFExportJob("Export LTL/CTL image...", getPage().getExportPath(),getController(),"/fopProperties.xsl");
			job.setPageFormat(getPage().getPageFormat());
			job.schedule();
			return true;
		}
		return false;
	}

	
}
