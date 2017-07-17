package stpaverifier.util;

import stpaverifier.controller.IProperty;
import stpaverifier.controller.model.STPAVerifierController;
import stpaverifier.model.properties.CTLFormular;
import stpaverifier.model.properties.LTLFormular;
import stpaverifier.util.jobs.LoggerJob;


/**
 * NuSMVCMDScanner is an abstraction of {@link LoggerJob} that only implements the abstract method
 * {@link #scanLine(String, int)} for cmd runs of the nusmv model checker.
 * 
 * The reason this class exists is to ensure that all jobs that execute nusmv model checking
 * can by extending this class access the same scanLine method 
 * 
 * @author Lukas Balzer
 * @since 1.0.0
 */
public abstract class NuSMVCMDScanner extends LoggerJob{

	private String counterexample;
	private boolean hasCE;
	protected boolean checkLTL;
	protected boolean checkCTL;
	
	public NuSMVCMDScanner(String name,
			STPAVerifierController dataModelController, IProperty formular) {
		super(name, dataModelController, formular);
		hasCE = false;
		this.checkLTL = formular instanceof LTLFormular;
		this.checkCTL = formular instanceof CTLFormular;

		counterexample="";
	}

	@Override
	public int scanLine(String line, int current) {
		try{

			if(line.toLowerCase().contains("error")){
				addPropertyMessage(line);
				return IProperty.STATE_SYNTAX_ERROR;
			}
			if(line.contains("aborting")){
				return IProperty.STATE_CANCELED | current;
			}
			
			if (line.contains(NuSMV_VERSION_STRING)) {
				int pos = line.indexOf(NuSMV_VERSION_STRING);
				getModel().setModelChecker(line.substring(pos,pos + NuSMV_VERSION_STRING.length() + 6));
			}
			if(line.matches("-- specification.*is false") || line.equals("-- as demonstrated by the following execution sequence")){
				this.hasCE = true;
				return IProperty.STATE_COUNTEREXAMPLE;
			}else if(line.contains("Memory in use")){
				getVerifyResult().setUsedMemory(getDigit(line)[0].toString());
			}else if(line.contains("table growth")){
				getVerifyResult().setDepth(getDigit(line)[0].toString());
			}else if(line.contains("reachable states")){
				getVerifyResult().setStoredStates(getDigit(line)[1].toString());
			}else if(line.contains("Fair transitions")){
				getVerifyResult().setTransitions(getDigit(line)[0].toString());
			}else if(line.contains("ERROR")){
				addPropertyMessage(line);
				return IProperty.STATE_SYNTAX_ERROR;
			}else if(line.contains("aborting")){
				addPropertyMessage(line);
				return IProperty.STATE_CANCELED & current;
			}else if(line.contains("seconds")){
				getVerifyResult().setTime(getDigit(line)[1].toString());
				hasCE = false;
			}
		}catch(IndexOutOfBoundsException e){}
		
		if(hasCE){
			counterexample = counterexample + line+"\n";
		}
		return current;
	}

	/**
	 * @return the counterexample
	 */
	public String getCounterexample() {
		return this.counterexample;
	}

	/**
	 * @return the hasCE
	 */
	public boolean isHasCE() {
		return this.hasCE;
	}
}
