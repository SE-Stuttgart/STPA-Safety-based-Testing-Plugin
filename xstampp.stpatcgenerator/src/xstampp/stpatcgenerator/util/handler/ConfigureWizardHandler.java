package xstampp.stpatcgenerator.util.handler;

import java.util.UUID;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import xstampp.stpatcgenerator.wizards.ConfigurationWizard;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.navigation.IProjectSelection;
import xstampp.ui.navigation.ProjectExplorer;

/**
 * The handler for the start wizard of STPA TCGenerator to initialize and configure this tool.
 * @author Ting Luk-He
 *
 */

public class ConfigureWizardHandler extends AbstractHandler{
	
	private UUID projectID;	
	private String projectExt;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		this.projectExt = ProjectManager.getContainerInstance().getDataModel(getProjectId()).getFileExtension();
		if(this.projectExt.equals("haz") || this.projectExt.equals("hazx")){
			ConfigurationWizard wizard = new ConfigurationWizard();
			//get shell
			Shell parent = PlatformUI.getWorkbench().getDisplay().getActiveShell();   
			WizardDialog dialog = new WizardDialog(parent, wizard);
			dialog.open();
		}else {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageDialog.openError(shell, "Error", "Please select the project with extension .haz or .hazx !");
		}
		return null;
	}
	
	public UUID getProjectId(){
		IViewPart part =PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(ProjectExplorer.ID);
		if(part != null){
			Object selection = part.getViewSite().getSelectionProvider().getSelection();
			
			if(selection instanceof IProjectSelection){
				projectID = ((IProjectSelection) selection).getProjectId();
			}
		}
		return projectID;
	}
//	private String getProjectType() {
////		for (UUID id : ProjectManager.getContainerInstance().getProjectKeys()) {
////			
////		}
//		String projectType = "";
//		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//		
//	    if (window != null)
//	    {
//	    	System.out.println("window!=null");
//	        IStructuredSelection selection = (IStructuredSelection) window.getSelectionService().getSelection("org.eclipse.jdt.ui.PackageExplorer");
//	        Object firstElement = selection.getFirstElement();
//	        System.out.println("first Element: " + firstElement.toString());
//	        if (firstElement instanceof IAdaptable)
//	        {
//	            IProject project = (IProject)((IAdaptable)firstElement).getAdapter(IProject.class);
////	            IPath path = project.getFullPath();
//	            projectType = project.getFileExtension();
//	        }
//	    }
//		
//		return projectType;
//	}
}
