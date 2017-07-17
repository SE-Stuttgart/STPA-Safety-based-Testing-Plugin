package xstampp.stpatcgenerator.util.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;

import xstampp.stpatcgenerator.ui.editors.EFSMTreeGraphEditor;
import xstampp.stpatcgenerator.ui.editors.TCGeneratorEditorInput;
import xstampp.stpatcgenerator.util.TCGeneratorPluginUtils;

/**
 * The handler for opening efsm tree graph editor.
 * @author Ting Luk-He
 *
 */
public class OpenEFSMTreeGraphEditorHandler extends AbstractHandler{
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TCGeneratorEditorInput input = (TCGeneratorEditorInput) TCGeneratorPluginUtils.getInputMap().get(EFSMTreeGraphEditor.ID);
		if(input != null){
			TCGeneratorPluginUtils.openEditor(event, EFSMTreeGraphEditor.ID, input);
		}else{
			input = new TCGeneratorEditorInput(EFSMTreeGraphEditor.ID);
			TCGeneratorPluginUtils.getInputMap().put(EFSMTreeGraphEditor.ID, input);
			TCGeneratorPluginUtils.openEditor(event, EFSMTreeGraphEditor.ID, input);
		}		
		return null;
	}
}
