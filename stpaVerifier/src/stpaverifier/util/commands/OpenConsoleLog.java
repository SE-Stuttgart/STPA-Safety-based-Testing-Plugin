package stpaverifier.util.commands;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.PlatformUI;

import stpaverifier.controller.IVerifierController;

public class OpenConsoleLog extends AbstractHandler {

	public OpenConsoleLog() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IVerifierController service =(IVerifierController) PlatformUI.getWorkbench().getService(IVerifierController.class);
		IFile logFile = service.getLog();
		if(logFile != null){
			if (logFile.exists()) {
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().open(new File(logFile.getLocationURI()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

}
