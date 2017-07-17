package stpaverifier.util.jobs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.SimpleMarkerAnnotation;

import stpaverifier.controller.model.STPAVerifierController;
import stpaverifier.model.properties.ModelProperty;
import stpaverifier.ui.editors.AbstractModelEditor;
import stpaverifier.ui.editors.ModelEditorInput;

public abstract class AbstractModelCheckJob extends LoggerJob {

	static final String ERROR = "error:";
	private TextEditor textEditor;
	protected List<PositionAttribute> positions;
	private IDocument document;
	private String filelName;
	private IFile file;
	
	/**
	 * An object of this class can hold information to create a marker 
	 * and an associated annotation in a text editor 
	 */
	protected class PositionAttribute{
		private Position position;
		private String markerType;
		private String message;
		public PositionAttribute(Position pos, String message) {
			this.markerType = "stpaVerifier.syntax.marker";
			this.message = message;
			this.position = pos;
		}
	}
	
	public AbstractModelCheckJob(String name,STPAVerifierController controller) {
		super(name,controller,new ModelProperty());
		this.positions = new ArrayList<>();
		IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(part != null && part instanceof TextEditor){
			this.textEditor = (TextEditor) part;
		}
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(getName(), IProgressMonitor.UNKNOWN);
		if(textEditor == null ||!(textEditor.getEditorInput() instanceof ModelEditorInput)){
			return Status.CANCEL_STATUS;
		}
		
		if(textEditor instanceof AbstractModelEditor){
			((AbstractModelEditor) textEditor).invalidate();
		}
		IDocumentProvider docProvider = textEditor.getDocumentProvider();
		//This is the document we want to connect to. This is taken from 
		//the current editor input.
		setDocument(docProvider.getDocument(textEditor.getEditorInput()));
		setFile(((ModelEditorInput) textEditor.getEditorInput()).getFile());
		setFilelName(getFile().getName());
		try {
			//delete all the markers from the resource and clear the annotations on the textEditor
			getFile().deleteMarkers("stpaVerifier.syntax.marker", true, IResource.DEPTH_INFINITE);
			IAnnotationModel annotationModel = docProvider.getAnnotationModel(textEditor.getEditorInput());
			Iterator<?> annotationIter = annotationModel.getAnnotationIterator();
			while(annotationIter.hasNext()){
				annotationModel.removeAnnotation((Annotation)annotationIter.next());
			}
			createRuntime();
			if(textEditor != null && docProvider!= null){
				docProvider.changed(((ModelEditorInput) textEditor.getEditorInput()));
			}
			for (PositionAttribute attribute : positions) {
				addAnnotation(createMarker(getFile(),attribute), attribute, textEditor);
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		monitor.done();
		return Status.OK_STATUS;
	}
	
	public static IMarker createMarker(IResource res,PositionAttribute attribute)
			throws CoreException {
			       IMarker marker = null;
			       //note: you use the id that is defined in your plugin.xml
			       marker = res.createMarker(attribute.markerType);
			       marker.setAttribute("description,", (Object) "this is one of my markers");
			       //note: you can also use attributes from your supertype
			       marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			       marker.setAttribute(IMarker.MESSAGE, attribute.message);
			       
			       return marker;
			}
	
	public static void addAnnotation(IMarker marker, PositionAttribute attribute, 
			ITextEditor editor) {
		//The DocumentProvider enables to get the document currently loaded in the editor
		IDocumentProvider docProvider = editor.getDocumentProvider();
		
		//This is the document we want to connect to. This is taken from 
		//the current editor input.
		IDocument document = docProvider.getDocument(editor.getEditorInput());
		//The IannotationModel enables to add/remove/change annotation to a Document 
		//loaded in an Editor
		IAnnotationModel annotationModel = docProvider.getAnnotationModel(editor.getEditorInput());
		
		//Note: The annotation type id specify that you want to create one of your 
		//annotations
		SimpleMarkerAnnotation ma = new SimpleMarkerAnnotation("stpaVerifier.specification",marker);

		//Finally add the new annotation to the model
		annotationModel.connect(document);
		annotationModel.addAnnotation(ma, attribute.position);
		annotationModel.disconnect(document);
	}

	protected abstract void createRuntime();

	/**
	 * @return the document
	 */
	public IDocument getDocument() {
		return this.document;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument(IDocument document) {
		this.document = document;
	}

	/**
	 * @return the filelName
	 */
	public String getFilelName() {
		return this.filelName;
	}

	/**
	 * @param filelName the filelName to set
	 */
	public void setFilelName(String filelName) {
		this.filelName = filelName;
	}

	/**
	 * @return the file
	 */
	public IFile getFile() {
		return this.file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(IFile file) {
		this.file = file;
	}
}
