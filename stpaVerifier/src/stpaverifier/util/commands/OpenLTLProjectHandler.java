package stpaverifier.util.commands;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.PlatformUI;

import stpaverifier.ModelCheckerPerspective;
import stpaverifier.controller.IVerifierController;
import xstampp.model.IDataModel;
import xstampp.model.ISafetyDataModel;
import xstampp.ui.navigation.IProjectSelection;
import xstampp.util.STPAPluginUtils;

public class OpenLTLProjectHandler extends AbstractHandler {

	public OpenLTLProjectHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if(!PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective().getId().equals(ModelCheckerPerspective.ID)){
			Map<String,String> values = new HashMap<>();
			values.put(OpenCloseSTPAVerifier.openClosePara, OpenCloseSTPAVerifier.openClosePara_OPEN);
			STPAPluginUtils.executeParaCommand(OpenCloseSTPAVerifier.ID,values);
		}

		IVerifierController service =(IVerifierController) PlatformUI.getWorkbench().getService(IVerifierController.class);
		ISelection selection =PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		
		if(selection != null){
			if(selection instanceof IProjectSelection 
					&& PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.getPerspective().getId().equals(ModelCheckerPerspective.ID)){
				IDataModel controller = (IDataModel) ((IProjectSelection)selection).getProjectData();
				if(controller instanceof ISafetyDataModel){
					service.importData((ISafetyDataModel) controller);
				}
			}
		}
		return null;
	}

}
