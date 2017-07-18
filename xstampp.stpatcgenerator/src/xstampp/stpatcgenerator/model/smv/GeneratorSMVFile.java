package xstampp.stpatcgenerator.model.smv;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import xstampp.astpa.model.extendedData.RefinedSafetyRule;
import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.gui.Logfile;
import xstampp.stpatcgenerator.model.ProjectInformation;
import xstampp.stpatcgenerator.model.astpa.ProcessModelValue;
import xstampp.stpatcgenerator.model.astpa.STPADataModelController;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.DataVariable;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.StateNode;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.Tree;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.Action;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.StateTransition;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.Transition;

/**
 * This class reads the information of stateflow and STPA to generate SMV model
 * There are two differents methods With/Without STPA data
 *
 * @author asimabdulkhaleq and Ting Luk-He
 */
public class GeneratorSMVFile {

    List<Action> actions = new ArrayList<Action>();
    Map<Action, ArrayList<Action>> mapActionStates = new HashMap<Action, ArrayList<Action>>();
    ArrayList<String> smvstatck = new ArrayList<String>();
    private boolean isUsedbySTPA = false;
    private String smvModeldata;
    private String smvModelFile;
    private boolean isMatchedVariables = true;
    private static IWorkspace workspace = ResourcesPlugin.getWorkspace();
    private static IWorkspaceRoot root = workspace.getRoot();
    private static IFile smvIFile;
    private static File smvFile;
    private static List<String> ltlStack = new  ArrayList<String>();

    
    public static IWorkspaceRoot getRoot() {
		return root;
	}

	public static void setRoot(IWorkspaceRoot root) {
		GeneratorSMVFile.root = root;
	}

	public static IFile getsmvIFile() {
		return smvIFile;
	}

	public static void setsmvIFile(IFile smvIFile) {
		GeneratorSMVFile.smvIFile = smvIFile;
	}

	public String getSMVModelFile() {
        return smvModelFile;
    }

    public static File getSmvFile() {
		return smvFile;
	}

	public static void setSmvFile(File smvFile) {
		GeneratorSMVFile.smvFile = smvFile;
	}

	public static String getAbsoluteSmvFilePath(){
		return smvFile.getAbsolutePath();
	}
	public static String getSmvFilePath(){
		return smvFile.getPath();
	}
	public static String getSmvIFilePath(){
		String root = GeneratorSMVFile.root.getLocation().toString();
		return root + smvIFile.getFullPath().toString();
	}
	public static ArrayList<String> getLTL(){
		return (ArrayList<String>) ltlStack;
	}
	
	public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public Map<Action, ArrayList<Action>> getMapActionStates() {
        return mapActionStates;
    }

    public void setMapActionStates(Map<Action, ArrayList<Action>> mapActionStates) {
        this.mapActionStates = mapActionStates;
    }

    public ArrayList<String> getSmvstatck() {
        return smvstatck;
    }

    public void setSmvstatck(ArrayList<String> smvstatck) {
        this.smvstatck = smvstatck;
    }

    public boolean isIsUsedbySTPA() {
        return isUsedbySTPA;
    }

    public void setIsUsedbySTPA(boolean isUsedbySTPA) {
        this.isUsedbySTPA = isUsedbySTPA;
    }

    public String getSmvModelFile() {
        return smvModelFile;
    }

    public void setSmvModelFile(String smvModelFile) {
        this.smvModelFile = smvModelFile;
    }

    public boolean isIsMatchedVariables() {
        return isMatchedVariables;
    }

    public void setIsMatchedVariables(boolean isMatchedVariables) {
        this.isMatchedVariables = isMatchedVariables;
    }

    public String getSmvModeldata() {
        return smvModeldata;
    }

	public GeneratorSMVFile() {
		
    }

    public boolean isUsedbySTPA() {
        return isUsedbySTPA;
    }

    public void setUsedbySTPA(boolean isUsedbySTPA) {
        this.isUsedbySTPA = isUsedbySTPA;
    }

    static String filename = null;
    STPADataModelController STPAdataModel;
    Tree StateflowTree;

    public STPADataModelController getSTPAdataModel() {
        return STPAdataModel;
    }

    public void setSTPAdataModel(STPADataModelController sTPAdataModel) {
        STPAdataModel = sTPAdataModel;
    }

    public Tree getStateflowTree() {
        return StateflowTree;
    }

    public void setStateflowTree(Tree stateflowTree) {
        StateflowTree = stateflowTree;
    }

    public static String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public SMV getSmv() {
        return smv;
    }

    public void setSmv(SMV smv) {
        this.smv = smv;
    }

    SMV smv = new SMV();

    public static String nextSessionId() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(10, random).toString(5);
    }

//    private IFile convert(File file) {
//    	   IWorkspace workspace= ResourcesPlugin.getWorkspace();   
//    	   IPath location= Path.fromOSString(file.getAbsolutePath());
//    	   IFile ifile= workspace.getRoot().getFileForLocation(location);
//    	 return ifile;
//    }
    
    /**
     * Create SMV IFile so that the File can be opened in editor
     * @author Ting Luk-He
     * @param  fileName
     * @param  fileContent
     * @return
     */
    private IFile createSMVFileInWorkspace(String fileName, String fileContent){
    	IProject project  = root.getProject("stpatcgenerator");
    	IFolder folder = project.getFolder("files");
    	IFile file = folder.getFile(fileName);
    	//at this point, no resources have been created    		
		try {
			if (!project.exists()) project.create(null);
			if (!project.isOpen()) project.open(null);
        	if (!folder.exists()) 
        	    folder.create(IResource.NONE, true, null);
        	if (file.exists()) {
        		file.delete(true, null);
        		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!! Delete and Creating File in Workspace!!!!!!!!!!!!!!!!!!!");
        		file = folder.getFile(fileName);
        		byte[] bytes = fileContent.getBytes();
        	    InputStream source = new ByteArrayInputStream(bytes);
        	    file.create(source, IResource.NONE, null);
        	} else {
        		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!! Creating File in Workspace!!!!!!!!!!!!!!!!!!!");
        		byte[] bytes = fileContent.getBytes();
        	    InputStream source = new ByteArrayInputStream(bytes);
        	    file.create(source, IResource.NONE, null);
        	}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return file;
    }
    
    private String getCopyright() {
        String str = "--#############################################\n";
        str += "--This model is automtically generated by SMVGenerator tool which is developed by Asim Abdulkhaleq, Stefan Wagner \n";
        str += ("--University of Stuttgart, Institute of Software Technology,  Germany\n");
        str += ("--Copyright (c) 2016, at Institute of Software Technology, Software Engineering Group-2016\n");

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss\n");
        Calendar cal = Calendar.getInstance();

        str += ("--Date/Time:" + dateFormat.format(cal.getTime()) + "\n");

        str += ("--#############################################\n");
        str += ("\n");
        return str;
    }

    private void writeSMVInFile() {
    	String strFile = "";
        if(ProjectInformation.getTypeOfUse() == 2){
        	strFile = STPATCGModelController.getConfWizard().getProjectName() + ".smv";
        } else {
        	String stateFlowPath = ProjectInformation.getStateflow();
            String[] splitPath = stateFlowPath.split("\\/");
            String fName = splitPath[splitPath.length - 1];
            strFile = fName.split("\\.")[0] + ".smv";
        }        
        this.smvModelFile = strFile;
        this.setFilename(strFile);

        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(this.getFilename(), true)))){
        	out.println(getCopyright());
            System.out.println(getCopyright());
            this.smvModeldata = getCopyright();

            for (int i = 0; i < smvstatck.size(); i++) {
                smvModeldata += smvstatck.get(i).replace("\0", "") + "\n";
                out.println(smvstatck.get(i).replace("\0", ""));
                System.out.println(smvstatck.get(i));
            }
            // more code
        } catch (IOException e) {
            Logfile.wirteToLog(e.getMessage());
            Logfile.addMessage(e.getMessage());
            System.out.println(e.getMessage());
        }
        smvIFile = createSMVFileInWorkspace(getFilename(), smvModeldata);
        System.out.println("smvIFile: " + smvIFile.getName());
        File tmpFile = new File(getFilename());
        smvFile = tmpFile;
    }

    private void addVariables(SMV smv) {
        String str = smv.getSmvstr();
        Map<String, String> variables = smv.getSTPAvariables();

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String key = entry.getKey();
            String values = entry.getValue();

            str += key + ":" + values + ";\n";

        }

        smv.setSmvstr(str);
    }

    public void parseControllers(String SMVfile, STPADataModelController STPADataModel) {
        String str = null;
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(SMVfile, true)))) {
            str = "Controllers: {";

            for (String controller : STPADataModel.fetchControllersComponents()) {
                str += controller + " ,";
            }
            str = str.substring(0, str.length() - 1);
            str += "}";

            str = str.replaceAll("\\s", "");
            out.println(str);
            // more code
        } catch (IOException e) {
            Logfile.wirteToLog(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    private String getDefaultTransitions(List<StateNode> states, List<StateTransition> transitions) {
        String str = null;
        List<StateTransition> defaulttransitions = new ArrayList<StateTransition>();

        for (StateTransition st : transitions) {
            if (st.isDefault()) {
                defaulttransitions.add(st);
            }
        }
        if (defaulttransitions.size() > 0) {
            str = "{";
            for (StateTransition Dt : defaulttransitions) {
                if (Dt.getDestination() != null) {
                    StateNode destination = getStatebySourceID(Dt.getDestination());
                    if (destination != null) {
                        str += destination.getName() + ",";
                    }
                }

            }
            str = str.substring(0, str.length() - 1);
            str += "}";

            str = str.replaceAll("\\s+", "");

        }
        return str;
    }

    private StateNode getStatebySourceID(String source) {
        boolean isfound = false;
        StateNode state = null;

        if (StateflowTree.getListOfState().size() > 0) {
            for (StateNode s : StateflowTree.getListOfState()) {
                if (s.getId().equals(source)) {

                    isfound = true;
                    state = s;
                    break;

                }

            }

        }
        return state;
    }

    public List<Transition> removeRepated(List<Transition> lt) {

        // add elements to al, including duplicates
        HashSet<Transition> hs = new LinkedHashSet<Transition>(lt);
        List<Transition> al = new ArrayList<Transition>(hs);

        return al;
    }

    private StateNode getStatebyExecutionOrder(List<StateNode> states) {
        StateNode state = states.get(0);
        int orderMin = 0;
        try {
            orderMin = Integer.parseInt(state.getExecutionOrder());
            for (int i = 1; i < states.size(); i++) {

                if (Integer.parseInt(states.get(i).getExecutionOrder()) < orderMin) {
                    state = states.get(i);
                }

            }
        } catch (NumberFormatException ex) {
            return states.get(0);
        }

        return state;
    }

    private String parseInit(SMV smv) {
        String str = "";
        if (smv.getStates().size() > 0) {

            str = smv.getSmvstr() + "\n" + "init (states):=" + getStatebyExecutionOrder(smv.getStates()).getName()
                    + ";\n";

        }
        return str;
    }

    private String parseStatesDefault(SMV smv, StateNode root) {
        String str = null;
        str = " {";
        ArrayList<String> statesSMV = new ArrayList<String>();
        if (root.isHasChildren()) {
            List<StateNode> states = root.getChildren();
            for (StateNode s : states) {
                statesSMV.add(s.getName());
                smv.addState(s);
                str += s.getName() + " ,";
            }
            str = str.substring(0, str.length() - 1);
            str += "}";

        }

        return str;
    }

    private String parseStates(SMV smv, StateNode root) {
        String str = "";
        str = "states: {";
        ArrayList<String> statesSMV = new ArrayList<String>();
        if (root.isHasChildren()) {
            List<StateNode> states = root.getChildren();
            for (StateNode s : states) {
                statesSMV.add(s.getName());
                smv.addState(s);
                str += s.getName() + " ,";
            }
            str = str.substring(0, str.length() - 1);
            str += "}";

        }
        return str;
    }

    public String parsesubModule(SMV smv, StateNode root) {
        String str = "";
        List<StateNode> states = root.getChildren();
        for (StateNode s : states) {
            if (s.isSubModule()) {
                str += s.getName() + ":" + "Sub_" + s.getName() + "(" + getDataVariablesStr(smv) + ");\n";
                smv.addSubSystems(str, s);
            }
        }

        return str;
    }

    public String parseControlActions() {
        String str = "";

        str = " controlaction : {";

        for (String controlaction : this.STPAdataModel.fetchControlActionComponents()) {
            str += controlaction + " ,";
        }
        str = str.substring(0, str.length() - 1);
        str += "};";

        str = str.replaceAll("\\s+", "");

        // more code
        return str;
    }

    public String parseControlActions(String varname) {
        String str = "";
        if (varname != null) {
            str = varname + " : {";
        } else {
            str = " controlaction : {";
        }

        for (String controlaction : this.STPAdataModel.fetchControlActionComponents()) {
            str += controlaction + " ,";
        }
        str = str.substring(0, str.length() - 1);
        str += "};";

        str = str.replaceAll("\\s+", "");

        // more code
        return str;
    }

    public Map<String, String> parseVariables(STPADataModelController STPADataModel) {

        ProcessModelValue pmValueObject = null;
        Map<String, String> variablesList = null;

        String str = null;

        List<Map<String, List<xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent>>> variables = STPADataModel.fetchProcessComponentsAsMap();

        variablesList = new HashMap<String, String>();
        for (Map<String, List<xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent>> variable : variables) {

            for (Entry<String, List<xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent>> entry : variable
                    .entrySet()) {
                str = null;
                String var = entry.getKey();
                // out.print(entry.getKey() + ": {");

                List<xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent> values = entry.getValue();
                str = "{";
                for (xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent value : values) {

                    str += " " + value.getText();
                    // out.print(value.getText());
                    // out.print(",");
                    str += ",";

                }
                str = str.substring(0, str.length() - 1);
                str += "}";
                // out.print("}");

                variablesList.put(var, str);

            }
        }

        return variablesList;
    }

    private String getDataVariablesStr(SMV smv) {
        String str = "";
        for (DataVariable var : smv.getVariables()) {
            str += var.getName() + ",";
        }
        if (str.length() > 1) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private String parseInitDataVariables(SMV smv) {
        String str = "";
        for (DataVariable var : smv.getVariables()) {
            if (var.getType().equals("int8") || var.getType().equals("int16") || var.getType().equals("int32")
                    || var.getType().equals("uint8") || var.getType().equals("uint16")
                    || var.getType().equals("uint32")) {
                str += "init (" + var.getName() + ") :=0 ; \n";
            } else if (var.getType().equals("single") || var.getType().equals("double")) {
                str += "init (" + var.getName() + ") := 0 ; \n";
                // }// else if (var.getType().contains("Enum")) {
                // str += "init (" + var.getName() + ") := ; \n";
            } else if (var.getType().contains("boolean")) {
                str += "init (" + var.getName() + ") := FALSE ;\n";
            }

        }
        return str;
    }

    private boolean checkVaraible(String var) {
        boolean isfound = false;

        // List<ProcessModelValue> processModelValue=
        // STPADataModel.fetchProcessComponentsAList();
        List<DataVariable> stateflowvaraibles = this.StateflowTree.getDatavariables();

        for (DataVariable dv : stateflowvaraibles) {

            if (dv.getName().equals(var)) {
                isfound = true;
                break;
            }

        }

        return isfound;

    }

    private String parseDataVariables(SMV smv) {
        StringBuffer str = new StringBuffer();

        for (DataVariable var : smv.getVariables()) {
            if (var.getType().equals("int8") || var.getType().equals("int16") || var.getType().equals("int32")
                    || var.getType().equals("uint8") || var.getType().equals("uint16")
                    || var.getType().equals("uint32")) {
                str.append(var.getName() + ": 0..10 ; \n");
            } else if (var.getType().equals("single") || var.getType().equals("double")) {
                str.append(var.getName() + ": 0..10 ; \n");
            } else if (var.getType().contains("Enum")) {
                if (this.isUsedbySTPA() == false) {
                    str.append(var.getName() + ": {   }; \n");
                } else if (this.isUsedbySTPA() == true && var.getName().contains("controlAction") || var.getName().contains("controlaction")) {
                    str.append(parseControlActions(var.getName()) + "\n");
                }
                /*else if (this.isUsedbySTPA() == true && checkVaraible(var.getName())) {
                    str.append(var.getName() + ": {" + parseDataVariables(smv) + "}; \n");
                }*/
            } else if (var.getType().contains("Inherit")) {
                if (var.getMethod().equals("double")) {
                    str.append(var.getName() + ": 0..10 ; \n");
                } else {
                    Logfile.wirteToLog("The variable " + var.getName() + " has no data type in simulink stateflow");
                    str.append(var.getName() + ": \t ; \n");
                }
            } else {
                str.append(var.getName() + ": " + var.getType() + "; \n");
            }

        }

        return str.toString();

    }

    private void printMapStateActions(TreeMap<String, ArrayList<Action>> hashMap) {
        for (Entry<String, ArrayList<Action>> entry : hashMap.entrySet()) {
            String key = entry.getKey();

            ArrayList<Action> value = entry.getValue();
            System.out.println("key, " + key);
            if (value != null) {
                for (Action ac : value) {
                    System.out.println("Name " + ac.getName() + " source " + ac.getSource());

                }
            }
        }

    }

    private TreeMap<String, ArrayList<Action>> getSimilarActions(List<Action> actions) {
        TreeMap<String, ArrayList<Action>> hashMap = new TreeMap<String, ArrayList<Action>>();

        for (int j = 0; j < actions.size(); j++) {
            if (!hashMap.containsKey(actions.get(j).getName().trim())) {
                ArrayList<Action> subAcitons = new ArrayList<Action>();
                subAcitons.add(actions.get(j));
                hashMap.put(actions.get(j).getName().trim(), subAcitons);
            } else {
                hashMap.get(actions.get(j).getName().trim()).add(actions.get(j));
            }
        }
        return hashMap;
    }
    /**
     * 
     * These two methods created by Asim Abdulkhaleq 
     * @param source
     * @return
     */
    
    private StateNode getStatebySourceName(String source) {
        boolean isfound = false;
        StateNode state = null;

        if (StateflowTree.getListOfState().size() > 0) {
            for (StateNode s : StateflowTree.getListOfState()) {
                if (s.getName().equals(source.trim())) {

                    isfound = true;
                    state = s;
                    break;

                }

            }

        }
        return state;
    }
    
    private String parseStateActionSimilar(Action a, StateNode source) {
        String strFun = "";
        String straction = "";
        List<StateTransition> transitions = source.getStateTransitions();

        for (StateTransition st : transitions) {
            if (!st.isDefault()) {
                StateNode destination = getStatebySourceID(st.getDestination().trim());
                System.out.println(source.getName() + " ->" + a.getName() + " -> " + st.getCondition() + " -> " + destination.getName());
                List<Action> actionsDestination = destination.getActions();
                if (destination != null) {
                    for (Action b : actionsDestination) {
                        strFun = "";
                        if (a.getName().trim().equals(b.getName().trim())) {
                            if (b.getFunction().contains("/")) {
                                strFun = " & " + b.getFunction().substring(b.getFunction().indexOf("/") + 1) + " > 0 ";

                            }
                            String strcondition = "";

                            if (st.getCondition() != null) {
                                strcondition = " & (" + st.getCondition() + " )";
                            }
                            straction += " states =" + a.getSource() + strcondition + strFun + " :" + b.getFunction() + ";\n";
                        }
                    }
                }

            }
        }

        return straction;
    }

    /**
     * This method is updated to consider the StateAction of the next operator
     *
     * @param smv
     */
    private void parseStateActionsNew(SMV smv) {
        actions.addAll(StateflowTree.getStatesActions());

        String straction = "";

        for (int i = 0; i < actions.size(); i++) {
            straction = "";
            // ArrayList<Action> newAction = new ArrayList<Action>();
            Action a = actions.get(i);

            StateNode source = getStatebySourceName(a.getSource().trim());

            if (a.isVisted() == false) {
                straction += "next (" + a.getName() + " ):=case \n";
                String strFun = "";
                if (a.getFunction().contains("/")) {
                    strFun = " & " + a.getFunction().substring(a.getFunction().indexOf("/") + 1) + " > 0 ";

                }
                straction += " states =" + a.getSource() + strFun + " :" + a.getFunction() + ";\n";
                a.setIsVisted(true);
                straction += parseStateActionSimilar(a, source);

                for (int j = i + 1; j < actions.size(); j++) {
                    Action b = actions.get(j);
                    if (a.getName().trim().equals(b.getName().trim())) {
                        StateNode sourceb = getStatebySourceName(b.getSource().trim());
                        if (b.isVisted() == false) {
                            strFun = "";
                            if (b.getFunction().contains("/")) {
                                strFun = " & " + b.getFunction().substring(b.getFunction().indexOf("/") + 1) + " > 0 ";

                            }
                            straction += " states =" + b.getSource() + strFun + " :" + b.getFunction() + ";\n";
                            b.setIsVisted(true);
                            straction += parseStateActionSimilar(b, sourceb);
                        }

                    }
                }

                straction += "TRUE:" + a.getName() + ";\n esac;\n";

                smv.setSmvstr(smv.getSmvstr() + straction);
            }
        }

    }
    
    
    
    
    
    
    
    
    private void parseStateActions(SMV smv) {
        actions.addAll(StateflowTree.getStatesActions());

        String straction = "";

        for (int i = 0; i < actions.size(); i++) {
            straction = "";
            // ArrayList<Action> newAction = new ArrayList<Action>();
            Action a = actions.get(i);

            if (a.isVisted() == false) {
                straction += "next (" + a.getName() + " ):=case \n";
                String strFun = "";
                if (a.getFunction().contains("/")) {
                    strFun = " & " + a.getFunction().substring(a.getFunction().indexOf("/") + 1) + " > 0 ";

                }
                straction += " states =" + a.getSource() + strFun + " :" + a.getFunction() + ";\n";
                for (int j = i + 1; j < actions.size(); j++) {
                    if (!a.getName().contains(actions.get(j).getName())) {

                        // newAction.add(actions.get(j));
                    } else if (a.getName().trim().contains(actions.get(j).getName().trim())) {
                        actions.get(j).setVisted(true);
                        String strFun1 = "";
                        if (a.getFunction().contains("/")) {
                            strFun1 = " & " + a.getFunction().substring(a.getFunction().indexOf("/") + 1) + " > 0 ";

                        }

                        straction += " states =" + actions.get(j).getSource() + strFun1 + " :" + actions.get(j).getFunction()
                                + ";\n";

                    }

                }

                straction += "TRUE:" + a.getName() + ";\n esac;\n";

                smv.setSmvstr(smv.getSmvstr() + straction);
            }
        }

    }

    private void parseSTPALTL(SMV smv) {
    	
        List<xstampp.astpa.model.extendedData.RefinedSafetyRule> LTLmap =
        		(List<xstampp.astpa.model.extendedData.RefinedSafetyRule>)(List<?>) STPAdataModel.getdataModel().getAllRefinedRules(null);
        String ltl = "";
        if (this.isUsedbySTPA()) {
            for (RefinedSafetyRule entry : LTLmap) {
                ltl += "LTLSPEC  " + entry.getLtlProperty().replace("[", "G").replace("]", "").replace("==", "=").replace("&&", "&").replace ("\n", "")
                        .replace("*", "") + "\n";
                ltl = ltl.replace("=On", "=TRUE").replace("=Off", "=FALSE");
                ltl = ltl.replace("= On", "=TRUE").replace("= Off", "=FALSE");
                ltl = ltl.replace("=ON", "=TRUE").replace("=OFF", "=FALSE");
                ltl = ltl.replace("=ON", "=TRUE").replace("=OFF", "=FALSE");
                 
                String subltl = entry.getLtlProperty().replace("[", "G").replace("]", "").replace("==", "=").replace("&&", "&").replace ("\n", "")
                         .replace("*", "");
                subltl.replace("=On", "=TRUE").replace("=Off", "=FALSE").replace("= On", "=TRUE").
          replace("= Off", "=FALSE").replace("=ON", "=TRUE").replace("=OFF", "=FALSE").replace("=ON", "=TRUE").replace("=OFF", "=FALSE");
                ltlStack.add(subltl);
            }
            smv.setSmvstr(smv.getSmvstr() + ltl);
            System.out.println("Statring parse LTL" + ltl);
        }
    }

    /*private void parseSTPALTL(SMV smv) {
		Map<String, String> LTLmap = STPAdataModel.getdataModel(). getAllRefinedRules ();
		String ltl = "";
		if (this.isUsedbySTPA()) {
			for (Entry<String, String> entry : LTLmap.entrySet()) {
				ltl += "LTLSPEC  " + entry.getKey() + "  " + entry.getValue().replace("==", "=").replace("&&", "&")
						.replace("*", "").replace("[", "G").replace("]", "") + "\n";
			}
			smv.setSmvstr(smv.getSmvstr() + ltl);
                        System.out.println("Statring parse LTL" + ltl);
		}
	}*/
    private void parseTransitions(SMV smv, StateNode child) {
        String str = "";
        String strTransition = "";

        if (child.isHasTransitions() && child.isHasChildren()) {
            // Initial the next section

            str = "next (states):=case\n";

            StateNode source = null;
            List<StateTransition> transitions = child.getStateTransitions();
            // System.out.println("state Node =" +child.getId() + " transitions"
            // + transitions.size());
            String strDefault = getDefaultTransitions(child.getChildren(), transitions);
            if (strDefault != null) {
                str += "TRUE:" + strDefault + ";\n";
            }

            for (StateTransition st : transitions) {
                // System.out.println(root.getName() + " -" +
                // st.getFullTransition());;
                strTransition = "";

                if (st.isDefault() == false) {
                    if (st.getSource() != null) {
                        source = getStatebySourceID(st.getSource());
                        if (source != null) {

                            strTransition += "states=" + source.getName();
                        }
                    }
                    if (st.getCondition() == null) {
                        if (st.getSource() == null) {
                            strTransition += "" + "TRUE" + " : ";
                        } else {
                            strTransition += " & " + "TRUE" + ":";
                        }
                    } else if (st.getSource() == null) {
                        strTransition += " (" + st.getCondition().replace("~", "!").replace("&&", "&")
                                .replace("==", "=").replace("||", "|") + ") : ";
                    } else {
                        strTransition += " & (" + st.getCondition().replace("~", "!").replace("&&", "&")
                                .replace("==", "=").replace("||", "|") + ") : ";
                    }
                    if (st.getDestination() != null) {
                        StateNode destination = getStatebySourceID(st.getDestination());
                        if (destination != null) {

                            strTransition += destination.getName() + ";\n";
                        } else {
                            strTransition += ";\n";
                        }
                    }
                }
                str += strTransition;

            }
            // if (strDefault==null) {
            strDefault = parseStatesDefault(smv, child);

            // if (strDefault !=null)
            str += "TRUE:" + strDefault + ";\n";
            // }

            str += "esac";

            smv.setSmvstr(smv.getSmvstr() + "\n" + str + ";" + "\n");
            // System.out.println(smv.getSmvstr());
        }
        // return str;

    }

    private void build_SubModule(SMV smv, StateNode root) {

        if (root != null) {

            smv.setHeader("MODULE" + " Sub_" + root.getName() + "(" + getDataVariablesStr(smv) + ")");
            // smv.setVariables(parseVariables(this.getSTPAdataModel()));

            smv.setSmvstr(smv.getHeader() + "\n" + "VAR" + "\n");
            if (smv.getVariables().size() > 0) {
                // smv.setSmvstr(smv.getSmvstr()+ parseDataVariables(smv));
            }

            // smv.setControlActions(parseControlActions(this.getSTPAdataModel()));
            // write control actions into SMV model
            // smv.setSmvstr(smv.getSmvstr() + smv.getControlActions());
            // check if the root has Sub module;
            if (root.isSubModule()) {
                String strsubmodul = parsesubModule(smv, root);

                if (strsubmodul != "") {

                    smv.setSmvstr(smv.getSmvstr() + "\n" + strsubmodul + "\n");

                }

                // wirte the states
                String strStates = parseStates(smv, root);
                if (strStates != "") {
                    smv.setSmvstr(smv.getSmvstr() + "\n" + strStates + ";" + "\n");
                }

                // write the Assign section
            }
            smv.setSmvstr(smv.getSmvstr() + "ASSIGN\n");
            // write the inti seciton

            String strInit = parseInit(smv);
            if (strInit != "") {
                smv.setSmvstr(strInit + "\n");
            }
            // if (root.getActions().size()>0)
            // {
            // String strNextVaraibles =parseNextDataVaraibles (root);
            // smv.setSmvstr(smv.getSmvstr() +"\n"+ strNextVaraibles);
            // }
            // parseTransitions
            parseTransitions(smv, root);
            // saveSMVintoFile

            if (root.isSubModule()) {

                for (StateNode s : root.getChildren()) {
                    if (s.isSubModule()) {

                        SMV smvsub = new SMV();
                        smvsub.setVariables(smv.getVariables());
                        build_SubModule(smvsub, s);
                        smvstatck.add(smvsub.getSmvstr());

                    }
                }
            }

        }
    }

    private void buildSMVModel(Tree stateflowTree) {

        if (stateflowTree != null) {
            StateNode root = stateflowTree.getRoot();
            System.out.println("Tree");
            stateflowTree.printTree(stateflowTree.getRoot());
//            SMV smv = new SMV();
            smv.setVariables(stateflowTree.getDatavariables());
            smv.setHeader("MODULE main");
            // smv.setVariables(parseVariables(this.getSTPAdataModel()));

            smv.setSmvstr(smv.getHeader() + "\n" + "VAR" + "\n");
            if (smv.getVariables().size() > 0) {

                smv.setSmvstr(smv.getSmvstr() + parseDataVariables(smv));
            }

//            String random = nextSessionId();
//            String SMVfile = this.getFilename() + random + ".smv";
            String SMVfile = STPATCGModelController.getConfWizard().getProjectName() + ".smv";
            this.setFilename(SMVfile);

            // smv.setControlActions(parseControlActions(this.getSTPAdataModel()));
            // write control actions into SMV model
            // smv.setSmvstr(smv.getSmvstr() + smv.getControlActions());
            // check if the root has Sub module;
            if (root.isSubModule()) {
                String strParseModul = parsesubModule(smv, this.getStateflowTree().getRoot());
                if (strParseModul != "") {
                    if (smv.getSmvstr() == null) {
                        smv.setSmvstr("\n" + strParseModul + "\n");
                    } else {
                        smv.setSmvstr(smv.getSmvstr() + "\n" + strParseModul + "\n");
                    }
                }
                // write the Assign section
            }

            // wirte the states
            String strStates = parseStates(smv, this.getStateflowTree().getRoot());
            if (strStates != "") {
                smv.setSmvstr(smv.getSmvstr() + "\n" + strStates + ";" + "\n");
            }
            smv.setSmvstr(smv.getSmvstr() + "ASSIGN\n");
            // write the inti seciton

            String strInit = parseInit(smv);
            if (strInit != "") {
                smv.setSmvstr(strInit + "\n");
            }

            String strInitvariables = parseInitDataVariables(smv);
            if (strInitvariables != "") {

                smv.setSmvstr(smv.getSmvstr() + strInitvariables + "\n");

            }

            if (root.getActions().size() > 0) {

                parseStateActions(smv);
                // smv.setSmvstr(smv.getSmvstr() +"\n"+ strNextVaraibles);
            }

            // parseTransitions
            parseTransitions(smv, this.getStateflowTree().getRoot());

            // saveSMVintoFile
            if (root.isSubModule()) {
                for (StateNode s : root.getChildren()) {
                    if (s.isSubModule()) {
                        SMV smvsub = new SMV();
                        smvsub.setVariables(stateflowTree.getDatavariables());
                        build_SubModule(smvsub, s);
                        smvstatck.add(smvsub.getSmvstr());
                        //
                    }
                }
            }
            if (this.isUsedbySTPA == true) {

                parseSTPALTL(smv);
            }
            smvstatck.add(smv.getSmvstr());

            writeSMVInFile();
            // System.out.println(smv.getSmvstr());

            Logfile.wirteToLog("Number of Transitions =" + StateflowTree.getTotalOfTransitions());
            Logfile.wirteToLog("Number of States=" + StateflowTree.getSize());
            Logfile.wirteToLog("SMV model is generated ");

        }
    }

    public GeneratorSMVFile(String filename, Tree stateflowTree) {
        super();
        this.filename = filename;
        StateflowTree = stateflowTree;
    }

    public GeneratorSMVFile(String filename) {
        super();
        this.filename = filename;
    }

    /*
	 * Build SMV model based on the information of stateflow diagram without
	 * considering the STPA information.
     */
    public void generateSMVModel(Tree stateflowTree) {

        if (stateflowTree != null) {
            System.out.println("Start creating a new file ....!");

            this.setStateflowTree(stateflowTree);
            this.isUsedbySTPA = false;
            buildSMVModel(stateflowTree);
        }
    }

    /*
	 * Check if the variables in the stateflow diagram matched the variables in
	 * the STPA control structure diagram.
     */
    private boolean checkCorrectnessofVaraibles(STPADataModelController STPADataModel, Tree stateflowTree) {
        boolean isMatch = true;

        // List<ProcessModelValue> processModelValue=
        // STPADataModel.fetchProcessComponentsAList();
        List<String> processmodelvariables = STPADataModel.fetchProcessModelvariables();
        boolean[] isfound = new boolean[processmodelvariables.size()];
        List<DataVariable> stateflowvaraibles = stateflowTree.getDatavariables();
        for (int i = 0; i < processmodelvariables.size(); i++) {
            for (DataVariable dv : stateflowvaraibles) {

                if (processmodelvariables.get(i).equals(dv.getName())) {
                    isfound[i] = true;
                    break;
                }
                if (isfound[i] == true) {
                    break;
                }

            }
        }
        for (int j = 0; j < isfound.length; j++) {
            if (isfound[j] == false) {
                isMatch = false;
                isMatchedVariables = false;
                Logfile.wirteToLog("**Warning** The process model variable " + processmodelvariables.get(j)
                        + " does not match any variables in Stateflow model");
                System.out.println("**Warning** The process model variable " + processmodelvariables.get(j)
                        + " does not match any variables in Stateflow model");

            }

        }

        return isMatch;

    }

    public void generateSMVModelWithSTPA(STPADataModelController STPAdata, Tree stateflowTree) {

        if (stateflowTree != null) {
            this.setStateflowTree(stateflowTree);
            // check if the variables are the same in STPA or not.
            this.isUsedbySTPA = true;
            this.setSTPAdataModel(STPAdata);
            boolean isMacth = checkCorrectnessofVaraibles(this.getSTPAdataModel(), this.StateflowTree);
            if (isMacth != true) {
                System.out.println("Please check the generated SMV model, if it contains differ variables");
            }
            buildSMVModel(stateflowTree);

        }
    }

    private void buildSMVmodelWithSTPAData() {

        if (this.getSTPAdataModel() != null && this.getStateflowTree() != null) {
            SMV smv = new SMV();

            smv.setHeader("MODULE main");
            smv.setSTPAvariables(parseVariables(this.getSTPAdataModel()));

            smv.setSmvstr(smv.getHeader() + "\n" + "VAR" + "\n");
            if (smv.getVariables().size() > 0) {
                addVariables(smv);
            }

            String random = nextSessionId();
            String SMVfile = this.getSTPAdataModel().getProjectName() + random + ".smv";
            this.setFilename(SMVfile);

            smv.setControlActions(parseControlActions());
            // write control actions into SMV model
            smv.setSmvstr(smv.getSmvstr() + smv.getControlActions());

            // wirte the states
            parseStates(smv, this.getStateflowTree().getRoot());
            // write the Assign section
            smv.setSmvstr(smv.getSmvstr() + "ASSIGN\n");
            // write the inti seciton

            parseInit(smv);
            // parseTransitions
            parseTransitions(smv, this.getStateflowTree().getRoot());
            // saveSMVintoFile
            // SMVStack.push(smv.getSmvstr());
            writeSMVInFile();
            Logfile.wirteToLog("Number of Transitions =" + StateflowTree.getTotalOfTransitions());
            Logfile.wirteToLog("Number of States=" + StateflowTree.getSize());
            Logfile.wirteToLog("SMV model is generated ");
            System.out.println("======================");
            System.out.println(smv.getSmvstr());
        }
    }

    public void generateSMVModel(STPADataModelController STPADataModel, Tree stateflowTree) {

        if (STPADataModel != null) {
            System.out.println("Start creating a new file ....!");

            this.setStateflowTree(stateflowTree);
            this.setSTPAdataModel(STPADataModel);

            buildSMVmodelWithSTPAData();
        }

    }
}

