package xstampp.stpatcgenerator.util.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import xstampp.stpatcgenerator.ui.editors.StateflowTreeGraphEditor;
import xstampp.stpatcgenerator.ui.editors.TCGeneratorEditorInput;
import xstampp.stpatcgenerator.ui.views.StateFlowPropertiesView;
import xstampp.stpatcgenerator.ui.views.StateFlowTruthTableView;
import xstampp.stpatcgenerator.ui.views.ValidateSTPAandSBM;
import xstampp.stpatcgenerator.util.TCGeneratorPluginUtils;

/**
 * The handler for opening safe behavioral model tree graph editor.
 * @author Ting Luk-He
 *
 */
public class OpenStateFlowTreeGraphHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TCGeneratorEditorInput input = (TCGeneratorEditorInput) TCGeneratorPluginUtils.getInputMap().get(StateflowTreeGraphEditor.ID);
		openEditor(event, StateflowTreeGraphEditor.ID, input);
		TCGeneratorPluginUtils.showView(StateFlowPropertiesView.ID);
		TCGeneratorPluginUtils.showView(ValidateSTPAandSBM.ID);
		TCGeneratorPluginUtils.showView(StateFlowTruthTableView.ID);
		return null;
	}

	public void openEditor(ExecutionEvent event, String editorId, TCGeneratorEditorInput input) {
		// get the page
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
        IWorkbenchPage page = window.getActivePage();
//        TCGeneratorEditorInput input = new TCGeneratorEditorInput(editorId);
        try {
			page.openEditor(input, editorId);			
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
