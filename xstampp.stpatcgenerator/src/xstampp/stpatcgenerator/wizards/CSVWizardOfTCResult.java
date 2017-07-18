package xstampp.stpatcgenerator.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import xstampp.stpatcgenerator.Activator;
import xstampp.stpatcgenerator.util.jobs.ExportCSVJob;
import xstampp.ui.wizards.CSVExportPage;

/**
 * This class creates the wizard for export test case result table as CSV.
 * 
 * @author Ting Luk-He
 *
 */
public class CSVWizardOfTCResult extends STPATCGExportWizard {

	public static final String ID = "xstampp.stpatcgenerator.exportWizard.csv.tcResult";

	public CSVWizardOfTCResult() {
		setWindowTitle("Export CSV");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		super.init(workbench, selection);
		CSVExportPage page = new CSVExportPage("Export Test Case Result", Activator.PLUGIN_ID);
		page.setNameSuggestion("Test_Case_Result_");
		setExportPage(page);
	}

	@Override
	public boolean performFinish() {
		if (super.performFinish()) {
			new ExportCSVJob("Export Test Case Result...", getPage().getExportPath(), ExportCSVJob.Test_Result)
					.schedule();
			return true;
		}
		return false;
	}
}
