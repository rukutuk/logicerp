/**
 * 
 */
package pohaci.gumunda.titis.application;

import java.awt.Component;
import java.awt.Font;
import java.text.DecimalFormat;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author dark-knight
 * 
 */
public class FormattedDoubleCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

	private int fontStyle = Font.PLAIN;
	
	private int alignment = JLabel.RIGHT;

	private DecimalFormat decimalFormat = new DecimalFormat(
			"###,##0.00;(###,##0.00)");

	public FormattedDoubleCellRenderer() {

	}

	public FormattedDoubleCellRenderer(int alignment, int fontStyle) {
		this.alignment = alignment;
		this.fontStyle = fontStyle;
	}

	public FormattedDoubleCellRenderer(String decimalFormat) {
		this.decimalFormat = new DecimalFormat(decimalFormat);
	}

	public FormattedDoubleCellRenderer(String decimalFormat, int alignment,
			int fontStyle) {
		this.decimalFormat = new DecimalFormat(decimalFormat);
		this.alignment = alignment;
		this.fontStyle = fontStyle;
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);

		if (value == null)
			setText("");
		else {
			if (value instanceof Double)
				setText(this.decimalFormat.format(((Double) value)
						.doubleValue()));
			else
				setText(value.toString());
		}

		Font font = getFont();

		setFont(font.deriveFont(this.fontStyle));
		setHorizontalAlignment(this.alignment);
		
		return this;
	}
}