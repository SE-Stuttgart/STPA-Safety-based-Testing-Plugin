package stpaverifier.util;

import stpaverifier.controller.IProperty;
import stpaverifier.controller.model.STPAVerifierController;
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
public abstract class SpinCMDScanner extends LoggerJob{

	private final static String DEPTH_REACHED = "depth reached";
	private final static String TRANSITIONS = "transitions";
	private final static String MEMORY_USED = "total actual memory usage";
	private final static String STORED_STATES = "states, stored";
	private final static String TIME_ELAPSED = "elapsed time";
	
	public SpinCMDScanner(String name,
			STPAVerifierController dataModelController, IProperty formular) {
		super(name, dataModelController, formular);
		
	}

	@Override
	public int scanLine(String line, int current) {
		if(line.contains("pml.trail")){
			return IProperty.STATE_COUNTEREXAMPLE;
		}
		if(line.toLowerCase().contains("spin: error")){
			addPropertyMessage(line);
			return IProperty.STATE_FAILED & IProperty.STATE_CANCELED;
		}
		if(line.contains("_spin") || line.contains("Error")){
			addPropertyMessage(line);
			return IProperty.STATE_SYNTAX_ERROR;
		}else if(line.contains("assertion violated")){
    		return IProperty.STATE_FAILED;
    	}
		if(line.contains("^")){
			addPropertyMessage(line);
			return IProperty.STATE_SYNTAX_ERROR;
		}
		
		int pos;
		if (line.contains(SPIN_VERSION_STRING)) {
			pos = line.indexOf(SPIN_VERSION_STRING);
			getModel().setModelChecker(line.substring(pos,pos + SPIN_VERSION_STRING.length() + 6));
		}
		if (line.contains(DEPTH_REACHED)) {
			pos = line.indexOf(DEPTH_REACHED) + DEPTH_REACHED.length();
			getVerifyResult().setDepth(getDigit(line.substring(pos).trim())[0].toString());
		} else if (line.contains(TRANSITIONS)) {
			getVerifyResult().setTransitions(getDigit(line.trim())[0].toString());
		} else if (line.contains(STORED_STATES)) {
			getVerifyResult().setStoredStates(getDigit(line.trim())[0].toString());
		} else if (line.contains(MEMORY_USED)) {
			getVerifyResult().setUsedMemory(getDigit(line.trim())[0].toString());
		} else if (line.contains(TIME_ELAPSED)) {
			pos = line.indexOf(TIME_ELAPSED) + TIME_ELAPSED.length();
			getVerifyResult().setTime(getDigit(line.substring(pos).trim())[0].toString());
		}
		return current;
	}
}
