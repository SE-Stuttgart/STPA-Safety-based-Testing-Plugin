package xstampp.stpatcgenerator.ui.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.model.ProjectInformation;
import xstampp.stpatcgenerator.model.astpa.ProcessModelValue;
import xstampp.stpatcgenerator.model.astpa.STPADataModelController;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.DataVariable;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.StateNode;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.Tree;
import xstampp.stpatcgenerator.ui.table.ButtonsEditor;
import xstampp.stpatcgenerator.ui.table.ButtonsPanel;
import xstampp.stpatcgenerator.ui.table.ButtonsRenderer;
import xstampp.stpatcgenerator.ui.table.ColumnGroup;
import xstampp.stpatcgenerator.ui.table.GroupableTableHeader;

/**
 * This class defines the view of validation between STPA and SBM.
 * @author Ting Luk-He
 *
 */
public class ValidateSTPAandSBM extends ViewPart{
	
	public static final String ID = "xstampp.stpatcgenerator.view.compareSTPAStateflow";
	
	STPADataModelController stpaDataModel = STPATCGModelController.getConfWizard().getStpaDataModel();
	Tree tree = STPATCGModelController.getConfWizard().getTree();

	// Variables in STPA model
	List<ProcessModelValue> pmvvs;
	List<String> statesInSTPA;
	List<String> controlActionsInSTPA;
	// process model variables
	List<String> pmvsInSTPA;
	// process mode variable values
	List<String> pmvvsInSTPA;
	
	// IDs for relevant variables in STPA XML file
	public static List<UUID> stateInSTPAIds;
	public static List<UUID> caInSTPAIds;
	public static List<UUID> pmvInSTPAIds;
	public static List<UUID> pmvvInSTPAIds;
	
	// Variables in Stateflow model(SBM)
	List<StateNode> statesInSBM;
	List<DataVariable> dataVariablesInSBM;
	List<String> controlActionsInSBM;
	
	// IDs for relevant variables in SBM XML file
	public static List<String> stateInSBMIds;
	public static List<String> dvInSBMIds;
	public static List<String> caStateInSBMIds;
	
	
	// Array to record if the relevant variables was found
	boolean[] statesInSTPAIsFound;
	boolean[] statesInSBMIsFound;
	boolean[] caInSTPAIsFound;
	boolean[] caInSBMIsFound;
	boolean[] dataVariablesInSBMIsFound;
	boolean[] pmvsInSTPAIsFound;
	boolean[] pmvvsInSTPAIsFound;
	
	 
	public static List<Integer> listIndexForSTPA = new ArrayList<Integer>();
	public static List<Integer> listIndexForSBM = new ArrayList<Integer>();
	
	Composite parentFrame;
	private JPanel panel;
	private Frame frame;
	JPanel btnPanel = new ButtonsPanel();
	
	ImageIcon okIcon = createImageIcon("/icons/Ok-icon.png","ok icon");
	ImageIcon wrongIcon = createImageIcon("/icons/Button-Close-icon.png", "wrong icon");
	ImageIcon questionIcon = createImageIcon("/icons/question-icon.png", "question icon");;
	
	DefaultTableModel dm;
	String[] columnNames = {"", "", "", "", "Result", "Action"};
	
	public void init() {
		if(stpaDataModel != null && tree != null) {
			// initialize variables in STPA
			// states
			pmvvs = stpaDataModel.fetchProcessComponentsAsList();
			statesInSTPA = new ArrayList<String>();
			stateInSTPAIds = new ArrayList<UUID>();
			for(ProcessModelValue pmvv: pmvvs){
				if(pmvv.getPMV().equals("states")){
					statesInSTPA.add(pmvv.getValueText());
					System.out.println("Add state into statesInSTPA: " + pmvv.getValueText());
					stateInSTPAIds.add(pmvv.getId());
					System.out.println("Add Id into stateInSTPAIds: " + pmvv.getId());
				}
			}
			// control actions
			controlActionsInSTPA = stpaDataModel.fetchControlActionComponents();
			caInSTPAIds = new ArrayList<UUID>();
			Map<String, UUID> tempCAMapInSTPA = stpaDataModel.fetchControlActionComponentsWithID();
			for(int i = 0; i < controlActionsInSTPA.size(); i++) {
				caInSTPAIds.add(tempCAMapInSTPA.get(controlActionsInSTPA.get(i)));
			}
			// process model variables
			pmvsInSTPA = stpaDataModel.fetchProcessModelvariables();
			pmvInSTPAIds = new ArrayList<UUID>();
			Map<String, UUID> tempPMV = stpaDataModel.fetchProcessModelvariablesWithId();
			for(int i = 0; i < pmvsInSTPA.size(); i++) {
				pmvInSTPAIds.add(tempPMV.get(pmvsInSTPA.get(i)));
			}
			// process model variable values
			pmvvsInSTPA = new ArrayList<String>();
			pmvvInSTPAIds = new ArrayList<UUID>();
			for(ProcessModelValue pmvv: pmvvs){
				if(!pmvv.getPMV().equals("states")){
					pmvvsInSTPA.add(pmvv.getValueText());
					pmvvInSTPAIds.add(pmvv.getId());
				}
			}
			
			// initialize variables in SBM
			// states
			statesInSBM = tree.getListOfState();
			stateInSBMIds = new ArrayList<String>();
			for(StateNode s: statesInSBM){
				stateInSBMIds.add(s.getId());
			}
			// data variables
			dataVariablesInSBM = tree.getDatavariables();
			dvInSBMIds = new ArrayList<String>();
			for(DataVariable dv: dataVariablesInSBM) {
				dvInSBMIds.add(dv.getSSID());
			}
			controlActionsInSBM = new ArrayList<String>();	
			caStateInSBMIds = new ArrayList<String>();
			for(String ca: controlActionsInSTPA){
				if( searchCAInSFFile(ProjectInformation.getStateflow(),ca) != null){
					controlActionsInSBM.add(ca);
					caStateInSBMIds.add(searchCAInSFFile(ProjectInformation.getStateflow(),ca));
					continue;
				}else if(searchCAInSFFile(ProjectInformation.getStateflow(),ca.toLowerCase()) != null){
					controlActionsInSBM.add(ca.toLowerCase());
					caStateInSBMIds.add(searchCAInSFFile(ProjectInformation.getStateflow(),ca.toLowerCase()));
					continue;
				}else if(searchCAInSFFile(ProjectInformation.getStateflow(),ca.replaceAll("\\s+","")) != null){
					controlActionsInSBM.add(ca.replaceAll("\\s+",""));
					caStateInSBMIds.add(searchCAInSFFile(ProjectInformation.getStateflow(),ca.replaceAll("\\s+","")));
					continue;					
				}else if(searchCAInSFFile(ProjectInformation.getStateflow(),ca.replaceAll("\\s+","").toLowerCase()) != null){
					controlActionsInSBM.add(ca.replaceAll("\\s+","").toLowerCase());
					caStateInSBMIds.add(searchCAInSFFile(ProjectInformation.getStateflow(),ca.replaceAll("\\s+","").toLowerCase()));
					continue;					
				}
			}		
		}
	}
	
	// set rows for table
	private void setRows(JTable table) {
		// set rows for states and validate the consistency
		if(statesInSTPA != null && statesInSBM != null) {
			statesInSTPAIsFound = new boolean[statesInSTPA.size()];
			statesInSBMIsFound = new boolean[statesInSBM.size()];
			for(int i = 0; i < statesInSTPA.size(); i++) {
				String sSTPA = statesInSTPA.get(i);
				String sSTPALowerCase = sSTPA.toLowerCase();
				String sSTPALCNoWhitespace = sSTPALowerCase.replaceAll("\\s+", "");
				for (int j = 0; j < statesInSBM.size(); j++) {
					if (statesInSBMIsFound[j]) {
	                    continue;
	                }
					StateNode stateNodeSBM = statesInSBM.get(j);
					String sNameSBM = stateNodeSBM.getName();
					String sNameSBMLowerCase = sNameSBM.toLowerCase();
					String sNameSBMLCNoWhitespce = sNameSBMLowerCase.replaceAll("\\s+", "");
	                if (sSTPALCNoWhitespace.contains(sNameSBMLCNoWhitespce) || sNameSBMLCNoWhitespce.contains(sSTPALCNoWhitespace)) {
	                	statesInSTPAIsFound[i] = true;
	                	statesInSBMIsFound[j] = true;
	                	dm.addRow(new Object[]{sSTPA, "State", sNameSBM, "State", compareVariables(sSTPA, "State", sNameSBM, "State"), btnPanel});
	                	listIndexForSTPA.add(i);
	                	listIndexForSBM.add(j);
	                    break;
	                }
	                
	            }
			}
			// set rows for control actions and validate the consistency
			if(controlActionsInSTPA != null && controlActionsInSBM != null){
				caInSTPAIsFound = new boolean[controlActionsInSTPA.size()];
				caInSBMIsFound = new boolean[controlActionsInSBM.size()];
				for (int i = 0; i < controlActionsInSTPA.size(); i++){
					String caInSTPA = controlActionsInSTPA.get(i);
					String caInSTPALowerCase = caInSTPA.toLowerCase();
					String caInSTPALCNoWhitespace = caInSTPALowerCase.replaceAll("\\s+", "");
					for(int j = 0; j < controlActionsInSBM.size(); j++){
						if (caInSBMIsFound[j]) {
		                    continue;
		                }
						String caInSBM = controlActionsInSBM.get(j);
						String caInSBMLowerCase = caInSBM.toLowerCase();
						String caInSBMLCNoWhitespace = caInSBMLowerCase.replaceAll("\\s+", "");
						if (caInSTPALCNoWhitespace.contains(caInSBMLCNoWhitespace) || caInSBMLCNoWhitespace.contains(caInSTPALCNoWhitespace)) {
							caInSTPAIsFound[i] = true;
							caInSBMIsFound[j] = true;
		                	dm.addRow(new Object[]{caInSTPA, "Control Action", caInSBM, "Control Action", compareVariables(caInSTPA, "Control Action", caInSBM, "Control Action"), btnPanel});
		                	listIndexForSTPA.add(i);
		                	listIndexForSBM.add(j);
		                	break;
		                }
					}
				}
			}
			
			// set rows for variables and validate the consistency
			if(dataVariablesInSBM != null && pmvvs != null){
				dataVariablesInSBMIsFound = new boolean[dataVariablesInSBM.size()];
				pmvsInSTPAIsFound = new boolean[pmvsInSTPA.size()];
				pmvvsInSTPAIsFound = new boolean[pmvvs.size()];
				
				for (int i = 0; i < dataVariablesInSBM.size(); i++){
					if(dataVariablesInSBMIsFound[i]){
						continue;
					}
					String dvInSBM = dataVariablesInSBM.get(i).getName();
					String dvInSBMLowerCase = dvInSBM.toLowerCase();
					String dvInSBMLCNoWhitespace = dvInSBMLowerCase.replaceAll("\\s+", "");
					for(int j = 0; j< pmvsInSTPA.size(); j++){
						if(pmvsInSTPAIsFound[j]) continue;
						String pmvInSTPA = pmvsInSTPA.get(j);
						String pmvInSTPALowerCase = pmvInSTPA.toLowerCase();
						String pmvInSTPALCNoWhitespace = pmvInSTPALowerCase.replaceAll("\\s+", "");
						if (dvInSBMLCNoWhitespace.indexOf(pmvInSTPALCNoWhitespace) != -1){
							dataVariablesInSBMIsFound[i] = true;
							pmvsInSTPAIsFound[j] = true;
							dm.addRow(new Object[]{pmvInSTPA, "Process Model Variable", dvInSBM, "Data Variable", compareVariables(pmvInSTPA, "Process Model Variable", dvInSBM, "Data Variable"), btnPanel});
							listIndexForSTPA.add(j);
							listIndexForSBM.add(i);
							break;
						}
					}
				}
				for (int i = 0; i < dataVariablesInSBM.size(); i++){
					if(dataVariablesInSBMIsFound[i]){
						continue;
					}
					String dvInSBM = dataVariablesInSBM.get(i).getName();
					String dvInSBMLowerCase = dvInSBM.toLowerCase();
					String dvInSBMLCNoWhitespace = dvInSBMLowerCase.replaceAll("\\s+", "");
					for(int j = 0; j < pmvvs.size(); j++){
						if (pmvvsInSTPAIsFound[j]) {
		                    continue;
		                }
						String pmvvInSTPA = pmvvs.get(j).getValueText();
						String pmvvInSTPALowerCase = pmvvInSTPA.toLowerCase();
						String pmvvInSTPALCNoWhitespace = pmvvInSTPALowerCase.replaceAll("\\s+", "");
						if (dvInSBMLCNoWhitespace.contains(pmvvInSTPALCNoWhitespace) || pmvvInSTPALCNoWhitespace.contains(dvInSBMLCNoWhitespace)) {
							dataVariablesInSBMIsFound[i] = true;
							pmvvsInSTPAIsFound[j] = true;
		                	dm.addRow(new Object[]{pmvvInSTPA, "PMV Value", dvInSBM, "Data Variable", compareVariables(pmvvInSTPA, "PMV Value", dvInSBM, "Data Variable"), btnPanel});
		                	listIndexForSTPA.add(j);
		                	listIndexForSBM.add(i);
		                	break;
		                }
					}
				}
			}
		}
	}
	// set rows with not matched variables
	private void setRestRows(JTable table){
		// add STPA states into table
		if(statesInSTPA != null){
			for(int i = 0; i < statesInSTPA.size(); i++) {
				if(!statesInSTPAIsFound[i])
					dm.addRow(new Object[]{statesInSTPA.get(i), "State", "", "", compareVariables(statesInSTPA.get(i), "State", "", "")});
			}
		}else{
			System.err.println("statesInsTPA is null!");
		}
		// add STPA control actions into table
		if(controlActionsInSTPA != null){
			for(int i = 0; i < controlActionsInSTPA.size(); i++) {
				if(!caInSTPAIsFound[i])
					dm.addRow(new Object[]{controlActionsInSTPA.get(i), "Control Action", "", "", compareVariables(controlActionsInSTPA.get(i), "Control Action", "", "")});
			}
		}else{
			System.err.println("controlActionsInSTPA is null!");
		}
		// add SBM data variables into table
		if(dataVariablesInSBM != null){
			for(int i = 0; i < dataVariablesInSBM.size(); i++) {
				if(!dataVariablesInSBMIsFound[i])
					dm.addRow(new Object[]{"", "", dataVariablesInSBM.get(i).getName(), "Data Variable", compareVariables("", "", dataVariablesInSBM.get(i).getName(), "Data Variable")});
			}
		}else{
			System.err.println("dataVariablesInSBM is null!");
		}
	}

	
//	public  ImageIcon compareVariables(String name1, String type1, String name2, String type2) {
//		if(name1.equals(name2) && type1.equals(type2)){
//			return okIcon;
//		}else if (name1.equals(name2) && (type1.isEmpty() || type2.isEmpty())){
//			return questionIcon;
//		}else {
//			return wrongIcon;
//		}
//	}
	
	public  ImageIcon compareVariables(String name1, String type1, String name2, String type2) {
		
		if(name1.equals(name2)){
			return okIcon;
		}else if (name1.isEmpty() || name2.isEmpty() || (type1.equals("PMV Value") && type2.equals("Data Variable"))){
			return questionIcon;
		}else {
			return wrongIcon;
		}
				
	}
	
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		parentFrame = new Composite(parent, SWT.EMBEDDED);
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		frame = SWT_AWT.new_Frame(parentFrame);
		frame.setLayout(new BorderLayout());
	    
		dm = new DefaultTableModel() {
			@Override
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 4: 
					return ImageIcon.class;
				case 5:
					return ButtonsPanel.class;
				default:
					return Object.class;
				}
			}
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 0 || column == 2 || column == 5;
			}
		};
	    dm.setColumnIdentifiers(columnNames);
	    
	    JTable table = new JTable( dm ) {
		      protected JTableHeader createDefaultTableHeader() {
		    	  return new GroupableTableHeader(columnModel);
		      }
		};
		table.getColumn("Action").setCellRenderer(new ButtonsRenderer());
		table.getColumn("Action").setCellEditor(new ButtonsEditor(table));
		table.setName("CompareVariables");
		table.setRowHeight(30);
		// combine columns in group
	    TableColumnModel cm = table.getColumnModel();
	    ColumnGroup cgSTPA = new ColumnGroup("<html><center>STPA<br>Process Model Variable |"+ setSpaces(10)+"Type"+setSpaces(22));
	    cgSTPA.add(cm.getColumn(0));
	    cgSTPA.add(cm.getColumn(1));
	    ColumnGroup cgStateflow = new ColumnGroup("<html><center>SBM<br>SBM Variable" + setSpaces(10) + "|" + setSpaces(15) + "Type" + setSpaces(10));
	    cgStateflow.add(cm.getColumn(2));
	    cgStateflow.add(cm.getColumn(3));
	    GroupableTableHeader header = (GroupableTableHeader)table.getTableHeader();
	    header.setPreferredSize(new Dimension(30,30));
	    header.addColumnGroup(cgSTPA);
	    header.addColumnGroup(cgStateflow);
	    if(ProjectInformation.getTypeOfUse() == 2){
	    	init();
		    setRows(table);
		    setRestRows(table);
	    }
	    
	    
	    JScrollPane scroll = new JScrollPane( table );
	    panel.add(scroll);
	    panel.setVisible(true);
		frame.add(panel);
	}

	protected ImageIcon createImageIcon(String path,
            String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	private String setSpaces (int spaceNumber){
		String result = "";
		for(int i = 0; i < spaceNumber; i++){
			result += "&nbsp;";
		}
		return result;
	}
	
	/**
	 * Search control action in Stateflow XML File
	 * @param filePath
	 * 				state flow (save behavioral) XML file path
	 * @param controlAction
	 * 				control action in STPA Data Model
	 * @return SSID in state flow
	 * 				SSID of state which contains the relevant control action
	 * 	
	 */
	public String searchCAInSFFile(String filePath, String controlAction){   	
    	try {
    		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filePath);			
			// Get the state element by tag name directly
			NodeList states = doc.getElementsByTagName("state");
			// search control action in XML File
			for(int i = 0; i < states.getLength(); i++){
				String SSID = states.item(i).getAttributes().getNamedItem("SSID").getTextContent();
				String content = states.item(i).getChildNodes().item(1).getTextContent();
				if(content.contains(controlAction)){
					return SSID;
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
		}
    	return null;
    }
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
