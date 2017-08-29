package xstampp.stpatcgenerator.wizards;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import xstampp.stpatcgenerator.model.smv.GeneratorSMVFile;
import xstampp.stpatcgenerator.ui.views.LogErrorView;
import xstampp.stpatcgenerator.util.TCGeneratorPluginUtils;
import xstampp.stpatcgenerator.util.handler.VerifyWithoutVerifierHandler;
import xstampp.stpatcgenerator.util.jobs.VerifySMVJob;
import xstampp.stpatcgenerator.wizards.pages.VerifyWizardPage;

/**
 * This class creates the configuration wizard for verify with local model
 * checker.
 * 
 * @author Ting Luk-He
 *
 */
public class VerifyWizard extends Wizard {
	private VerifyWizardPage page;
	String message;
	Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	String nusmvPath;

	public VerifyWizard() {
		page = new VerifyWizardPage("Verify Configuration");
		this.addPage(this.page);
	}

	@Override
	public boolean performFinish() {
		nusmvPath = page.getNuSMVpath();
		if (nusmvPath==null)
		{
			nusmvPath="/usr/local/NuSMV/bin";
		}
		if (nusmvPath != null) {
			TCGeneratorPluginUtils.writeLocationToFile(TCGeneratorPluginUtils.NUSMV_LOCATION_FILE, nusmvPath);
			String lastString = nusmvPath.substring(nusmvPath.length() - 5);
			if (lastString.equals("NuSMV")) {
				VerifySMVJob job = new VerifySMVJob("Verify SMV...", nusmvPath, GeneratorSMVFile.getSmvIFilePath());
				job.schedule();
				return true;
			} else {
				MessageDialog.openError(shell, "Error",
						"Please check the path of NuSMV: An executable file with the name NuSMV must be chosen.");
				return false;
			}
		} else {
			MessageDialog.openError(shell, "Error", "Please check the path of NuSMV.");
			return false;
		}
	}
}
