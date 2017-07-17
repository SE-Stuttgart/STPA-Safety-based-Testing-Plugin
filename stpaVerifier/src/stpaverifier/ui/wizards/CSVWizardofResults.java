package stpaverifier.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import stpaverifier.Activator;
import stpaverifier.util.jobs.ExportVerifierCSVJob;
import xstampp.ui.wizards.CSVExportPage;

public class CSVWizardofResults extends STPAVerifierExportWizard  {

	public static final String ID ="stpaVerifier.exportWizard.csv.counterexample";
	
	public CSVWizardofResults() {
		setWindowTitle("Export CSV");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		super.init(workbench, selection);
		
		CSVExportPage page = new CSVExportPage("Export Results",Activator.PLUGIN_ID);
		page.setNameSuggestion("Results_");
		setExportPage(page);
	}

	@Override
	public boolean performFinish() {
		if(super.performFinish()){
			ExportVerifierCSVJob exportVerifierCSVJob = new ExportVerifierCSVJob("Export Counterexamle...", getController(), getPage().getExportPath(),
					ExportVerifierCSVJob.RESULTS);
			exportVerifierCSVJob.setSwitchCol_Row(true);
			exportVerifierCSVJob.schedule();
			return true;
		}
		return false;
	}


}
