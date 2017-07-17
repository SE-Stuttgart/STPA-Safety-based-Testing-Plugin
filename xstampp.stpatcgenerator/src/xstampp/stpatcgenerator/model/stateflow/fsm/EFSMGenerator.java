package xstampp.stpatcgenerator.model.stateflow.fsm;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.Tree;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.StateNode;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.StateTransition;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.Action;

public class EFSMGenerator {
	Tree tree;
    EFSM fsm;
    List<StateTransition> truthtable = new ArrayList<StateTransition>();
    List<StateNode> defaultstates = null;

    public EFSMGenerator() {
        fsm = new EFSM();
    }

    private boolean hasNoparent(StateNode state) {
        if (state.getParentID() == null) {
            return true;
        } else {
            return false;
        }
    }

    public EFSMGenerator(Tree stateflowtree) {
        this.tree = stateflowtree;
        fsm = new EFSM();

    }

    private StateNode getStateNode(StateNode root, String stId) {
        if (root.getId().equals(stId)) {
            return root;
        } else if (root.isHasChildren()) {
            for (StateNode s : root.getChildren()) {
                getStateNode(s, stId);
            }
        }
        return null;
    }

    private StateNode getStatebyExecutionOrder(List<StateNode> states) {
        StateNode state = states.get(0);
        int orderMin = 0;
        try {
            orderMin = Integer.parseInt(state.getExecutionOrder());
            for (int i = 1; i < states.size(); i++) {

                if (Integer.parseInt(states.get(i).getExecutionOrder()) < orderMin) {
                    state = states.get(i);
                }

            }
        } catch (NumberFormatException ex) {
            return states.get(0);
        }

        return state;
    }

    public String identifyTypeOFRoot(StateNode state) {
        int numberOR = 0;
        int numberAND = 0;
        boolean sametype = false;
        for (StateNode s : state.getChildren()) {
            if (s.getTypeDecomposition().equals("OR_STATE")) {
                numberOR++;
            } else if (s.getTypeDecomposition().equals("AND_STATE")) {
                numberAND++;
            }

        }
        if (numberOR == state.getChildren().size()) {
            return "OR_STATE";
        } else if (numberAND == state.getChildren().size()) {
            return "AND_STATE";
        } else {
            return "null";
        }

    }

    private boolean isFinalState(StateNode s) {

        if (s.isHasTransitions()) {
            return false;
        } else if (!s.isHasChildren() == true) {
            return true;
        } else {
            return false;
        }
    }

    public List<StateTransition> getfsmTruthTable() {
        return fsm.getfsmTruthTable();
    }

    public String printFSM(EFSM fsm) {
        String str = "";
        if (fsm != null) {
            str += "\nInitial State is ={" + fsm.getInitialState().getName() + "}";
            str += "\n Final States are {";

            for (StateEFSM statefsm : fsm.getFinalStates()) {
                str += statefsm.getName() + ",";
            }
            if (str.endsWith(",")) {
                str = str.substring(0, str.length() - 1);
            }

            str += "}";
            str += "\n states of FSM";

            for (StateEFSM sfsm : fsm.getFsmstates()) {

                if (sfsm.getLinkedStates().size() > 0) {

                    for (StateTransition st : sfsm.getLinkedStatesTransitions(getStateNode(sfsm.getId()))) {

                        str += "\n" + sfsm.getName() + "->" + getStateNode(st.getDestination()).getName();

                    }
                }
            }
        }

        return str;
    }

    private StateTransition createNewTranstions(StateTransition st, StateNode state) {
        StateTransition t = st;
        if (t != null && state != null) {
            if (t.getSource() != null && state.getId() != null) {

                t.setDestination(state.getId());
            }
        }
        return t;
    }

    private void printactions(List<Action> ac) {
        for (Action a : ac) {
            System.out.print(a.toString());
        }

    }

    private StateEFSM createNewFSMState(StateNode s) {
        StateEFSM state = null;
        if (s != null) {
            state = new StateEFSM();
            state.setId(s.getId());
            state.setName(s.getName());
            state.setRoot(false);
            state.setIsFinal(false);
            state.setActions(s.getActions());
            //printactions (s.getActions());
            /* List<StateTransition> transitions = getTransitionsOfState1(s);

            for (StateTransition t : transitions) {

                state.addLinkedState(s, t);
                System.out.println(t.toString());
                fsm.addTransition(t);

            }*/
        }
        return state;
    }

    public HashMap<StateNode, StateTransition> getChildren(StateNode key) {
        HashMap<StateNode, StateTransition> linkedstates = new HashMap<StateNode, StateTransition>();

        for (StateTransition st : this.truthtable) {
            if (st.getSource() != null) {
                if (st.getSource().equals(key.getId())) {
                    StateNode stnode = getStateNode(st.getDestination());
                    linkedstates.put(stnode, st);
                    fsm.addTransition(st);
                }
            }
        }
        return linkedstates;
    }

    public void buildFSMbyTruthtable(StateEFSM state) {
        if (state != null) {
            if (state.getLinkedStates().size() > 0) {

                for (HashMap<StateNode, StateTransition> entry : state.getLinkedStates()) {
                    StateNode key = entry.keySet().iterator().next();
                    HashMap<StateNode, StateTransition> linkedstates = getChildren(key);
                    StateEFSM fsmState = new StateEFSM();
                    fsmState.setId(key.getId());
                    fsmState.setName(key.getName());
                    fsmState.setRoot(false);
                    fsmState.setIsFinal(false);
                    fsmState.addLinkedState(entry);

                    fsm.Gstates.add(fsmState);
                }

            }
        }
    }

    public String generateSartTransitionID() {

        boolean isfound = false;

        int randomID = (int) (Math.random() * (tree.getListOfState().size() * 10) + 1);
        String parentID = Integer.toString(randomID);
        for (StateTransition stateT : tree.getTruthTable()) {
            if (stateT.getSSID().equals(randomID)) {
                isfound = true;
            }
        }

        if (isfound == false) {
            parentID = Integer.toString(randomID);
        } else {
            generateSartTransitionID();

        }
        return parentID;
    }

    private void getDefaultStates(StateNode root) {

        for (StateNode s : root.getChildren()) {

            for (StateTransition st : s.getStateTransitions()) {
                if (st.isDefault()) {

                    defaultstates.add(s);
                }
            }

        }
        /* if (defaultstates == null && root.isHasChildren()) {
            for (StateNode child : root.getChildren()) {
                getDefaultStates(child);
            }
        }*/

    }

    private StateNode getStateNode(String stId) {
        StateNode state = null;

        for (StateNode s : tree.getListOfState()) {
            if (s.getId().equals(stId)) {
                state = s;
                break;
            }
        }
        return state;
    }

    private StateNode getDefaultState(StateNode root) {
        StateNode sDefault = null;
        for (StateTransition st : root.getStateTransitions()) {
            if (st.isDefault()) {
                sDefault = getStateNode(st.getDestination());

                break;

            }

        }
        if (sDefault == null) {
            sDefault = getStatebyExecutionOrder(root.getChildren());

        }

        return sDefault;
    }

    private void updateTruthTableAND(StateNode superState, EFSM fsm) {

        if (superState.isHasChildren()) {
            List<StateNode> children = superState.getChildren();
            for (int i = 0; i < children.size(); i++) {
                for (int j = 0; j < children.size(); j++) {
                    if (!children.get(i).getName().equals(children.get(j).getName())) {
                        if (children.get(i).getTypeDecomposition().equals("AND_STATE") && children.get(j).getTypeDecomposition().equals("AND_STATE")) {
                            StateTransition st = new StateTransition();
                            st.setSSID(generateSartTransitionID());
                            st.setCondition("true");
                            st.setDefault(true);
                            st.setDestination(children.get(j).getId());
                            st.setSource(children.get(i).getId());
                            fsm.getfsmTruthTable().add(st);

                        }
                    }
                }

            }
        }
    }

    private void updateTruthTable(List<StateTransition> fsmTruthtable) {

        List<StateTransition> newTruthTable = new ArrayList<StateTransition>();
        defaultstates = new ArrayList<StateNode>();
        System.out.println("updated");
        tree.printTruthTable(fsmTruthtable);
        for (StateTransition st : fsmTruthtable) {
            StateNode s = getStateNode(st.getSource());
            StateNode d = getStateNode(st.getDestination());

            if (s != null && d != null) {
                // case one source and destination has no children
                if (!s.isHasChildren() && !d.isHasChildren()) {
                    newTruthTable.add(st);
                } else if (s.isHasChildren() && !d.isHasChildren()) {
                    //case when the source has children and destination has no children
                    List<StateNode> children = s.getChildren();
                    for (StateNode child : children) {

                        StateTransition newTransition = new StateTransition();
                        newTransition.setSSID(generateSartTransitionID());
                        newTransition.setDefault(false);
                        newTransition.setCondition(st.getCondition());
                        newTransition.setEventInput(st.getEventInput());
                        newTransition.setFullTransition(st.getFullTransition());
                        newTransition.setTransition(st.getTransition());
                        newTransition.setTransition_Action(st.getTransition_Action());

                        newTransition.setSource(child.getId());

                        newTransition.setDestination(d.getId());
                        newTruthTable.add(newTransition);

                    }

                } else if (!s.isHasChildren() && d.isHasChildren()) {

                    if (d.getTypeDecomposition().equals("OR_STATE")) {

                        StateNode defaultState = getDefaultState(d);
                        // StateNode defaultnode = getDefaultState(d);
                        if (defaultState == null) {

                            defaultstates = d.getChildren();
                            for (StateNode defaultnode : defaultstates) {
                                StateTransition newTransition = new StateTransition();
                                newTransition.setSSID(generateSartTransitionID());

                                newTransition.setDefault(false);
                                newTransition.setCondition(st.getCondition());
                                newTransition.setEventInput(st.getEventInput());
                                newTransition.setFullTransition(st.getFullTransition());
                                newTransition.setTransition(st.getTransition());
                                newTransition.setTransition_Action(st.getTransition_Action());

                                newTransition.setSource(s.getId());
                                newTransition.setDestination(defaultnode.getId());
                                newTruthTable.add(newTransition);
                            }

                        } else {
                            StateTransition newTransition = new StateTransition();
                           newTransition.setSSID(generateSartTransitionID());
                             
                            newTransition.setDefault(false);
                            newTransition.setCondition(st.getCondition());
                            newTransition.setEventInput(st.getEventInput());
                            newTransition.setFullTransition(st.getFullTransition());
                            newTransition.setTransition(st.getTransition());
                            newTransition.setTransition_Action(st.getTransition_Action());
                            newTransition.setSource(s.getId());
                            newTransition.setDestination(defaultState.getId());
                            newTruthTable.add(newTransition);
                        }

                    } else if (d.getTypeDecomposition().equals("AND_STATE")) {

                        for (StateNode state : d.getChildren()) {
                            System.out.println(d.getName() + " " + d.getTypeDecomposition() + "=" + s.getName() + "->" + state.getName());
                            StateTransition newTransition = new StateTransition();
                            newTransition.setSSID(generateSartTransitionID());

                            newTransition.setDefault(false);
                            newTransition.setCondition(st.getCondition());
                            newTransition.setEventInput(st.getEventInput());
                            newTransition.setFullTransition(st.getFullTransition());
                            newTransition.setTransition(st.getTransition());
                            newTransition.setTransition_Action(st.getTransition_Action());

                            newTransition.setSource(s.getId());

                            newTransition.setDestination(state.getId());
                            newTruthTable.add(newTransition);
                        }

                    }

                    //case the source has no children and destination has children
                } else if (s.isHasChildren() && d.isHasChildren()) {

                    for (StateNode child : s.getChildren()) {
                        for (StateNode defaultNode : d.getChildren()) {
                            StateTransition newTransition = new StateTransition();
                            newTransition.setSSID(generateSartTransitionID());

                            newTransition.setDefault(false);
                            newTransition.setCondition(st.getCondition());
                            newTransition.setEventInput(st.getEventInput());
                            newTransition.setFullTransition(st.getFullTransition());
                            newTransition.setTransition(st.getTransition());
                            newTransition.setTransition_Action(st.getTransition_Action());

                            newTransition.setSource(child.getId());

                            newTransition.setDestination(defaultNode.getId());
                            newTruthTable.add(newTransition);
                        }
                    }

                }

            }
        }
        fsm.setfsmTruthTable(newTruthTable);
    }

    public void generateTranstions(List<StateTransition> transitionTable, StateEFSM node) {
        List<StateTransition> transitions = new ArrayList<StateTransition>();

        for (StateTransition st : truthtable) {
            if (st.getSource() != null) {
                if (st.getSource().equals(node.getId())) {
                    node.addOutputTranstion(st);
                }
            }
            if (st.getSource() == null) {
                if (st.getDestination().equals(node.getId())) {
                    node.addInputTranstion(st);
                }
            }

        }

    }

    public boolean hasChildrenState(List<StateTransition> transitionTable) {
        boolean noSuperState = false;

        for (StateTransition st : transitionTable) {
            if (st.getSource() != null) {
                StateNode s = getStateNode(st.getSource());
                if (s.isHasChildren()) {
                    noSuperState = true;
                    break;
                }
            }

            if (st.getDestination() != null) {
                StateNode d = getStateNode(st.getDestination());
                if (d.isHasChildren()) {
                    noSuperState = true;
                    break;
                }
            }
        }
        return noSuperState;
    }

    public void unVisitedstates(EFSM fsm) {
        for (StateEFSM s : fsm.getFsmGstates()) {
            s.setIsvisited(false);
        }
    }

    public void parsefsmTree(StateNode initial, List<StateTransition> fsmTruthtable) {
        List<StateNode> states = null;
        if (initial.isIsvisited() == false) {
            StateEFSM fsmstate = createNewFSMState(initial);

            for (StateTransition stFsm : fsmTruthtable) {
                if (stFsm.getSource().equals(initial.getId())) {
                    StateNode d = getStateNode(stFsm.getDestination());

                    fsmstate.addLinkedState(d, stFsm);

                }

            }
            initial.setIsvisited(true);
            fsm.Gstates.add(fsmstate);
        }
    }

    public StateNode identifyInitailState(StateNode root, List<StateNode> states) {
        StateNode initalstate = null;
        if (states != null & root != null) {
            if (root.getTypeDecomposition().equals("OR_STATE")) {
                // initalstate = getStatebyExecutionOrder(states);
                initalstate = getDefaultState(root);

            } else if (root.getTypeDecomposition().equals("AND_STATE")) {
                initalstate = getStatebyExecutionOrder(states);
            } else {
                initalstate = getDefaultState(root);
            }
        }
        return initalstate;
    }

    public EFSM generateFSM() {
        // fsm = new EFSM();

        if (tree != null) {

            StateNode root = tree.getRoot();

            // System.out.println("Root " + root.getName());
            StateEFSM start = new StateEFSM();

            truthtable = tree.generateTruthTable(tree.getRoot());

            fsm.setDatavariables(tree.getDatavariables());
            StateNode initialstate;

            initialstate = root;
            if (root.isHasChildren()) {
                int i = 0;
                do {
                    initialstate = identifyInitailState(initialstate, initialstate.getChildren());

                    i++;
                    if (initialstate == null) {
                        break;
                    }

                } while (initialstate.isHasChildren() && i < 100);
                if (initialstate == null) {
                    initialstate = root.getChildren().get(0);
                }
                if (i >= 100) {
                    initialstate = identifyInitailState(tree.getRoot(), tree.getRoot().getChildren());
                }

                initialstate.setIsRoot(true);
                start = createNewFSMState(initialstate);
                start.setIsInitial(true);

                fsm.setInitialState(start);

                generateTranstions(truthtable, start);

                fsm.setfsmTruthTable(truthtable);

                //updateTruthTable(fsm.getfsmTruthTable());
                // update TruthTable with AND states 
                for (StateNode s : tree.getListOfState()) {
                    if (s.isHasChildren()) {

                        updateTruthTableAND(s, fsm);
                    }
                }
                while (hasChildrenState(fsm.getfsmTruthTable())) {

                    updateTruthTable(fsm.getfsmTruthTable());

                }

                int steps = fsm.getfsmTruthTable().size() * 100;
                //fsm.setfsmTruthTable(newTruthtable);

                /* while (hasChildrenState(fsm.getfsmTruthTable())) {
                    steps--;
                    updateTruthtableSource(fsm.getfsmTruthTable());
                    //updateTruthtableDestination(fsm.getfsmTruthTable());
                    if (steps <= 0 && hasChildrenState(truthtable)) {
                        break;
                    }
                }*/
                if (steps > 0) {

                    fsm.StartState = start;
                    //fsm.addTransition(startTransition);
                    System.out.println("Satring parsing EFSM model....");

                    for (StateNode state : tree.getListOfState()) {
                        state.setIsvisited(false);
                        if (state.isHasChildren() == false) {
                            parsefsmTree(state, fsm.getfsmTruthTable());
                        }
                    }

                } else if (steps <= 0) {
                    System.out.println("The tool could not generated EFMS of satechart");
                    fsm.setFSMErrors("The tool could not generated EFMS of satechart");
                }
                //buildFSMbyTruthtable(start);
            }

        }

        if (fsm.finalStates.size() <= 0) {
            fsm.finalStates.add(fsm.initialState);
        }

        //LinkedHashSet link = new LinkedHashSet(fsm.getFsmGstates());
        //List<StateEFSM> listOfValues = new ArrayList<StateEFSM>();
        //listOfValues.addAll(link);
        //fsm.setFsmstates(listOfValues);
        unVisitedstates(fsm);

        return fsm;
    }
}
