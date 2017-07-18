package xstampp.stpatcgenerator.model.stateflow.fsm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xstampp.stpatcgenerator.model.stateflow.chart.parse.*;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.StateNode;

public class StateEFSM {
	interface Visitor {

        void visited(State state);
    }

    enum StateType {
        Initial(false),
        Final(true),
        Error(false);
        final boolean accepting;

        StateType(boolean accepting) {
            this.accepting = accepting;
        }
    }
    private String id;
    private List<HashMap<StateNode, StateTransition>> linkedStates = null;
    private List<StateTransition> inputTransitions = null;
    private List<StateTransition> outputTransitions = null;
    private List<Action> Actions = new ArrayList<Action>();

    public List<Action> getActions() {
        return Actions;
    }

    public void setActions(List<Action> Actions) {
        this.Actions = Actions;
    }
    private boolean isRoot = false;

    public void addInputTranstion(StateTransition st) {
        this.inputTransitions.add(st);
    }

    public void addOutputTranstion(StateTransition st) {
        this.outputTransitions.add(st);
    }

    public List<StateTransition> getInputTransitions() {
        return inputTransitions;
    }

    public void setInputTransitions(List<StateTransition> inputTransitions) {
        this.inputTransitions = inputTransitions;
    }

    public List<StateTransition> getOutputTransitions() {
        return outputTransitions;
    }

    public void setOutputTransitions(List<StateTransition> outputTransitions) {
        this.outputTransitions = outputTransitions;
    }

    public boolean isIsRoot() {
        return isRoot;
    }

    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }
    private String Name;
    private boolean isFinal = false;

    public boolean isIsvisited() {
        return isvisited;
    }

    public void setIsvisited(boolean isvisited) {
        this.isvisited = isvisited;
    }
    private boolean isInitial = false;
    private StateType statetype;
    private boolean isvisited = false;

    public void setIsFinal(boolean isfinal) {
        this.isFinal = isfinal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getIsFinal() {
        return this.isFinal;
    }

    public void setIsInitial(boolean isinitial) {
        this.isInitial = isinitial;
    }

    public void setLinkedStates(List<HashMap<StateNode, StateTransition>> linkedstates) {
        this.linkedStates = linkedstates;
    }

    public List<HashMap<StateNode, StateTransition>> getLinkedStates() {
        return this.linkedStates;
    }

    public void addLinkedState(HashMap<StateNode, StateTransition> entry) {
        if (this.linkedStates != null) {

            this.linkedStates.add(entry);

        }
    }

    public List<StateNode> getLinkedStatesAsList() {
        List<StateNode> children = new ArrayList<StateNode>();

        if (this.linkedStates != null) {
            for (HashMap<StateNode, StateTransition> entry : this.linkedStates) {
                for (StateNode s : entry.keySet()) {
                    children.add(s);
                }

            }
        }
        return children;
    }

    public List<StateTransition> getLinkedStatesTransitions(StateNode root) {
        List<StateTransition> transtions = new ArrayList<StateTransition>();

        if (this.linkedStates != null) {
            for (HashMap<StateNode, StateTransition> enrty : this.linkedStates) {
                StateTransition value = enrty.values().iterator().next();
                transtions.add(value);
            }
        }
        return transtions;
    }

    public void addLinkedState(StateNode state, StateTransition stateTranistion) {
        if (this.linkedStates != null) {
            HashMap<StateNode, StateTransition> entry = new HashMap<StateNode, StateTransition>();
            entry.put(state, stateTranistion);
            this.linkedStates.add(entry);

        }
    }

    public boolean getIsInitial() {
        return this.isInitial;
    }

    public StateEFSM() {
        this.linkedStates = new ArrayList<  HashMap<StateNode, StateTransition>>();
        inputTransitions = new ArrayList<StateTransition>();
        outputTransitions = new ArrayList<StateTransition>();
    }

    public StateEFSM(String name) {
        this.Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public StateType getStatetype() {
        return statetype;
    }

    public void setStatetype(StateType statetype) {
        this.statetype = statetype;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    @Override
    public String toString() {
        return "StateFSM{" + "isRoot=" + isRoot + ", Name=" + Name + ", isFinal=" + isFinal + ", isInitial=" + isInitial + ", statetype=" + statetype + '}';
    }
}
