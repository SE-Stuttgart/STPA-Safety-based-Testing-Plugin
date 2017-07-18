package xstampp.stpatcgenerator.controller;

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

import xstampp.stpatcgenerator.model.ExportInformation;
import xstampp.stpatcgenerator.model.VerificationResult;
import xstampp.stpatcgenerator.model.properties.AbstractProperty;
import xstampp.stpatcgenerator.model.properties.ModelProperty;
//import xstampp.stpatcgenerator.ui.editors.ModelEditorInput;
//import xstampp.stpatcgenerator.util.VerificationStateProvider;
//import xstampp.stpatcgenerator.util.jobs.CheckNuSMVLTLJob;
//import xstampp.stpatcgenerator.util.jobs.CheckPROMELAJob;
//import xstampp.stpatcgenerator.util.jobs.CheckSpinLTLJob;
//import xstampp.stpatcgenerator.util.jobs.CompoundJob;
//import xstampp.stpatcgenerator.util.jobs.LoggerJob;
//import xstampp.stpatcgenerator.util.jobs.NuSMVRunJob;
//import xstampp.stpatcgenerator.util.jobs.SpinRunJob;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.ISafetyDataModel;
import xstampp.ui.common.ProjectManager;

/**
 * An Object of this class contains a STPATCGModelController which controls all
 * models for STPA TCGenerator
 *
 * @author Ting Luk-He
 *
 */
public class STPATCGController extends Observable implements IVerifierController {

	private STPATCGModelController stpaTCGmodel;
	private boolean useSpin;
	private IFile modelFile;
	private ISafetyDataModel stpaSafetyData;
	private boolean updateLock;

	public STPATCGController() {
		ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI.getWorkbench()
				.getService(ISourceProviderService.class);
		this.stpaTCGmodel = new STPATCGModelController();
		updateLock = false;
		useSpin = false;
	}

	public void addLTLFormular(AbstractLTLProvider provider) {
		stpaTCGmodel.addLTLFormular(provider);
		setChanged();
		this.setChangeAndUpdate(ObserverValues.FORMULARS_CHANGED);

	}

	public void addLTLFormular(String id, String formular) {
		stpaTCGmodel.addLTLFormular(id, formular);
		setChanged();
		this.setChangeAndUpdate(ObserverValues.FORMULARS_CHANGED);

	}

	@Override
	public void addCTLFormular(String id, String formular) {
		// model.addCTLFormular(id,formular);
		// setChanged();
		// this.setChangeAndUpdate(ObserverValues.FORMULARS_CHANGED);
	}

	@Override
	public List<IProperty> getAllProperties() {
		ArrayList<IProperty> list = new ArrayList<>();
		list.addAll(_int_getAllProperties());
		return list;
	}

	private List<AbstractProperty> _int_getAllProperties() {
		ArrayList<AbstractProperty> list = new ArrayList<>();
		// if(!useSpin){
		// list.addAll(model.getCtlList());
		// }
		list.addAll(stpaTCGmodel.getLtlList());
		Collections.sort(list, new Comparator<IProperty>() {

			@Override
			public int compare(IProperty o1, IProperty o2) {
				return o1.compareTo(o2);
			}

		});
		return list;
	}

	public STPATCGModelController getStpaTCGmodel() {
		return stpaTCGmodel;
	}

	public void setStpaTCGmodel(STPATCGModelController stpaTCGmodel) {
		this.stpaTCGmodel = stpaTCGmodel;
	}

	/**
	 * @return the sFile
	 */
	public IFile getFile() {
		return modelFile;
	}

	/**
	 * @param sFile
	 *            the sFile to set
	 */
	public void setFile(IFile modelFile) {
		if (this.modelFile != modelFile) {
			this.modelFile = modelFile;
			// if(modelFile != null &&
			// modelFile.getFileExtension().equals("smv") && useSpin){
			// setUseSpin(false);
			// }else if(modelFile != null &&
			// modelFile.getFileExtension().equals("pml") && !useSpin){
			// setUseSpin(true);
			// }
			this.setChangeAndUpdate(ObserverValues.MODEL_CHANGED);
		}
	}

	public void setsFile(String path) {
		IFile newModel = null;
		IResource res = null;
		File fModel = null;
		if (path != null) {
			IPath p = new Path(path.replace(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString(), ""));
			res = ResourcesPlugin.getWorkspace().getRoot().findMember(p);
			fModel = new File(path);
		}
		if (path == null || path.isEmpty()) {
			newModel = null;
		} else if (res != null && res instanceof IFile) {
			newModel = (IFile) res;
		} else if (fModel != null && fModel.exists()) {
			try {
				BufferedInputStream stream = new BufferedInputStream(new FileInputStream(fModel));
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				IWorkspaceRoot root = workspace.getRoot();
				String sName = fModel.getName();
				IProject project = root.getProject(sName.substring(0, sName.lastIndexOf('.')));
				if (project.getLocation() != null && !project.getLocation().toFile().exists() && project.exists()) {
					project.delete(true, null);
				}
				newModel = project.getFile(sName);

				// at this point, no resources have been created
				if (!project.exists()) {
					project.create(null);
				}
				if (!project.isOpen())
					project.open(null);

				if (newModel.exists()) {
					newModel.delete(true, null);
				}

				newModel.create(stream, IResource.FORCE, null);

			} catch (CoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			newModel = null;
		}
		if ((modelFile == null && newModel != null) || (modelFile != null && !modelFile.equals(newModel))) {
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
	 * @param id
	 *            the id to which the formula Object is mapped
	 * @param state
	 *            the state to set
	 * 
	 * @return if the UUID is key to a legal ltl/ctl property and when if the
	 *         given state is legal
	 */
	public boolean setPropertyState(UUID id, int state) {
		boolean result = stpaTCGmodel.setPropertyState(id, state);
		if (result) {
			setChanged();
			setChangeAndUpdate(ObserverValues.FORMULARS_CHANGED);
		}
		return result;
	}

	/**
	 * changes the value of a the property that is stored with this id
	 * 
	 * @param id
	 *            the id to which the formula Object is mapped
	 * @param property
	 *            the new formula value
	 */
	public void setProperty(UUID id, String property) {
		stpaTCGmodel.setProperty(id, property);
		setChanged();
		setChangeAndUpdate(ObserverValues.FORMULARS_CHANGED);

	}

	@Override
	public void setUseAllProperties(boolean use) {
		stpaTCGmodel.setUseAllProperties(use);
		setChanged();
		setChangeAndUpdate(ObserverValues.FORMULARS_CHANGED);
	}

	/**
	 * 
	 * @param id
	 *            the id to which the formula Object is mapped which is used in
	 *            this setter
	 * @param checked
	 *            a boolean indicating whether the property should be verified
	 *            in the next job or not
	 */
	public void setUseProperty(UUID id, boolean checked) {
		stpaTCGmodel.setUseProperty(id, checked);
		setChanged();
		setChangeAndUpdate(ObserverValues.FORMULARS_CHANGED);
	}

	public boolean clearLTLImport() {
		if (stpaTCGmodel.clearLTLImport()) {
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
	 * removes a property from one of the two property lists and triggers an
	 * update to {@link ObserverValues#FORMULARS_CHANGED}
	 * 
	 * @param id
	 *            the UUID key which should be removed, if null than all stored
	 *            properties are removed
	 * @param removeAll
	 *            if true than all stored properties are removed
	 */
	public boolean removeProperty(UUID id, boolean removeAll) {

		if (stpaTCGmodel.removeProperty(id, removeAll)) {
			setChanged();
			setChangeAndUpdate(ObserverValues.FORMULARS_CHANGED);
			return true;
		}
		return false;
	}

	@Override
	public boolean removeChecked() {
		for (IProperty property : getAllProperties()) {
			if (property.useProperty() && !stpaTCGmodel.removeProperty(property.getUUID(), false)) {
				return false;
			}
		}
		setChanged();
		setChangeAndUpdate(ObserverValues.FORMULARS_CHANGED);
		return true;
	}

	@Override
	public List<IVerificationResult> getResults() {
		ArrayList<IVerificationResult> list = new ArrayList<>();
		if (this.stpaTCGmodel.getResults() != null) {
			list.addAll(this.stpaTCGmodel.getResults());
		}
		return list;
	}

	/**
	 * creates or clears the results list and triggers a changed observer value
	 */
	public void resetResults() {
		stpaTCGmodel.resetResults();

		setChanged();
		setChangeAndUpdate(ObserverValues.RESULTS_CHANGED);
	}

	public void addResult(VerificationResult res) {
		if (stpaTCGmodel.addResult(res)) {
			setChangeAndUpdate(ObserverValues.RESULTS_CHANGED);
		}
	}

	// public ModexController getModexController() {
	// return this.modexController;
	// }

	private void setChangeAndUpdate(final Object o) {
		if (!updateLock) {
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

	/**
	 * @return the stpaSafetyData
	 */
	public ISafetyDataModel getStpaSafetyData() {
		return this.stpaSafetyData;
	}

	/**
	 * @param stpaSafetyData
	 *            the stpaSafetyData to set
	 */
	public void setStpaSafetyData(ISafetyDataModel stpaSafetyData) {
		this.stpaSafetyData = stpaSafetyData;
	}

	public Object getExportModel() {
		ExportInformation info = new ExportInformation();
		info.setProperties(_int_getAllProperties(), useSpin);
		info.setResults(getResults());
		if (getFile() != null) {
			info.setModelName(getFile().getName());
			if (getFile().getFileExtension().toLowerCase().equals("pml")) {//$NON-NLS-1$
				info.setModelType("Promela Model:");//$NON-NLS-1$
			} else {
				info.setModelType("SMV Model:");//$NON-NLS-1$
			}
		}
		if (getStpaSafetyData() != null) {
			info.setStpaProjectName(getStpaSafetyData().getProjectName());
		}

		return info;

	}

	@Override
	public IFile getLog() {
		if (modelFile != null) {
			return modelFile.getProject().getFile(".log");
		}
		return null;
	}

	@Override
	public boolean importData(ISafetyDataModel model) {
		if (clearLTLImport()) {
			setStpaSafetyData(model);
			for (AbstractLTLProvider entry : model.getLTLPropertys()) {
				addLTLFormular(entry);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean executeSTPAVerifier(boolean oneByOne) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkSyntax() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean resetProperties() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pauseVerification() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cancelVerification() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean resumeVerification() {
		// TODO Auto-generated method stub
		return false;
	}

}
