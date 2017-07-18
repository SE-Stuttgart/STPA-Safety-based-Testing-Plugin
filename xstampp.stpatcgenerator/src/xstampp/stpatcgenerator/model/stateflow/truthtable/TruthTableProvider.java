package xstampp.stpatcgenerator.model.stateflow.truthtable;

import java.util.ArrayList;
import java.util.List;

import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.Tree;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.*;
import xstampp.stpatcgenerator.wizards.ConfigurationWizard;

public enum TruthTableProvider {
	INSTANCE;

	private List<TruthTableStateTransition> stateTransitions;

	private TruthTableProvider() {
		
		stateTransitions = new ArrayList<TruthTableStateTransition>();
		
		Tree tree = STPATCGModelController.getConfWizard().getTree();
		
		if (tree != null) {
			System.out.println("tree in "+tree.toString());
			List<StateTransition> truthTable = tree.generateTruthTable(tree
					.getRoot());
			
			if (truthTable != null) {
				for (StateTransition t : truthTable) {
					String[] objects = new String[5];
					objects[0] = t.getSSID();
					objects[1] = tree.getNameOfstate(t.getSource());
					objects[2] = t.getCondition();
					objects[3] = t.getDestination();
					objects[4] = tree.getNameOfstate(t.getDestination());
					stateTransitions.add(new TruthTableStateTransition(objects[0], objects[1],objects[2],objects[3],objects[4]));
				}
			}
		}else {
			System.out.println("tree is null");
		}
		
	}

	public List<TruthTableStateTransition> getStateTransitions() {
		return stateTransitions;
	}
}
