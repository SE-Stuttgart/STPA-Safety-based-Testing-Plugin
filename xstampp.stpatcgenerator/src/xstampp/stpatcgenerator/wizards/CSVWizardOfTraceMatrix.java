package xstampp.stpatcgenerator.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import xstampp.stpatcgenerator.Activator;
import xstampp.stpatcgenerator.util.jobs.ExportCSVJob;
import xstampp.ui.wizards.CSVExportPage;

/**
 * This class creates the wizard for export traceability matrix as CSV.
 * 
 * @author Ting Luk-He
 *
 */
public class CSVWizardOfTraceMatrix extends STPATCGExportWizard {
	public static final String ID = "xstampp.stpatcgenerator.exportWizard.csv.traceMatrix";

	public CSVWizardOfTraceMatrix() {
		setWindowTitle("Export CSV");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		super.init(workbench, selection);
		CSVExportPage page = new CSVExportPage("Export Traceability Matrix", Activator.PLUGIN_ID);
		page.setNameSuggestion("Traceability_Matrix_");
		setExportPage(page);
	}

	@Override
	public boolean performFinish() {
		if (super.performFinish()) {
			new ExportCSVJob("Export Traceability Matrix...", getPage().getExportPath(), ExportCSVJob.TRACE_MATRIX)
					.schedule();
			return true;
		}
		return false;
	}
}
