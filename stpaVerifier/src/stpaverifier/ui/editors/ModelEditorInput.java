package stpaverifier.ui.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.part.FileEditorInput;

import stpaverifier.util.ModelFileObserver;

/**
 * An implementation of {@link IEditorInput} which is used to open a promela model
 * 
 * @author Lukas Balzer
 * @since 1.0.0
 *
 * @see IEditorInput
 */
public class ModelEditorInput extends FileEditorInput {

	private IFile fModel;

	public ModelEditorInput(IFile fModel) {
		super(fModel);
		this.fModel = fModel;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return getFile().getName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return this;
	}

	@Override
	public String getToolTipText() {
		return fModel.getFullPath().toString();
	}
	
	public IFile getFile(){
		return fModel;
	}
	@Override
	public void saveState(IMemento memento) {
		ModelFileObserver.saveState(memento, getFile().getFullPath().toOSString());	
	}
	@Override
	public String getFactoryId() {
		return ModelFileObserver.getId();
	}
	
}
