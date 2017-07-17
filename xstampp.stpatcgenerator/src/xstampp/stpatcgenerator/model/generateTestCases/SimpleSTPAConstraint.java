package xstampp.stpatcgenerator.model.generateTestCases;

import java.util.List;

/**
 * The object of this class is a structured STPA Safety Requirement object.
 * 
 * @author Ting Luk-He
 *
 */
public class SimpleSTPAConstraint {
	private String SSID;
	private String controlAction;
	private String sourceState;
	private List<String> transition;
	public SimpleSTPAConstraint(String SSID, String controlAction, String sourceState, List<String> transition) {
		this.SSID = SSID;
		this.controlAction = controlAction;
		this.sourceState = sourceState;
		this.transition = transition;
	}
	
	public String getSSID() {
		return SSID;
	}

	public void setSSID(String sSID) {
		SSID = sSID;
	}

	public String getControlAction() {
		return controlAction;
	}
	public void setControlAction(String controlAction) {
		this.controlAction = controlAction;
	}
	public String getSourceState() {
		return sourceState;
	}
	public void setSourceState(String sourceState) {
		this.sourceState = sourceState;
	}
	public List<String> getTransition() {
		return transition;
	}
	public void setTransition(List<String> transition) {
		this.transition = transition;
	}
	
	@Override
	public String toString(){
		return "SimpleSTPAConstraint: [SSID: " + SSID + " ControlAction: " + controlAction + " SourceState: " + sourceState + " Transition" + transition.toString() + "]";
	}
}
