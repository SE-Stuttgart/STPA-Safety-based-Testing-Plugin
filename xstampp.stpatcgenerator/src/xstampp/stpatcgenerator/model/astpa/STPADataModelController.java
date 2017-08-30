/*
 * @author Asim
 * I changed the parse to be direct from the XSTAMP project. 
 */
package xstampp.stpatcgenerator.model.astpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.causalfactor.ICausalController;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.controlaction.ControlAction;
import xstampp.astpa.model.controlaction.ControlActionController;
import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlstructure.ControlStructureController;
//import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
//import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlstructure.components.Component;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.AbstractLtlProviderData;
import xstampp.model.IDataModel;

public class STPADataModelController  {
	DataModelController dataModel =new DataModelController();
	

    public DataModelController getdataModel() {
    
        return dataModel;
    }

    public void setDataModelController(IDataModel datamodel) {
        this.dataModel = (DataModelController) datamodel;
       
    }

    public STPADataModelController(IDataModel datamodel) {
        this.dataModel = (DataModelController) datamodel;
       
    }
    
    public STPADataModelController() {
    }

    /**
     * fetches the current available value components from the data model
     *
     */
    
    
    
    
    public List<ProcessModelValue> fetchProcessComponentsAsList() {

        List<ProcessModelValue> valuesList = new ArrayList<>();
   //List<ICausalComponent> templist =  dataModel.getControlActionController().getCausalComponents();
        ControlStructureController csc =  dataModel.getControlStructureController();
        List<Component> templist= csc.getInternalComponents();
        for (int i = 0, n = templist.size(); i < n; i++) {

        	Component  parentComponent =   templist.get(i);
            if (parentComponent.getComponentType().name().equals("CONTROLLER")) {

                // get the process models
                for (IRectangleComponent tempPM : parentComponent.getChildren()) {

                    // get the variables
                    for (IRectangleComponent tempPMV : tempPM.getChildren()) {
                        // get the values and add the new object to the processmodel list
                        for (IRectangleComponent tempPMVV : tempPMV.getChildren()) {

                            ProcessModelValue pmValueObject = new ProcessModelValue();

                            pmValueObject.setController(parentComponent.getText());
                            pmValueObject.setPM(tempPM.getText());
                            pmValueObject.setPMV(tempPMV.getText());
                            pmValueObject.setValueText(tempPMVV.getText());
                            pmValueObject.setId(tempPMVV.getId());
                            System.out.println("DEBUG: set valueId: " + tempPMVV.getId() + " for " + tempPMVV.getText());
                            pmValueObject.setVariableID(tempPMV.getId());
                            pmValueObject.setComments(tempPMVV.getComment());
                            valuesList.add(pmValueObject);

                        }
                    }

                }
            }
        }
        return valuesList;
    }

    public List<String> fetchControllersComponents() {
        List<String> controllers = new ArrayList<String>();
//        List<ICausalComponent> templist = dataModel.getControlStructureEditorController().getCausalComponents();
        List<ICausalComponent> templist =   dataModel.getCausalComponents();
        for (int i = 0, n = templist.size(); i < n; i++) {

        	ICausalComponent parentComponent = templist.get(i);
            if (parentComponent.getComponentType().name().equals("CONTROLLER")) {
                controllers.add(parentComponent.getText());

            }

        }

        return controllers;
    }

    
    public List<AbstractLTLProvider> fetchSafetyConstraintsAs() {
         

       
         List<AbstractLTLProvider> tempSafetyConstraints = new ArrayList<AbstractLTLProvider>();

         tempSafetyConstraints = dataModel.getAllScenarios(true, false, false);

         return tempSafetyConstraints;
    }
    
    
    public List<String> fetchSafetyConstraints() {
        List<String> safetyconstraints = new ArrayList<String>();

       
         List<AbstractLTLProvider> tempSafetyConstraints = new ArrayList<AbstractLTLProvider>();

         tempSafetyConstraints = dataModel.getAllScenarios(true, false, false);

        for (AbstractLTLProvider sc : tempSafetyConstraints) {
             if (sc != null) {
            	 safetyconstraints.add(sc.getRefinedSafetyConstraint());
           }
         }
         
        
        return safetyconstraints;
    }
    
    
    
    
    public List<String> fetchLTLs() {
        List<String> LTLs = new ArrayList<String>();

       
         List<AbstractLTLProvider> tempLTLs = new ArrayList<AbstractLTLProvider>();

         tempLTLs = dataModel.getLTLPropertys();

        for (AbstractLTLProvider ltl : tempLTLs) {
             if (ltl != null) {
               LTLs.add(ltl.getLtlProperty());
           }
         }
         
        
        return LTLs;
    }
    
    public List<String> fetchControlActionComponents() {
        List<String> controlactions = new ArrayList<String>();

       
         List<IControlAction> tempControlactions = new ArrayList<IControlAction>();

         tempControlactions = dataModel.getAllControlActions();

        for (IControlAction ac : tempControlactions) {
             if (ac != null) {
               controlactions.add(ac.getTitle());
           }
         }
       // tempControlactions = dataModel.getAllControlActions();
        
        return controlactions;
    }
    
    public Map<String, UUID> fetchControlActionComponentsWithID() {
    	Map<String, UUID> result = new HashMap<String, UUID>();
    	List<IControlAction> tempControlactions = new ArrayList<IControlAction>();

        tempControlactions = dataModel.getAllControlActionsU();
        for (IControlAction ac : tempControlactions) {
            if (ac != null) {
            	result.put(ac.getTitle(), ac.getId());
            }
        }
        tempControlactions = dataModel.getAllControlActionsU();
        
        return result;
    }

    /**
     * fetches the current available value components from the data model
     *
     */
    public List<ProcessModelValue> fetchProcessComponent() {

        List<ProcessModelValue> valuesList = new ArrayList<>();
//        List<ICausalComponent> templist = dataModel.getControlStructureEditorController().getCausalComponents();
        List<ICausalComponent> templist = dataModel.getCausalComponents();
        for (int i = 0, n = templist.size(); i < n; i++) {

            Component parentComponent = (Component) templist.get(i);
            if (parentComponent.getComponentType().name().equals("CONTROLLER")) {

                // get the process models
                for (IRectangleComponent tempPM : parentComponent.getChildren()) {

                    // get the variables
                    for (IRectangleComponent tempPMV : tempPM.getChildren()) {
                        // get the values and add the new object to the processmodel list
                        for (IRectangleComponent tempPMVV : tempPMV.getChildren()) {

                            ProcessModelValue pmValueObject = new ProcessModelValue();

                            pmValueObject.setController(parentComponent.getText());
                            pmValueObject.setPM(tempPM.getText());
                            pmValueObject.setPMV(tempPMV.getText());
                            pmValueObject.setValueText(tempPMVV.getText());
                            pmValueObject.setId(tempPMVV.getId());
                            pmValueObject.setVariableID(tempPMV.getId());
               
                            valuesList.add(pmValueObject);

                        }
                    }

                }
            }
        }
        return valuesList;
    }

    public List<Map<String, List<IRectangleComponent>>> fetchProcessComponentsAsMap() {

        Map<String, List<IRectangleComponent>> variables = new HashMap<String, List<IRectangleComponent>>();
        List<Map<String, List<IRectangleComponent>>> PMVariablesvalues = new ArrayList<Map<String, List<IRectangleComponent>>>();
        String PMV = null;
//        List<ICausalComponent> templist = dataModel.getControlStructureEditorController().getCausalComponents();
        List<ICausalComponent> templist = dataModel.getCausalComponents();
        for (int i = 0, n = templist.size(); i < n; i++) {

            Component parentComponent = (Component) templist.get(i);
            if (parentComponent.getComponentType().name().equals("CONTROLLER")) {

                for (IRectangleComponent tempPM : parentComponent.getChildren()) {
                    for (IRectangleComponent tempPMV : tempPM.getChildren()) {
                        PMV = tempPMV.getText();
                        System.out.println(PMV + tempPMV.getChildren().size());
                        variables.put(PMV, tempPMV.getChildren());

                    }
                    PMVariablesvalues.add(variables);

                    System.out.println(variables.size());

                }
            }
        }
        return PMVariablesvalues;
    }

    public List<String> fetchProcessModelvariables() {
        List<String> processmodelvaraibles = new ArrayList<String>();

//        List<ICausalComponent> templist = dataModel.getControlStructureEditorController().getCausalComponents();
       // List<ICausalComponent> templist = dataModel.getCausalComponents();
        ControlStructureController csc =  dataModel.getControlStructureController();
        List<Component> templist= csc.getInternalComponents();
        
        for (int i = 0, n = templist.size(); i < n; i++) {

            Component parentComponent = (Component) templist.get(i);
            if (parentComponent.getComponentType().name().equals("CONTROLLER")) {

                // get the process models
                for (IRectangleComponent tempPM : parentComponent.getChildren()) {

                    // get the variables
                    for (IRectangleComponent tempPMV : tempPM.getChildren()) {
                        // get the values and add the new object to the processmodel list

                        processmodelvaraibles.add(tempPMV.getText());
                    }
                }
            }
        }
        return processmodelvaraibles;
    }

    public Map<String, UUID> fetchProcessModelvariablesWithId(){
    	Map<String, UUID> result = new HashMap<String, UUID>();
    	//List<ICausalComponent> templist = dataModel.getCausalComponents();
    	 ControlStructureController csc =  dataModel.getControlStructureController();
         List<Component> templist= csc.getInternalComponents();
    	
    	for (int i = 0, n = templist.size(); i < n; i++) {

            Component parentComponent = (Component) templist.get(i);
            if (parentComponent.getComponentType().name().equals("CONTROLLER")) {

                // get the process models
                for (IRectangleComponent tempPM : parentComponent.getChildren()) {

                    // get the variables
                    for (IRectangleComponent tempPMV : tempPM.getChildren()) {
                        // get the values and add the new object to the processmodel list

                    	result.put(tempPMV.getText(), tempPMV.getId());
                    }
                }
            }
        }
        return result;
    }
}
