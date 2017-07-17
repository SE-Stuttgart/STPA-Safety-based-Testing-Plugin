package stpaverifier.util;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import stpaverifier.controller.ObserverValues;
import stpaverifier.controller.model.STPAVerifierController;
import stpaverifier.ui.editors.ModelEditorInput;
import stpaverifier.ui.editors.promela.PromelaEditor;
import stpaverifier.ui.editors.smv.SMVEditor;
import stpaverifier.ui.views.utils.AModelContentView;

public class ModelFileObserver implements Observer,IElementFactory{
	private static final String ID="stpaverifier.editor.factory";
	private static final String TAG_PATH = "path"; 

	private static ModelEditorInput persistantInput;
	private static ModelFileObserver instance;
	private IEditorPart modelEditor;
	private STPAVerifierController controller;
	private IPartListener listener;
	private boolean ignore_Model_synch;
	
	public static ModelFileObserver getFileObserver(){
		if(instance == null){
			instance = new ModelFileObserver(true);
			
		}
		return instance;
	}
	public ModelFileObserver(){
		ignore_Model_synch = false;
	}
	
	private ModelFileObserver(boolean intern) {
		listener = new IPartListener() {
			
			@Override
			public void partOpened(IWorkbenchPart part) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void partDeactivated(IWorkbenchPart part) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void partClosed(IWorkbenchPart part) {
				//if the model file editor is closed than the model path is also set to null
				if(part == modelEditor && controller != null &&!ignore_Model_synch){
					controller.setsFile(null);
				}
				ignore_Model_synch = false;
			}
			
			@Override
			public void partBroughtToTop(IWorkbenchPart part) {
				if(part instanceof IEditorPart && ((IEditorPart) part).getEditorInput() instanceof ModelEditorInput){
					IFile input= ((ModelEditorInput)((IEditorPart) part).getEditorInput()).getFile();
					controller.setFile(input);
				}
			}
			
			@Override
			public void partActivated(IWorkbenchPart part) {
				if(part instanceof AModelContentView && modelEditor != null){
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().activate(modelEditor);
				}
			}
		};
	}
	/**
	 * this implementation of update handles changes of the model file
	 * if a file has been chosen it opens it in the accurate editor 
	 */
	@Override
	public void update(Observable o, Object arg) {
		ObserverValues value = (ObserverValues) arg;
		if(!(o instanceof STPAVerifierController)){
			return;
		}
		STPAVerifierController controller =(STPAVerifierController) o;
		switch(value){
		case MODEL_CHANGED:

			try {
				if(persistantInput != null){
					modelEditor=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findEditor(persistantInput);
					persistantInput = null;
					break;
				}
				IFile model = controller.getFile();
				if(modelEditor != null && ((ModelEditorInput)modelEditor.getEditorInput()).getFile() != controller.getFile()){
					ignore_Model_synch = true;
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(modelEditor, true);
					ignore_Model_synch = false;
					modelEditor = null;
				}else if( modelEditor != null){
					return;
				}
				
				String editorID;
				if(model == null){
					break;
				}else if(model.getFileExtension().equals("smv")){
					editorID = SMVEditor.ID;
				}else if(model.getFileExtension().equals("pml")){
					editorID = PromelaEditor.ID;
				}else{
					break;
				}
				IEditorInput input = new ModelEditorInput(controller.getFile());
				
					modelEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().
										openEditor(input, editorID);
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(listener);
			} catch (PartInitException e) {
				e.printStackTrace();
			}catch (NullPointerException e) {
				controller.deleteObserver(this);
			}
			
				
			break;
		default:
			break;
			
		}
	}

	
	@Override
	public IAdaptable createElement(IMemento memento) {
		// Get the file name.
        String fileName = memento.getString(TAG_PATH);
        if (fileName == null) {
			return null;
		}
        
        // Get a handle to the IFile...which can be a handle
        // to a resource that does not exist in workspace
        IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(fileName);
        if (resource.exists()) {
        	persistantInput = new ModelEditorInput((IFile) resource);
        	getFileObserver().setPersitantFile((IFile) resource);
        	return persistantInput;
		}
		return null;
	}
	
	private void setPersitantFile(IFile path){
		if(controller != null){
			controller.setFile(path);
		}
	}
	/**
	 * @return the id
	 */
	public static String getId() {
		return ID;
	}
	
	 /**
     * Saves the state of the given file editor input into the given memento.
     *
     * @param memento the storage area for element state
     * @param modelFile the full path of the model file
     */
    public static void saveState(IMemento memento, String modelFile) {
        memento.putString(TAG_PATH, modelFile);
    }
	/**
	 * @param controller the controller to set
	 */
	public void setController(STPAVerifierController controller) {
		if(controller == null){
			this.controller.deleteObserver(this);
		}
		this.controller = controller;
		if(persistantInput != null){
			this.controller.setFile(persistantInput.getFile());
		}
	}
	
}
