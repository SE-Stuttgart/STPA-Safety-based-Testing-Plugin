package stpaverifier.controller.model;

import java.io.File;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import stpaverifier.Activator;
import stpaverifier.controller.preferences.spin.SpinPreferenceConstants;
import stpaverifier.util.jobs.ModexJob;

public class ModexController {

	private static final int OVERWRITE= 1 << 3; 
	private String sourcePath;

	/**
	 * This setter sets the path to the source file written in C
	 *  
	 * @param path the path to the c source file
	 * @return whether or not the file exists and if it is a C file
	 */
	public boolean setSourceFile(String path){
		if(path.endsWith(".c")){ //$NON-NLS-1$
			this.sourcePath = path;
			return true;
		}
		return false;
	}
	
	/**
	 * this method is called to trigger the modex parse process
	 * it validates whether all needed information are available
	 * and if the OS is windows whether or not the required
	 * cygwin.dll is set
	 *  
	 */
	public String start(final STPAVerifierController controller){
		String modexPath = Activator.getDefault().getPreferenceStore().getString(SpinPreferenceConstants.PREF_MODEX_PATH);

		if(modexPath == null){
			return "Please define a legal path to a modex executable!";
		}
		if(sourcePath == null){
			return "please define a C-source file from which the model should be extracted!";
		}
		try {
			String catCMD = "cat";
//			if(!System.getProperty("os.name").toLowerCase().contains("win")){
//				catCMD = "./cat";
//			}
			ProcessBuilder procB= new ProcessBuilder(new String[]{catCMD,"--version"});
			Map<String,String> environmentMap = procB.environment();
			//if the current OS is a windows instance than the cygwin dll is required if the path is not set than this
			//method returns false
			if(System.getProperty("os.name").toLowerCase().contains("win")){
				String cygwinDllPath = Activator.getDefault().getPreferenceStore().getString(SpinPreferenceConstants.PREF_CYGWIN_DLL_PATH);
				if(environmentMap != null && cygwinDllPath != null &&  new File(cygwinDllPath).exists()){
					if(!environmentMap.get("PATH").contains(cygwinDllPath)){
						String pathENV = environmentMap.get("PATH");
						environmentMap.put("PATH", pathENV.concat(";"+cygwinDllPath));
					}
				}
			}
			//if the cat command can be executed than the installation is ready to run modex 
			procB.start();
			
		} catch (Exception exc) {
			exc.printStackTrace();
			return "modex can only be ran on a unix platform or a cygwin environment,\n"
					+ "if you are running the stpa model checker on a windows system the cygwin binary directory must be part of your\n"
					+ "PATH variable or must be defined in the above text box!";
		}
		
		final File sourceFile = new File(sourcePath);
		String projectName = sourceFile.getName();
		projectName = projectName.substring(0, projectName.lastIndexOf('.'));
		ProjectValidator validator = new ProjectValidator();
		InputDialog renameDiag = new InputDialog(Display.getCurrent()
				.getActiveShell(),"Rename Project", "A project with this name  allready exists in workspace please choose an other name for the project:",
				projectName, validator){
			@Override
			protected void createButtonsForButtonBar(Composite parent) {
				super.createButtonsForButtonBar(parent);
				Button overwrite = createButton(parent, OVERWRITE	, "Overwrite", false);
				overwrite.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						buttonPressed(IDialogConstants.OK_ID);
						setReturnCode(OVERWRITE);
						close();
					}
				});
			}
		};
		int choice;
		if(validator.isValid(projectName) != null){
			choice = renameDiag.open();
		}else{
			choice = Window.OK;
		}
		if(renameDiag.getValue() != null){
			projectName = renameDiag.getValue();
		}
		if(choice == Window.OK ||(choice == OVERWRITE && MessageDialog.openConfirm(Display.getCurrent().getActiveShell(),
							"Really overwrite the project?", "Do you really want to overwrite the project " + projectName 
							+"\n and lose all files currently stored in it?"))){
			
			final ModexJob job = new ModexJob("Extract Model..", sourcePath, modexPath,projectName,controller.getStpaSafetyData());
			job.schedule();
			job.addJobChangeListener(new JobChangeAdapter(){
				@Override
				public void done(IJobChangeEvent event) {
					if(event.getResult() == Status.OK_STATUS){
						controller.setFile(job.getModexResult());
					}
				}
			});
		}
		
		

		return null;
		
	}
	
	private class ProjectValidator implements IInputValidator{

		@Override
		public String isValid(String newText) {
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(newText);
			if(project.exists()){
				return "Project " + newText +" allready exists in workspace please choose an other name for the project";
			}
			return null;
		}
		
	}
}
