package xstampp.stpatcgenerator.util.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import xstampp.stpatcgenerator.model.smv.GeneratorSMVFile;
import xstampp.stpatcgenerator.wizards.VerifyWizard;
import xstampp.util.STPAPluginUtils;

/**
 * A handler for verify smv with STPA Verifier.
 * @author Ting Luk-He
 *
 */
public class VerifyWithVerifierHandler extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// open STPA Verifier
//		OpenCloseSTPAVerifier.perspective = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective().getId();
//		String toOpen = ModelCheckerPerspective.ID;
//		OpenCloseSTPAVerifier.extReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
//		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(true);
//		if(OpenCloseSTPAVerifier.verifierReferences != null && OpenCloseSTPAVerifier.acquireIntLock()){
//			for (IEditorReference ref : OpenCloseSTPAVerifier.verifierReferences) {
//				IEditorPart part = ref.getEditor(true);
//			}
//			OpenCloseSTPAVerifier.verifierReferences = null;
//			OpenCloseSTPAVerifier.intLock = false;
//		}
//		if(toOpen != null){
//			Map<String,String> values = new HashMap<>();
//			values.put("org.eclipse.ui.perspectives.showPerspective.perspectiveId", toOpen);
//			STPAPluginUtils.executeParaCommand("org.eclipse.ui.perspectives.showPerspective", values);
//		}
		if (GeneratorSMVFile.getSmvFile() != null){
			Map<String, String> values = new HashMap<String, String>();
			values.put("openCloseDirection", "open");
//			String root = GeneratorSMVFile.getRoot().getLocation().toString();
//			String smvPath = root + "/stpatcgenerator/files/" + GeneratorSMVFile.getSmvFilePath();
//			if(smvPath != null){
//				values.put("stpaVerifier.commandParameter.openFile", smvPath);
//			}		
			boolean fail = (boolean) STPAPluginUtils.executeParaCommand("stpaVerifier.command.openClose", values);
			if(fail){
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				MessageDialog.openInformation(shell, "Information", "Please install the STPA-Verifier plug-in via 'Help -> install new Software...' from the web site http://www.xstampp.de/STPAVerifier.html");
			}
		} else {		
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageDialog.openError(shell, "Error", "Please generate SMV file at first and then click verify.");
		}
		
		return null;
	}

}
