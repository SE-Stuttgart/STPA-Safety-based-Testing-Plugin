package stpaverifier.util.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

import stpaverifier.controller.IProperty;
import stpaverifier.controller.IVerifierController;
import stpaverifier.model.ICounterexample;
import stpaverifier.ui.views.FormularViewPart;

/**
 * removes the formula from the data model which is mapped to this id
 * the data model than triggers an update of the table input
 * 
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public class RemovePropertyHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
									     .getSelectionService().getSelection(FormularViewPart.ID);
		IVerifierController service =(IVerifierController) PlatformUI.getWorkbench().getService(IVerifierController.class);
		service.removeChecked();
		if(selection != null){
			for(Object property	: ((IStructuredSelection)selection).toArray()){
				if(property instanceof IProperty){
					service.removeProperty(((IProperty) property).getUUID());
				}else if(property instanceof ICounterexample){
					service.removeProperty(((ICounterexample) property).getPropertyID());
				}
			}
		}
		return true;
	}

}
