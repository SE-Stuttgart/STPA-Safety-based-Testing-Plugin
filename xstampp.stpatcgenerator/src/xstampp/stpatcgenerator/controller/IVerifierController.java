package xstampp.stpatcgenerator.controller;

import java.util.List;
import java.util.Observer;
import java.util.UUID;

import org.eclipse.core.resources.IFile;

import xstampp.model.ISafetyDataModel;

/**
 * An interface which provides all necessary informations and functions to control and observe the stpa verifier
 * this Interface is provided as a eclipse service and can be received by calling 
 * 
 * <br><code>PlatformUI.getWorkbench().getService(IVerifierController.class)</code>
 * 
 * 
 * @author Lukas Balzer
 * @since 1.0.0
 */
public interface IVerifierController {
	/**
	 * removes a property from one of the two property lists
	 * and triggers an update to {@link ObserverValues#FORMULARS_CHANGED}
	 * 
	 * @param id the UUID key which should be removed, if null than all stored properties are removed
	 */
	public boolean removeProperty(UUID id);
	
	/**
	 * removes all checked properties from the two property lists
	 * and triggers an update to {@link ObserverValues#FORMULARS_CHANGED}
	 * 
	 * @return whether or not there where checked entries and when then if they were removed
	 */
	public boolean removeChecked();
	
	/**
	 * the essential function to start a verification run of all selected properties.
	 * depending on the oneByOne boolean the verification is executed normal or as one by one run
	 * @param oneByOne <ul>
	 * 					<li> <i><b>if true</b></i> each property verification can be canceled in which case the verification is resumed with the next
	 * 						property in the queue or terminated if none<br>
	 * 					<li> <i><b>if false</b></i> canceling the verification of one property cancels the whole verification
	 * 					</ul>
	 * @return true if the job could be started successfully 
	 */
	public boolean executeSTPAVerifier(boolean oneByOne);
	
	/**
	 * removes all properties from the two property lists
	 * and triggers an update to {@link ObserverValues#FORMULARS_CHANGED}
	 */
	public boolean removeAllProperties();
	
	/**
	 * executes a syntax check of all properties which are set to be used
	 * The syntax check uses the currently chosen model checker to run the SyntaxCheck job 
	 * The Status of each included property is updated once the syntax check of it terminates
	 * 
	 * @see IProperty
	 * @return whether or not a syntax check of all checked properties was successful
	 */
	public boolean checkSyntax();
	
	/**
	 * Sets the boolean that indicates to use a property in a verification or syntax check in all properties
	 * 
	 * @param use indicates whether all properties should be used in a verification or syntax check
	 */
	public void setUseAllProperties(boolean shouldUse);
	
	/**
	 * 
	 * @return a List of all IPropertys stored 
	 */
	public List<IProperty> getAllProperties();
	public void addLTLFormular(String sId, String formula);
	public void addCTLFormular(String sId, String formula);
	public boolean resetProperties();
	public boolean pauseVerification();
	public boolean cancelVerification();
	public boolean resumeVerification();
	/**
	 * @return the results of a verifier run
	 */
	public List<IVerificationResult> getResults();
	
	/**
	 * 
	 * @return
	 */
	public IFile getLog();
	
	/**
	 * Imports the data from a given ISafetyDataModel which is an interface given by XSTAMPP
	 * to transport the for example an STPA Data Model from the astpa plugin
	 * 
	 * @param model the data model of an STPA Analysis or any other respective object that implements 
	 * 				{@link ISafetyDataModel} 
	 * @return true if the LTL data was successfully imported 
	 */
	public boolean importData(ISafetyDataModel model);
	
	/**
	 * adds an object which implements the observer interface to the
	 * observable controller of the STPA Verifier.
	 * Observers receive updates as <code>update(IVerifierController obj, ObserverValue value)</code>
	 *  
	 * @see ObserverValues
	 * @see Observer
	 * 
	 * @param ob the object which implements the observer interface
	 */
	public void addObserver(Observer ob);
	
}
