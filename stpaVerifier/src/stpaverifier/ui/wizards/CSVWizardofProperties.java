package stpaverifier.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import stpaverifier.Activator;
import stpaverifier.util.jobs.ExportVerifierCSVJob;
import xstampp.ui.wizards.CSVExportPage;

public class CSVWizardofProperties extends STPAVerifierExportWizard  {

	public static final String ID ="stpaVerifier.exportWizard.csv.counterexample";
	
	public CSVWizardofProperties() {
		setWindowTitle("Export CSV");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		super.init(workbench, selection);
		CSVExportPage page = new CSVExportPage("Export LTL/CTL Properties",Activator.PLUGIN_ID);
		page.setNameSuggestion("Properties_");
		setExportPage(page);
	}

	@Override
	public boolean performFinish() {
		if(super.performFinish()){
			new ExportVerifierCSVJob("Export Results...", getController(), getPage().getExportPath(),
										ExportVerifierCSVJob.PROPERTIES).schedule();
			return true;
		}
		return false;
	}


}
