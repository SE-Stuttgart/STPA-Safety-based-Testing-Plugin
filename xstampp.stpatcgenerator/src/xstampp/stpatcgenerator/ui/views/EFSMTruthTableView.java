package xstampp.stpatcgenerator.ui.views;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.Tree;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.StateTransition;
import xstampp.stpatcgenerator.model.stateflow.fsm.EFSM;

/**
 * This class defines the view of safe test model truth table.
 * @author Ting Luk-He
 *
 */
public class EFSMTruthTableView extends ViewPart{
	public final static String ID = "xstampp.stpatcgenerator.view.EFSMTruthTable";
	
	Composite parentFrame;
	private JPanel panel;
	private Frame frame;
	private EFSM fsm = STPATCGModelController.getSfmHandler().getFsm();
	private Tree tree = STPATCGModelController.getConfWizard().getTree();
	
	String[] columnNames = {"ID", "Source", "Conditions", "ID", "Destination"};
	static DefaultTableModel tableModel;
	
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		parentFrame = new Composite(parent, SWT.EMBEDDED);
		panel = new JPanel();
		panel.setLayout(new BorderLayout());

		frame = SWT_AWT.new_Frame(parentFrame);
		frame.setLayout(new BorderLayout());
		JTable table = new JTable();
		tableModel = new DefaultTableModel(0,0){
			@Override
			public boolean isCellEditable(int row, int column) {
		       return false;
		   }
		};
		tableModel.setColumnIdentifiers(columnNames);
		table.setModel(tableModel);
		setRowsForTable();
		STPATCGModelController.setEfsmTruthTableModel(tableModel);
		
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		
		panel.add(scrollPane);
		panel.setVisible(true);
		frame.add(panel);
	}
	
	private void setRowsForTable() {
		if(fsm != null && tableModel != null) {
			List<StateTransition> truthTable = fsm.getfsmTruthTable();
			for(StateTransition t : truthTable) {
				if (tree.getNameOfstate(t.getSource()).length() <= 0) {
					tableModel.addRow(new Object[]{t.getSSID(), "start", t.getCondition(), t.getDestination(), tree.getNameOfstate(t.getDestination())});
				} else {
					tableModel.addRow(new Object[]{t.getSSID(), tree.getNameOfstate(t.getSource()), t.getCondition(), t.getDestination(), tree.getNameOfstate(t.getDestination())});
				}		
			}
		} else {
			System.out.println("FSM is null");
		}
	}
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
}
