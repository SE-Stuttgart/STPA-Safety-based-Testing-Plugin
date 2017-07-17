package stpaverifier.ui.editors.promela;

import org.eclipse.ui.editors.text.TextEditor;

import stpaverifier.ui.editors.AbstractModelEditor;
import stpaverifier.ui.editors.ModelConfigurer;

/**
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 *
 * @see TextEditor
 */
public class PromelaEditor extends AbstractModelEditor {

	public static final String ID ="stpaVerifier.editor.text.promela";
	public PromelaEditor() {
		super();
		configurer = new ModelConfigurer(new PromelaScanner(getPreferenceStore()));
		this.setSourceViewerConfiguration(configurer);
	}
	
}
