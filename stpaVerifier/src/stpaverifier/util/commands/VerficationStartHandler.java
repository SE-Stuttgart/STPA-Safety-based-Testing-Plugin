package stpaverifier.util.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import stpaverifier.controller.IVerifierController;


/**
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public class VerficationStartHandler extends AbstractHandler {

	
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String verifier = event.getParameter("stpaVerifier.run.method");
		IVerifierController service =(IVerifierController) PlatformUI.getWorkbench().getService(IVerifierController.class);
		boolean isOneByOne = true;
		if(verifier != null && verifier.equals("normal")){
			isOneByOne = false;
		}
		return service.executeSTPAVerifier(isOneByOne);
	}

}
