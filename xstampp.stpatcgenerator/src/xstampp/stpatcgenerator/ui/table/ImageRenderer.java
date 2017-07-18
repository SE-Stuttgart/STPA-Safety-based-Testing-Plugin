package xstampp.stpatcgenerator.ui.table;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ImageRenderer extends DefaultTableCellRenderer{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JLabel lbl = new JLabel();

	  ImageIcon icon = createImageIcon("/icons/Ok-icon.png","ok icon");

	  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
	      boolean hasFocus, int row, int column) {
	    lbl.setText((String) value);
	    lbl.setIcon(icon);
	    return lbl;
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
}
