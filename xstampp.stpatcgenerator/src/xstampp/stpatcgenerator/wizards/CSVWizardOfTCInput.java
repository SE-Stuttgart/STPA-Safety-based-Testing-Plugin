package xstampp.stpatcgenerator.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import xstampp.stpatcgenerator.Activator;
import xstampp.stpatcgenerator.util.jobs.ExportCSVJob;
import xstampp.ui.wizards.CSVExportPage;

/**
 * This class creates the wizard for export test case input data as CSV.
 * @author Ting Luk-He
 *
 */
public class CSVWizardOfTCInput extends STPATCGExportWizard{
	public static final String ID ="xstampp.stpatcgenerator.exportWizard.csv.tcInput";
	
	public CSVWizardOfTCInput() {
		setWindowTitle("Export CSV");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		super.init(workbench, selection);
		CSVExportPage page = new CSVExportPage("Export Test Case Input Variables and Configuration",Activator.PLUGIN_ID);
		page.setNameSuggestion("Test_Case_Input_And_Config_");
		setExportPage(page);
	}

	@Override
	public boolean performFinish() {
		if(super.performFinish()){
			new ExportCSVJob("Export Test Case Input Variables and Configuration...", getPage().getExportPath(),
										ExportCSVJob.TEST_INPUT_AND_CONFIG).schedule();
			return true;
		}
		return false;
	}
}
