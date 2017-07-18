package xstampp.stpatcgenerator.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import xstampp.stpatcgenerator.Activator;
import xstampp.stpatcgenerator.util.jobs.ExportCSVJob;
import xstampp.ui.wizards.CSVExportPage;

/**
 * This class creates the wizard for export stateflow truth table as CSV.
 * 
 * @author Ting Luk-He
 *
 */
public class CSVWizardOfStateFlowTruthTable extends STPATCGExportWizard {

	public static final String ID = "xstampp.stpatcgenerator.exportWizard.csv.stateflowtruthtable";

	public CSVWizardOfStateFlowTruthTable() {
		setWindowTitle("Export CSV");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		super.init(workbench, selection);
		CSVExportPage page = new CSVExportPage("Export State Flow Truth Table", Activator.PLUGIN_ID);
		page.setNameSuggestion("State_Flow_Truth_Table_");
		setExportPage(page);
	}

	@Override
	public boolean performFinish() {
		if (super.performFinish()) {
			new ExportCSVJob("Export Results...", getPage().getExportPath(), ExportCSVJob.STATEFLOW_TRUTHTABLE)
					.schedule();
			return true;
		}
		return false;
	}
}
