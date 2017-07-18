package xstampp.stpatcgenerator.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import xstampp.stpatcgenerator.Activator;
import xstampp.stpatcgenerator.ui.editors.StateflowTreeGraphEditor;
import xstampp.stpatcgenerator.ui.views.StateFlowTreeGraphView;
import xstampp.stpatcgenerator.util.jobs.ExportImageJob;
import xstampp.ui.wizards.TableExportPage;

/**
 * This class creates the wizard for export stateflow tree graph as image.
 * 
 * @author Ting Luk-He
 *
 */
public class IMGWizardOfStateflowTreeGraph extends STPATCGExportWizard {
	public static final String ID = "xstampp.stpatcgenerator.export.stateFlowTreeGraph";
	// private StateFlowTreeGraphView view = (StateFlowTreeGraphView)
	// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(StateFlowTreeGraphView.ID);

	public IMGWizardOfStateflowTreeGraph() {
		setWindowTitle("Export State Flow Tree as Image");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		super.init(workbench, selection);

		TableExportPage page = new TableExportPage(new String[] { "*.png", "*.jpg" }, "Stateflow Tree",
				Activator.PLUGIN_ID);
		page.setNameSuggestion("Stateflow_Tree_Graph_");
		page.setNeedProject(false);
		page.setShowTextConfig(false);
		setExportPage(page);
	}

	@Override
	public boolean performFinish() {
		if (super.performFinish()) {
			ExportImageJob job = new ExportImageJob("Export Image...", getController(), getPage().getExportPath(),
					StateflowTreeGraphEditor.class, StateflowTreeGraphEditor.ID);
			job.setShowPreview(true);
			job.schedule();
			return true;
		}
		return false;
	}
}
