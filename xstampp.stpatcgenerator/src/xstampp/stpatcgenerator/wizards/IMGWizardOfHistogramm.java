package xstampp.stpatcgenerator.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import xstampp.stpatcgenerator.Activator;
import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.ui.views.TestCaseHistogrammView;
import xstampp.stpatcgenerator.util.jobs.ExportImageJob;
import xstampp.ui.wizards.TableExportPage;

/**
 * This class creates the wizard for export histogram as image.
 * 
 * @author Ting Luk-He
 *
 */
public class IMGWizardOfHistogramm extends STPATCGExportWizard {
	public static final String ID = "xstampp.stpatcgenerator.export.histogramm";

	public IMGWizardOfHistogramm() {
		setWindowTitle("Export the Histogramm of SSR Tracability Statistic as Image");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		super.init(workbench, selection);

		TableExportPage page = new TableExportPage(new String[] { "*.png", "*.jpg" },
				"Histogramm of SSR Tracability Statistic", Activator.PLUGIN_ID);
		page.setNameSuggestion("SSR_Traceability_Statistic_");
		page.setNeedProject(false);
		page.setShowTextConfig(false);
		setExportPage(page);
	}

	@Override
	public boolean performFinish() {
		if (super.performFinish()) {
			ExportImageJob job = new ExportImageJob("Export Image...", getController(), getPage().getExportPath(),
					TestCaseHistogrammView.class, TestCaseHistogrammView.ID,
					STPATCGModelController.getTcHistrogrammView().getChart());
			job.setShowPreview(true);
			job.schedule();
			return true;
		}
		return false;
	}
}
