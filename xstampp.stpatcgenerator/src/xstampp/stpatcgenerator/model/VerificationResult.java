package xstampp.stpatcgenerator.model;

import xstampp.stpatcgenerator.controller.IProperty;
import xstampp.stpatcgenerator.controller.IVerificationResult;
import xstampp.model.AbstractLTLProvider;

/**
 * instances of this class can store result informations of a SSR verification run
 * This informations are used to set up a result table as described by Asim Abdulkhaleq and Stefan Wagner
 * of the Institute of Software Technology, University of Stuttgart in the paper </p><code>
 * Integrated Safety Analysis <br>Using Systems-Theoretic Process Analysis and <br>Software Model Checking  
 * 
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public class VerificationResult implements IVerificationResult {
	
	private String ssrLiteral;
	private String depth;
	private String storedStates;
	private String transitions;
	private String time;
	private String usedMemory;
	private String result;
	private AbstractLTLProvider provider;
	
	
	/* (non-Javadoc)
	 * @see stpaverifier.model.IVerificationResult#getSsrLiteral()
	 */
	@Override
	public String getSsrLiteral() {
		return this.ssrLiteral;
	}
	/**
	 * @param ssrLiteral the ssrLiteral to set
	 */
	public void setSsrLiteral(String ssrLiteral) {
		this.ssrLiteral = ssrLiteral;
	}
	/* (non-Javadoc)
	 * @see stpaverifier.model.IVerificationResult#getDepth()
	 */
	@Override
	public String getDepth() {
		return String.valueOf(this.depth);
	}
	/**
	 * @param depth the depth to set
	 */
	public void setDepth(String depth) {
		this.depth = depth;
	}
	/* (non-Javadoc)
	 * @see stpaverifier.model.IVerificationResult#getStoredStates()
	 */
	@Override
	public String getStoredStates() {
		return String.valueOf(this.storedStates);
	}
	/**
	 * @param storedStates the storedStates to set
	 */
	public void setStoredStates(String storedStates) {
		this.storedStates = storedStates;
	}
	/* (non-Javadoc)
	 * @see stpaverifier.model.IVerificationResult#getTransitions()
	 */
	@Override
	public String getTransitions() {
		return String.valueOf(this.transitions);
	}
	/**
	 * @param transitions the transitions to set
	 */
	public void setTransitions(String transitions) {
		this.transitions = transitions;
	}
	/* (non-Javadoc)
	 * @see stpaverifier.model.IVerificationResult#getTime()
	 */
	@Override
	public String getTime() {
		return String.valueOf(this.time);
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}
	/* (non-Javadoc)
	 * @see stpaverifier.model.IVerificationResult#getUsedMemory()
	 */
	@Override
	public String getUsedMemory() {
		return String.valueOf(this.usedMemory);
	}
	/**
	 * @param usedMemory the usedMemory to set
	 */
	public void setUsedMemory(String usedMemory) {
		this.usedMemory = usedMemory;
	}
	/* (non-Javadoc)
	 * @see stpaverifier.model.IVerificationResult#getResult()
	 */
	@Override
	public String getResult() {
		return this.result;
	}
	/**
	 * @param returnState the result to set
	 */
	public void setResult(int returnState) {
		if((returnState& IProperty.STATE_CANCELED)!= 0){
			this.result = INTERRUPTED;
		}else if((returnState &(IProperty.STATE_COUNTEREXAMPLE 
								|IProperty.STATE_SYNTAX_ERROR 
								|IProperty.STATE_FAILED		))!= 0){
			this.result = FAILS;
		}else{
			this.result = SATISFIED;
		}
	}
	/**
	 * @param returnState the result to set
	 */
	public void setResult(String res) {
		this.result = res;
		
	}
	/* (non-Javadoc)
	 * @see stpaverifier.model.IVerificationResult#getProvider()
	 */
	@Override
	public AbstractLTLProvider getProvider() {
		return this.provider;
	}
	/**
	 * @param provider the provider to set
	 */
	public void setProvider(AbstractLTLProvider provider) {
		this.provider = provider;
	}
}
