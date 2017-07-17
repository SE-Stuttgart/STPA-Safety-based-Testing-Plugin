package stpaverifier.controller;

import java.util.UUID;

import stpaverifier.model.AbstractCounterexample;
import stpaverifier.model.ICounterexample;

/**
 * an interface that is providing integer constants and functions to manage the state and
 * value of a safety property formula
 * </br>
 * IProperty provides a list of bitwise created Integer constants
 * which can be merged together with the <code>|</code> operator to
 * describe the current state of this property 
 * 
 * 
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public interface IProperty extends Comparable<IProperty>{

	/**
	 * this constant represents the state in which this property is currently validated <code>1</code>
	 */
	public final static int STATE_VALIDATING = 1 << 0;
	/**
	 * this constant represents the state in which a property is added to the queue of validation jobs 
	 * waiting to be executed, when entering this state {@link #isLocked()} should return true to prevent the value from beeing
	 * changed after a validation job is created <code>2</code>
	 */
	public final static int STATE_WAITING = 1 << 1;

	/**
	 * this constant represents the state in which a property validation is
	 * canceled <code>4</code>
	 */
	public final static int STATE_CANCELED = 1 << 2;
	
	/**
	 * this constant represents the state in which a property is
	 * not checked.
	 * This is when the property is new or its value has been changed <code>8</code>
	 */
	public final static int STATE_UNCHECKED = 1 << 3;
	
	/**
	 * this constant represents the state in which a property is
	 * validated.
	 * This state should always be updated when the property changes 
	 * 
	 * <code>16</code>
	 */
	public final static int STATE_SUCCESS = 1 << 4;
	
	
	/**
	 * this constant represents the state in which a property does not hold for a certain model
	 * 
	 * <code>64</code>
	 */
	public final static int STATE_FAILED = 1 << 5;

	/**
	 * This constant represents the state of a property when the validation 
	 * terminates and a counterexample is found
	 * 
	 * <code>96</code>
	 */
	public final static int STATE_COUNTEREXAMPLE = 1 << 6 | STATE_FAILED;
	
	/**
	 * this constant represents the state in which a property's syntax is wrong
	 * 
	 * <code>128</code>
	 */
	public final static int STATE_SYNTAX_ERROR = 1 << 7;

	/**
	 * this constant represents the state in which a property's syntax is wrong
	 * 
	 * <code>256</code>
	 */
	public final static int STATE_SYNTAX_CORRECT = 1 << 8;
	/**
	 * this constant represents the state in which a property validation is paused
	 * 
	 * <code>512</code>
	 */
	public final static int STATE_PAUSED = 1 << 9;
	
	
	
	/**
	 * @param useSpin TODO
	 * @param forExport TODO
	 * @return the sFormular
	 */
	public abstract String getsFormular(boolean useSpin, boolean forExport);

	/**
	 * @return the sID
	 */
	public abstract String getsID();
	
	/**
	 * should always return one of or a combination of the constants declared in this interface,
	 * a combination can be accomplished by using the '|' operator 
	 * @return one of<ul>
	 * <li>{@link #STATE_COUNTEREXAMPLE}
	 * <li>{@link #STATE_FAILED}
	 * <li>{@link #STATE_SUCCESS}
	 * <li>{@link #STATE_UNCHECKED}
	 * <li>{@link #STATE_VALIDATING}
	 * <li>{@link #STATE_WAITING}
	 * </ul>
	 */
	public abstract int getState();
	
	public UUID getUUID();
	
	/**
	 * indicates whether or not this property should be verified
	 * in a run job
	 * 
	 * @return whether or not this property should be verified
	 */
	public boolean useProperty();

	/**
	 * when this property is already waiting for a validation, means that a job object is already created
	 * the formula should not be changed
	 * 
	 * @return the changeLock that is set by the implementation ad soon as the state changes to {@link #STATE_WAITING}
	 */
	public boolean isLocked();
	
	public int getNumber();
	
	public void setMessage(String msg);
	
	public String getMessage();
	
	public void setCounterexample(AbstractCounterexample example);
	
	public AbstractCounterexample getCounterexample();
}