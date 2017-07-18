package xstampp.stpatcgenerator.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import xstampp.stpatcgenerator.Activator;
import xstampp.stpatcgenerator.util.jobs.ExportCSVJob;
import xstampp.ui.wizards.CSVExportPage;

/**
 * This class creates the wizard for export safe behavioral model properties
 * table as CSV.
 * 
 * @author Ting Luk-He
 *
 */
public class CSVWizardOfStateFlowProperties extends STPATCGExportWizard {
	public static final String ID = "xstampp.stpatcgenerator.exportWizard.csv.stateflowProperties";

	public CSVWizardOfStateFlowProperties() {
		setWindowTitle("Export CSV");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		super.init(workbench, selection);
		CSVExportPage page = new CSVExportPage("Export Safe Behavioral Model Properties", Activator.PLUGIN_ID);
		page.setNameSuggestion("Safe_Behavioral_Model_Properties_");
		setExportPage(page);
	}

	@Override
	public boolean performFinish() {
		if (super.performFinish()) {
			new ExportCSVJob("Export Safe Behavioral Model Properties...", getPage().getExportPath(),
					ExportCSVJob.STATEFLOW_PROPERTIES).schedule();
			return true;
		}
		return false;
	}
}
