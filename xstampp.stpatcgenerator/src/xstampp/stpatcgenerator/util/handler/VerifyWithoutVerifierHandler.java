package xstampp.stpatcgenerator.util.handler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;













import xstampp.stpatcgenerator.model.smv.GeneratorSMVFile;
import xstampp.stpatcgenerator.ui.views.LogErrorView;
import xstampp.stpatcgenerator.util.TCGeneratorPluginUtils;
import xstampp.stpatcgenerator.wizards.ConfigurationWizard;
import xstampp.stpatcgenerator.wizards.VerifyWizard;
//import stpaverifier.ModelCheckerPerspective;
//import stpaverifier.util.commands.OpenCloseSTPAVerifier;
import xstampp.util.STPAPluginUtils;

/**
 * A handler for verify smv with local model checker.
 * @author Ting Luk-He
 *
 */
public class VerifyWithoutVerifierHandler extends AbstractHandler{
	
	public final static String commandId = "xstampp.stpatcgenerator.command.verify";
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (GeneratorSMVFile.getSmvFile() != null){
			VerifyWizard wizard = new VerifyWizard();
			//get shell
			Shell parent = PlatformUI.getWorkbench().getDisplay().getActiveShell();   
			WizardDialog dialog = new WizardDialog(parent, wizard);
			dialog.open();
		} else {		
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageDialog.openError(shell, "Error", "Please generate SMV file at first and then click verify.");
		}
		
		return null;

	}
	
	

}
