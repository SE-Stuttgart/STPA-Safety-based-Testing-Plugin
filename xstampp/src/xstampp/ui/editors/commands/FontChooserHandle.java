package xstampp.ui.editors.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.interfaces.ITextEditor;
/**
 * this Handler changes the current Font of a text editor
 * this is done by calling {@link ITextEditor#setFont(String, int)}
 * with the value of the parameters<ul>
 * <li> <code>xstampp.commandParameter.fontfamily</Code>
 * <li> <code>xstampp.commandParameter.fontsize</Code>
 * </ul> 
 * @author Lukas Balzer
 * 
 * @see ITextEditor
 * @since 1.0
 */
public class FontChooserHandle extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object activeEditor = PlatformUI.getWorkbench().
				getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(activeEditor instanceof ITextEditor){
			String fontName= event.getParameter("xstampp.commandParameter.fontfamily"); //$NON-NLS-1$
			String fontSize= event.getParameter("xstampp.commandParameter.fontsize");//$NON-NLS-1$
			
			if(fontName != null){
					((ITextEditor) activeEditor).setFont(fontName,0);
			}
			if(fontSize != null){
				try{
					((ITextEditor) activeEditor).setFontSize(ITextEditor.FONT_SIZE, Integer.parseInt(fontSize));
				}catch(NumberFormatException e){
					ProjectManager.getLOGGER().error(this.getClass() + " has recived: " + fontSize + " but expected an Integer");
				}
			}
			
		}
		return null;
	}

}
