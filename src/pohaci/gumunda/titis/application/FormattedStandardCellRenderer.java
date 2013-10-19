/**
 * 
 */
package pohaci.gumunda.titis.application;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author dark-knight
 *
 */
public class FormattedStandardCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

	private int fontStyle = Font.PLAIN;

	private int horizontalAlignment = JLabel.LEFT;

	public FormattedStandardCellRenderer(int fontStyle, int horizontalAlignment) {
		this.fontStyle = fontStyle;
		this.horizontalAlignment = horizontalAlignment;
	}

	public FormattedStandardCellRenderer(int fontStyle,
			int horizontalAlignment, Color fontColor) {
		this.fontStyle = fontStyle;
		this.horizontalAlignment = horizontalAlignment;
	}

	public FormattedStandardCellRenderer(int fontStyle,
			int horizontalAlignment, Color fontColor, Color backColor) {
		this.fontStyle = fontStyle;
		this.horizontalAlignment = horizontalAlignment;
	}

	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {
		super.getTableCellRendererComponent(table, value, isSelected,
				hasFocus, row, column);
		
		setText((value == null) ? "    " : value.toString());
		setHorizontalAlignment(this.horizontalAlignment);
		
		Font font = getFont();

		setFont(font.deriveFont(this.fontStyle));
		return this;
	}
}
