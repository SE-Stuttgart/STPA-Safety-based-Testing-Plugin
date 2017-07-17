package xstampp.stpatcgenerator.ui.views;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import xstampp.stpatcgenerator.util.TCGeneratorPluginUtils;

/**
 * This class defines the view of log error.
 * @author Ting Luk-He
 *
 */
public class LogErrorView extends ViewPart{
	public final static String ID = "xstampp.stpatcgenerator.view.logError";
	
	Composite parentFrame;
	private JPanel panel;
	private Frame frame;
	private JTable table;
	
	private static DefaultTableModel tableModel = new DefaultTableModel() {
		@Override
		public Class<?> getColumnClass(int column) {
			switch (column) {
			case 0: 
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
//	String[] columnNames = {"Type", "Message"};
//	public static ImageIcon wrongIcon = new ImageIcon(LogErrorView.class.getResource("/icons/Button-Close-icon.png"));
//	public static ImageIcon attentionIcon = new ImageIcon(LogErrorView.class.getResource("/icons/attention_icon.png"));
	String[] columnNames = {"Message"};
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		parentFrame = new Composite(parent, SWT.EMBEDDED);
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		frame = SWT_AWT.new_Frame(parentFrame);
		frame.setLayout(new BorderLayout());
	    
		tableModel = new DefaultTableModel(0, 0); 
		tableModel.setColumnIdentifiers(columnNames);
		
		table = new JTable();
		table.setModel(tableModel);
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		table.getColumnModel().getColumn(0).setPreferredWidth(40);
//		table.getColumnModel().getColumn(1).setPreferredWidth(2000);
		
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

	public DefaultTableModel getTableModel() {
		return tableModel;
	}
	
	public static void initErrorLog(){
		tableModel.addRow(new Object[]{"<html><font color=\"white\">" + "Error Log is ready to use." + "</font></html>"});
	}
	
	public static void writeWarning(String message, boolean deleteOldMsg){
		System.out.println("Wring Warning into Error Log");
		if(deleteOldMsg){
			TCGeneratorPluginUtils.deleteTableContent(tableModel);
		}
		tableModel.addRow(new Object[]{"<html><font color=\"orange\">" + message + "</font></html>"});
//		WriteMsgToErrorLogJob job = new WriteMsgToErrorLogJob("Writing Message to Error Log", "warning", message, tableModel, deleteOldMsg);
//		job.schedule();
//		tableModel.addRow(new Object[]{LogErrorView.attentionIcon, message});
	}
	
	public static void writeError(String message, boolean deleteOldMsg){
		System.out.println("Wring Error into Error Log");
		if(deleteOldMsg){
			TCGeneratorPluginUtils.deleteTableContent(tableModel);
		}
		tableModel.addRow(new Object[]{"<html><font color=\"red\">" + message + "</font></html>"});
//		WriteMsgToErrorLogJob job = new WriteMsgToErrorLogJob("Writing Message to Error Log", "error", message, tableModel, deleteOldMsg);
//		job.schedule();
//		tableModel.addRow(new Object[]{wrongIcon, message});
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

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
}
