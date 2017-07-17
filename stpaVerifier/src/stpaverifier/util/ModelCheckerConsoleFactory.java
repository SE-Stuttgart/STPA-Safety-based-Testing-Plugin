package stpaverifier.util;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IOConsole;

public class ModelCheckerConsoleFactory implements IConsoleFactory {

	@Override
	public void openConsole() {
		IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
		IOConsole console = new IOConsole("Hello Console", null);
	    consoleManager.addConsoles(new IConsole[]{console});
	    consoleManager.showConsoleView(console);
	}

}
