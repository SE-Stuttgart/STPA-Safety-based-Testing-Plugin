package stpaverifier.util.jobs;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import stpaverifier.controller.IProperty;
import stpaverifier.controller.model.STPAVerifierController;
import stpaverifier.controller.preferences.NuSMVArgumentHandler;
import stpaverifier.model.properties.ModelProperty;
import stpaverifier.util.NuSMVCMDScanner;

/**
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public class CheckNuSMVLTLJob extends NuSMVCMDScanner {


	/**
	 * 
	 * @param name {@link #getName()}
	 * @param dataModelController the controller which provides the execution information for this job
	 * @param formular the IProperty definition for the property that is to check
	 */
	public CheckNuSMVLTLJob(String name, STPAVerifierController dataModelController, IProperty formular) {
		super(name, dataModelController, formular);
		
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		if(getModel() == null || getModel().getFile() == null || (!checkCTL && !checkLTL)){
			return Status.CANCEL_STATUS;
		}
		IFile modelRessource = getModel().getFile();
		
		if(!getProperty().equals(ModelProperty.NULL_SID)){
			IFile checkFile = getModel().getFile().getProject().getFile(".cmd_args");
			
			try {
				if(checkFile.exists()){
					checkFile.delete(true, false, monitor);
				}
				checkFile.create(getModel().getFile().getContents(), true, monitor);
				BufferedWriter file = new BufferedWriter(new FileWriter(checkFile.getLocation().toFile()));
				String[] arguments = null;
				if(checkCTL){
					arguments = NuSMVArgumentHandler.getNuSMV_CHECK_SYNTAX_Args(false, getProperty());
				}else if(checkLTL){
					arguments = NuSMVArgumentHandler.getNuSMV_CHECK_SYNTAX_Args(true, getProperty());
				}
				boolean first = true;
				for (String string : arguments) {
					
					if(first){
						first=false;
					}else{
						file.newLine();
					}
					file.write(string);
				}
				file.close();
				int state = IProperty.STATE_UNCHECKED;
				arguments = NuSMVArgumentHandler.getNuSMV_INIT_Args(getModel().getFile().getLocation().toOSString(),checkFile.getLocation().toOSString());
				if(arguments != null){
					state =	inheritIO(modelRessource.getParent(), arguments, null, 0);
				}
				if((state & IProperty.STATE_SYNTAX_ERROR)!= 0){
					getModel().setPropertyState(getPropertyID(), IProperty.STATE_SYNTAX_ERROR);
				}else{
					getModel().setPropertyState(getPropertyID(), IProperty.STATE_SYNTAX_CORRECT);
				}
				checkFile.delete(true, false, monitor);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		return Status.OK_STATUS;
	}

}
