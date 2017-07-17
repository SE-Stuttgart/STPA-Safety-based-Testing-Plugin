package stpaverifier.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import stpaverifier.Activator;
import stpaverifier.util.jobs.PDFExportJob;
import xstampp.ui.wizards.TableExportPage;
import xstampp.util.JAXBExportJob;

public class PDFWizardofProperties extends STPAVerifierExportWizard  {

	public static final String ID ="stpaVerifier.exportWizard.csv.counterexample";
	
	public PDFWizardofProperties() {
		setWindowTitle("Export PDF");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		super.init(workbench, selection);
		
		TableExportPage page = new TableExportPage(new String[]{"*.pdf"},"Export the Properties as PDF",Activator.PLUGIN_ID);
		page.setNameSuggestion("Properties_PDF_");
		page.setNeedProject(false);
		page.setShowDecorateCSButton(false);
		setExportPage(page);
	}

	@Override
	public boolean performFinish() {
		if(super.performFinish()){
			JAXBExportJob job = new PDFExportJob("Export LTL/CTL pdf...", getPage().getExportPath(),getController(),"/fopProperties.xsl");
			job.setPageFormat(getPage().getPageFormat());
			job.schedule();
			return true;
		}
		return false;
	}

	
}
