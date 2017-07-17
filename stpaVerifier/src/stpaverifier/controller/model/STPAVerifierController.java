package stpaverifier.controller.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.UUID;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;

import stpaverifier.controller.IProperty;
import stpaverifier.controller.IVerificationResult;
import stpaverifier.controller.IVerifierController;
import stpaverifier.controller.ObserverValues;
import stpaverifier.model.ExportInformation;
import stpaverifier.model.STPAVerifierModel;
import stpaverifier.model.VerificationResult;
import stpaverifier.model.properties.AbstractProperty;
import stpaverifier.model.properties.ModelProperty;
import stpaverifier.ui.editors.ModelEditorInput;
import stpaverifier.util.VerificationStateProvider;
import stpaverifier.util.jobs.CheckNuSMVLTLJob;
import stpaverifier.util.jobs.CheckPROMELAJob;
import stpaverifier.util.jobs.CheckSpinLTLJob;
import stpaverifier.util.jobs.CompoundJob;
import stpaverifier.util.jobs.LoggerJob;
import stpaverifier.util.jobs.NuSMVRunJob;
import stpaverifier.util.jobs.SpinRunJob;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.ISafetyDataModel;
import xstampp.ui.common.ProjectManager;

/**
 * An Object of this class contains a STPAVerifierModel and communicates with a ModexController
 * This class can be used to store and read information to the data model and execute SPIN and NuSMV model checks
 * further it can be used to 
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public class STPAVerifierController extends Observable implements IVerifierController{

	private STPAVerifierModel model;
	private boolean useSpin;
	private IFile modelFile;
	private ModexController modexController;
	private ISafetyDataModel stpaSafetyData;
	private String checkerString;
	private CompoundJob checkJob;
	private VerificationStateProvider runStateService;
	private boolean updateLock;
	
	public STPAVerifierController() {
		ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI
				.getWorkbench().getService(ISourceProviderService.class);
		runStateService = (VerificationStateProvider) sourceProviderService
				.getSourceProvider(VerificationStateProvider.VERIFIER_STATE);
		this.model = new STPAVerifierModel();
		updateLock = false;
		this.modexController = new ModexController();
		useSpin = true;
	}
	public void addLTLFormular(AbstractLTLProvider provider){
		model.addLTLFormular(provider);
		setChanged();
		this.setChangeAndUpdate(ObserverValues.FORMULARS_CHANGED);
		
	}
	
	public void addLTLFormular(String id, String formular){
		model.addLTLFormular(id,formular);
		setChanged();
		this.setChangeAndUpdate(ObserverValues.FORMULARS_CHANGED);
		
	}

	@Override
	public void addCTLFormular(String id, String formular){
		model.addCTLFormular(id,formular);
		setChanged();
		this.setChangeAndUpdate(ObserverValues.FORMULARS_CHANGED);
	}
	
	@Override
	public List<IProperty> getAllProperties(){
		ArrayList<IProperty> list = new ArrayList<>();
		list.addAll(_int_getAllProperties());
		return list;
	}
	
	private List<AbstractProperty> _int_getAllProperties(){
		ArrayList<AbstractProperty> list = new ArrayList<>();
		if(!useSpin){
			list.addAll(model.getCtlList());
		}
		list.addAll(model.getLtlList());
		Collections.sort(list,new Comparator<IProperty>() {

			@Override
			public int compare(IProperty o1, IProperty o2) {
				return o1.compareTo(o2);
			}
			
		});
		return list;
	}
	/**
	 * @return the sFile
	 */
	public IFile getFile() {
		return modelFile;
	}
	/**
	 * @param sFile the sFile to set
	 */
	public void setFile(IFile modelFile) {
		if(this.modelFile != modelFile){
			this.modelFile = modelFile;
			if(modelFile != null && modelFile.getFileExtension().equals("smv") && useSpin){
				setUseSpin(false);
			}else if(modelFile != null && modelFile.getFileExtension().equals("pml") && !useSpin){
				setUseSpin(true);
			}
			this.setChangeAndUpdate(ObserverValues.MODEL_CHANGED);
		}
	}

	public void setsFile(String path){
		IFile newModel = null;
		IResource res=  null;
		File fModel = null;
		if(path != null){
			IPath p = new Path(path.replace(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString(), ""));
			res=  ResourcesPlugin.getWorkspace().getRoot().findMember(p);
			fModel = new File(path);
		}
		if(path == null|| path.isEmpty()){
			newModel = null;
		}else if(res != null && res instanceof IFile){
			newModel = (IFile)res;
		}else if(fModel != null && fModel.exists()){
			try {
				BufferedInputStream stream = new BufferedInputStream(new FileInputStream(fModel));
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				IWorkspaceRoot root = workspace.getRoot();
				String sName = fModel.getName();
				IProject project  = root.getProject(sName.substring(0, sName.lastIndexOf('.')));
				if(project.getLocation() != null && !project.getLocation().toFile().exists() && project.exists()){
					project.delete(true, null);
				}
				newModel = project.getFile(sName);
			
				//at this point, no resources have been created
				if (!project.exists()){
					project.create(null);
				}
				if (!project.isOpen()) project.open(null);
				
				if (newModel.exists()) {
					newModel.delete(true,null);
				}

				newModel.create(stream, IResource.FORCE, null);
	
			} catch (CoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			newModel = null;
		}
		if((modelFile == null && newModel != null) || (modelFile != null &&!modelFile.equals(newModel))){
			modelFile = newModel;
			setChanged();
			this.setChangeAndUpdate(ObserverValues.MODEL_CHANGED);
		}
	}
	/**
	 * @return the useSpin
	 */
	public boolean isUseSpin() {
		return useSpin;
	}
	/**
	 * @param useSpin the useSpin to set
	 */
	public void setUseSpin(boolean useSpin) {
		if(runStateService == null){
			ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI
					.getWorkbench().getService(ISourceProviderService.class);
			runStateService = (VerificationStateProvider) sourceProviderService
					.getSourceProvider(VerificationStateProvider.VERIFIER_STATE);
		}
		if(this.useSpin != useSpin){
			if(useSpin){
				runStateService.setVerifier(VerificationStateProvider.SPIN);
			}else{
				runStateService.setVerifier(VerificationStateProvider.NUSMV);
			}
			this.useSpin = useSpin;
			setFile(null);
			setChanged();
			setChangeAndUpdate(ObserverValues.USE_SPIN);
		}
	}
	
	/**
	 * @param id the id to which the formula Object is mapped
	 * @param state the state to set
	 * 
	 * @return if the UUID is key to a legal ltl/ctl property and when if 
	 * 			the given state is legal
	 */
	public boolean setPropertyState(UUID id,int state) {
		boolean result = model.setPropertyState(id, state);
		if(result){
			setChanged();
			setChangeAndUpdate(ObserverValues.FORMULARS_CHANGED);
		}
		return result;
	}
	/**
	 * changes the  value of a the property that is stored with this id
	 *  
	 * @param id the id to which the formula Object is mapped
	 * @param property the new formula value
	 */
	public void setProperty(UUID id, String property){
		model.setProperty(id, property);
		setChanged();
		setChangeAndUpdate(ObserverValues.FORMULARS_CHANGED);
		
	}
	
	@Override
	public void setUseAllProperties(boolean use){
		model.setUseAllProperties(use);
		setChanged();
		setChangeAndUpdate(ObserverValues.FORMULARS_CHANGED);
	}
	
	/**
	 * 
	 * @param id the id to which the formula Object is mapped which is used in this setter
	 * @param checked a boolean indicating whether the property should be verified in the next job or not
	 */
	public void setUseProperty(UUID id, boolean checked) {
		model.setUseProperty(id, checked);
		setChanged();
		setChangeAndUpdate(ObserverValues.FORMULARS_CHANGED);
	}
	
	public boolean clearLTLImport(){
		if(model.clearLTLImport()){
			setChanged();
			setChangeAndUpdate(ObserverValues.FORMULARS_CHANGED);
			return true;
		}
		return false;
		
	}

	@Override
	public boolean removeProperty(UUID id) {
		return removeProperty(id, false);
	}
	@Override
	public boolean removeAllProperties() {
		return removeProperty(null, true);
	}
	
	/**
	 * removes a property from one of the two property lists
	 * and triggers an update to {@link ObserverValues#FORMULARS_CHANGED}
	 * 
	 * @param id the UUID key which should be removed, if null than all stored properties are removed
	 * @param removeAll if true than all stored properties are removed
	 */
	public boolean removeProperty(UUID id, boolean removeAll) {
		
		if(model.removeProperty(id, removeAll)){
			setChanged();
			setChangeAndUpdate(ObserverValues.FORMULARS_CHANGED);
			return true;
		}
		return false;
	}

	@Override
	public boolean removeChecked() {
		for(IProperty property: getAllProperties()){
			if(property.useProperty() && !model.removeProperty(property.getUUID(),false)){
				return false;
			}
		}
		setChanged();
		setChangeAndUpdate(ObserverValues.FORMULARS_CHANGED);
		return true;
	}
	
	@Override
	public boolean checkSyntax() {
		for(IProperty property: getAllProperties()){
			if(property.useProperty() && isUseSpin()){
				Job job = new CheckSpinLTLJob("Check Property", this, property);
				job.schedule();
			}else if(property.useProperty()){
				Job job = new CheckNuSMVLTLJob("Check Property", this, property);
				job.schedule();
			}
		}
		return true;
	}
	
	@Override
	public boolean resetProperties(){
		setUpdateLock();
		for(IProperty property: getAllProperties()){
			setPropertyState(property.getUUID(), IProperty.STATE_UNCHECKED);
			property.setCounterexample(null);
		}
		releaseLockAndUpdate(ObserverValues.FORMULARS_CHANGED);
		releaseLockAndUpdate(ObserverValues.RESET);
		resetResults();
		return true;
	}
	
	@Override
	public List<IVerificationResult> getResults() {
		ArrayList<IVerificationResult> list = new ArrayList<>();
		if(this.model.getResults() != null){
			list.addAll(this.model.getResults());
		}
		return list;
	}
	/**
	 *creates or clears the results list  and triggers a changed observer value
	 */
	public void resetResults() {
		model.resetResults();
			
		setChanged();
		setChangeAndUpdate(ObserverValues.RESULTS_CHANGED);
	}
	
	public void addResult(VerificationResult res){
		if(model.addResult(res)){
			setChangeAndUpdate(ObserverValues.RESULTS_CHANGED);
		}
	}

	public ModexController getModexController() {
		return this.modexController;
	}
	
	private void setChangeAndUpdate(final Object o){
		if(!updateLock){
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					ProjectManager.getLOGGER().debug("Update " + o.toString());
					setChanged();
					notifyObservers(o);
				}
			});
		}
	}
	
	public void setUpdateLock(){
		this.updateLock = true;
	}
	
	public void releaseLockAndUpdate(Object update){
		this.updateLock = false;
		setChangeAndUpdate(update);
	}
	/**
	 * @return the stpaSafetyData
	 */
	public ISafetyDataModel getStpaSafetyData() {
		return this.stpaSafetyData;
	}
	/**
	 * @param stpaSafetyData the stpaSafetyData to set
	 */
	public void setStpaSafetyData(ISafetyDataModel stpaSafetyData) {
		this.stpaSafetyData = stpaSafetyData;
	}
	
	public Object getExportModel(){
		ExportInformation info = new ExportInformation();
		info.setProperties(_int_getAllProperties(),useSpin);
		info.setResults(getResults());
		if(getFile() != null){
			info.setModelName(getFile().getName());
			if(getFile().getFileExtension().toLowerCase().equals("pml")){//$NON-NLS-1$
				info.setModelType("Promela Model:");//$NON-NLS-1$
			}else{
				info.setModelType("SMV Model:");//$NON-NLS-1$
			}
		}
		if(getStpaSafetyData() != null){
			info.setStpaProjectName(getStpaSafetyData().getProjectName());
		}
		info.setModelCheckerVersion(checkerString);
		return info;
		
	}
	
	@Override
	public boolean executeSTPAVerifier(boolean oneByOne){
		if(checkJob != null){
			return false;
		}
		if(runStateService == null){

			ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI
					.getWorkbench().getService(ISourceProviderService.class);
			runStateService = (VerificationStateProvider) sourceProviderService
					.getSourceProvider(VerificationStateProvider.VERIFIER_STATE);
		}
		if(getFile() == null){
			MessageDialog.openInformation(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
											"Please select a model", "please select a model file to run the accociated model checker");
			return false;
		}
		IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(part != null && part.getEditorInput() instanceof ModelEditorInput){
			setFile(((ModelEditorInput)part.getEditorInput()).getFile());
		}
		List<IProperty> formulars = new ArrayList<>();
		for(IProperty prop : getAllProperties()){
			if(prop.useProperty()){
				if(setPropertyState(prop.getUUID(), IProperty.STATE_WAITING)){
					formulars.add(prop);
				}
			}
		}

		List<LoggerJob> checkQueue = new ArrayList<>();
		
		String jobName ="run";
		boolean isOneByOne = true;
		
		if(oneByOne){
			jobName = " one by one " + jobName;
		}
		
		if(isUseSpin()){
			checkQueue.add(new CheckPROMELAJob("Checking Model", this));
			if(formulars.isEmpty()){
				checkQueue.add(new SpinRunJob("Checking Model" +jobName,this, new ModelProperty()));
			}
		}else{
			checkQueue.add(new NuSMVRunJob("Checking Model" +jobName,this, new ModelProperty()));
		}
		for (IProperty prop : formulars) {
				LoggerJob validation;
				if(isUseSpin()){
					validation =new SpinRunJob("Spin " +jobName,this, prop);
				}else{
					validation =new NuSMVRunJob("NuSMV " + jobName,this, prop);
				}
				addObserver(validation);
				checkQueue.add(validation);
		}
		
		checkJob = new CompoundJob(this,checkQueue.toArray(new LoggerJob[0]),isOneByOne);
		checkJob.addJobChangeListener(new JobChangeAdapter(){
			@Override
			public void aboutToRun(IJobChangeEvent event) {
				runStateService.setVerifierState(VerificationStateProvider.RUNNING);
			}
			@Override
			public void done(IJobChangeEvent event) {
				if(checkJob.isPaused()){
					runStateService.setVerifierState(VerificationStateProvider.PAUSED);
				}else{
					checkJob = null;
					runStateService.setVerifierState(VerificationStateProvider.READY);
				}
			}
		});
		resetResults();
		checkJob.schedule();
		return true;
	}
	
	@Override
	public boolean pauseVerification() {
		if(checkJob != null){
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					checkJob.pauseJob();
				}
			});
			return true;
		}
		return false;
	}
	@Override
	public boolean resumeVerification() {
		if(checkJob != null){
			checkJob.schedule();
			return true;
		}
		return false;
	}
	@Override
	public boolean cancelVerification() {
		if(checkJob != null){
			if(checkJob.cancel()){
				checkJob.cleanUP();
				checkJob = null;
				runStateService.setVerifierState(VerificationStateProvider.READY);
				return true;
			}
		}
		return false;
	}
	public void setModelChecker(String checker) {
		this.checkerString = checker;		
	}

	@Override
	public IFile getLog() {
		if(modelFile != null){
			return modelFile.getProject().getFile(".log");
		}
		return null;
	}
	@Override
	public boolean importData(ISafetyDataModel model) {
		if(clearLTLImport()){
			setStpaSafetyData(model);
			for(AbstractLTLProvider entry : model.getLTLPropertys()){
				addLTLFormular(entry);
			}
			return true;
		}
		return false;
	}
	
}
