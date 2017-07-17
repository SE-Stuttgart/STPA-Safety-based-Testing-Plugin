package xstampp.stpatcgenerator.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import xstampp.stpatcgenerator.Activator;
import xstampp.stpatcgenerator.util.jobs.ExportCSVJob;
import xstampp.ui.wizards.CSVExportPage;

/**
 * This class creates the wizard for export final reportas CSV.
 * 
 * @author Ting Luk-He
 *
 */
public class CSVWizardOfFinalReport extends STPATCGExportWizard {
	public static final String ID = "xstampp.stpatcgenerator.export.final";

	public CSVWizardOfFinalReport() {
		setWindowTitle("Export CSV");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		super.init(workbench, selection);
		CSVExportPage page = new CSVExportPage("Export Final Report", Activator.PLUGIN_ID);
		page.setNameSuggestion("Final_Report_");
		setExportPage(page);
	}

	@Override
	public boolean performFinish() {
		if (super.performFinish()) {
			new ExportCSVJob("Export Final Report...", getPage().getExportPath(), ExportCSVJob.FINAL_REPORT).schedule();
			return true;
		}
		return false;
	}
}
