package stpaverifier.util.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IOConsole;

import stpaverifier.controller.IProperty;
import stpaverifier.controller.IVerificationResult;
import stpaverifier.controller.ObserverValues;
import stpaverifier.controller.model.STPAVerifierController;
import stpaverifier.controller.preferences.spin.SpinPreferenceConstants;
import stpaverifier.model.AbstractCounterexample;
import stpaverifier.model.SpinTrail;
import stpaverifier.model.VerificationResult;
import stpaverifier.model.properties.LTLFormular;
import stpaverifier.model.properties.ModelProperty;
import stpaverifier.util.ConsoleRuntime;
import stpaverifier.util.ICMDScanner;

/**
 * an abstract Job that provides methodes to communicate with the stpa checker console
 * if and open console view can be found
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public abstract class LoggerJob extends Job  implements Observer,SpinPreferenceConstants,ICMDScanner{

	private class VerifierConsole extends IOConsole implements Observer{
		private Observable observable;
		
		/**
		 * this constructor will create a IOConsole that listens
		 * to a given observable 
		 * @param controller an observable that can reset this console via an observer interface,
		 * 					controller can be null in which case it will be ignored
		 */
		public VerifierConsole(Observable controller) {
			super(CONSOLE_NAME,null);
			this.observable = controller;
			if(observable != null){
				controller.addObserver(this);
			}
		}

		@Override
		protected void dispose() {
			if(observable != null){
				observable.deleteObserver(this);
			}
			super.dispose();
		}
		
		/**
		 * removes the this console froom the list of observers of the current observable 
		 * and registers it in the given observable
		 * @param o an observable that can reset this console via an observer interface,
		 * 					controller can be null in which case it will be ignored
		 */
		private void setObserver(Observable o) {
			if(observable != null){
				observable.deleteObserver(this);
			}
			observable = o;
			observable.addObserver(this);
		}
		@Override
		public void update(Observable o, Object arg) {
			switch((ObserverValues)arg){
			case RESET:
				clearConsole();
				break;
			default:
				break;
			
			}
		}
	}
	private static final String CONSOLE_NAME = "STPA Verifier Console";
	protected static String SPIN_VERSION_STRING ="Spin Version"; //$NON-NLS-1$
	protected static String NuSMV_VERSION_STRING ="This is NuSMV"; //$NON-NLS-1$
	protected static int STATE_ERROR = 0;
	private IProject logParentResource;
	private VerifierConsole console;
	private STPAVerifierController model;
	private IProgressMonitor monitor;
	private IProperty property;
	private ConsoleRuntime runThread;
	private int returnState;
	private VerificationResult verifyResult;
	private StringBuffer message;
	private IFile modelResource;
	
	/**
	 * 
	 * @param name {@link Job#getName()}
	 * @param dataModelController the controller that is used in the console listen to reset commands and 
	 * 								to which state changes of the property can be send, dataModelController can be null in that case 
	 */
	public LoggerJob(String name,STPAVerifierController dataModelController) {
		super(name);
		IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
		for(IConsole cons :consoleManager.getConsoles()){
			if(cons.getName().equals(CONSOLE_NAME) && cons instanceof IOConsole){
				console = (VerifierConsole) cons;
				if(dataModelController != null){
					console.setObserver(dataModelController);
				}
			}
		}
		if(console == null){
			
			console =new VerifierConsole(dataModelController);
		    consoleManager.addConsoles(new IConsole[]{console});
		}
	    consoleManager.showConsoleView(console);
	}
	
	/**
	 * 
	 * @param name {@link Job#getName()}
	 * @param dataModelController the Controller object that is responsible for collecting and storing all the data
	 * @param formular The property which should be checked by this job, or null when no property should be checked
	 */
	public LoggerJob(String name, STPAVerifierController dataModelController, IProperty formular) {
		this(name,dataModelController);
		verifyResult = new VerificationResult();
		if(formular instanceof LTLFormular && ((LTLFormular) formular).getLtlProvider() != null){
			verifyResult.setProvider(((LTLFormular) formular).getLtlProvider());
		}
		verifyResult.setSsrLiteral(formular.getsID());
		model = dataModelController;
		property = formular;
		modelResource = model.getFile();
	}
	public IFile getModelResource() {
		return this.modelResource;
	}
	/**
	 * 
	 * @param container the location in which the command is to be executed
	 * @param command an array which contains the name of the command in the first element 
	 * 				  followed by a list of arguments
	 * @param out the console output where the output shall be printed to
	 * @param property 
	 * @return one of the integer constants defined in the <code>IProperty</code> interface
	 * 
	 * @see IProperty
	 */
	protected final int inheritIO(final IContainer container,final String[] command,final IOConsole out, final int property){
		if(command == null){
			return STATE_ERROR;
		}
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.redirectErrorStream(true);
		
		if(container != null){
			setLogParentResource(container.getProject());
			builder.directory(container.getLocation().toFile());
		}
	    try {
	    	
			returnState = IProperty.STATE_SUCCESS;
			
			if(this.property != null && this.model != null &&
					(!model.setPropertyState(getPropertyID(), IProperty.STATE_VALIDATING) && !(this.property instanceof ModelProperty))){
				return IProperty.STATE_CANCELED;
			}

        	StringBuilder outString = new StringBuilder();
		    runThread = new ConsoleRuntime(builder, out,outString,this);
		    runThread.setLogParentResource(getLogParentResource());
		    runThread.start();
	    	if(runThread.isAlive()){
	    		runThread.join();
	    	}
	    	returnState = runThread.getReturn();
	    	//when calculating the spin counterexample by replaying the provided error trail this method 
	    	//is given the STATE_COUNTEREXAMPLE property so that the produced output can be stored
            if(property == IProperty.STATE_COUNTEREXAMPLE){
				LoggerJob.this.property.setCounterexample(new SpinTrail(outString.toString(),this.property.getsID(),getProperty()));
            }else if(verifyResult != null){
	    		verifyResult.setResult(returnState);
	    	}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    if(this.model != null){
	    	this.getModel().deleteObserver(this);
	    }
	   
        return returnState;
	}
	
	protected void setConsole(VerifierConsole console) {
		this.console = console;
	}

	public void setMonitor(IProgressMonitor monitor) {
		this.monitor = monitor;
	}

	protected IOConsole getConsole() {
		return console;
	}
	
	/**
	 * @return the property
	 */
	public String getProperty() {
		return this.property.getsFormular(getModel().isUseSpin(), false);
	}
	
	public String getPropertyLiteral(){
		return this.property.getsID();
	}

	public UUID getPropertyID(){
		return this.property.getUUID();
	}
	
	public IProgressMonitor getMonitor() {
		return monitor;
	}
	
	/**
	 * @return the model
	 */
	public STPAVerifierController getModel() {
		return model;
	}
	 
	/**
	 * this method sets the state of the property to unchecked if it has been canceled or
	 * is still waiting to be executed
	 */
	public void cleanUp(){
		if(property.getState() == IProperty.STATE_WAITING
				||property.getState() == IProperty.STATE_CANCELED
				||property.getState() == IProperty.STATE_PAUSED){
			getModel().setPropertyState(getPropertyID(), IProperty.STATE_UNCHECKED);
		}
		getModel().deleteObserver(this);
	}

	/**
	 * @return the verifyResult
	 */
	public VerificationResult getVerifyResult() {
		return this.verifyResult;
	}

	public void addPropertyMessage(String msg){
		if(this.message == null){
			this.message = new StringBuffer();
			this.message.append(msg);
		}else{
			this.message.append("\n"+msg);
		}
		
		property.setMessage(this.message.toString());
	}
	
	protected void setCounterexample(AbstractCounterexample example){
		this.property.setCounterexample(example);
	}
	protected Double[] getDigit(String line){
		List<Double> numbers= new ArrayList<>();
		char[] digit = new char[line.length()];
		int ignoreC=0;
		char charValue;
		boolean isE=false;
		boolean isPositive = true;
		for(int i=0;i < line.length();i++){
			charValue = line.charAt(i);
			/*
			 * this if statement skips this character when it is in brackets
			 */
				if(charValue == 40){
					ignoreC++;
				}else if(charValue == 41){
					ignoreC--;
				}
				if(ignoreC != 0){
					continue;
				}
			/*
			 * if the character which is read is a small or capital e than
			 * the directly following number, if it exists is treated as exponent.
			 * both '+' and '-' are ignored
			 */
				if((charValue == 43 || charValue == 45) && digit.length == 0){
				}else if(Character.isDigit(charValue) || charValue == 46){
					digit[i] = line.charAt(i);
				}else{
					addNumber(numbers, isE, isPositive, new String(digit));
					digit = new char[line.length()];
				}
			/*
			 * if the character which is read is a small or capital e than
			 * the directly following number, if it exists is treated as exponent
			 */
				if(charValue == 101 || charValue == 69){
					isE = true;
				}else if(!(Character.isDigit(charValue) || charValue == 43 || charValue == 45)){
					isE = false;
				}
			/*
			 * if the character which is read is a '-' than an immediately following number is set negative
			 */
			if(!Character.isDigit(charValue)){
				isPositive = charValue != 45;
			}
		}
		addNumber(numbers, isE, isPositive,new String(digit));
		
		return numbers.toArray(new Double[0]);
	}

	private void addNumber(List<Double> list, boolean isE,boolean isPositive,String d){
		if(d.trim().replaceAll("\\.", "").isEmpty()){
			return;
		}
		
		if(isE){
			double potenz =Math.pow(10,Double.parseDouble(d));
			double old= list.get(list.size()-1) * potenz;
			list.remove(list.size()-1);
			list.add(old);
			isE = false;
		}else if(isPositive){
			list.add(Double.parseDouble(d)); 
		}else{
			list.add(-Double.parseDouble(d)); 
		}
	}
	/**
	 * @return the returnState
	 */
	public int getReturnState() {
		return this.returnState;
	}

	/**
	 * @return the logParentResource
	 */
	public IProject getLogParentResource() {
		return this.logParentResource;
	}

	/**
	 * @param logParentResource the logParentResource to set
	 */
	public void setLogParentResource(IProject logParentResource) {
		this.logParentResource = logParentResource;
	}

	public void pause(){
		if(runThread != null){
			runThread.cancel();
		}
	}

	@Override
	protected void canceling() {
		if(runThread != null){
			runThread.cancel();
		}
		returnState = IProperty.STATE_CANCELED;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if((property.getState() & IProperty.STATE_CANCELED) != 0){
			returnState = IProperty.STATE_CANCELED;
			canceling();
		}
	}
}
