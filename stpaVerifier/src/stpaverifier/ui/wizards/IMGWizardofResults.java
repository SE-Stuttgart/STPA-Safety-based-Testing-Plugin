package stpaverifier.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import stpaverifier.Activator;
import stpaverifier.util.jobs.PDFExportJob;
import xstampp.ui.wizards.TableExportPage;
import xstampp.util.JAXBExportJob;

public class IMGWizardofResults extends STPAVerifierExportWizard {

	public static final String ID ="stpaVerifier.exportWizard.csv.counterexample";
	
	public IMGWizardofResults() {
		setWindowTitle("Export the Results image");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		
		super.init(workbench, selection);
		
		TableExportPage page = new TableExportPage(new String[]{"*.png"},"Results image",Activator.PLUGIN_ID);
		page.setNameSuggestion("Results_Image_");
		setExportPage(page);
		
	}

	@Override
	public boolean performFinish() {
		if(super.performFinish()){
			JAXBExportJob job = new PDFExportJob("Export Results image...", getPage().getExportPath(),getController(),"/fopSafetyResults.xsl");
			job.setPageFormat(getPage().getPageFormat());
			job.schedule();
			return true;
		}
		return false;
	}


	
}
