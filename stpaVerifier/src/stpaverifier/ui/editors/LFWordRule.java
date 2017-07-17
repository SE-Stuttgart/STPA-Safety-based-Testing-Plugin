package stpaverifier.ui.editors;

import java.util.HashMap;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

/**
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public class LFWordRule extends WordRule {
	private final HashMap<String, IToken> initialWords;

	public LFWordRule(SimpleWordDetector detector, IToken defaultToken,IToken ruleToken) {
		super(detector, defaultToken);
		Assert.isTrue(detector instanceof SimpleWordDetector);
		this.initialWords = new HashMap<>();
		for (String rule : ((ITokenDetector)detector).getWords()) {
			addWord(rule, ruleToken);
			this.initialWords.put(rule, ruleToken);
		}
		
		detector.setRule(this,ruleToken);
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		int c= scanner.read();
		if(c == ICharacterScanner.EOF){
			((SimpleWordDetector)fDetector).setEOF();

			scanner.unread();
			return Token.UNDEFINED;
		}
		scanner.unread();
		
		return super.evaluate(scanner);
	}

	public void setToken(IToken token) {
		((ITokenDetector)fDetector).setToken(token);
	}
	
	@SuppressWarnings("unchecked")
	public void clearSymbols(){
		fWords.clear();
		fWords.putAll(initialWords);
		((SimpleWordDetector)fDetector).clearSymbols();
	}
}
