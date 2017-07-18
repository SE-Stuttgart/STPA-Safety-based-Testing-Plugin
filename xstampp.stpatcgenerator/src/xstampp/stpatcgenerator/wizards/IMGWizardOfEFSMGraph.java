package xstampp.stpatcgenerator.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import xstampp.stpatcgenerator.Activator;
import xstampp.stpatcgenerator.ui.editors.EFSMTreeGraphEditor;
import xstampp.stpatcgenerator.util.jobs.ExportImageJob;
import xstampp.ui.wizards.TableExportPage;

/**
 * This class creates the wizard for export EFSM graph as image.
 * 
 * @author Ting Luk-He
 *
 */
public class IMGWizardOfEFSMGraph extends STPATCGExportWizard {
	public static final String ID = "xstampp.stpatcgenerator.export.efsmGraph";

	public IMGWizardOfEFSMGraph() {
		setWindowTitle("Export the Stateflow Tree as Image");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		super.init(workbench, selection);

		TableExportPage page = new TableExportPage(new String[] { "*.png", "*.jpg" }, "Stateflow Tree",
				Activator.PLUGIN_ID);
		page.setNameSuggestion("EFSM_Graph_");
		page.setNeedProject(false);
		page.setShowTextConfig(false);
		setExportPage(page);
	}

	@Override
	public boolean performFinish() {
		if (super.performFinish()) {
			ExportImageJob job = new ExportImageJob("Export Image...", getController(), getPage().getExportPath(),
					EFSMTreeGraphEditor.class, EFSMTreeGraphEditor.ID);
			job.setShowPreview(true);
			job.schedule();
			return true;
		}
		return false;
	}
}
