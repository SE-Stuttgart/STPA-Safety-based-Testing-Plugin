package stpaverifier.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import stpaverifier.Activator;
import stpaverifier.util.jobs.PDFExportJob;
import xstampp.ui.wizards.PdfExportPage;
import xstampp.util.JAXBExportJob;

public class PDFWizardFinal extends STPAVerifierExportWizard  {

	public static final String ID ="stpaVerifier.exportWizard.csv.counterexample";
	
	public PDFWizardFinal() {
		setWindowTitle("Export Final Report");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		super.init(workbench, selection);
		
		PdfExportPage page = new PdfExportPage("Export the Final Report as PDF","",Activator.PLUGIN_ID);
		page.setFilterExtensions(new String[]{"*.pdf"},new String[]{"Stpa Verifier PDF"});
		page.setNameSuggestion("StpaVerifierReport_");
		page.setNeedProject(false);
		page.setShowDecorateCSButton(false);
		setExportPage(page);
	}


	@Override
	public boolean performFinish() {
		if(super.performFinish()){
			JAXBExportJob job = new PDFExportJob("Export Results pdf...", getPage().getExportPath(),getController(),"/fopxsl.xsl");
			job.setPageFormat(getPage().getPageFormat());
			job.schedule();
			return true;
		}
		return false;
	}

	
}
