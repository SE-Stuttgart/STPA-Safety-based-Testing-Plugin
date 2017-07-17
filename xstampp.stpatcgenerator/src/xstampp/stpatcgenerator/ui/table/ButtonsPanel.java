package xstampp.stpatcgenerator.ui.table;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.swing.AbstractAction;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import xstampp.stpatcgenerator.model.ProjectInformation;
import xstampp.stpatcgenerator.ui.views.ValidateSTPAandSBM;
import xstampp.stpatcgenerator.ui.views.StateFlowPropertiesView;
import xstampp.stpatcgenerator.wizards.ConfigurationWizard;

/**
 * This class defines the button panel which can be used in tables.
 * @author Ting Luk-He
 *
 */
public class ButtonsPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	public final List<JButton> buttons =
		    Arrays.asList(new JButton("save"));
    public ButtonsPanel() {
        super();
        setOpaque(true);
        for (JButton b: buttons) {
            b.setFocusable(false);
            b.setRolloverEnabled(false);           
            add(b);
        }
    }
}

//class EditAction extends AbstractAction {
//    private final JTable table;
//    protected EditAction(JTable table) {
//        super("edit");
//        this.table = table;
//    }
//    @Override public void actionPerformed(ActionEvent e) {
//    	int row = table.convertRowIndexToModel(table.getEditingRow());
//    	table.changeSelection(row, 1, false, false);
//    	table.requestFocus();
//    }
//}

class SaveAction extends AbstractAction {
    private final JTable table;
    protected SaveAction(JTable table) {
        super("save");
        this.table = table;
    }
    @Override 
    public void actionPerformed(ActionEvent e) {
        int row = table.convertRowIndexToModel(table.getEditingRow());
        
        if(table.getName().contains("state")){
        	String id = (String) table.getModel().getValueAt(row, 0);
            String name = (String)table.getModel().getValueAt(row, 1);
        	StateFlowPropertiesView.modifyStateName(id, name);
        	modifyStateNameInStateFlowXML(ProjectInformation.getStateflow(), id, name);
        }else if(table.getName().contains("VariablesTable")){
        	String id = (String) table.getModel().getValueAt(row, 0);
            String name = (String)table.getModel().getValueAt(row, 1);
        	StateFlowPropertiesView.modifyVariableName(id, name);
        	modifyVariableNameInStateFlowXML(ProjectInformation.getStateflow(), id, name);
        }else if(table.getName().contains("CompareVariables")){
        	ValidateSTPAandSBM compare = new ValidateSTPAandSBM();
        	String name1 = (String) table.getModel().getValueAt(row, 0);
        	String name2 = (String) table.getModel().getValueAt(row, 2);
        	String type1 = (String) table.getModel().getValueAt(row, 1);
        	String type2 = (String) table.getModel().getValueAt(row, 3);
        	// save value into xml file
//        	String type = "";
//        	if (type2 != ""){
//        		type = type2;
//        	}else if (type1 != "") {
//        		type = type1;
//        	}
        	int indexForSBM = ValidateSTPAandSBM.listIndexForSBM.get(row);
        	int indexForSTPA = ValidateSTPAandSBM.listIndexForSTPA.get(row);
        	String nameSTPA = (String)table.getModel().getValueAt(row, 0);
            String nameSBM = (String)table.getModel().getValueAt(row, 2);
        	switch(type1){
        	
        	case "State":
        		String stateIdInSBM = ValidateSTPAandSBM.stateInSBMIds.get(indexForSBM);
        		UUID stateIdInSTPA = ValidateSTPAandSBM.stateInSTPAIds.get(indexForSTPA);
                modifyStateNameInStateFlowXML(ProjectInformation.getStateflow(), stateIdInSBM, nameSBM);
        		modifyComponentInSTPAXML(ProjectInformation.getSTPAPath(), stateIdInSTPA, nameSTPA);
        		break;
        	case "Control Action":
        		// update control action In All XML
        		String caIdInSBM = ValidateSTPAandSBM.caStateInSBMIds.get(indexForSBM);
        		UUID caIdInSTPA = ValidateSTPAandSBM.caInSTPAIds.get(indexForSTPA);
        		modifyControlActionInStateFlowXML(ProjectInformation.getStateflow(), caIdInSBM, nameSBM);
        		modifyControlActionInSTPAXML(ProjectInformation.getSTPAPath(), caIdInSTPA, nameSTPA);
        		break;
        	case "Process Model Variable":
        		// update process model variable and data variable in all XML
        		String dvIdInSBM = ValidateSTPAandSBM.dvInSBMIds.get(indexForSBM);
        		UUID pmvIdInSTPA = ValidateSTPAandSBM.pmvInSTPAIds.get(indexForSTPA);
        		modifyVariableNameInStateFlowXML(ProjectInformation.getStateflow(), dvIdInSBM, nameSBM);
        		modifyComponentInSTPAXML(ProjectInformation.getSTPAPath(), pmvIdInSTPA, nameSTPA);
        		break;
        	case "PMV Value":
        		// update process model variable value and data variable In All XML
        		dvIdInSBM = ValidateSTPAandSBM.dvInSBMIds.get(indexForSBM);
        		UUID pmvvIdInSTPA = ValidateSTPAandSBM.pmvvInSTPAIds.get(indexForSTPA);
        		modifyVariableNameInStateFlowXML(ProjectInformation.getStateflow(), dvIdInSBM, nameSBM);
        		modifyComponentInSTPAXML(ProjectInformation.getSTPAPath(), pmvvIdInSTPA, nameSTPA);
        		break;
        	default:
        		break;
        	}
        	// check consistency
        	ImageIcon result = compare.compareVariables(name1, type1, name2, type2);
        	table.getModel().setValueAt(result, row, 4);
        }
//        GenerateTestCaseWizard.parse();
//        GenerateTestCaseWizard.openStateFlowTreeGraphEditor();
    }
    
    /**
     * modify state name in state flow xml file
     * @param filePath
     * 			path of state flow xml file
     * @param id
     * 			SSID of modified state
     * @param name
     * 			new name for state
     */
    public void modifyStateNameInStateFlowXML(String filePath, String id, String name){   	
    	try {
    		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filePath);			
			// Get the state element by tag name directly
			NodeList states = doc.getElementsByTagName("state");
			// modify state name
			for(int i = 0; i < states.getLength(); i++){
				String SSID = states.item(i).getAttributes().getNamedItem("SSID").getTextContent();
				if(SSID.equals(id)){
					String oldName = states.item(i).getChildNodes().item(1).getTextContent();
					String textStr[] = oldName.split("\\r\\n|\\n|\\r");
					String newName = name +"\n";
					for(int j = 1; j < textStr.length; j++){
						newName += textStr[j] + "\n"; 
					}
					states.item(i).getChildNodes().item(1).setTextContent(newName);
					
					// write the content into xml file
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(new File(filePath));
					transformer.transform(source, result);

					System.out.println("Done");
					break;
				}
			}
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     *  modify variable name in state flow xml file
     * @param filePath
     * 			path of state flow xml file
     * @param id
     * 			SSID of modified variable
     * @param name
     * 			new name for variable
     */    
    public void modifyVariableNameInStateFlowXML(String filePath, String id, String name) {  	
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filePath);			
			// Get the data element by tag name directly
			NodeList dataVariables = doc.getElementsByTagName("data");
			// modify data variable name
			for(int i = 0; i < dataVariables.getLength(); i++){
				String SSID = dataVariables.item(i).getAttributes().getNamedItem("SSID").getTextContent();
				if(SSID.equals(id)){
					// set name for variable
					String oldName = dataVariables.item(i).getAttributes().getNamedItem("name").getTextContent();
					System.out.println("Debug: data variable old name: " + oldName);
					dataVariables.item(i).getAttributes().getNamedItem("name").setTextContent(name);
					
					// write the content into xml file
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(new File(filePath));
					transformer.transform(source, result);

					System.out.println("Done");
					break;
				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    
    /**
     * modify control action for state in state flow xml file
     * @param filePath
     * 			path of state flow xml file
     * @param id
     * 			SSID of modified state
     * @param name
     * 			new name for state
     */
    public void modifyControlActionInStateFlowXML(String filePath, String id, String name){   	
    	try {
    		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filePath);			
			// Get the state element by tag name directly
			NodeList states = doc.getElementsByTagName("state");
			// modify state name
			for(int i = 0; i < states.getLength(); i++){
				String SSID = states.item(i).getAttributes().getNamedItem("SSID").getTextContent();
				if(SSID.equals(id)){
					String oldName = states.item(i).getChildNodes().item(1).getTextContent();
					String textStr[] = oldName.split("\\r\\n|\\n|\\r");
					String newName = "";
					for(int j = 0; j < textStr.length; j++){
						if(!textStr[j].contains("controlAction")){
							newName += textStr[j] + "\n"; 
						}else {
							String[] subStrings = textStr[j].split("controlAction="); 
							newName += subStrings[0] + "controlAction=" + name + ";";
						}
					}
					System.out.println("DEBUG: old name for ca in stateflow: " + oldName);
					System.out.println("DEBUG: new name for ca in stateflow: " + newName);
					states.item(i).getChildNodes().item(1).setTextContent(newName);
					
					// write the content into xml file
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(new File(filePath));
					transformer.transform(source, result);

					System.out.println("Done");
					break;
				}
			}
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * Modify state name and process variable value in STPA Hazard file
     * @param filePath
     * 				path of STPA Hazard file
     * @param id
     * 				ID of modified component in XML file
     * @param name
     * 				Name of modified component
     */
    public void modifyComponentInSTPAXML(String filePath, UUID id, String name){
    	try {
    		
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filePath);			
			// Get the data element by tag name directly
			NodeList components = doc.getElementsByTagName("component");
			// modify data variable name
			for(int i = 0; i < components.getLength(); i++){
				String UUID = components.item(i).getChildNodes().item(1).getTextContent();
				System.out.println("UUID above if in ButtonsPanel: " + UUID);
				System.out.println("id as UUID in parameter: " + id);
				if(UUID.equals(id.toString())){
					
					// set name for variable
					String oldName = components.item(i).getChildNodes().item(3).getTextContent();
					System.out.println("Debug: component old name: " + oldName);
					components.item(i).getChildNodes().item(3).setTextContent(name);
					
					// write the content into xml file
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(new File(filePath));
					transformer.transform(source, result);
					System.out.println("Done");
					break;
				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * Modify state name in STPA Hazard file
     * @param filePath
     * 				path of STPA Hazard file
     * @param controlActionId
     * 				ID of modified control action in XML file (not the component id)
     * @param name
     * 				Name of modified component
     */
    public void modifyControlActionInSTPAXML(String filePath, UUID controlActionId, String name){
    	try {
    		
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filePath);			
			// Get the data element by tag name component
			NodeList components = doc.getElementsByTagName("component");
			// modify control action name in tag component
			for(int i = 0; i < components.getLength(); i++){
//				String UUID = components.item(i).getChildNodes().item(1).getTextContent();
				String UUID = components.item(i).getChildNodes().item(9).getTextContent();
				System.out.println("UUID above if in ButtonsPanel: " + UUID);
				System.out.println("id as UUID in parameter: " + controlActionId);
				if(UUID.equals(controlActionId.toString())){					
					// set name for variable
					String oldName = components.item(i).getChildNodes().item(3).getTextContent();
					System.out.println("STPA file path: " + filePath);
					System.out.println("Debug: In STPA change component old name: " + oldName + " to " + name);
					components.item(i).getChildNodes().item(3).setTextContent(name);
					
					// write the content into xml file
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(new File(filePath));
					transformer.transform(source, result);
					System.out.println("Done");
					break;
				}
			}
			// Get the data element by tag name controlaction
			NodeList controlactions = doc.getElementsByTagName("controlaction");
			
			// modify control action name in tag controlaction
			for (int i = 0; i < controlactions.getLength(); i++){
				String UUID = controlactions.item(i).getChildNodes().item(7).getTextContent();
				System.out.println("UUID2 above if in ButtonsPanel: " + UUID);
				System.out.println("id as UUID in parameter: " + controlActionId);
				if(UUID.equals(controlActionId.toString())){
					// set name for variable
					String oldName = controlactions.item(i).getChildNodes().item(3).getTextContent();
					System.out.println("Debug: In STPA change controlaction old name: " + oldName + " to " + name);
					controlactions.item(i).getChildNodes().item(3).setTextContent(name);
					
					// write the content into xml file
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(new File(filePath));
					transformer.transform(source, result);
					System.out.println("Done");
					break;
				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}


