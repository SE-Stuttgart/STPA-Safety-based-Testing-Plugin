package stpaverifier.ui.editors;

import org.eclipse.ui.editors.text.FileDocumentProvider;

/**
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 *
 * @see FileDocumentProvider
 */
public class ModelFileProvider extends FileDocumentProvider {


	@Override
	public boolean isModifiable(Object element) {
		return true;
	}
	@Override
	public boolean isSynchronized(Object element) {
		return true;
	}

	@Override
	public boolean isReadOnly(Object element) {
		return false;
	}
}