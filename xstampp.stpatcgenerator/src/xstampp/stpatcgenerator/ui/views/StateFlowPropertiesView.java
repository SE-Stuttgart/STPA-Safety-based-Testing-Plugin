package xstampp.stpatcgenerator.ui.views;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.DataVariable;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.StateNode;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.Tree;
import xstampp.stpatcgenerator.ui.table.ButtonsEditor;
import xstampp.stpatcgenerator.ui.table.ButtonsPanel;
import xstampp.stpatcgenerator.ui.table.ButtonsRenderer;

/**
 * This class defines the view of safe behavioral model properties.
 * @author Ting Luk-He
 *
 */
public class StateFlowPropertiesView extends ViewPart{
	public final static String ID = "xstampp.stpatcgenerator.view.stateFlowProperties";
//	private TableViewer viewer;
	Composite parentFrame;
	private JPanel panel;
	private Frame frame;
	JPanel btnPanel = new ButtonsPanel();
	
	private static Tree tree = STPATCGModelController.getConfWizard().getTree();
	private static List<StateNode> statesList = tree.getListOfState();
	private static List<DataVariable> dataVarList = tree.getDatavariables();
	
	String[] columnNamesForStates = {"ID",
            "Name", "Parent ID", "Type Decomposition", "Action"}; 
	String[] columnNamesForVariableProperties = {"ID", "Name", "Type", "Action"}; 
	
	DefaultTableModel statesTableModel;
	DefaultTableModel inputVarTableModel;
	DefaultTableModel outputVarTableModel;
	DefaultTableModel localVarTableModel;

	public void setRowsForStatesTable() {
		if(tree != null && statesTableModel != null) {
			for(StateNode s: statesList) {
				statesTableModel.addRow(new Object[]{s.getId(), s.getName(), s.getParentID(), s.getTypeDecomposition(), btnPanel});
				
			}
		}
	}
	
	public void setRowsForVariablesTables() {
		if(tree != null && inputVarTableModel != null && outputVarTableModel != null && localVarTableModel != null) {
			for(DataVariable d: dataVarList) {				
				if(d.getScope().equals("INPUT_DATA")) {
					inputVarTableModel.addRow(new Object[]{d.getSSID(), d.getName(), d.getType(), btnPanel});
				}else if(d.getScope().equals("OUTPUT_DATA")) {
					outputVarTableModel.addRow(new Object[]{d.getSSID(), d.getName(), d.getType(), btnPanel});
				}else if(d.getScope().equals("LOCAL_DATA")) {
					localVarTableModel.addRow(new Object[]{d.getSSID(), d.getName(), d.getType(), btnPanel});
				}
			}
		}
	}
	
	@Override
	public void createPartControl(Composite parent) {

//		System.out.println("************************ Create part control for Stateflow Properties ************************");
		parentFrame = new Composite(parent, SWT.EMBEDDED);
		panel = new JPanel();
		panel.setLayout(new BorderLayout());

		frame = SWT_AWT.new_Frame(parentFrame);
		frame.setLayout(new BorderLayout());
		
	    JTabbedPane tabbedPane = new JTabbedPane();
	    
	    // set model for statesTable and set only the variable name editable
	    JTable statesTable = new JTable();   
	    statesTable.setName("statesTable");
	    statesTableModel = new DefaultTableModel(0, 0)
	    {
	    	public boolean isCellEditable(int row, int column) {
	    		return column == 1 || column == 4;
	    	}
	    };
	    statesTableModel.setColumnIdentifiers(columnNamesForStates);	    		
	    statesTable.setModel(statesTableModel);
	    statesTable.getColumn("Action").setCellRenderer(new ButtonsRenderer());
	    statesTable.getColumn("Action").setCellEditor(new ButtonsEditor(statesTable));		    
	    // set table layout
	    statesTable.setRowHeight(30);
	    setJTableColumnsWidth(statesTable, 480, 10, 30, 10, 30, 20);	    
	    // set table contents
	    setRowsForStatesTable();	    
	    
	    // set model for inputVariablesTable and set only the variable name editable
		JTable inputVariablesTable = new JTable();
		inputVariablesTable.setName("inputVariablesTable");
		inputVarTableModel = new DefaultTableModel(0, 0)
		{
	    	public boolean isCellEditable(int row, int column) {
		        //Only the third column
		        return column == 1 || column == 3;
	    	}
		};
		inputVarTableModel.setColumnIdentifiers(columnNamesForVariableProperties);
		inputVariablesTable.setModel(inputVarTableModel);
		inputVariablesTable.getColumn("Action").setCellRenderer(new ButtonsRenderer());
		inputVariablesTable.getColumn("Action").setCellEditor(new ButtonsEditor(inputVariablesTable));		    
	    // set table layout
		inputVariablesTable.setRowHeight(30);
	    setJTableColumnsWidth(inputVariablesTable, 480, 20, 20, 20, 40);
		
	    // set model for outputVariablesTable and set only the variable name editable
		JTable outputVariablesTable = new JTable();
		outputVariablesTable.setName("outputVariablesTable");
		outputVarTableModel = new DefaultTableModel(0, 0)
		{
	    	public boolean isCellEditable(int row, int column) {
		        //Only the third column
		        return column == 1 || column == 3;
	    	}
		};
		outputVarTableModel.setColumnIdentifiers(columnNamesForVariableProperties);
		outputVariablesTable.setModel(outputVarTableModel);
		outputVariablesTable.getColumn("Action").setCellRenderer(new ButtonsRenderer());
		outputVariablesTable.getColumn("Action").setCellEditor(new ButtonsEditor(outputVariablesTable));		    
	    // set table layout
		outputVariablesTable.setRowHeight(30);
	    setJTableColumnsWidth(outputVariablesTable, 480, 20, 20, 20, 40);
		
	    // set model for localVariablesTable and set only the variable name editable
		JTable localVariablesTable = new JTable();
		localVariablesTable.setName("localVariablesTable");
		localVarTableModel = new DefaultTableModel(0, 0)
		{
	    	public boolean isCellEditable(int row, int column) {
		        //Only the third column
		        return column == 1 || column == 3;
	    	}
		};
		localVarTableModel.setColumnIdentifiers(columnNamesForVariableProperties);
		localVariablesTable.setModel(localVarTableModel);
		localVariablesTable.getColumn("Action").setCellRenderer(new ButtonsRenderer());
		localVariablesTable.getColumn("Action").setCellEditor(new ButtonsEditor(localVariablesTable));		    
	    // set table layout
		localVariablesTable.setRowHeight(30);
	    setJTableColumnsWidth(localVariablesTable, 480, 20, 20, 20, 40);
		
	    // set contents for all Variable tables
		setRowsForVariablesTables();
		
		STPATCGModelController.setStatesTableModel(statesTableModel);
	    STPATCGModelController.setInputVarTableModel(inputVarTableModel);
	    STPATCGModelController.setOutputVarTableModel(outputVarTableModel);
	    STPATCGModelController.setLocalVarTableModel(localVarTableModel);
		
		JScrollPane scrollPane1 = new JScrollPane(statesTable);
		JScrollPane scrollPane2 = new JScrollPane(inputVariablesTable);
		JScrollPane scrollPane3 = new JScrollPane(outputVariablesTable);
		JScrollPane scrollPane4 = new JScrollPane(localVariablesTable);

		statesTable.setFillsViewportHeight(true);
		inputVariablesTable.setFillsViewportHeight(true);
		outputVariablesTable.setFillsViewportHeight(true);
		localVariablesTable.setFillsViewportHeight(true);
		
		tabbedPane.add("States", scrollPane1);
		tabbedPane.add("Input Varialbles", scrollPane2);
		tabbedPane.add("Output Variables", scrollPane3);
		tabbedPane.add("Local Variables", scrollPane4);
		
		panel.add(tabbedPane);
		panel.setVisible(true);
		frame.add(panel);

	}
	
	public static void setJTableColumnsWidth(JTable table, int tablePreferredWidth,
	        double... percentages) {
	    double total = 0;
	    for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
	        total += percentages[i];
	    }
	 
	    for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
	        TableColumn column = table.getColumnModel().getColumn(i);
	        column.setPreferredWidth((int)
	                (tablePreferredWidth * (percentages[i] / total)));
	    }
	}
	
	// modify state name in stateflow model
	public static void modifyStateName(String id, String name){
		for(StateNode s: statesList){
			if(s.getId().equals(id)){
				s.setName(name);
				tree.setListOfState(statesList);
			}
		}
	}
	// modify name of variables in stateflow model 
	public static void modifyVariableName(String id, String name){
		for(DataVariable d: dataVarList){
			if(d.getSSID().equals(id)){
				d.setName(name);
				tree.setDatavariables(dataVarList);
			}
		}
	}

//	private void createViewer(Composite parent) {
		// TODO Auto-generated method stub
//		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
//		        | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
//		
//		createColumns(parent, viewer);
//		    final Table table = viewer.getTable();
//		    table.setHeaderVisible(true);
//		    table.setLinesVisible(true);
//		    viewer.setContentProvider(new ArrayContentProvider());
//		    // get the content for the viewer, setInput will call getElements in the
//		    // contentProvider
//		    viewer.setInput(StateFlowPropertiesProvider.INSTANCE.getProperties());
//		    // make the selection available to other views
//		    getSite().setSelectionProvider(viewer);
		
		 
//	}
	
//	private void createColumns(Composite parent, TableViewer viewer) {
//		// TODO Auto-generated method stub
//		String[] titles = { "States", "Transitions", "Input??"};
//		int[] bounds = { 100, 100, 100};
//		
//		// first column is for the source id
//	    TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
//	    col.setLabelProvider(new ColumnLabelProvider() {
//	      @Override
//	      public String getText(Object element) {
//	    	  StateFlowProperties p = (StateFlowProperties) element;
//	        return p.getStatesNumber();
//	      }
//	    });
//	    
//	    // second column is for the source state
//	    col = createTableViewerColumn(titles[1], bounds[1], 1);
//	    col.setLabelProvider(new ColumnLabelProvider() {
//	      @Override
//	      public String getText(Object element) {
//	    	  StateFlowProperties p = (StateFlowProperties) element;
//	        return p.getTransitionNumber();
//	      }
//	    });
//	    
//	    // third column is for the conditions
//	    col = createTableViewerColumn(titles[2], bounds[2], 2);
//	    col.setLabelProvider(new ColumnLabelProvider() {
//	      @Override
//	      public String getText(Object element) {
//	    	  StateFlowProperties p = (StateFlowProperties) element;
//	        return p.getInputNumber();
//	      }
//	    });
//	}
	
//	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
//	    final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
//	        SWT.NONE);
//	    final TableColumn column = viewerColumn.getColumn();
//	    column.setText(title);
//	    column.setWidth(bound);
//	    column.setResizable(true);
//	    column.setMoveable(true);
//	    return viewerColumn;
//	  }
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	public static List<StateNode> getStatesList() {
		return statesList;
	}

	public static void setStatesList(List<StateNode> statesList) {
		StateFlowPropertiesView.statesList = statesList;
	}
	
	

}
