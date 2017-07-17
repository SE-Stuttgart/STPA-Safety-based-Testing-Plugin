package stpaverifier.ui.editors.smv;

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
public class SMVEditor extends AbstractModelEditor {

	public static final String ID ="stpaVerifier.editor.text.smv";
	public SMVEditor() {
		super();
		configurer = new ModelConfigurer(new SMVScanner(getPreferenceStore()));
		this.setSourceViewerConfiguration(configurer);
	}
}
