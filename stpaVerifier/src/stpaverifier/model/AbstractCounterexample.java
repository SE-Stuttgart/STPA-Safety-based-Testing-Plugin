package stpaverifier.model;

import java.util.List;
import java.util.UUID;

public abstract class AbstractCounterexample implements ICounterexample{
	private String stringID;
	private String property;
	private UUID propertyID;
	public AbstractCounterexample(String stringID, String property) {
		this.stringID = stringID;
		this.property = property;
	}
	@Override
	public String getStringID() {
		return stringID;
	}
	
	public abstract String getCounterexample();
	

	public abstract List<String> getLines();
	
	@Override
	public String getProperty() {
		return property;
	}
	@Override
	public UUID getPropertyID() {
		return this.propertyID;
	}
	/**
	 * @param propertyID the propertyID to set
	 */
	public void setPropertyID(UUID propertyID) {
		this.propertyID = propertyID;
	}
}
