package stpaverifier.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import stpaverifier.Activator;
import stpaverifier.util.jobs.PDFExportJob;
import xstampp.ui.wizards.TableExportPage;
import xstampp.util.JAXBExportJob;

public class PDFWizardofResults extends STPAVerifierExportWizard {

	public static final String ID ="stpaVerifier.exportWizard.csv.counterexample";
	
	public PDFWizardofResults() {
		setWindowTitle("Export PDF");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		
		super.init(workbench, selection);
		
		TableExportPage page = new TableExportPage(new String[]{"*.pdf"},"Export the Results as PDF",Activator.PLUGIN_ID);
		page.setNameSuggestion("Results_PDF_");
		page.setNeedProject(false);
		setExportPage(page);
		
	}

	@Override
	public boolean performFinish() {
		if(super.performFinish()){
			JAXBExportJob job = new PDFExportJob("Export Results pdf...", getPage().getExportPath(),getController(),"/fopSafetyResults.xsl");
			job.setPageFormat(getPage().getPageFormat());
			job.schedule();
			return true;
		}
		return false;
	}


	
}
