package stpaverifier.controller.preferences;

import java.util.ArrayList;

import org.eclipse.jface.preference.IPreferenceStore;

import stpaverifier.Activator;
import stpaverifier.controller.preferences.nusmv.NuSMVPreferenceConstants;

public class NuSMVArgumentHandler {
	private static final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	
	private static String getNuSMVCommand(){
		String command = store.getString(NuSMVPreferenceConstants.NuSMV_PATH);
		if(command.isEmpty()){
			return null;
		}
//		else if(!System.getProperty("os.name").toLowerCase().contains("win")){
//			command = "./" + command;
//		}
		return command;
	}
	
	public static String[] getNuSMVArgs(String file){
		ArrayList<String> argList = new ArrayList<>();
		String command = getNuSMVCommand();
		if(command != null && !command.isEmpty()){
			argList.add(command);
		}else{
			return null;
		}
		argList.add(file);
		return argList.toArray(new String[0]);
	}

	public static String[] getNuSMV_CHECK_SYNTAX_Args(boolean isLTL,String property){
		ArrayList<String> argList = new ArrayList<>();
		argList.add("go");
		argList.add(getCheckArgs(isLTL, property));
		argList.add("quit");
		return argList.toArray(new String[0]);
	}
	
	/**
	 * this function creates a string which contains the call of the check_property argument for NuSMV
	 * Dependent on the isLTL bool the parameter <code>-l</code> (for LTL) or <code>-c</code>(for CTL)
	 * is inserted in the string 
	 * @param isLTL if the given formula is expressed in ltl or ctl syntax
	 * @param property the formula
	 * @return a string which contains the call of the check_property argument for NuSMV
	 */
	private static String getCheckArgs(boolean isLTL,String property){
		char property_literal = 'c';
		if(isLTL){
			property_literal = 'l';
		}
		return "check_property -"+property_literal+" -p "+'"'+property +'"';
	}
	/**
	 * 
	 * @param isLTL {@link #getCheckArgs(boolean, String)}
	 * @param property {@link #getCheckArgs(boolean, String)}
	 * @return the arguments for a validation run of a property
	 */
	public static String[] getNuSMV_CHECK_FILE_Args(boolean isLTL,String property){
		ArrayList<String> argList = new ArrayList<>();
		int cx_plugin = store.getInt(NuSMVPreferenceConstants.Pref_NuSMV_NR_COUNTEREX_PLUGIN);
		argList.add("set default_trace_plugin " + cx_plugin);
		if(store.getBoolean(NuSMVPreferenceConstants.Pref_NuSMV_USE_BDD)){
			argList.add("go");
			argList.add(getCheckArgs(isLTL, property));
		}else if(isLTL && store.getBoolean(NuSMVPreferenceConstants.Pref_NuSMV_USE_BMC)){
			argList.add("go_bmc");
			argList.add("check_ltlspec_bmc -p "+'"'+property +'"');
		}else{
			return null;
		}
		argList.add("time");
		argList.add("print_usage");
		if(store.getBoolean(NuSMVPreferenceConstants.Pref_NuSMV_USE_BDD)){
			argList.add("print_reachable_states");
			argList.add("print_fair_transitions");
			argList.add("print_bdd_stats");
			argList.add("print_fsm_stats");
		}
		argList.add("quit");
		return argList.toArray(new String[0]);
	}
	
	/**
	 * This method is used to calculate the cmd command for executing the 
	 * NuSMV in Interactive mode and executing the inteactive commands written in the
	 * cmdFile
	 *   			 
	 * @param file The file containing the smv model, this method does not check if
	 * 				 this file contains a valid smv model
	 * @param cmdFile the file containing the NuSMV arguments
	 * @return an array containing:<ol>
	 * <li> the path to nusmv
	 * <li>-source
	 * <li>[cmdFile]
	 * <li>[file]
	 * </ol> 	 
	 */
	public static String[] getNuSMV_INIT_Args(String file,String cmdFile){
		ArrayList<String> argList = new ArrayList<>();
		String command = getNuSMVCommand();
		if(command != null && !command.isEmpty()){
			argList.add(command);
		}else{
			return null;
		}
		argList.add("-source");
		argList.add(cmdFile);
		argList.add(file);
		return argList.toArray(new String[0]);
	}

	public static String[] getNuSMV_MODEL_Args(String osString) {
		ArrayList<String> argList = new ArrayList<>();
		String command = getNuSMVCommand();
		if(command != null && !command.isEmpty()){
			argList.add(command);
		}else{
			return null;
		}
		argList.add(osString);
		return argList.toArray(new String[0]);
	}
}
