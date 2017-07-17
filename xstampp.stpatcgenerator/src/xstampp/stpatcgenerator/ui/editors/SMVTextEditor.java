package xstampp.stpatcgenerator.ui.editors;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.services.ISourceProviderService;

import xstampp.ui.menu.file.commands.CommandState;
/**
 * This class contains the function of smv text editor.
 * @author Ting
 *
 */
public class SMVTextEditor extends TextEditor{
	public static final String ID = "xstampp.stpatcgenerator.editor.smvTextEditor";
	
	public SMVTextEditor(){
		super();
	}
	
	@Override
	public boolean isDirty() {
		boolean dirty= super.isDirty();
		// Enable the save entries in the menu
		ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI
				.getWorkbench().getService(ISourceProviderService.class);
		CommandState saveStateService = (CommandState) sourceProviderService
				.getSourceProvider(CommandState.SAVE_STATE);
		saveStateService.isSaveable(dirty, this);
		return dirty;
	}
	
}
