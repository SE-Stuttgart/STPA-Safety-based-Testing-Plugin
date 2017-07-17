package stpaverifier.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;

public class VerificationStateProvider extends AbstractSourceProvider {

	public static final String VERIFIER_TYPE ="verifier.checker.type";
	public static final String VERIFIER_STATE ="verifier.run.state";
	public static final String PAUSED ="paused";
	public static final String RUNNING ="running";
	public static final String READY ="ready";
	public static final String NUSMV ="NuSMV";
	public static final String SPIN ="SPIN";
	
	private Map<String,String> verifierStates;
	
	public VerificationStateProvider() {
		verifierStates = new HashMap<String, String>();
		verifierStates.put(VERIFIER_STATE, READY);
		verifierStates.put(VERIFIER_TYPE, SPIN);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Map getCurrentState() {
		return verifierStates;
	}

	@Override
	public String[] getProvidedSourceNames() {
		return verifierStates.keySet().toArray(new String[0]);
	}

	public void setVerifierState(String verifierState) {
		verifierStates.put(VERIFIER_STATE, verifierState);
		fireSourceChanged(0, VERIFIER_STATE, verifierState);
	}
	
	public void setVerifier(String verifierType) {
		verifierStates.put(VERIFIER_TYPE, verifierType);
		fireSourceChanged(0, VERIFIER_TYPE, verifierType);
	}
}
