package xstampp.stpatcgenerator.model.stateflow.chart.parse;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlRootElement(name = "Children")
@XmlAccessorType(XmlAccessType.FIELD)
public class Children {

	@XmlElement(name = "state")
	private List<State> states = new ArrayList<State>();

	@XmlElement(name = "transition")
	private List<Transition> transitions = new ArrayList<Transition>();

	@XmlElement(name = "P")
	private List<P> p = new ArrayList<P>();

	@XmlElement(name = "data")
	private List<Data> data = new ArrayList<Data>();
	
	public List<P> getP() {
		return p;
	}

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
		System.out.println("hello");
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

	public Children() {
		this.p = new ArrayList<P>();
		this.transitions = new ArrayList<Transition>();
		this.states = new ArrayList<State>();
		this.data= new ArrayList<Data>();
	}

	public Children(List<State> states, List<Transition> transitions, List<P> p, List<Data> data) {

		this.states = states;
		this.transitions = transitions;
		this.p = p;
		this.data=data;
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}

	public void addState(State s) {
		this.states.add(s);
	}

	public void addTransition(Transition t) {
		this.transitions.add(t);
	}

	public void addData(Data t)
	{
		this.data.add(t);
	}
	
	 
}
