package xstampp.stpatcgenerator.util.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import xstampp.stpatcgenerator.model.ProjectInformation;
import xstampp.stpatcgenerator.ui.editors.SMVTextEditor;
import xstampp.stpatcgenerator.ui.editors.TCGeneratorEditorInput;
import xstampp.stpatcgenerator.util.TCGeneratorPluginUtils;

/**
 * The handler for generating SMV file and open smv text editor.
 * @author Ting Luk-He
 *
 */
public class GenerateSMVHandler extends AbstractHandler{
	int typeOfUse = ProjectInformation.getTypeOfUse();
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TCGeneratorEditorInput input = (TCGeneratorEditorInput) TCGeneratorPluginUtils.getInputMap().get(SMVTextEditor.ID);
		if(input == null){
			input = new TCGeneratorEditorInput(SMVTextEditor.ID);
			TCGeneratorPluginUtils.getInputMap().put(SMVTextEditor.ID, input);
		}
		if (typeOfUse == 1) {			
			input.runCommand("generate smv without stpa");
		} else if (typeOfUse == 2) {
			input.runCommand("generate smv with stpa");
		}
		
		return null;
	}
}
