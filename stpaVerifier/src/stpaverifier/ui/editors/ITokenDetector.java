package stpaverifier.ui.editors;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;

public interface ITokenDetector extends IWordDetector{

	public String[] getWords();

	public void setToken(IToken token);

}