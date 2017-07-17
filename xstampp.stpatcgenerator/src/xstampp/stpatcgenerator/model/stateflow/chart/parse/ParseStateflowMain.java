package xstampp.stpatcgenerator.model.stateflow.chart.parse;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import xstampp.stpatcgenerator.model.stateflow.properties.StateFlowProperties;

public class ParseStateflowMain {

    ModelInformation dataStateflowModel;
    String Stateflowfilepath = "/Users/asimabdulkhaleq/Documents/MATLAB/ACCSimulator.xml";
    final String StateflowSchema = "Stateflowschema.xsd";
    StateFlowProperties stateFlowProperties; 
    List<State> states = new ArrayList<State>();
    List<Data> inputData = new ArrayList<Data>();
	List<Data> outputData = new ArrayList<Data>();
	List<Data> localData = new ArrayList<Data>();
	
    public ModelInformation getdataStateflowModel() {
        return dataStateflowModel;
    }

    public ParseStateflowMain() {

    }

    public ParseStateflowMain(String path) {
        this.Stateflowfilepath = path;
    }

    public List<Integer> getStatesIDs() {
        List<State> states = dataStateflowModel.getStateflow().getMachine().getChildrenchart().getChart().getRoot()
                .getStates();

        List<Integer> iDs = new ArrayList<Integer>();
        if (states.size() > 0) {

            for (State s : states) {
                iDs.add(Integer.parseInt(s.getsSID()));
            }
        }
        return iDs;
    }

    public void printState(State state) {
        if (state == null) {
            return;
        }
        System.out.println(state.toString());
        if (state.isHasChildern()) {
            for (State s : state.getchildren().getStates()) {
                s.setParentID(state.getsSID());
                printState(s);
                //printEntryDuringExistActions (s);
                printStateTransitions(s.getchildren().getTransitions());

            }
        }
    }

    public void printTransition(Transition t) {
        try {
            if (t == null) {
                return;
            }
            System.out.print(t.toString());
            if (t.getP() != null) {

                System.out.print(" Transition=" + t.getP().get(0).getValue());

            }
            if (t.getSrc().getSource() != null) {

                System.out.print(" source=" + t.getSrc().getSource().get(0).getValue());
            }
            if (t.getDst().getDestination() != null) {

                System.out.print(" destination=" + t.getDst().getDestination().get(0).getValue());

            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }

    }

    public void printData(Data t) {

        if (t == null) {
            return;
        }

        if (t.getP() != null) {

            System.out.println("SSID= " + t.getsSID() + "Data=" + t.getP().get(0).getValue() + " Type =" + t.getP().get(1).getValue());
        }

    }

    public void printStateTransitions(List<Transition> rootTransition) {

        for (Transition t : rootTransition) {
            printTransition(t);

            System.out.println("]");
        }
    }

    public void printStateDataVariables(List<Data> rootDataVariables) {

        for (Data t : rootDataVariables) {
            printData(t);

        }
    }

    public void PrintStateflowModel() {
        if (dataStateflowModel != null) {

            List<State> rootStates = dataStateflowModel.getStateflow().getMachine().getChildrenchart().getChart()
                    .getRoot().getStates();
            printStateTransitions(dataStateflowModel.getStateflow().getMachine().getChildrenchart().getChart()
                    .getRoot().getTransitions());
            for (State s : rootStates) {
                printState(s);

                printStateTransitions(s.getchildren().getTransitions());

            }

            System.out.println("Data varaibles");
            printStateDataVariables(dataStateflowModel.getStateflow().getMachine().getChildrenchart().getChart().getRoot().getData());
        }

    }
    
    // save state data variables
    public void saveStateDataVariables(List<Data> rootDataVariables) {
    	for (Data t : rootDataVariables) {
    		saveData(t);
    	}
    }
    
    public void saveData(Data t) {
    	if (t == null) {
            return;
        }

        if (t.getP() != null) {
        	String sSID = t.getsSID();
        	String name = t.getName();
        	String dataType = t.getP().get(0).getValue();
        	String valueType = t.getP().get(1).getValue();
        	if(dataType.equals("INPUT_DATA")) {
        		this.inputData.add(t);
        	}else if (dataType.equals("OUTPUT_DATA")) {
        		this.outputData.add(t);
        	}else if (dataType.equals("LOCAL_DATA")){
        		this.localData.add(t);
        	}
            
        }
    }
    
    public void saveState(State state) {
    	if (state == null) {
            return;
        }
        if (state.isHasChildern()) {
            for (State s : state.getchildren().getStates()) {
                s.setParentID(state.getsSID());
                this.states.add(state);
            }
        }
        this.states.add(state);
    }
    
    public void saveStateFlowProperties() {
    	if (dataStateflowModel != null) {

            List<State> rootStates = dataStateflowModel.getStateflow().getMachine().getChildrenchart().getChart()
                    .getRoot().getStates();
            for (State s : rootStates) {
            	System.out.println("rootStates not null");
            	saveState(s);
            }
            saveStateDataVariables(dataStateflowModel.getStateflow().getMachine().getChildrenchart().getChart().getRoot().getData());
            stateFlowProperties = new StateFlowProperties(this.states, this.inputData, this.outputData, this.localData);
        }
    }
    
    public void parseTransition() {

    }

    public void replaceCharacter(String name) {
        Path path = Paths.get(name);
        Charset charset = StandardCharsets.UTF_8;

        String content = null;
        try {
            content = new String(Files.readAllBytes(path), charset);
        } catch (IOException ex) {
            Logger.getLogger(ParseStateflowMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        content = content.replaceAll("&ldquo;", " ").replaceAll("&rdquo;", "  ");
        try {
            Files.write(path, content.getBytes(charset));
        } catch (IOException ex) {
            Logger.getLogger(ParseStateflowMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public StateFlowProperties ParseStateflowXML() {
        try {
            String file = null;
            if (Stateflowfilepath != null) {
                if (Stateflowfilepath.endsWith(".xml")) {
                    file = Stateflowfilepath;
                }

                File xmlFile = new File(file);
                if (!xmlFile.exists()) {
                    System.out.println("The stateflow is not exist");
                    return null;
                }

                JAXBContext context = JAXBContext.newInstance(ModelInformation.class);

                Unmarshaller unmarshaller = context.createUnmarshaller();
               replaceCharacter (Stateflowfilepath);
                dataStateflowModel = (ModelInformation) unmarshaller.unmarshal(new File(Stateflowfilepath));
                
              PrintStateflowModel();
                saveStateFlowProperties();
//                System.getProperties().remove("com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize"); //$NON-NLS-1$
            }
        } catch (JAXBException | NullPointerException e) {
            e.printStackTrace();
        }
		return stateFlowProperties;
    }

}

