package stpaverifier.util.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import stpaverifier.controller.IProperty;
import stpaverifier.controller.model.STPAVerifierController;
import stpaverifier.util.SpinCMDScanner;

public class CheckSpinLTLJob extends SpinCMDScanner {

	public CheckSpinLTLJob(String name, STPAVerifierController dataModelController,
			IProperty formular) {
		super(name, dataModelController, formular);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		String[] command = new String[]{
				"spin","-f","!("+getProperty()+")"
		};
		int resultState = inheritIO(null, command, getConsole(), 0);
		if((resultState & IProperty.STATE_SYNTAX_ERROR) != 0){
			getModel().setPropertyState(getPropertyID(), IProperty.STATE_SYNTAX_ERROR);
		}else{
			getModel().setPropertyState(getPropertyID(), IProperty.STATE_SYNTAX_CORRECT);
		}
		
		return Status.OK_STATUS;
	}

}
