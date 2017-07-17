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
import stpaverifier.model.NuSMVCounterexample;
import stpaverifier.model.properties.ModelProperty;
import stpaverifier.util.NuSMVCMDScanner;

/**
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public class NuSMVRunJob extends NuSMVCMDScanner {


	public NuSMVRunJob(String name, STPAVerifierController dataModelController, IProperty formular) {
		super(name, dataModelController, formular);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		if(getModel() == null || (!checkCTL && !checkLTL)){
			return Status.CANCEL_STATUS;
		}
		IFile modelResource = getModelResource();
		
		if(getProperty().equals(ModelProperty.NULL_SID)){
			String[] arguments = NuSMVArgumentHandler.getNuSMVArgs(getModel().getFile().getName());
			if(arguments != null){
				inheritIO(modelResource.getParent(), arguments, getConsole(), 0);
			}
		}else{
			IFile checkFile = getModel().getFile().getProject().getFile(".cmd_args");
			
			try {
				if(checkFile.exists()){
					checkFile.delete(true, false, monitor);
				}
				checkFile.create(getModel().getFile().getContents(), true, monitor);
				BufferedWriter file = new BufferedWriter(new FileWriter(checkFile.getLocation().toFile()));
				String[] arguments = NuSMVArgumentHandler.getNuSMV_CHECK_FILE_Args(true, getProperty());
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
				
				arguments = NuSMVArgumentHandler.getNuSMV_INIT_Args(getModel().getFile().getLocation().toOSString(),checkFile.getLocation().toOSString());
				if(arguments != null){
					getModel().setPropertyState(getPropertyID(), 
							inheritIO(modelResource.getParent(), arguments, getConsole(), 0));
				}
				this.setCounterexample(new NuSMVCounterexample(getCounterexample(),getPropertyLiteral(),getProperty()));
				this.getModel().addResult(getVerifyResult());
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
