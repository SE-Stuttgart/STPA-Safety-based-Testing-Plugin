package xstampp.stpatcgenerator.model.smv;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import xstampp.stpatcgenerator.model.stateflow.chart.parse.State;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.StateTransition;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.Transition;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.StateNode;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.DataVariable;


public class SMV {
    private String header;
	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	private  List<DataVariable>  variables =new ArrayList<DataVariable>();
	private Map<String, String> STPAvariables ;
	
	public Map<String, String> getSTPAvariables() {
		return STPAvariables;
	}

	public void setSTPAvariables(Map<String, String> sTPAvariables) {
		STPAvariables = sTPAvariables;
	}

	public List<DataVariable> getVariables() {
		return variables;
	}

	public void setVariables(List<DataVariable> variables) {
		this.variables = variables;
	}

	private  Map<String, StateNode>  subSystems;
	private ArrayList<String> Ltl;
	private  ArrayList<String> init;
	private ArrayList<String> NextSection;
	private  ArrayList<StateTransition>transitions =new ArrayList<StateTransition>();
	private ArrayList<String> headerOfsubsystems;
      
	public ArrayList<String> getHeaderOfsubsystems() {
		return headerOfsubsystems;
	}

	public ArrayList<String> getNextSection() {
		return NextSection;
	}

	public void setNextSection(ArrayList<String> nextSection) {
		NextSection = nextSection;
	}

	public void setHeaderOfsubsystems(ArrayList<String> headerOfsubsystems) {
		this.headerOfsubsystems = headerOfsubsystems;
	}

	private  ArrayList<StateNode>  states=new ArrayList<StateNode>();;
	private  String  controlActions;
    public  String  getControlActions() {
		return controlActions;
	}

	public void setControlActions(String controlActions) {
		this.controlActions = controlActions;
	}

	private String smvstr =null;
    
	public String getSmvstr() {
		return smvstr;
	}

	public void setSmvstr(String smvstr) {
		this.smvstr = smvstr;
	}

	public ArrayList<String> getInit() {
		return init;
	}

	public void setInit( ArrayList<String> init) {
		this.init = init;
	}

	public void addTransiton(StateTransition t)
	{
		this.transitions.add(t);
	}
	public  ArrayList<StateTransition> getTransitions() {
		return transitions;
	}

	public void setTransitions( ArrayList<StateTransition> transitions) {
		this.transitions = transitions;
	}

	public   ArrayList<StateNode> getStates() {
		return states;
	}
	public   void addState(StateNode s) {
		this.states.add(s);
	}
	public void setStates(  ArrayList<StateNode>  states) {
		this.states = states;
	}

	public void loadVariables() {

	}

	 
	public void addSubSystems (String name, StateNode node)
	{
		if (subSystems!=null)
		{
			subSystems.put(name, node );
		}
	}
	public Map<String,  StateNode > getSubSystems() {
		return subSystems;
	}

	public void setSubSystems(Map<String,  StateNode > subSystems) {
		this.subSystems = subSystems;
	}

	public ArrayList<String> getLtl() {
		return Ltl;
	}

	public void setLtl(ArrayList<String> ltl) {
		Ltl = ltl;
	}

	public SMV() {
  this.states=new ArrayList<StateNode>();
  this.subSystems=new HashMap<String, StateNode>();
	}

}

