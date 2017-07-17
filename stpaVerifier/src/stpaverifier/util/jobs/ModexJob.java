package stpaverifier.util.jobs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import stpaverifier.ui.ValueDefinitionShell;
import xstampp.model.ISafetyDataModel;

public class ModexJob extends LoggerJob {

	private String org_sourcePath;
	private String modexPath;
	private String projectName;
	private List<String> defineEnumValues;
	private IFile modexResult;
	private ISafetyDataModel stpaData;
	public ModexJob(String name,String sourceFile,String path,String projectName,ISafetyDataModel stpaData) {
		super(name,null);
		this.org_sourcePath = sourceFile;
		this.modexPath = path;
		this.projectName = projectName;
		this.stpaData = stpaData;
		this.defineEnumValues = new ArrayList<>();
	}


	@Override
	protected IStatus run(IProgressMonitor monitor) {

		if(org_sourcePath != null && new File(org_sourcePath).exists()){
			try {
				File fModel = new File(org_sourcePath);
				
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				IWorkspaceRoot root = workspace.getRoot();
				String sName = fModel.getName();
				IProject project  = root.getProject(projectName);
				IFolder modexRun = project.getFolder("modex");
				if(project.getLocation() != null && !project.getLocation().toFile().exists() && project.exists()){
					project.delete(true, null);
				}
				IFile sourceFile = modexRun.getFile(sName);
			
				//at this point, no resources have been created
				if (!project.exists()){
					project.create(null);
				}
				if (!project.isOpen()) project.open(null);
				
				if(!modexRun.exists()){
					modexRun.create(true, true, null);
				}else{
					modexRun.delete(true, monitor);
					modexRun.create(true, true, null);
					
				}

				sourceFile.delete(true,null);
				/*
				 * The chosen c Source is copied into a new file located in the modex run directory 
				 */
				BufferedWriter modexWriter = new BufferedWriter(new FileWriter(sourceFile.getLocation().toFile()));
				BufferedReader sourceReader = new BufferedReader(new FileReader(fModel));
				try{
					String line = sourceReader.readLine();
					while(line != null){
						if(line.toLowerCase().startsWith("#define")){
							this.defineEnumValues.add(line.toLowerCase());
						}
						modexWriter.write(line);
						modexWriter.append('\n');
						line = sourceReader.readLine();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				sourceReader.close();
				modexWriter.close();
				
//				if(!System.getProperty("os.name").toLowerCase().contains("win")){
//					modexPath = "./"+modexPath;
//				}
				inheritIO(modexRun,	new String[]{modexPath,sName}, getConsole()	,0);

				File model_decl_File = new File(modexRun.getLocation().toOSString(),"model");
				
				if(defineEnumValues == null || defineEnumValues.isEmpty() && stpaData != null){
					Display.getDefault().syncExec(new Runnable() {
						
						@Override
						public void run() {
							Shell parent =PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
							defineEnumValues = new ValueDefinitionShell().open(parent,stpaData.getValuesTOVariables());
						}
					});
				}
				/*
				 * since the model is stored in a file named 'model' it is copied in a new file located in the project
				 * root
				 */
				if(model_decl_File.exists()){
					File projectModel= new File(project.getFile(projectName + ".pml").getLocationURI());
					BufferedWriter completeModelWriter = new BufferedWriter(new FileWriter(projectModel));
					BufferedReader decl_reader = new BufferedReader(new FileReader(model_decl_File));
					try{
						
						String line = decl_reader.readLine();
						boolean isHead= true;
						while(line != null){
							if(isHead && !line.startsWith("//")){
								isHead = false;
								completeModelWriter.append('\n');
								for(String define: this.defineEnumValues){
									completeModelWriter.append(define);
									completeModelWriter.append('\n');
								}
								completeModelWriter.append('\n');
							}
							completeModelWriter.write(line);
							completeModelWriter.append('\n');
							line = decl_reader.readLine();
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					completeModelWriter.close();
					decl_reader.close();
					
				}
				modexRun.delete(true, monitor);
				modexResult = project.getFile(projectName + ".pml");
				return Status.OK_STATUS;
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return Status.CANCEL_STATUS;
	}


	@Override
	public int scanLine(String line, int current) {
		return 0;
	}

	public IFile getModexResult() {
		return this.modexResult;
	}
}
