package stpaverifier.util.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import stpaverifier.controller.IVerifierController;

/**
 *  Adds an empty LTL formula to the data model
 * the data model than triggers an update of the table input
 * 
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public class AddLTLPropertyHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IVerifierController service =(IVerifierController) PlatformUI.getWorkbench().getService(IVerifierController.class);
		service.addLTLFormular(null, "");
		
		return true;
	}

}
