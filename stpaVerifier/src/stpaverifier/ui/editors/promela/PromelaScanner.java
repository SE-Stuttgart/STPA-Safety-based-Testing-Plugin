package stpaverifier.ui.editors.promela;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import stpaverifier.ui.editors.LFWordRule;
import stpaverifier.ui.editors.ModelScanner;
import stpaverifier.ui.editors.SimpleWordDetector;
import stpaverifier.ui.editors.SymbolTableDetector;
import stpaverifier.ui.editors.WhitespaceDetector;

/**
 * an extension of the {@link RuleBasedScanner} to scan the syntax of a promela model file  
 * 
 * @author Lukas Balzer
 * @since 1.0.0
 *
 * @see RuleBasedScanner
 */
public class PromelaScanner extends ModelScanner {
	

	public static final Token CCODE = new Token(new TextAttribute(new Color(null, new RGB(100, 100, 0)),null,SWT.ITALIC));
	public static final Token DEFINE = new Token(new TextAttribute(new Color(null, new RGB(140, 20, 20)),null,SWT.ITALIC));
	
	public PromelaScanner(IPreferenceStore store) {
		setDefaultReturnToken(UNKNOWN);
		
		SimpleWordDetector typeDetector = new SimpleWordDetector(new String[]{
				"bit","bool","byte","chan","int","short"});
		
		SimpleWordDetector keyDetector = new SimpleWordDetector(new String[]{
				"active","assert","atomic","bit",
				"bool","break","byte","chan",
				"d_step","D_proctype","do","else",
				"empty","enabled","fi","full",
				"goto","hidden","if","init",
				"int","len","mtype","nempty",
				"never","nfull","od","of",
				"pc_value","printf","priority","proctype",
				"provided","run","short","skip",
				"timeout","typedef","unless","unsigned","xr","xs"});
		
		SimpleWordDetector seperatorDetector = new SimpleWordDetector(new String[]{
				"::",";","{","}"});
		
		SimpleWordDetector operatorDetector = new SimpleWordDetector(new String[]{
				"+","-","*","/","%",">",
				">=","<","<=","==","!=","!",
				"&","||","&&","|","~",">>","<<","^","++","--"});

		List<IRule> rules = new ArrayList<>();

		rules.add(new SingleLineRule("//","\n", COMMENT));
		rules.add(new SingleLineRule("/*","*/", COMMENT));
		rules.add(new MultiLineRule("c_code {","}", CCODE));
		rules.add(new MultiLineRule("c_expr {","}", CCODE));
		rules.add(new MultiLineRule("c_decl {","}", CCODE));
		rules.add(new MultiLineRule("c_track {","}", CCODE));
		rules.add(new MultiLineRule("c_state {","}", CCODE));
		rules.add(new SingleLineRule("/*","*/", COMMENT));
		rules.add(new LFWordRule(typeDetector, Token.UNDEFINED, TYPE));
		rules.add(new LFWordRule(new SimpleWordDetector(new String[]{"#define","#DEFINE"}), Token.UNDEFINED, DEFINE));
		rules.add(new LFWordRule(keyDetector, Token.UNDEFINED, KEYWORD));
		rules.add(new LFWordRule(seperatorDetector, Token.UNDEFINED,SEPERTAOR));
		rules.add(new LFWordRule(operatorDetector, Token.UNDEFINED,OPERATOR));
		rules.add(new WhitespaceRule(new WhitespaceDetector())); 
		rules.add(new LFWordRule(new SymbolTableDetector(), Token.UNDEFINED,IDENTIFIER));
		
		setRules(rules.toArray(new IRule[0]));
	}
}

