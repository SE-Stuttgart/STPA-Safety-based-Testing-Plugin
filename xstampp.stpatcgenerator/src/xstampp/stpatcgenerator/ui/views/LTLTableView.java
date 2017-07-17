package xstampp.stpatcgenerator.ui.views;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import xstampp.stpatcgenerator.model.smv.GeneratorSMVFile;

/**
 * This class defines the view of LTL table.
 * @author Ting Luk-He
 *
 */
public class LTLTableView extends ViewPart{
	public final static String ID = "xstampp.stpatcgenerator.view.ltlTable";
	
	List<String> ltlList = GeneratorSMVFile.getLTL();
	Composite parentFrame;
	private JPanel panel;
	private Frame frame;
	private JTable table;
	
	private static DefaultTableModel tableModel = new DefaultTableModel() {
		@Override
		public Class<?> getColumnClass(int column) {
			switch (column) {
			case 1: 
				return ImageIcon.class;
			default:
				return Object.class;
			}
		}
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	String[] columnNames = {"ltl", "status"};
	public static ImageIcon wrongIcon = new ImageIcon(LTLTableView.class.getResource("/icons/Button-Close-icon.png"));
	public static ImageIcon okIcon = new ImageIcon(LTLTableView.class.getResource("/icons/Ok-icon.png"));
	public static ImageIcon questionIcon = new ImageIcon(LTLTableView.class.getResource("/icons/question-icon.png"));
	@Override
	public void createPartControl(Composite parent) {

		parentFrame = new Composite(parent, SWT.EMBEDDED);
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		frame = SWT_AWT.new_Frame(parentFrame);
		frame.setLayout(new BorderLayout());
		
		tableModel.setColumnIdentifiers(columnNames);
		
		table = new JTable();
		table.setModel(tableModel);
		setRows();
		resizeColumns();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		table.getColumnModel().getColumn(0).setPreferredWidth(800);
//		table.getColumnModel().getColumn(1).setPreferredWidth(120);
				
		JScrollPane scroll = new JScrollPane(table);
	    panel.add(scroll, BorderLayout.CENTER);
	    panel.setVisible(true);
	    frame.pack();
		frame.add(panel);
	}

	private void setRows(){
		if(ltlList != null) {
			for(String ltl: ltlList){
				tableModel.addRow(new Object[] {ltl,questionIcon});
			}			
		}
	}
		
	private void resizeColumns() {
		//SUMS 100%
		float[] columnWidthPercentage = {0.9f, 0.1f};
		
	    int tW = (int) (java.awt.Toolkit.getDefaultToolkit().getScreenSize().width * 0.8);
	    System.out.println("Table width = " + tW);
	    TableColumn column;
	    TableColumnModel jTableColumnModel = table.getColumnModel();
	    int cantCols = jTableColumnModel.getColumnCount();
	    for (int i = 0; i < cantCols; i++) {
	        column = jTableColumnModel.getColumn(i);
	        int pWidth = Math.round(columnWidthPercentage[i] * tW);
	        column.setPreferredWidth(pWidth);
	        System.out.println("Table width for column " +  i + " is " + pWidth);
	    }
	}
	public static void updateLTLTable(List<String> ltlResults){
		for(int i = 0; i < ltlResults.size(); i++){
			if(ltlResults.get(i).equals("true")){
				tableModel.setValueAt(okIcon, i, 1);
			} else if (ltlResults.get(i).equals("false")) {
				tableModel.setValueAt(wrongIcon, i, 1);
			}
		}
	}
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	public static DefaultTableModel getTableModel() {
		return tableModel;
	}

	public static void setTableModel(DefaultTableModel tableModel) {
		LTLTableView.tableModel = tableModel;
	}
	
}
