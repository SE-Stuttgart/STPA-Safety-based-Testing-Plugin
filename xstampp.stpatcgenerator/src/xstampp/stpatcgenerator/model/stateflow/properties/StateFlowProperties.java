package xstampp.stpatcgenerator.model.stateflow.properties;

import java.util.List;

import xstampp.stpatcgenerator.model.stateflow.chart.parse.Data;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.State;

public class StateFlowProperties {
//	String statesNumber;
//	String transitionNumber;
//	String inputNumber;
//	
//	public StateFlowProperties(){}
//	
//	public StateFlowProperties(String statesNumber, String transitionNumber, String inputNumber){
//		this.statesNumber = statesNumber;
//		this.transitionNumber = transitionNumber;
//		this.inputNumber= inputNumber;
//	}
//
//	public String getStatesNumber() {
//		return statesNumber;
//	}
//
//	public void setStatesNumber(String statesNumber) {
//		this.statesNumber = statesNumber;
//	}
//
//	public String getTransitionNumber() {
//		return transitionNumber;
//	}
//
//	public void setTransitionNumber(String transitionNumber) {
//		this.transitionNumber = transitionNumber;
//	}
//
//	public String getInputNumber() {
//		return inputNumber;
//	}
//
//	public void setInputNumber(String inputNumber) {
//		this.inputNumber = inputNumber;
//	}

	private List<State> states;
	private List<Data> inputData;
	private List<Data> outputData;
	private List<Data> localData;
	
	public StateFlowProperties(List<State> states, List<Data> inputData, List<Data> outputData, List<Data> localData) {
		this.states = states;
		this.inputData = inputData;
		this.outputData = outputData;
		this.localData = localData;
	}
	
	public List<State> getStates() {
		return states;
	}
	public void setStates(List<State> states) {
		this.states = states;
	}
	public List<Data> getInputData() {
		return inputData;
	}
	public void setInputData(List<Data> inputData) {
		this.inputData = inputData;
	}
	public List<Data> getOutputData() {
		return outputData;
	}
	public void setOutputData(List<Data> outputData) {
		this.outputData = outputData;
	}
	public List<Data> getLocalData() {
		return localData;
	}
	public void setLocalData(List<Data> localData) {
		this.localData = localData;
	}
	
	
}
