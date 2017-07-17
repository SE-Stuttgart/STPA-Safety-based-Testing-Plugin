package stpaverifier.ui.editors;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class WhitespaceDetector implements IWhitespaceDetector {

	public WhitespaceDetector() {
		// TODO Auto-generated constructor stub
	}


	@Override
	public boolean isWhitespace(char c) {
		if(Character.isWhitespace(c)){
			return true;
		}
		return false;
	}

}
