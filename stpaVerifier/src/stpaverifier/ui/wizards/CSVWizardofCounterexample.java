package stpaverifier.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import stpaverifier.Activator;
import stpaverifier.util.jobs.ExportVerifierCSVJob;
import xstampp.ui.wizards.CSVExportPage;

public class CSVWizardofCounterexample extends STPAVerifierExportWizard  {

	public static final String ID ="stpaVerifier.exportWizard.csv.counterexample";
	
	public CSVWizardofCounterexample() {
		setWindowTitle("Export CSV");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		super.init(workbench, selection);
		CSVExportPage page = new CSVExportPage("Export Counterexample",Activator.PLUGIN_ID);
		page.setNameSuggestion("Counterexample_");
		setExportPage(page);
	}

	@Override
	public boolean performFinish() {
		if(super.performFinish()){
			ExportVerifierCSVJob exportVerifierCSVJob = new ExportVerifierCSVJob("Export Counterexamle...", getController(), getPage().getExportPath(),
										ExportVerifierCSVJob.COUNTEREXAMPLE);
			exportVerifierCSVJob.schedule();
			
			return true;
		}
		return false;
	}

	
}
