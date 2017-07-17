package xstampp.stpatcgenerator.model.generateTestCases;

import java.util.ArrayList;
import java.util.List;

import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.StateNode;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.Action;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.Action.ActionType;
import xstampp.stpatcgenerator.model.stateflow.fsm.EFSM;
import xstampp.stpatcgenerator.ui.views.StateFlowPropertiesView;

/**
 * The object of this class is a structured extended finite state machine transition object.
 * 
 * @author Ting Luk-He
 *
 */
public class SimpleEFSMTransition {
	String id;
	String sourceStateId;
	String sourceState;
	String controlAction;
	List<String> conditions;
	EFSM fsm;
	
	public SimpleEFSMTransition(EFSM fsm, String id, String sourceStateId, List<String> conditions){
		this.id = id;
		this.sourceStateId = sourceStateId;
		this.sourceState = fsm.getNameOfstate(sourceStateId);
		this.conditions = conditions;
		this.controlAction = initCA(sourceStateId);
	}
	
	// search and find the relevant control action for a certain state
	private String initCA (String stateId){
		System.out.println("Initial Ca " + stateId);
		String ca = "";
		List<StateNode> stateNodeList = StateFlowPropertiesView.getStatesList();
		for(StateNode s: stateNodeList){
			if(s.getId().equals(stateId)){
				List<Action> actions = s.getActions();
				for(Action a: actions){
					System.out.println("ActionType is " + a.getType());
					if(a.getType() == ActionType.Entry) {
						if(a.getName().equals("controlAction")){
							System.out.println("Control Action is " + a.getFunction());
							ca = a.getFunction();
						}
					}
				}
			}
		}		
		return ca;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSourceState() {
		return sourceState;
	}
	public void setSourceState(String sourceState) {
		this.sourceState = sourceState;
	}
	
	public String getSourceStateId() {
		return sourceStateId;
	}
	public void setSourceStateId(String sourceStateId) {
		this.sourceStateId = sourceStateId;
	}
	
	public List<String> getConditions() {
		return conditions;
	}
	public void setConditions(List<String> conditions) {
		this.conditions = conditions;
	}
	
	public String getControlAction() {
		return controlAction;
	}

	public void setControlAction(String controlAction) {
		this.controlAction = controlAction;
	}

	@Override
	public String toString(){
		return "SimpleEFSMTransition: [ID: " + id + " SourceState: " + sourceStateId + " Conditions" + conditions.toString() + "]";
	}
}
