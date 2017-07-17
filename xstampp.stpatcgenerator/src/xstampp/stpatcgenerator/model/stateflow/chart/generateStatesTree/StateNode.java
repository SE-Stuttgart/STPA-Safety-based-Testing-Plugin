package xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import xstampp.stpatcgenerator.model.stateflow.chart.parse.Action;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.State;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.StateTransition;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.Transition;

public class StateNode {

    private List<StateNode> children = new ArrayList<StateNode>();
    private String id;
    private String parentID;
    private String name;
    private boolean isRoot = false;
    private boolean hasChildren = false;
    private List<Transition> transitions = new ArrayList<Transition>();
    private List<StateTransition> stateTransitions = new ArrayList<StateTransition>();
    private String typeDecomposition;
    private boolean hasTransitions = false;
    private String executionOrder = null;
    private boolean isSubModule = false;
    private List<Action> actions = new ArrayList<Action>();

    public boolean isIsRoot() {
        return isRoot;
    }

    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public boolean isIsSubModule() {
        return isSubModule;
    }

    public void setIsSubModule(boolean isSubModule) {
        this.isSubModule = isSubModule;
    }

    public boolean isIsvisited() {
        return isvisited;
    }

    public void setIsvisited(boolean isvisited) {
        this.isvisited = isvisited;
    }
    private boolean isvisited = false;

    public List<Action> getActions() {
        return actions;
    }

    public void addAction(Action action) {
        this.actions.add(action);
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public boolean isSubModule() {
        return isSubModule;
    }

    public void setSubModule(boolean isSubModule) {
        this.isSubModule = isSubModule;
    }

    public String getExecutionOrder() {
        return executionOrder;
    }

    public void setExecutionOrder(String executionOrder) {
        this.executionOrder = executionOrder;
    }

    public boolean hasTransitions() {
        return hasTransitions;
    }

    public void setHasTransitions(boolean hasTransitions) {
        this.hasTransitions = hasTransitions;
    }

    public String getTypeDecomposition() {
        return typeDecomposition;
    }

    @Override
    public String toString() {
        return "StateNode [ id=" + id + ", parentID=" + parentID + ", name=" + name
                + ", isRoot=" + isRoot + ", isSubModule=" + isSubModule + ", hasChildren=" + hasChildren + ", typeDecomposition=" + typeDecomposition + "]";
    }

    public void setTypeDecomposition(String typeDecomposition) {
        this.typeDecomposition = typeDecomposition;
    }

    public void addTransition(Transition t) {
        if (t != null && transitions != null) {
            this.transitions.add(t);
        }
    }

    public List<StateNode> getChildren() {
        return children;
    }

    public void setChildren(List<StateNode> children) {
        this.children = children;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getRoot() {
        return isRoot;
    }

    public void setRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public void addChild(StateNode child) {
        children.add(child);
    }

    public StateNode() {
        this.children = new ArrayList<StateNode>();
        this.transitions = new ArrayList<Transition>();

    }

    public List<Transition> removeRepated(List<Transition> lt) {

        // add elements to al, including duplicates
        HashSet<Transition> hs = new LinkedHashSet<Transition>(lt);
        List<Transition> al = new ArrayList<Transition>(hs);

        return al;
    }

    public void addALLAciontOfChildren(List<State> states) {
        for (State s : states) {
            addALLAcionts(s.getActions());
        }

    }

    public void addALLAcionts(List<Action> actions) {
        for (Action a : actions) {
                
               if (a.getType().equals("Enum"))
               {
                   a.setValue(a.getFunction());
                   
               }
            this.addAction(a);
        }
    }

    public void addAllTransitions(List<Transition> lt) {
        lt = removeRepated(lt);

        if (lt != null) {

            for (Transition t : lt) {

                StateTransition st = new StateTransition();
                st.setSSID(t.getSSID());
                if (t.getP().get(0) != null) {
                    st.setTransition(t.getP().get(0).getValue());
                }
                if (t.getSrc().getSource().get(0) != null) {
                    String source = t.getSrc().getSource().get(0).getValue();
                    if (source.contains("[") && source.contains("]")) {
                        st.setSource(null);
                    } else {
                        st.setSource(t.getSrc().getSource().get(0).getValue());
                    }
                }
                if (st.getSource() == null) {
                    st.checkIsDefaultTransition();
                }
                if (t.getDst().getDestination().get(0).getValue() != null) {
                    st.setDestination(t.getDst().getDestination().get(0).getValue());
                }

                this.stateTransitions.add(st);

            }
        }
    }

    public void addAllTransitionsOfSingalNode(List<Transition> lt) {
        lt = removeRepated(lt);

        if (lt != null) {

            for (Transition t : lt) {

                StateTransition st = new StateTransition();
                st.setSSID(t.getSSID());
                if (t.getP().get(0) != null) {
                    st.setTransition(t.getP().get(0).getValue());
                }
                if (t.getSrc().getSource().get(0) != null) {
                    String source = t.getSrc().getSource().get(0).getValue();
                    if (source.contains("[") && source.contains("]")) {
                        st.setSource(null);
                    } else {
                        st.setSource(t.getSrc().getSource().get(0).getValue());
                    }
                }
                if (st.getSource() == null) {
                    st.checkIsDefaultTransition();
                }
                if (t.getDst().getDestination().get(0).getValue() != null) {
                    st.setDestination(t.getDst().getDestination().get(0).getValue());
                }

                if (st.getSource() == null && st.getDestination().equals(this.getId())) {
                    
                    this.stateTransitions.add(st);
                } else if (st.getSource() != null) {
                    if (st.getSource().equals(this.getId())) {
                        this.stateTransitions.add(st);
                    }
                    
                      
                    
                }

            }
        }
    }

    public List<StateTransition> getStateTransitions() {

        return stateTransitions;
    }

    public void setStateTransitions(List<StateTransition> stateTransitions) {
        this.stateTransitions = stateTransitions;
    }

    public boolean isHasTransitions() {
        return hasTransitions;
    }
}
