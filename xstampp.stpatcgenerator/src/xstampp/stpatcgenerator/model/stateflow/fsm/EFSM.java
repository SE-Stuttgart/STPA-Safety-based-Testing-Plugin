package xstampp.stpatcgenerator.model.stateflow.fsm;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.DataVariable;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.StateTransition;

public class EFSM {
	public String FSMErrors="";
    StateEFSM StartState;

    public String getFSMErrors() {
        return FSMErrors;
    }

    public void setFSMErrors(String FSMErrors) {
        this.FSMErrors = FSMErrors;
    }

    public void addstate(StateEFSM st) {
        this. Gstates.add(  st);
    }
    
    public List<StateEFSM> getFsmGstates() {
        return Gstates;
    }

    public void setFsmTreestates(List<StateEFSM> fsmTreestates) {
        this.Gstates = fsmTreestates;
    }

    public StateEFSM getStartState() {
        return StartState;
    }

    public void setStartState(StateEFSM StartState) {
        this.StartState = StartState;
    }

     

     
    List<StateEFSM> Gstates;
    StateEFSM initialState;
    StateEFSM currentState;
    List<StateEFSM> finalStates;

    private List<DataVariable> datavariables = new ArrayList<DataVariable>();
    private List <StateTransition> fsmTruthTable = new ArrayList<StateTransition> ();
    
    public void addTransition(StateTransition fsmTransition)
    {
        if (this.fsmTruthTable !=null)
        {
             this.fsmTruthTable.add(fsmTransition);
        }
    }
    public String getNameOfstate(String Id) {
        String str = "";
        if (this.Gstates != null) {
            for (StateEFSM s : this.Gstates) {
                if (s.getId().equals(Id)) {
                    str = s.getName();
                    break;
                }
            }
        }
        return str;
    }

    public StateEFSM getstateFSM(String Id) {
        StateEFSM state = null;
        if (this.Gstates != null) {
            for (StateEFSM s : this.Gstates) {
                if (s.getId().equals(Id)) {
                    state =s;
                    break;
                }
            }
        }
        return state;
    }
 public void printFSMTruthTable(List<StateTransition> truthTable) {
        if (truthTable != null) {
            for (StateTransition st : truthTable) {

                System.out.println("[SSID=" + st.getSSID() + ", source=" + getNameOfstate(st.getSource()) + " condition =" + st.getCondition() + " Destination=" + getNameOfstate(st.getDestination()) + "]");

            }
        }
    }
  
    
    public List<StateEFSM> getFsmstates() {
        return Gstates;
    }

    public List<StateTransition> getfsmTruthTable() {
      //  removeDuplicateElements ();
        return fsmTruthTable;
    }

    public void setfsmTruthTable(List<StateTransition> fsmTruthTable) {
        this.fsmTruthTable = fsmTruthTable;
    }

    public void setFsmstates(List<StateEFSM> fsmstates) {
        this.Gstates = fsmstates;
    }

    public StateEFSM getInitialState() {
        return initialState;
    }

    public void setInitialState(StateEFSM initialState) {
        this.initialState = initialState;
    }

    public StateEFSM getCurrentState() {
        return currentState;
    }

    public void setCurrentState(StateEFSM currentState) {
        this.currentState = currentState;
    }

    public List<StateEFSM> getFinalStates() {
        return finalStates;
    }

    public void setFinalStates(List<StateEFSM> finalStates) {
        this.finalStates = finalStates;
    }

    public List<DataVariable> getDatavariables() {
        return datavariables;
    }

    public void setDatavariables(List<DataVariable> datavariables) {
        this.datavariables = datavariables;
    }

    public void addInitialState(StateEFSM state) {
        this.initialState = state;
    }

    public void removeDuplicateElements ()
    {
        List<StateTransition> newThruthTable = new ArrayList<StateTransition>();
        boolean found =false;
        for (StateTransition st : fsmTruthTable)
        {   found =false;
               for (StateTransition newst : newThruthTable) {
                   
                   if (newst.getSSID().equals(st.getSSID()))
                           {
                               found =true;
                               break;
                           }
                   
               }
                 
            if ( found==false)
            {
               newThruthTable.add(st);
            }
        }
        
         
        setfsmTruthTable(newThruthTable);
    }
    
    
    public EFSM() {
        this.datavariables= new ArrayList<DataVariable>();
        this.Gstates=new ArrayList<StateEFSM> ();
        this.finalStates=new ArrayList<StateEFSM>();
        this.fsmTruthTable = new ArrayList <StateTransition> ();
    }
}
