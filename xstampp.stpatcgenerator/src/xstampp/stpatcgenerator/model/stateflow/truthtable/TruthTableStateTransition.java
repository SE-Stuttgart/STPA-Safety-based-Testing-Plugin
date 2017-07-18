package xstampp.stpatcgenerator.model.stateflow.truthtable;

public class TruthTableStateTransition {
	private String sourceId;
	private String sourceState;
	private String condition;
	private String destinationId;
	private String destinationState;
	
	public TruthTableStateTransition() {}
	
	public TruthTableStateTransition(String sourceId,String sourceState,String condition,String destinationId,String destinationState) {
		this.sourceId = sourceId;
		this.sourceState = sourceState;
		this.condition = condition;
		this.destinationId = destinationId;
		this.destinationState = destinationState;
	}
	
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getSourceState() {
		return sourceState;
	}
	public void setSourceState(String sourceState) {
		this.sourceState = sourceState;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getDestinationId() {
		return destinationId;
	}
	public void setDestinationId(String destinationId) {
		this.destinationId = destinationId;
	}
	public String getDestinationState() {
		return destinationState;
	}
	public void setDestinationState(String destinationState) {
		this.destinationState = destinationState;
	}
	
	
}
