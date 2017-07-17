package stpaverifier.ui.editors;

import org.eclipse.jface.text.DefaultTextHover;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

/**
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 * @see SourceViewerConfiguration
 */
public class ModelConfigurer extends SourceViewerConfiguration {

	private ModelScanner scanner;
	private PresentationReconciler reconciler;
	private DefaultDamagerRepairer dr;

	public ModelConfigurer(ModelScanner scanner) {
		this.scanner = scanner; 
		
	}
	@Override
	public ITextHover getTextHover(ISourceViewer sourceViewer,
			String contentType) {
		// TODO Auto-generated method stub
		return new DefaultTextHover(sourceViewer);
	}
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {IDocument.DEFAULT_CONTENT_TYPE };
	}
	
	protected ModelScanner getCScanner() {
		return scanner;
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		reconciler = new PresentationReconciler();
	
		dr = new DefaultDamagerRepairer(getCScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		return reconciler;
	}
	public void invalidate() {
		getCScanner().clearSymbols();
	}
	
}
