package xstampp.stpatcgenerator.ui.editors;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.util.TCGeneratorPluginUtils;
import xstampp.stpatcgenerator.util.jobs.UpdateResultTableJob;
import xstampp.stpatcgenerator.util.jobs.UpdateTraceTableJob;
/**
 * This class illustrates the views and functions for the test case result editor.
 *  
 * @author Ting Luk-He
 *
 */
public class TestCaseResultEditor extends TCGeneratorAbstractEditor {
	public static final String ID = "xstampp.stpatcgenerator.editor.testCaseResult";
	private final TestCaseResultEditor editor = this;
	// GUI Components
	Composite parentFrame;
	private JPanel panel;
	private Frame frame;
	private JScrollPane scrollPane1;
	private JScrollPane scrollPane2;
	private JTree jTreeOfTestCases;
	private JTable table;
	private DefaultTableModel modelOrig;
	DefaultTableModel model;
	// Filter
	JLabel filterLbl;
	String[] filterItems = {"with all test cases", "only with SSR traced test cases "};
	JComboBox filter;

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

	@Override
	public void createPartControl(Composite parent) {
		parentFrame = new Composite(parent, SWT.EMBEDDED);
		panel = new JPanel(new BorderLayout(), true);

		frame = SWT_AWT.new_Frame(parentFrame);
		frame.setLayout(new BorderLayout());
		
		jTreeOfTestCases = new JTree();
		table = new JTable();
		modelOrig = TCGeneratorPluginUtils.saveTableContent(STPATCGModelController.getTestCaseTableModel());
		setTree();
		setTable();
		
		scrollPane1 = new JScrollPane(jTreeOfTestCases);
		panel.add(scrollPane1, BorderLayout.WEST);
		
		// Filter
		JPanel filterPanel = new JPanel();
		filterLbl = new JLabel("Show Result: ");
		filter = new JComboBox(filterItems);
		filter.setSelectedIndex(0);		
		filter.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				UpdateResultTableJob job = new UpdateResultTableJob("Update Test Cases Result Table...", editor, filter.getSelectedIndex());
				job.schedule();
			} 
		});
		filterPanel.add(filterLbl);
		filterPanel.add(filter);
		
		scrollPane2 = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JPanel eastPanel = new JPanel(new BorderLayout(), true);		
		eastPanel.add(filterPanel, BorderLayout.NORTH);
		eastPanel.add(scrollPane2, BorderLayout.CENTER);

		panel.add(eastPanel, BorderLayout.CENTER);
		panel.setVisible(true);
		frame.add(panel);
	}

	private void setTable() {
		model = TCGeneratorPluginUtils.saveTableContent(modelOrig);
		table.setModel(model);
		TableColumn column = null;
		table.setAutoCreateRowSorter(true);
        column = table.getColumnModel().getColumn(0);
        column.setPreferredWidth(50);
        column = table.getColumnModel().getColumn(3);
        column.setPreferredWidth(50);		
	}

	private void setTree() {
		jTreeOfTestCases.removeAll();
		jTreeOfTestCases.setExpandsSelectedPaths(true);
		jTreeOfTestCases.setModel(STPATCGModelController.getTestCaseTreeModel());		
	}

	public void updateResultTable(boolean allTC){
		TCGeneratorPluginUtils.deleteTableContent(model);
		model = TCGeneratorPluginUtils.saveTableContent(modelOrig);
		if(allTC){			
			table.setModel(model);
		} else{
			for(int i = 0; i < model.getRowCount(); i++){
				if (model.getValueAt(i, 3).equals("") || model.getValueAt(i, 3).equals(" ")){
					model.removeRow(i);
					i--;
				}
			}
			table.setModel(model);
		}
		TableColumn column = null;
		table.setAutoCreateRowSorter(true);
        column = table.getColumnModel().getColumn(0);
        column.setPreferredWidth(50);
        column = table.getColumnModel().getColumn(3);
        column.setPreferredWidth(50);	
	}
	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

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

}

