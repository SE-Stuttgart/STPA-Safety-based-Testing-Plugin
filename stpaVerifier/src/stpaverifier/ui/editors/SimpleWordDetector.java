package stpaverifier.ui.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;

/**
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 *
 *@see IWordDetector
 */
public class SimpleWordDetector  implements ITokenDetector{
	private int index;
	protected List<String> words;
	private List<String> rules;
	protected IToken lastToken;
	private LFWordRule parentRule;
	private IToken symbolToken;
	private boolean accept;
	
	public SimpleWordDetector(String[] strings) {
		this.words = new ArrayList<>();
		for (String string : strings) {
			this.words.add(string);
		}
		this.rules = new ArrayList<>();
	}
	
	
	@Override
	public boolean isWordStart (char c) {
		accept = false;
		this.rules.clear();
		for(String word : this.words){
			if(c == word.charAt(0)){
				this.rules.add(word);
			}
		}
		if(!this.rules.isEmpty()){
			this.index = 0;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isWordPart (char c) {
		this.index++;
		boolean result = false;
		if(accept && Character.isAlphabetic(c)){
			result = true;
		}
		accept = false;
		List<String> iRedundant = new ArrayList<>();
		
		for(String rule : this.rules){
			if(index >= rule.length()){
				iRedundant.add(rule);
			}else if( c	== rule.charAt(index)){
				result = result || true;
				if(index == rule.length()-1){
					/*
					 * if c is the last word than the accept flag is set to true
					 * if the accept flag is set to true than the next character has to be an accepting one
					 * if not true is returned indicating that the word isn't finished
					 */
					accept = true;
				}
			}else{
				iRedundant.add(rule);
			}
		}
		this.rules.removeAll(iRedundant);
		
		return result;
	}
	
	protected void addWord(String string) {
		if(ModelScanner.TYPE.equals(lastToken)){
			this.words.add(string);	
			this.parentRule.addWord(string, symbolToken);
		}else{
			this.words.add(string);	
			this.parentRule.addWord(string, ModelScanner.UNDECLARED);
		}
	}
	
	public void setEOF(){
	}
	
	/* (non-Javadoc)
	 * @see stpaverifier.ui.editors.ITokenDetector#getWords()
	 */
	@Override
	public String[] getWords(){
		return words.toArray(new String[0]);
	}
	
	/* (non-Javadoc)
	 * @see stpaverifier.ui.editors.ITokenDetector#setToken(org.eclipse.jface.text.rules.IToken)
	 */
	@Override
	public void setToken(IToken token) {
		if(!token.isWhitespace()){
			this.lastToken = token;
		}
	}


	public void setRule(LFWordRule lfWordRule, IToken token) {
		this.parentRule = lfWordRule;
		this.symbolToken = token;
	}


	public void clearSymbols() {
		
	}
	
}