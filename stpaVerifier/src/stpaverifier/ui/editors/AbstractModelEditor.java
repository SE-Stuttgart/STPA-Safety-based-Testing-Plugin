package stpaverifier.ui.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.services.ISourceProviderService;

import xstampp.ui.menu.file.commands.CommandState;

/**
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 *
 * @see TextEditor
 */
public abstract class AbstractModelEditor extends TextEditor {
	public static final String MODEL_PATH ="model path";
	private ModelFileProvider mProvider;
	protected ModelConfigurer configurer;

	public AbstractModelEditor() {
		super();
		setPartProperty(MODEL_PATH, new String());
		this.mProvider = new ModelFileProvider();
		setDocumentProvider(mProvider);
		
	}
	
	public void invalidate(){
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable(){
			@Override
			public void run() {
				configurer.invalidate();
				getSourceViewer().invalidateTextPresentation();				
			}
		});
	}
	
//	@Override
//	protected void doSetInput(IEditorInput input) throws CoreException {
////		mProvider.setFile(((PromelaModelInput)input).getFile());
//		super.doSetInput(input);
//	}

	@Override
	public boolean isDirty() {
		boolean dirty= super.isDirty();
		// Enable the save entries in the menu
		ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI
				.getWorkbench().getService(ISourceProviderService.class);
		CommandState saveStateService = (CommandState) sourceProviderService
				.getSourceProvider(CommandState.SAVE_STATE);
		saveStateService.isSaveable(dirty, this);
		return dirty;
	}
	
	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		super.doSave(progressMonitor);
		invalidate();
	}
	@Override
	public void setFocus() {
	}
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setPartProperty(MODEL_PATH, ((ModelEditorInput)input).getFile().getLocation().toOSString());
		super.init(site, input);
	}


}
