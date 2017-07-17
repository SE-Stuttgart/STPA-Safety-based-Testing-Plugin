package stpaverifier.ui.editors;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public abstract class ModelScanner extends RuleBasedScanner {

	public static final Token TYPE = new Token(new TextAttribute(new Color(null, new RGB(100, 0, 0)),null,SWT.BOLD));
	public static final Token KEYWORD = new Token(new TextAttribute(new Color(null, new RGB(100, 0, 0)),null,SWT.BOLD));
	public static final Token IDENTIFIER = new Token(new TextAttribute(new Color(null, new RGB(100, 0, 0)),null,SWT.ITALIC));
	public static final Token CONSTANT = new Token(new TextAttribute(new Color(null, new RGB(0, 0, 100)),null,SWT.BOLD));
	public static final Token SEPERTAOR = new Token(new TextAttribute(new Color(null, new RGB(0, 0, 0))));
	public static final Token OPERATOR = new Token(new TextAttribute(new Color(null, new RGB(0, 0, 0)),null,SWT.NORMAL));
	public static final Token UNKNOWN = new Token(new TextAttribute(new Color(null, new RGB(0, 0, 0))));
	public static final Token UNDECLARED = new Token(new TextAttribute(new Color(null, new RGB(0, 0, 0))));
	public static final Token COMMENT = new Token(new TextAttribute(new Color(null, new RGB(0,105, 0))));

	public ModelScanner() {
		super();
	}

	@Override
	public IToken nextToken() {
		IToken token = super.nextToken();
		
		for (IRule rule : fRules) {
			if(rule instanceof LFWordRule){
				((LFWordRule)rule).setToken(token);
			}
		}
		return token;
	}

	public void clearSymbols() {
		for (IRule rule : fRules) {
			if(rule instanceof LFWordRule){
				((LFWordRule)rule).clearSymbols();
			}
		}
	}

}