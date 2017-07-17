package stpaverifier.model;

import java.util.UUID;

public interface ICounterexample {

	public String[] getContent();
	public String getStringID();
	public String getProperty();
	/**
	 * @return the propertyID
	 */
	public UUID getPropertyID();
}