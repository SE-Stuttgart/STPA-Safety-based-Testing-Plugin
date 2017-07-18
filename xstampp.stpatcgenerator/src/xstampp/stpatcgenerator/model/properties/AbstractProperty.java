package xstampp.stpatcgenerator.model.properties;

import java.util.UUID;

import xstampp.stpatcgenerator.controller.IProperty;
//import xstampp.stpatcgenerator.model.AbstractCounterexample;

public abstract class AbstractProperty implements IProperty {

	private final static String AND ="_AND_";
	private final static String Equals ="_EQUALS_";
	private final static String OR ="_OR_";
	private final static String Always ="_ALWAYS_";
	private final static String Finally ="_FINALLY_";
	
	private String sFormular;
	private String sID;
	private int state;
	private final UUID uuID;
	private boolean use;
	private boolean changeLock;
	private int number;
	private String message;
//	private AbstractCounterexample counterexample;
	
	public AbstractProperty(String idLiteral, String formular) {
		setsFormular(formular);
		String nrString = "0";
		for(char c : idLiteral.toCharArray()){
			if(Character.isDigit(c)){
				if(nrString.equals("0")){
					nrString = "";
				}
				nrString = nrString + c;
			}
		}
		this.number = Integer.parseInt(nrString);
		this.sID = idLiteral;
		this.setState(IProperty.STATE_UNCHECKED);
		this.uuID = UUID.randomUUID();
	}

	@Override
	public int compareTo(IProperty arg0) {
		if(arg0.getNumber() > number){
			return -1;
		}
		if(number > arg0.getNumber()){
			return 1;
		}
		return 0;
	}
	
	public int getNumber() {
		return this.number;
	}
	@Override
	public String getsFormular(boolean useSpin, boolean forExport) {
		String formulaCode = new String(sFormular);
		String expSpace = "";
		if(forExport){
			expSpace =" ";
		}
		if(useSpin){
			formulaCode = formulaCode.replaceAll("TRUE","true");
			formulaCode = formulaCode.replaceAll("FALSE","false");
			formulaCode = formulaCode.replaceAll(Always,"[]");
			formulaCode = formulaCode.replaceAll(Finally,"<>");
			formulaCode = formulaCode.replaceAll(AND,expSpace +"&&");
			formulaCode = formulaCode.replaceAll(OR,expSpace +"||");
			formulaCode = formulaCode.replaceAll(Equals,"==");
		}else{
			formulaCode = formulaCode.replaceAll("true","TRUE");
			formulaCode = formulaCode.replaceAll("false","FALSE");
			formulaCode = formulaCode.replaceAll(Always+"\\s*","G ");
			formulaCode = formulaCode.replaceAll(Finally+"\\s*","F ");
			formulaCode = formulaCode.replaceAll(AND,expSpace +"&");
			formulaCode = formulaCode.replaceAll(OR,expSpace +"|");
			formulaCode = formulaCode.replaceAll(Equals,"=");
		}
		return new String(formulaCode);
	}

	/**
	 * @param sFormular the sFormular to set
	 */
	public void setsFormular(String sFormular) {
		String formulaCode = new String(sFormular);
		formulaCode = formulaCode.replaceFirst("(^G)(\\W)", Always+"$2");
		formulaCode = formulaCode.replaceFirst("(\\W)(G)(\\W)", "$1"+Always+"$3");

		formulaCode = formulaCode.replaceFirst("(^F)(\\W)", Finally+"$2");
		formulaCode = formulaCode.replaceFirst("(\\W)(F)(\\W)", "$1"+Finally+"$3");

		formulaCode = formulaCode.replaceFirst("<>", Finally);
		formulaCode = formulaCode.replaceFirst("\\[\\]", Always);
		
		formulaCode = formulaCode.replaceAll("\\&\\&|\\&", AND);
		formulaCode = formulaCode.replaceAll("\\|\\||\\|", OR);
		formulaCode = formulaCode.replaceAll("==|=", Equals);
		if(this.sFormular == null){
			this.sFormular = new String();
		}
		
		if(!this.sFormular.equals(formulaCode)){
			this.sFormular = formulaCode;
			this.message = "";
			this.state = STATE_UNCHECKED;
		}
	}

	@Override
	public String getsID() {
		return sID;
	}

	@Override
	public int getState() {
		return this.state;
	}
	@Override
	public UUID getUUID() {
		return this.uuID;
	}
	/**
	 * @param state the state to set
	 * 
	 * @return whether or not state is a legal state
	 * 
	 *@see IProperty#STATE_COUNTEREXAMPLE
	 *@see IProperty#STATE_FAILED
	 *@see IProperty#STATE_SUCCESS
	 *@see IProperty#STATE_CANCELED
	 *@see IProperty#STATE_SYNTAX_CORRECT
	 *@see IProperty#STATE_SYNTAX_ERROR
	 *@see IProperty#STATE_UNCHECKED
	 *@see IProperty#STATE_VALIDATING
	 *@see IProperty#STATE_WAITING
	 */
	public boolean setState(int state) {
		this.changeLock = false;
		if(state == IProperty.STATE_VALIDATING && this.state == IProperty.STATE_CANCELED){
			return false;
		}
		if((state & STATE_WAITING)!=0){
				this.changeLock = this.state != STATE_SYNTAX_ERROR;
				if(this.changeLock){
					this.state = state;
				}
				return this.changeLock;
		}else if((state & (STATE_CANCELED
						| STATE_SYNTAX_CORRECT
						| STATE_UNCHECKED
						| STATE_FAILED
						| STATE_SYNTAX_ERROR
						| STATE_COUNTEREXAMPLE
						| STATE_VALIDATING
						|STATE_PAUSED
						| STATE_SUCCESS))!= 0){
				this.state = state;
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param checked indicates whether this property should be verified or not
	 */
	public void use(boolean checked) {
		this.use = checked;
	}
	
	@Override
	public boolean useProperty() {
		return this.use;
	}
	
	@Override
	public boolean isLocked() {
		return this.changeLock;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the counterexample
	 */
//	public AbstractCounterexample getCounterexample() {
//		return this.counterexample;
//	}

	/**
	 * @param counterexample the counterexample to set
	 */
//	public void setCounterexample(AbstractCounterexample counterexample) {
//		this.counterexample = counterexample;
//	}
	
	
	
}
