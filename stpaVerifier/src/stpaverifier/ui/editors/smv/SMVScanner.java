package stpaverifier.ui.editors.smv;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
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
public class SMVScanner extends ModelScanner {
	

	public static final Token CONSTRAINT = new Token(new TextAttribute(new Color(null, new RGB(100, 50, 0)),null,SWT.BOLD));
	public SMVScanner(IPreferenceStore store) {
		setDefaultReturnToken(UNKNOWN);
		
		SimpleWordDetector typeDetector = new SimpleWordDetector(new String[]{
				"array","of","real","signed","unsigned",
				"boolean","byte","chan","integer","short","word","word1"});
		
		SimpleWordDetector keyDetector = new SimpleWordDetector(new String[]{
				"MODULE","DEFINE","MDEFINE","CONSTANTS",
				"VAR","IVAR","SPEC",
				"CTLSPEC","LTLSPEC","PSLSPEC","COMPUTE","NAME",
				"INVARSPEC","ISA",
				"CONSTRAINT","SIMPWFF","CTLWFF","LTLWFF","PSLWFF","COMPWFF",
				"IN","MIN","MAX","MIRROR","PRED","PREDICATES","process",
				"extend","resize","sizeof","uwconst","swconst","EX","AX","EF","AF",
				"EG","AG","E","F","O","G","H","X","Y","Z","A","U","S","V",
				"T","BU","EBF","ABF","EBG","ABG","case","esac"	,"mod","next",
				"init","union","in","xor","xnor","self","TRUE","FALSE","count",
				"abs","max","min"});
		
		SimpleWordDetector constraintDetector = new SimpleWordDetector(new String[]{
				"INIT","TRANS","INVAR","JUSTICE",	"COMPASSION","FAIRNESS","ASSIGN","CONSTRAINT"});
		SimpleWordDetector seperatorDetector = new SimpleWordDetector(new String[]{
				"::",";","{","}"});
		
		SimpleWordDetector operatorDetector = new SimpleWordDetector(new String[]{
				"+","-","*","/","%",">",
				">=","<","<=","==","!=","!",
				"&","||","&&","|","~",">>","<<","^","++","--"});

		List<IRule> rules = new ArrayList<>();

		rules.add(new SingleLineRule("--",null, COMMENT));
		rules.add(new LFWordRule(typeDetector, Token.UNDEFINED, TYPE));
		rules.add(new LFWordRule(constraintDetector, Token.UNDEFINED, CONSTRAINT));
		rules.add(new LFWordRule(keyDetector, Token.UNDEFINED, KEYWORD));
		rules.add(new LFWordRule(seperatorDetector, Token.UNDEFINED,SEPERTAOR));
		rules.add(new LFWordRule(operatorDetector, Token.UNDEFINED,OPERATOR));
		rules.add(new WhitespaceRule(new WhitespaceDetector())); 
		rules.add(new LFWordRule(new SymbolTableDetector(), Token.UNDEFINED,IDENTIFIER));
		
		setRules(rules.toArray(new IRule[0]));
	}
}

