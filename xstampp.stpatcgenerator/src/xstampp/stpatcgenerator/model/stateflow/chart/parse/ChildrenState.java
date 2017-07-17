package xstampp.stpatcgenerator.model.stateflow.chart.parse;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
@XmlRootElement(name="Children")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChildrenState {
	

	@XmlElement(name = "state")	
 private List <State> states ;

 @XmlElement(name = "transition")	
 private List<Transition> transitions ;

 @XmlElement(name = "P")
 private List<P> p  ;
 

 @XmlElement(name = "data")
 private List<Data> data  ;

public List<Data> getData() {
	return data;
}

public void setData(List<Data> data) {
	this.data = data;
}

public List<P> getP() {
	return p;
}

public void setP(List<P> p) {
	this.p = p;
}
	
	
 public List<State> getStates() {
	return states;
}
 
public void setStates(List<State> states) {
	this.states = states;
}
  
public ChildrenState() {
 this.p=new ArrayList<P> ();
 this.transitions=new ArrayList<Transition> () ;
 this.states=new ArrayList<State> ();
 this.data=new ArrayList<Data> ();
}

public ChildrenState(List<State> states, List<Transition> transitions, List<P> p) {
	 
	this.states = states;
	this.transitions = transitions;
	this.p = p;
}

public List<Transition> getTransitions() {
	return transitions;
}


public void setTransitions(List<Transition> transitions) {
	this.transitions = transitions;
}
 
public void addState (State s)
{
	this.states.add(s);
}
public void addTransition (Transition t)
{
	this.transitions.add(t);
}
}

