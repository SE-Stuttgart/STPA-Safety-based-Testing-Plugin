package xstampp.stpatcgenerator.ui.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * A renderer for buttons.
 * 
 * @author Ting Luk-He
 *
 */
public class ButtonsRenderer extends ButtonsPanel implements TableCellRenderer {

	private static final long serialVersionUID = 1L;
	public ButtonsRenderer() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override public void updateUI() {
        super.updateUI();
        setName("Table.cellRenderer");
    }
    @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        return this;
    }
}

