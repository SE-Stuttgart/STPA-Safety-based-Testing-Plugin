package xstampp.stpatcgenerator.util.handler;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import xstampp.stpatcgenerator.wizards.ConfigurationWizard;

/**
 * The handler for closing STPA TCGenerator.
 * @author Ting Luk-He
 *
 */
public class CloseTCGeneratorHandler extends AbstractHandler{
	private static final Logger LOGGER = Logger.getRootLogger();
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.closeAllEditors(true);
//		IViewPart view = (IViewPart) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
//		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(view);
		
		setDefaultPerspective();
		return null;
	}
	
	public void setDefaultPerspective() {
		CloseTCGeneratorHandler.LOGGER.debug("Setup Default Perspective"); //$NON-NLS-1$
		IPerspectiveDescriptor descriptor = PlatformUI.getWorkbench()
				.getPerspectiveRegistry()
				.findPerspectiveWithId("xstampp.defaultPerspective"); //$NON-NLS-1$
		if (descriptor != null) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().setPerspective(descriptor);
		}
	}

}
