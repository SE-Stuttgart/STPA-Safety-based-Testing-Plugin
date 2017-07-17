package stpaverifier.util.commands;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;

import stpaverifier.ModelCheckerPerspective;
import stpaverifier.ui.editors.AbstractModelEditor;
import xstampp.DefaultPerspective;
import xstampp.util.STPAPluginUtils;

public class OpenCloseSTPAVerifier extends AbstractHandler {
	public static String ID="stpaVerifier.command.openClose"; //$NON-NLS-1$ 
	public static String openClosePara="openCloseDirection"; //$NON-NLS-1$ 
	public static String openClosePara_OPEN="open"; //$NON-NLS-1$ 
	public static String openClosePara_CLOSE="close"; //$NON-NLS-1$ 
	public static String perspective;
	public static IEditorReference[] verifierReferences;
	public static boolean intLock;
	public static IEditorReference[] extReferences;
	public static boolean extLock;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String direction = event.getParameter("openCloseDirection");
		String toOpen = null;

		if(direction.equals("open")){
			perspective = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective().getId();
			toOpen = ModelCheckerPerspective.ID;
			extReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(true);
			if(verifierReferences != null && acquireIntLock()){
				for (IEditorReference ref : verifierReferences) {
					IEditorPart part = ref.getEditor(true);
				}
				verifierReferences = null;
				intLock = false;
			}
		}else{
			if(perspective != null){
				toOpen = new String(perspective);
			}else{
				toOpen = DefaultPerspective.ID;
			}
			verifierReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(true);
			if(extReferences != null && acquireExtLock()){
				for (IEditorReference ref : extReferences) {
					ref.getEditor(true);
				}
				extReferences = null;
				extLock = false;
			}
			perspective= null;
			
		}
		
		if(toOpen != null){
			Map<String,String> values = new HashMap<>();
			values.put("org.eclipse.ui.perspectives.showPerspective.perspectiveId", toOpen);
			STPAPluginUtils.executeParaCommand("org.eclipse.ui.perspectives.showPerspective", values);
		}
		
		return null;
	}

	public static synchronized boolean acquireIntLock(){
		if(!intLock){
			intLock = true;
			return true;
		}
		return false;
	}
	public static synchronized boolean acquireExtLock(){
		if(!extLock){
			extLock = true;
			return true;
		}
		return false;
	}
}
