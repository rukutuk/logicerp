/**
 * 
 */
package pohaci.gumunda.titis.accounting.cgui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TableRowNumRenderer extends  DefaultTableCellRenderer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Object newValue = new Integer(row+1);
		return super.getTableCellRendererComponent(table, newValue, isSelected, hasFocus,
				row, column);
	}
	
}