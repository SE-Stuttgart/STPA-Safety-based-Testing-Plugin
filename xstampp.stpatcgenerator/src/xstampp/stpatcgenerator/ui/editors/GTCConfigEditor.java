package xstampp.stpatcgenerator.ui.editors;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import xstampp.astpa.model.extendedData.RefinedSafetyRule;
import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.model.ProjectInformation;
import xstampp.stpatcgenerator.model.astpa.ParseSTPAMain;
import xstampp.stpatcgenerator.model.generateTestCases.SimpleEFSMTransition;
import xstampp.stpatcgenerator.model.generateTestCases.SimpleSTPAConstraint;
import xstampp.stpatcgenerator.model.generateTestCases.TestConfigurations;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.DataVariable;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.Tree;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.StateTransition;
import xstampp.stpatcgenerator.model.stateflow.fsm.EFSM;
import xstampp.stpatcgenerator.util.TCGeneratorPluginUtils;
import xstampp.stpatcgenerator.util.jobs.GenerateTestCasesJob;
import xstampp.stpatcgenerator.util.jobs.UpdateTraceTableJob;
/**
 * This class contains the view and functions of the test case generate configuration editor.
 * @author Ting Luk-He
 *
 */
public class GTCConfigEditor extends TCGeneratorAbstractEditor{
	
	public static final String ID = "xstampp.stpatcgenerator.editor.GTCconfig";
	final GTCConfigEditor editor = this;
	private static Tree tree = STPATCGModelController.getConfWizard().getTree();
	private EFSM fsm = STPATCGModelController.getSfmHandler().getFsm();
	private ParseSTPAMain parseSTPA;
	TestConfigurations configure = new TestConfigurations();
	List<RefinedSafetyRule> STPAConstraints;
	List<StateTransition> truthtable = fsm.getfsmTruthTable();
	// Normalize and spilt ssr into control action, source state and transitions
	List<SimpleSTPAConstraint> ssrList;
	// Normalize EFSM transition conditions
	List<SimpleEFSMTransition> efsmConditionList = normalizeCondition(fsm, truthtable);
	int testsuite = 0;
    int testcases = 0;
	
    Shell shell;
    // GUI Components
	Composite parentFrame;
	private JPanel panel;	
	private Frame frame;
	private JScrollPane scrollPane;
	// input variables table part
	private JTable inputVariablesTable = new JTable();	
	// input variables table model
	static List<DataVariable> dataVarList = tree.getDatavariables();	
	DefaultTableModel inputVarTableModel;
	String[] columnNames = {"ID",
            "Variable name", "Data Type", "Scope", "Initial Value", "Minimum Value", "Maximum Value"};
	// traceability matrix table
	private JTabbedPane tabbedPane;
	private JScrollPane scrollPane1;
	private JScrollPane scrollPane2;
	private JScrollPane scrollPane3;
	private JTable transConditionTable;
	private JTable ssrTable;
	private JTable traceTable;
	// traceability matrix table model
	// Filter
	JLabel filterLbl = new JLabel("Minimum degree of Similarity");
	String[] degree = {"0%", "5%", "10%", "15%", "20%", "25%", "30%", "35%", "40%", "45%", "50%", "55%", "60%", "65%", "70%", "75%", "80%", "85%", "90%", "95%", "100%"};
	JComboBox filter;
	// Data models for tables
	private DefaultTableModel transConditionTableModel;
	private DefaultTableModel ssrTableModel;
	private DefaultTableModel traceTableModel;
	private String[] colNamesFortransCondition = { "ID",
			"Transition Conditions", "Source State", "Control Action", "Full Transition" };
	private String[] colNamesForSSR = { "ID", "STPA Safety Constraints" };
	private String[] colNamesForTrace = { "ID of Transition",
			"ID of STPA Safety Constraints", "Degree Of Similarity"};
	// configuration part
	private JPanel bottomPanel;
	private JLabel testStepLbl;
	private JTextField testStep;
	private JLabel testAlgLbl;
	private JLabel testSuitNoLbl;
	private JRadioButton oneTSBtn;
	private JRadioButton moreTSBtn;
	private JRadioButton dfsBtn;
	private JRadioButton bfsBtn;
	private JRadioButton randomBtn;
	private JLabel testCoverageLbl;
	private JCheckBox allStatesCov;
	private JCheckBox allTransCov;
	private JCheckBox ssrCov;
	private JLabel stopConditonLbl;
	private JComboBox stopCondition;
	private JButton gtcBtn;
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		if (!(input instanceof TCGeneratorEditorInput)) {
			throw new RuntimeException("Wrong input");
		}
		this.setInput((TCGeneratorEditorInput) input);
		setSite(site);
		setInput(input);		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void createPartControl(Composite parent) {
//		shell =  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		if(ProjectInformation.getTypeOfUse() == 2){
			parseSTPA = STPATCGModelController.getConfWizard().getParseSTPA();
			STPAConstraints = (List<RefinedSafetyRule>) (List<?>) parseSTPA
					.getdataModel().getAllScenarios(true, false, false);
			ssrList = normalizeSSR(STPAConstraints);
		}
		parentFrame = new Composite(parent, SWT.EMBEDDED);
		
		panel = new JPanel(new BorderLayout(), true);
		
		frame = SWT_AWT.new_Frame(parentFrame);
		frame.setLayout(new BorderLayout());
		
		if(ProjectInformation.getTypeOfUse() == 2){
			setTraceMatrixTablePart();
		} else {
			setInVarTablePart();
		}				
		setConfigPart();
		
		JPanel topPanel = new JPanel(new BorderLayout(), true);
//		topPanel.setPreferredSize(new Dimension(2000, 280));
	
//		tabbedPane.setPreferredSize(new Dimension(1000, 200));
		topPanel.add(tabbedPane, BorderLayout.CENTER);
		panel.add(topPanel, BorderLayout.CENTER);
		
//		bottomPanel.setPreferredSize(new Dimension(1000, 160));
		panel.add(bottomPanel, BorderLayout.SOUTH);
		panel.setVisible(true);
		frame.add(panel);
	}
	private void setInVarTablePart() {
		tabbedPane = new JTabbedPane();
		// input variable table
		inputVariablesTable.setName("inputVariablesTable");
		inputVariablesTable.setAutoCreateRowSorter(true);
		inputVarTableModel = new DefaultTableModel(0, 0) {
			public boolean isCellEditable(int row, int column) {
				return column == 4 || column == 5 || column == 6;
			}
		};
		inputVarTableModel.setColumnIdentifiers(columnNames);
		inputVariablesTable.setModel(inputVarTableModel);
		setRowsForVariablesTables();
		STPATCGModelController.setTestInputVarTableModel(inputVarTableModel);
		scrollPane = new JScrollPane(inputVariablesTable);
		
		tabbedPane.add("Test Input", scrollPane);
	}
	
	private void setRowsForVariablesTables() {
		if(tree != null && inputVarTableModel != null) {
			for(DataVariable d: dataVarList) {				
				if(d.getScope().equals("INPUT_DATA")) {
					if (d.getType().equals("boolean")){
						inputVarTableModel.addRow(new Object[]{d.getSSID(), d.getName(), d.getType(), d.getScope(), false, false, true});
					} else {
						inputVarTableModel.addRow(new Object[]{d.getSSID(), d.getName(), d.getType(), d.getScope(), 0.0, 0.0, 100.0});
					}					
				}
			}
		}
	}
	private void setTraceMatrixTablePart() {
		tabbedPane = new JTabbedPane();
		// input variable table
		inputVariablesTable.setName("inputVariablesTable");
		inputVariablesTable.setAutoCreateRowSorter(true);
		inputVarTableModel = new DefaultTableModel(0, 0) {
			public boolean isCellEditable(int row, int column) {
				return column == 4 || column == 5 || column == 6;
			}
		};
		inputVarTableModel.setColumnIdentifiers(columnNames);
		inputVariablesTable.setModel(inputVarTableModel);
		setRowsForVariablesTables();
		STPATCGModelController.setTestInputVarTableModel(inputVarTableModel);
		
		// Transition Conditions Table
		transConditionTable = new JTable();
		transConditionTable.setName("EFSM Transition Table");
		transConditionTable.setAutoCreateRowSorter(true);
		transConditionTableModel = new DefaultTableModel(0, 0);
		transConditionTableModel
				.setColumnIdentifiers(colNamesFortransCondition);
		// set table contents
		setRowsForTransConditionTable();
		transConditionTable.setModel(transConditionTableModel);
		STPATCGModelController.setTransConditionTableModel(transConditionTableModel);
		
		// STPA Safety Conditions Table
		ssrTable = new JTable();
		ssrTable.setName("STPA Safety Requriements Table");
		ssrTable.setAutoCreateRowSorter(true);
		ssrTableModel = new DefaultTableModel(0, 0);
		ssrTableModel.setColumnIdentifiers(colNamesForSSR);
		// set table contents
		setRowsForSSRTable();
		ssrTable.setModel(ssrTableModel);
		STPATCGModelController.setSsrTableModel(ssrTableModel);
		
		// Trace Table		
		traceTable = new JTable();
		traceTable.setName("Trace Table");
		traceTable.setAutoCreateRowSorter(true);
		traceTableModel = new DefaultTableModel(0, 0);
		traceTableModel.setColumnIdentifiers(colNamesForTrace);
		// set table contents
		setRowsTraceTable(0.5);
		// Filter
		JPanel filterPanel = new JPanel();
		filterLbl = new JLabel("Minimum Required Similarity Degree");
		filter = new JComboBox(degree);
		filter.setSelectedIndex(10);
		
		filter.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				UpdateTraceTableJob job = new UpdateTraceTableJob("Update Traceability Matirx Table...", editor, filter.getSelectedIndex());
				job.schedule();
//				gtcButtonPressed();
			} 
		});
		filterPanel.add(filterLbl);
		filterPanel.add(filter);
		
		
		traceTable.setModel(traceTableModel);
		STPATCGModelController.setTraceMatrixTableModel(traceTableModel);
		
		scrollPane = new JScrollPane(inputVariablesTable);
		scrollPane1 = new JScrollPane(transConditionTable);
		scrollPane2 = new JScrollPane(ssrTable);
		scrollPane3 = new JScrollPane(traceTable);
		
		GridBagLayout layout = new GridBagLayout();
		JPanel subPanel = new JPanel();		
		subPanel.setLayout(new BorderLayout());
	
		subPanel.add(filterPanel, BorderLayout.PAGE_START);	
		subPanel.add(scrollPane3, BorderLayout.CENTER);
		
		tabbedPane.add("Test Input", scrollPane);
		tabbedPane.add("EFSM Transitions", scrollPane1);
		tabbedPane.add("Safety Requriements", scrollPane2);
		tabbedPane.add("Traceability between EFSM Transitions and Safety Requirements",
				subPanel);	
	}
	
	private void setRowsTraceTable(double minSimilarity) {
		String[] objects = new String[3];
		for(SimpleSTPAConstraint ssr: ssrList){			
			for(SimpleEFSMTransition efsmTrans: efsmConditionList){
				float similarity = calSimilaritySSRandEFSMCondition(ssr, efsmTrans);
				if(similarity >= minSimilarity){
					objects[0] = efsmTrans.getId();
					objects[1] = ssr.getSSID();
					objects[2] = String.valueOf((int)(similarity * 100)) + "%";
					traceTableModel.addRow(objects);
				}
			}
		}
//		new TableRowSorter<TableModel>(traceTableModel);
	}
	
	public void updateTraceTable(double d) {
		TCGeneratorPluginUtils.deleteTableContent(traceTableModel);
		setRowsTraceTable(d);
	}
	
	// calculate the similarity between STPA Safety Requirement and EFSM transition condition
	private float calSimilaritySSRandEFSMCondition(SimpleSTPAConstraint ssr, SimpleEFSMTransition efsmTransition){
		float result = 0;		
		float weightOfCA;
		float weightOfState;
		float weightOfCondition;
		// set weight for each part
		if(ssr.getControlAction().equals("")){
			weightOfCA = 0.0f;
			weightOfState = 0.5f;
			weightOfCondition = 0.5f;
		}else{
			weightOfCA = 0.33f;
			weightOfState = 0.33f;
			weightOfCondition = 0.34f;
		}		

		String ssrCA = ssr.getControlAction();
		String ssrSourceState = ssr.getSourceState();
		List<String> ssrConditions = ssr.getTransition();
		String efsmCA = efsmTransition.getControlAction();
		String efsmSourceState = efsmTransition.getSourceState();
		List<String> efsmConditions = efsmTransition.getConditions();
		
		float caPart = 0;
		String ssrCALC = ssrCA.toLowerCase();
		String efsmCALC = efsmCA.toLowerCase();
		if(ssrCALC.equals(efsmCALC)){
			caPart = 1;
		}
		
		float statePart = 0;
		if(ssrSourceState.equals(efsmSourceState)) 
			statePart = 1;
		float conditionPart = calWordsSimilarity(ssrConditions, efsmConditions);
		
		result = weightOfCA * caPart + weightOfState * statePart + weightOfCondition * conditionPart;
		if(result > 0){
//			System.out.format("%f * %f + %f * %f + %f * %f = %f%n", weightOfCA, caPart,weightOfState,statePart,weightOfCondition,conditionPart,result);
//			System.out.println(ssr.toString());
//			System.out.println(efsmTransition.toString());
		}		
		return result;
	}

	// calculate the similarity of two words list
	private float calWordsSimilarity(List<String> words1, List<String> words2){
		float score = 0.0f;
		for (String s1: words1){
			for(String s2: words2){
				if(s1.equals(s2)){
					score++;
				}
			}
		}
		float NumberOfSections = Math.max(words1.size(),words2.size());
		float similarity = score/NumberOfSections;
//		System.out.format("similarity=%f devides %f = %f%n",score, NumberOfSections, similarity);
		return similarity;
	}
	
	/**
	 * Normalize STPA Safety Requirements(SSR) and split the requirement into three parts: control action, source state and transition
	 * @param STPAConstraints
	 * @return A List contains following information for each SSR: SSID, control action, source state and transition
	 */
	private List<SimpleSTPAConstraint> normalizeSSR(List<RefinedSafetyRule> STPAConstraints){
		List<SimpleSTPAConstraint> ssrList = new ArrayList<SimpleSTPAConstraint>();
		for (RefinedSafetyRule acc : STPAConstraints) {
			String rule = acc.getCriticalCombies();	
			String constraint = acc.getRefinedSafetyConstraint();
//			System.out.println(constraint);
			if (rule != null && constraint != null) {
				rule = rule.replace(",", " && ").replace(" ", "").replace("==", "=");
				String[] parts = rule.split("&&");
				String sourceState = parts[0].substring(parts[0].lastIndexOf("=") + 1);
				List<String> transition = new ArrayList<String>();
				for (int i = 1; i < parts.length; i++){
					transition.add(parts[i]);
				}
				String controlAction = "";
				if(constraint.contains("must not be provided too")){
					String[] caParts = constraint.split("must not be provided too");
					controlAction = caParts[0].replace(" ", "");
				}else if(constraint.contains("must be provided when")){
					String[] caParts = constraint.split("must be provided when");
					controlAction = caParts[0].replace(" ", "");
				}
				SimpleSTPAConstraint ssr = new SimpleSTPAConstraint(String.valueOf(acc.getNumber()), controlAction, sourceState, transition);
//				System.out.println(ssr.toString());
				ssrList.add(ssr);
			}
		}
		return ssrList;
	}
	
	/**
	 * Normalize EFST transition conditions
	 * @param truthtable
	 * 				EFSM truth table
	 * @return A List contains following information for each StateTransition: ID, source state id, control action and condition
	 */
	private List<SimpleEFSMTransition> normalizeCondition(EFSM fsm, List<StateTransition> truthtable){
		List<SimpleEFSMTransition> result = new ArrayList<SimpleEFSMTransition>();
		for (StateTransition stFSM : truthtable) {
			String condition = stFSM.getCondition().replace(" ", "").replace("==", "=").replace("~", "!");
			List<String> conditionParts = new ArrayList<String>();
			if (condition != null) {
//				condition = condition.replace(" ", "");
				String[] parts = condition.split("(&&)|(\\|\\|)");
				for(int i = 0; i < parts.length; i++){
					conditionParts.add(parts[i]);
				}
//				System.out.println(condition);
//				System.out.println("Transition Condition: " + conditionParts);
			}
			String sourceState = stFSM.getSource();
			SimpleEFSMTransition transition = new SimpleEFSMTransition(fsm, String.valueOf(stFSM.getSSID()), sourceState, conditionParts);
			result.add(transition);
		}
		return result;
	}

	private void setRowsForSSRTable() {
		// TODO Auto-generated method stub
		
		for (RefinedSafetyRule acc : STPAConstraints) {
			String[] objects = new String[2];
			objects[0] = String.valueOf(acc.getNumber());
			objects[1] = acc.getSafetyRule();

			ssrTableModel.addRow(objects);
		}
//		new TableRowSorter<TableModel>(ssrTableModel);
	}

	private void setRowsForTransConditionTable() {
		if(fsm != null){
			List<StateTransition> truthtable = fsm.getfsmTruthTable();
			if (truthtable != null) {
				Collections.sort(truthtable, new Comparator<StateTransition>() {
			        @Override
			        public int compare(StateTransition t2, StateTransition t1)
			        {
			            return  Integer.valueOf(t2.getSSID()).compareTo(Integer.valueOf(t1.getSSID()));
			        }
			    });
				for (StateTransition t : truthtable) {
					System.out.println("StateTransition: " + t.toString());
					String[] objects = new String[5];
					objects[0] = t.getSSID();
					String condition = t.getCondition().replace("~", "!");
					objects[1] = condition;
					String sourceState = fsm.getNameOfstate(t.getSource());
					objects[2] = sourceState;
					String ca = "";
					for(SimpleEFSMTransition trans: this.efsmConditionList){
						if(trans.getSourceState().equals(sourceState)){
							ca = trans.getControlAction();
						}
					}
					objects[3] = ca;
					String fullTransition = "";
					if(ca.equals("")){
						fullTransition = "Source State = "  + sourceState + " && " + "Condition = " + condition;
					}else{
						fullTransition = "Source State = "  + sourceState + " && " + "Control Action = " + ca + " && " + "Condition = " + condition;
					}
							
					objects[4] = fullTransition;
					transConditionTableModel.addRow(objects);
				}
			}
//			new TableRowSorter<TableModel>(transConditionTableModel);
		}		
	}
	
	private void setConfigPart(){
		bottomPanel = new JPanel();
		GridLayout gridLayout = new GridLayout(5,0);
		bottomPanel.setLayout(gridLayout);
//		bottomPanel.setPreferredSize(new Dimension(1000, 180));
		
		// panel for test steps
		JPanel panel1 = new JPanel();		
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		panel1.setLayout(flowLayout);
		testStepLbl = new JLabel("No. Test Steps");
		testStep = new JTextField();
		testStep.setText("20");
		Dimension d = new Dimension();
		d.setSize(110, 28);
		testStep.setPreferredSize(d);
		//add components		
		panel1.add(testStepLbl);
		panel1.add(testStep);
		
		//panel for test suits number
		JPanel panel2 = new JPanel();
		panel2.setLayout(flowLayout);
		testSuitNoLbl = new JLabel("Test Results will be saved in ");
		oneTSBtn = new JRadioButton("One Test Suit");
		moreTSBtn = new JRadioButton("More Test Suits");
		oneTSBtn.setSelected(true);
		//Group the radio buttons.
		ButtonGroup group1 = new ButtonGroup();
		group1.add(oneTSBtn);
		group1.add(moreTSBtn);
		//add components
	    panel2.add(testSuitNoLbl);
	    panel2.add(oneTSBtn);
	    panel2.add(moreTSBtn);
		
		//panel for test algorithm		
		JPanel panel3 = new JPanel();	
//		gridLayout = new GridLayout(1,4);
		panel3.setLayout(flowLayout);
		testAlgLbl = new JLabel("Test Algorithm");
		dfsBtn = new JRadioButton("Depth First Search Algorithm");
		bfsBtn = new JRadioButton("Breath First Search Algorithm");
		randomBtn = new JRadioButton("Random Walk with Both Algorithms");
		randomBtn.setSelected(true);
		//Group the radio buttons.
		ButtonGroup group2 = new ButtonGroup();
	    group2.add(dfsBtn);
	    group2.add(bfsBtn);
	    group2.add(randomBtn);
	    //add components
	    panel3.add(testAlgLbl);
	    panel3.add(dfsBtn);
	    panel3.add(bfsBtn);
	    panel3.add(randomBtn);
	    
	    //panel for Test Coverage
	    JPanel panel4 = new JPanel();	
		panel4.setLayout(flowLayout);
	    testCoverageLbl = new JLabel("Test Coverages");
		allStatesCov = new JCheckBox("All States Coverage");
		allTransCov = new JCheckBox("All Transitions Coverage");
		ssrCov = new JCheckBox("STPA Safety Requirements Coverage");
		allStatesCov.setSelected(true);
		allStatesCov.setEnabled(false);
		allTransCov.setSelected(true);
		allTransCov.setEnabled(false);
//		System.out.println("allStatesCov selected ? " + allStatesCov.isSelected());
		
		//add components
	    panel4.add(testCoverageLbl);
	    panel4.add(allStatesCov);
	    panel4.add(allTransCov);
	    panel4.add(ssrCov);
	    
	    //panel for stop condition and button
	    JPanel panel5 = new JPanel();
//	    gridLayout = new GridLayout(1,3);
		panel5.setLayout(flowLayout);
		stopConditonLbl = new JLabel("Stop Condition");
		String[] stopConditions = {"State Coverage", "Transition Coverage", "STPA Safety Requirements Coverage"};
		//Create the combo box
		stopCondition = new JComboBox(stopConditions);
		stopCondition.setSelectedIndex(0);
		stopCondition.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (stopCondition.getSelectedIndex() == 2){
					ssrCov.setSelected(true);
				}
			} 
		});
		//add horizontal spaces
		String whiteSpace = " ";
		String padded = String.format("%-100s", whiteSpace);
		JLabel spaceLbl = new JLabel(padded);
		
		//Generate Test Case Button
		gtcBtn = new JButton("Generate Test Cases");
		gtcBtn.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean[] selectedAlg = {dfsBtn.isSelected(), bfsBtn.isSelected(), randomBtn.isSelected()};
				boolean[] selectedCoverage = {allStatesCov.isSelected(), allTransCov.isSelected(), ssrCov.isSelected()};
				boolean isOneSuit = true;
				if(moreTSBtn.isSelected()){
					isOneSuit = false;
				}
				GenerateTestCasesJob job = new GenerateTestCasesJob("Generating Test Cases...", fsm, tree, parseSTPA, configure, inputVariablesTable, traceTableModel, testStep.getText(), selectedAlg, selectedCoverage, stopCondition.getSelectedIndex(), isOneSuit);
				job.schedule();
			} 
		} );
		panel5.add(stopConditonLbl);
		panel5.add(stopCondition);
		panel5.add(spaceLbl);
		panel5.add(gtcBtn);
		
	    bottomPanel.add(panel1);
		bottomPanel.add(panel2);
		bottomPanel.add(panel3);
		bottomPanel.add(panel4);
		bottomPanel.add(panel5);

	}


	public DefaultTableModel getSsrTableModel() {
		return ssrTableModel;
	}

	
	public List<RefinedSafetyRule> getSTPAConstraints() {
		return STPAConstraints;
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}

}

