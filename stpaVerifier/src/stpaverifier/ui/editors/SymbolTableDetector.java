package stpaverifier.ui.editors;


public class SymbolTableDetector extends SimpleWordDetector {

	private StringBuffer newID;
	
	public SymbolTableDetector() {
		super(new String[0]);
	}

	/* (non-Javadoc)
	 * @see IWordDetector#isWordStart(char)
	 */
	@Override
	public boolean isWordStart(char c) {
		if(super.isWordStart(c) || Character.isAlphabetic(c) || c == '_'){
			this.newID = new StringBuffer();
			this.newID.append(c);
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see IWordDetector#isWordPart(char)
	 */
	@Override
	public boolean isWordPart(char c) {
		if(super.isWordPart(c) || Character.isAlphabetic(c) || Character.isDigit(c) || c == '_'){
			this.newID.append(c);
			return true;
		}
		if(!words.contains(newID.toString())){
			addWord(this.newID.toString());
		}
		return false;
		
	}

	@Override
	public void clearSymbols() {
		this.words.clear();
	}
}
