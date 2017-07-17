package stpaverifier.util.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import stpaverifier.ui.views.CounterexampleViewPart;

public class ExportCECommand extends AbstractHandler {


	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
									.getActivePage().findView(CounterexampleViewPart.ID);
		if(view == null){
			System.err.println("Command can only be executed from a valid view");
			return null;
		}
		DirectoryDialog diag = new DirectoryDialog(view.getSite().getShell());
		diag.open();
		return null;
	}

}
