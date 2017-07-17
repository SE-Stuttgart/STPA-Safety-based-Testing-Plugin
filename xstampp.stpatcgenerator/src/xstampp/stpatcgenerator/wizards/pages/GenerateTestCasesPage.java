package xstampp.stpatcgenerator.wizards.pages;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;

import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.DataVariable;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.Tree;
import xstampp.ui.wizards.AbstractWizardPage;

/**
 * This class defines the layout for the start configuration wizard of STPA
 * TCGenerator.
 * 
 * @author Ting Luk-He
 *
 */
public class GenerateTestCasesPage extends AbstractWizardPage implements SelectionListener {
	Composite parentFrame;
	private JPanel panel;
	private Frame frame;
	private JScrollPane scrollPane1;
	private JScrollPane scrollPane2;
	private JTable inputVariablesTable = new JTable();

	private static Tree tree = STPATCGModelController.getConfWizard().getTree();
	static List<DataVariable> dataVarList = tree.getDatavariables();

	DefaultTableModel inputVarTableModel;
	String[] columnNames = { "ID", "Variable name", "Data Type", "Scope", "Initial Value", "Minimum Value",
			"Maximum Value" };

	public GenerateTestCasesPage(String pageName, String projectName) {
		super(pageName, projectName);
		// TODO Auto-generated constructor stub
	}

	private void setRowsForVariablesTables() {
		if (tree != null && inputVarTableModel != null) {
			for (DataVariable d : dataVarList) {
				if (d.getScope().equals("INPUT_DATA")) {
					if (d.getType().equals("boolean")) {
						inputVarTableModel.addRow(new Object[] { d.getSSID(), d.getName(), d.getType(), d.getScope(),
								false, false, true });
					} else {
						inputVarTableModel.addRow(
								new Object[] { d.getSSID(), d.getName(), d.getType(), d.getScope(), 0.0, 0.0, 100.0 });
					}
				}
			}
		}
	}

	@Override
	public void createControl(Composite parent) {

		parentFrame = new Composite(parent, SWT.EMBEDDED);
		panel = new JPanel();
		panel.setLayout(new BorderLayout());

		frame = SWT_AWT.new_Frame(parentFrame);
		frame.setLayout(new BorderLayout());

		// input variable table
		inputVariablesTable.setName("inputVariablesTable");
		inputVarTableModel = new DefaultTableModel(0, 0) {
			public boolean isCellEditable(int row, int column) {
				return column == 4 || column == 5 || column == 6;
			}
		};
		inputVarTableModel.setColumnIdentifiers(columnNames);
		inputVariablesTable.setModel(inputVarTableModel);

		setRowsForVariablesTables();

		scrollPane1 = new JScrollPane(inputVariablesTable);
		scrollPane2 = new JScrollPane(inputVariablesTable);
		panel.add(scrollPane1);
		panel.add(scrollPane2);
		panel.setVisible(true);
		frame.add(panel);
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean checkFinish() {
		// TODO Auto-generated method stub
		return false;
	}

}
