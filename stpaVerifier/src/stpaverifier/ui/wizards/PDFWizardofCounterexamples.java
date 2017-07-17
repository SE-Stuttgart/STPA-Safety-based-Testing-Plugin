package stpaverifier.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import stpaverifier.Activator;
import stpaverifier.util.jobs.PDFExportJob;
import xstampp.ui.wizards.TableExportPage;
import xstampp.util.JAXBExportJob;

public class PDFWizardofCounterexamples extends STPAVerifierExportWizard  {

	public static final String ID ="stpaVerifier.exportWizard.csv.counterexample";
	
	public PDFWizardofCounterexamples() {
		setWindowTitle("Export PDF");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		super.init(workbench, selection);
		
		TableExportPage page = new TableExportPage(new String[]{"*.pdf"},"Export the Counterexamples as PDF",Activator.PLUGIN_ID);
		page.setNameSuggestion("Counterexamples_PDF_");
		page.setNeedProject(false);
		page.setShowDecorateCSButton(false);
		setExportPage(page);
	}

	@Override
	public boolean performFinish() {
		if(super.performFinish()){
			JAXBExportJob job = new PDFExportJob("Export Counterexamples pdf...", getPage().getExportPath(),getController(),"/fopCounterexamples.xsl");
			job.setPageFormat(getPage().getPageFormat());
			job.schedule();
			return true;
		}
		return false;
	}

	
}
