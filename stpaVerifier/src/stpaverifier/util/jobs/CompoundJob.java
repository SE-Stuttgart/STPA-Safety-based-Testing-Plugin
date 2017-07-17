package stpaverifier.util.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import stpaverifier.controller.IProperty;
import stpaverifier.controller.ObserverValues;
import stpaverifier.controller.model.STPAVerifierController;

public class CompoundJob extends Job{

	private LoggerJob[] checkJobs;
	private boolean oneByOne;
	private LoggerJob activeCheck;
	private boolean isPaused;
	private STPAVerifierController controller;
	
	/**
	 * This number is used to store the state in case the job is paused
	 */
	private int currentJobNr; 

	public CompoundJob(STPAVerifierController stpaVerifierController, LoggerJob[] checkJobs,boolean oneByOne) {
		super("Verification...");
		this.controller = stpaVerifierController;
		this.checkJobs = checkJobs;
		this.oneByOne =oneByOne;
		this.isPaused = false;
		this.currentJobNr = 0;
		
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(getName(), IProgressMonitor.UNKNOWN);

		isPaused = false;
		for(int checkNr = currentJobNr; checkNr <checkJobs.length;checkNr++){
			if(monitor.isCanceled()){
				cleanUP();
				return Status.CANCEL_STATUS;
			}else{
				activeCheck = checkJobs[checkNr];
				monitor.setTaskName(activeCheck.getProperty());
				activeCheck.schedule();
				try {
					activeCheck.join();
					if(activeCheck.getResult().equals(Status.CANCEL_STATUS) && isPaused){
						currentJobNr = checkNr;
						this.controller.setPropertyState(activeCheck.getPropertyID(), IProperty.STATE_PAUSED);
						return Status.OK_STATUS;
					}
					//if the last job has been canceled and the checkJob is not to be executed one by one
					//then the whole check is canceled 
					else if(activeCheck.getResult().equals(Status.CANCEL_STATUS) && !oneByOne){
						cleanUP();
						return Status.CANCEL_STATUS;
					}
				} catch (InterruptedException e) {
					activeCheck.cancel();
				}
			}
		}
		cleanUP();
		if(monitor.isCanceled()){
			return Status.CANCEL_STATUS;
		}else{
			return Status.OK_STATUS;
		}
	}

	public void cleanUP(){
		controller.setUpdateLock();
		for(LoggerJob job: checkJobs){
			job.cleanUp();
		}
		controller.releaseLockAndUpdate(ObserverValues.FORMULARS_CHANGED);
	}
	
	@Override
	protected void canceling() {
		isPaused = false;
		if (activeCheck != null) {
			controller.setPropertyState(activeCheck.getPropertyID(),IProperty.STATE_CANCELED);
		}
	}
	public void pauseJob(){
		if (activeCheck != null) {
			activeCheck.cancel();
			this.isPaused = true;
		}
	}
	
	public boolean isPaused(){
		return isPaused;
	}
}
