package pohaci.gumunda.titis.application;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class FormattedDoubleCellEditor extends AbstractCellEditor implements
		TableCellEditor, ActionListener{

	private static final long serialVersionUID = 1L;

	private int alignment = JLabel.LEFT;

	private JFormattedTextField textJFfield;

	private DecimalFormat decimalFormat = new DecimalFormat("###,##0.00;(###,##0.00)");


	public FormattedDoubleCellEditor(){
		this.textJFfield = new JFormattedTextField(decimalFormat);
		this.textJFfield.addActionListener(this);
	}

	public FormattedDoubleCellEditor(int alignment) {
		this.alignment = alignment;
		this.textJFfield = new JFormattedTextField(this.decimalFormat);
		this.textJFfield.addActionListener(this);
	}

	public FormattedDoubleCellEditor(String format){
		this.decimalFormat = new DecimalFormat(format);
		this.textJFfield = new JFormattedTextField(this.decimalFormat);
		this.textJFfield.addActionListener(this);
	}

	public FormattedDoubleCellEditor(String format, int alignment) {
		this.decimalFormat = new DecimalFormat(format);
		this.alignment = alignment;
		this.textJFfield = new JFormattedTextField(decimalFormat);
		this.textJFfield.addActionListener(this);
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {

		if(value==null)
			textJFfield.setValue(new Double(0.0D));
		else
			textJFfield.setValue(value);
		textJFfield.setHorizontalAlignment(alignment);
		return textJFfield;
	}

	public Object getCellEditorValue() {
		try {
			textJFfield.commitEdit();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Number val = (Number) textJFfield.getValue();
		Double value = new Double(val.doubleValue());
		return value;
	}

	public boolean isCellEditable(EventObject e) {
		if(e instanceof MouseEvent)
            return ((MouseEvent)e).getClickCount() >= 1;
        else
            return true;
	}

	public void cancelCellEditing() {
		fireEditingCanceled();
	}

	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	public boolean stopCellEditing() {
		fireEditingStopped();
		return true;
	}

	public void actionPerformed(ActionEvent e) {
		stopCellEditing();
	}

}
