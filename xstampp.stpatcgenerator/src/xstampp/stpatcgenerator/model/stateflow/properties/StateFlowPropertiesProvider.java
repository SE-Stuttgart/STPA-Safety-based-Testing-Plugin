package xstampp.stpatcgenerator.model.stateflow.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.Tree;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.StateTransition;
import xstampp.stpatcgenerator.model.stateflow.truthtable.TruthTableStateTransition;
import xstampp.stpatcgenerator.wizards.ConfigurationWizard;

public enum StateFlowPropertiesProvider {
	INSTANCE;
	
	private List<StateFlowProperties> properties;
	
	private StateFlowPropertiesProvider() {
		properties = new ArrayList<StateFlowProperties>();
		Integer transitionNumber = new Integer(0);
		Integer inputNumber = new Integer(0);
		// save state names which are not duplicated
		List<String> states = new ArrayList<String>();
		
		Tree tree = STPATCGModelController.getConfWizard().getTree();
		
		List<StateTransition> truthTable = tree.generateTruthTable(tree
				.getRoot());
		
		if (truthTable != null) {
			for (StateTransition t : truthTable) {
				String sourceState = tree.getNameOfstate(t.getSource());
				String destinationState = tree.getNameOfstate(t.getDestination());
				if(!states.contains(sourceState)) {
					states.add(sourceState);
				}
				if(!states.contains(destinationState)) {
					states.add(destinationState);
				}
			}
		}
//		properties.add(new StateFlowProperties(new Integer(states.size()).toString(), transitionNumber.toString(),inputNumber.toString()));
	}
	
	public List<StateFlowProperties> getProperties() {
		return properties;
	}
}
