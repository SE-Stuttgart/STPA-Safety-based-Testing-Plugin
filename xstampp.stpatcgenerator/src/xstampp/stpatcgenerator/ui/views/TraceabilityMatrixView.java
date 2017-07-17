package xstampp.stpatcgenerator.ui.views;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import xstampp.astpa.model.extendedData.RefinedSafetyRule;
import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.model.astpa.ParseSTPAMain;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.StateTransition;
import xstampp.stpatcgenerator.model.stateflow.fsm.EFSM;

/**
 * This class defines the view of traceability matrix.
 * @author Ting Luk-He
 *
 */
public class TraceabilityMatrixView extends ViewPart {
	public final static String ID = "xstampp.stpatcgenerator.view.traceabilityMatrix";

	// Components for GUI
	Composite parentFrame;
	private JPanel panel;
	private Frame frame;
	private JTabbedPane tabbedPane;
	private JScrollPane scrollPane1;
	private JScrollPane scrollPane2;
	private JScrollPane scrollPane3;
	private JTable transConditionTable;
	private JTable ssrTable;
	private JTable traceTable;

	// Data models for tables
	private DefaultTableModel transConditionTableModel;
	private DefaultTableModel ssrTableModel;
	private DefaultTableModel traceTableModel;
	private String[] colNamesFortransCondition = { "ID",
			"Transition Conditions" };
	private String[] colNamesForSSR = { "ID", "STPA Safety Constraints" };
	private String[] colNamesForTrace = { "ID of Transition",
			"ID of STPA Safety Constraints" };

	// data
	private EFSM fsm = STPATCGModelController.getSfmHandler().getFsm();
	private ParseSTPAMain parseSTPA = STPATCGModelController.getConfWizard().getParseSTPA();

	@Override
	public void createPartControl(Composite parent) {
		parentFrame = new Composite(parent, SWT.EMBEDDED);
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		frame = SWT_AWT.new_Frame(parentFrame);
		frame.setLayout(new BorderLayout());
		tabbedPane = new JTabbedPane();

		// Transition Conditions Table
		transConditionTable = new JTable();
		transConditionTable.setName("Transition Condition Table");
		transConditionTableModel = new DefaultTableModel(0, 0);
		transConditionTableModel
				.setColumnIdentifiers(colNamesFortransCondition);
		// set table contents
		setRowsForTransConditionTable();
		transConditionTable.setModel(transConditionTableModel);

		// STPA Safety Conditions Table
		ssrTable = new JTable();
		ssrTable.setName("STPA Safety Requriements Table");
		ssrTableModel = new DefaultTableModel(0, 0);
		ssrTableModel.setColumnIdentifiers(colNamesForSSR);
		// set table contents
		setRowsForSSRTable();
		ssrTable.setModel(ssrTableModel);

		// Trace Table
		traceTable = new JTable();
		traceTable.setName("Trace Table");
		traceTableModel = new DefaultTableModel(0, 0);
		traceTableModel.setColumnIdentifiers(colNamesForTrace);
		// set table contents
		setRowsTraceTable();
		traceTable.setModel(traceTableModel);

		scrollPane1 = new JScrollPane(transConditionTable);
		scrollPane2 = new JScrollPane(ssrTable);
		scrollPane3 = new JScrollPane(traceTable);

		tabbedPane.add("Transition Condition", scrollPane1);
		tabbedPane.add("STPA Safety Requriements", scrollPane2);
		tabbedPane.add("Traceability between Transition Conditions and SSR",
				scrollPane3);

		panel.add(tabbedPane);
		panel.setVisible(true);
		frame.add(panel);
	}

	// TODO improvement of traceability matrix
	private void setRowsTraceTable() {
//		System.out.println("+++++++++++++++++++++++++++ Achtung! +++++++++++++++++++++++++++++++++");
		List<RefinedSafetyRule> STPAConstraints = (List<RefinedSafetyRule>) (List<?>) parseSTPA
				.getdataModel().getAllRefinedRules(null);
		List<StateTransition> truthtable = fsm.getfsmTruthTable();
		for (RefinedSafetyRule acc : STPAConstraints) {			
			String[] objects = new String[2];
			String rule = acc.getCriticalCombies();
			if (rule != null) {
				rule = rule.replace(",", " && ").replace(" ", "");
				System.out.println("STPA Rule:" + rule);
			}

			// rule = rule.replace("is less than",
			// "<").replace("is greater than", ">").replace(" and ", " & ");
			for (StateTransition stFSM : truthtable) {
				String condition = stFSM.getCondition().replace(" ", "");

				// condition = condition.replace("==", "=");
				if (condition != null) {
					condition = condition.replace(" ", "");
					System.out.println("Transition Condition: " + condition);
				}

				// System.out.println("conditons " + condition);
				if (rule != null) {
					if (rule.trim().contains(condition)) {

						objects[0] = String.valueOf(stFSM.getSSID());
						objects[1] = String.valueOf(acc.getNumber());

						traceTableModel.addRow(objects);
					}
				}

			}
		}
		new TableRowSorter<TableModel>(traceTableModel);
	}
	
	// calculate the similarity of two words list
	private float calSimilarity(ArrayList<String> words1, ArrayList<String> words2){
		int score = 0;
		for (String s1: words1){
			for(String s2: words2){
				if(s1.equals(s2)){
					score++;
				}
			}
		}
		float similarity = score/(Math.max(words1.size(),words2.size()));
		return similarity;
	}

	private void setRowsForSSRTable() {
		// TODO Auto-generated method stub
		List<RefinedSafetyRule> STPAConstraints = (List<RefinedSafetyRule>) (List<?>) parseSTPA
				.getdataModel().getAllRefinedRules(null);
		for (RefinedSafetyRule acc : STPAConstraints) {
			String[] objects = new String[2];
			objects[0] = String.valueOf(acc.getNumber());
			objects[1] = acc.getSafetyRule();

			ssrTableModel.addRow(objects);
		}
		new TableRowSorter<TableModel>(ssrTableModel);
	}

	private void setRowsForTransConditionTable() {
		if(fsm != null){
			List<StateTransition> truthtable = fsm.getfsmTruthTable();
			if (truthtable != null) {
				for (StateTransition t : truthtable) {
					String[] objects = new String[2];
					objects[0] = t.getSSID();
					String condition = t.getCondition().replace("~", "!");
					objects[1] = condition;
					transConditionTableModel.addRow(objects);
				}
			}
			new TableRowSorter<TableModel>(transConditionTableModel);
		}		
	}
	
	

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
