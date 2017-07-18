package xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree;

import java.util.ArrayList;
import java.util.List;

import xstampp.stpatcgenerator.model.stateflow.chart.parse.*;
import java.util.HashSet;
import java.util.Set;

public class Tree {

    StateNode root;
    int size;
    List<StateTransition> truthTable = new ArrayList<StateTransition>(); // extract
    // all
    // transitions
    private int totalOfTransitions = 0;

    private List<DataVariable> datavariables = new ArrayList<DataVariable>();
    private List<Action> StatesActions = new ArrayList<Action>();

    public List<DataVariable> getDatavariables() {
        return datavariables;
    }

    public List<Action> getStatesActions() {
        return StatesActions;
    }

    public void setStatesActions(List<Action> statesActions) {
        StatesActions = statesActions;
    }

    public void setDatavariables(List<DataVariable> datavariables) {
        this.datavariables = datavariables;
    }

    public int getTotalOfTransitions() {
        return totalOfTransitions;
    }

    public void setTotalOfTransitions(int totalOfTransitions) {
        this.totalOfTransitions = totalOfTransitions;
    }

    private List<StateNode> listOfState = new ArrayList<StateNode>();

    public List<StateTransition> getTruthTable() {
        return truthTable;
    }

    public void setTruthTable(List<StateTransition> truthTable) {
        this.truthTable = truthTable;
    }

    public List<StateNode> getListOfState() {
        return listOfState;
    }

    public void setListOfState(List<StateNode> listOfState) {
        this.listOfState = listOfState;
    }

    public Tree() {
        super();
        truthTable = new ArrayList<StateTransition>();
        listOfState = new ArrayList<StateNode>();
    }

    public StateNode getRoot() {
        return root;
    }

    public void setRoot(StateNode root) {
        this.root = root;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void printTree(StateNode state) {

        if (state == null) {
            return;
        }
        System.out.println(state.toString());
        System.out.println("childern=" + state.getChildren().size());
        if (state.isHasChildren()) {
            for (StateNode s : state.getChildren()) {
                printTree(s);

            }
        }

    }

    public void addAction(Action a) {
        this.StatesActions.add(a);
    }

    public String getNameOfstate(String Id) {
        String str = "";
        if (this.listOfState != null) {
            for (StateNode s : this.listOfState) {
                if (s.getId().equals(Id)) {
                    str = s.getName();
                    break;
                }
            }
        }
        return str;
    }

    public void addAllActionStates(StateNode root) {

        for (Action a : root.getActions()) {
            addAction(a);

        }
        if (root.getChildren().size() > 0) {
            for (StateNode s : root.getChildren()) {
                addAllActionStates(s);
            }

        }

    }

    public void addAllDataVariables(List<Data> datalist) {
        if (datalist != null) {
            for (Data d : datalist) {
                DataVariable var = new DataVariable();
                var.setSSID(d.getsSID());
                var.setName(d.getName());
                if (d.getP().size() > 1) {
                    var.setScope(d.getP().get(0).getValue());
                    var.setType(d.getP().get(1).getValue());
                }
                if (d.getProps().getTypedata().getPdata().size() > 1) {
                    if (d.getProps().getTypedata().getPdata().get(0).getName().equals("method")) {
                        String type = d.getProps().getTypedata().getPdata().get(0).getValue();
                        if (!type.equals("SF_ENUM_TYPE")) {
                            var.setMethod("double");
                        }

                    } else if (d.getProps().getTypedata().getPdata().get(1).getName().equals("primitive")) {
                        {
                            String type = d.getProps().getTypedata().getPdata().get(1).getValue();
                            if (!type.equals("SF_BOOLEAN_TYPE")) {
                                var.setMethod("double");
                            }
                        }
                    } else {
                        var.setMethod("");
                    }
                }

                addDataVaraible(var);
            }

        }
    }

    public void printTruthTable(List<StateTransition> truthTable) {
        if (truthTable != null) {
            for (StateTransition st : truthTable) {

                System.out.println("[SSID=" + st.getSSID() + ", source=" + getNameOfstate(st.getSource()) + " condition =" + st.getCondition() + " Destination=" + getNameOfstate(st.getDestination()) + "]");

            }
        }
    }

    public String getTruthTableAsString(List<StateTransition> truthTable) {
        String str = "";
        if (truthTable != null) {
            for (StateTransition st : truthTable) {

                str += "\n[SSID=" + st.getSSID() + ", source=" + getNameOfstate(st.getSource()) + " condition =" + st.getCondition() + " Destination=" + getNameOfstate(st.getDestination()) + "]";

            }
        }
        return str;
    }

    private void generateTruthTable(List<StateTransition> truthTable, List<StateNode> states) {
        if (states != null) {
            
            for (StateNode s : states) {

                for (Transition t : s.getTransitions()) {

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

                    if (t.getSrc().getSource().get(0) == null) {
                        
                        st.setSource(null);
                    }
                    if (t.getDst().getDestination().get(0).getValue() != null) {
                        st.setDestination(t.getDst().getDestination().get(0).getValue());
                    }

                    truthTable.add(st);

                }

                if (s.isHasChildren()) {

                    generateTruthTable(truthTable, s.getChildren());
                }

            }

        }
    }

    public List<StateTransition> generateTruthTable(StateNode root) {
        List<StateTransition> transitonsTable = new ArrayList<StateTransition>();
        truthTable=new ArrayList<StateTransition>();
        if (root != null) {
           
            for (Transition t : root.getTransitions()) {
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
                 
                transitonsTable.add(st);

            }
           
            if (root.isHasChildren()) {

                generateTruthTable(transitonsTable, root.getChildren());
            }
        }
         truthTable.addAll(transitonsTable);
        // System.out.println("Size of truthtable=" + truthTable.size());
        return truthTable;
    }

    public void addStateInList(StateNode state) {
        this.listOfState.add(state);
    }

    public void printAllstates() {
        for (StateNode s : this.listOfState) {
            System.out.println("ID=" + s.getId() + " Name=" + s.getName());
        }
    }

    public void addDataVaraible(DataVariable datavaraible) {
        this.datavariables.add(datavaraible);
    }

}
