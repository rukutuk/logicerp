// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 6/26/2007 11:33:06 AM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) noinners 
// Source File Name:   DoubleCellEditor.java

package pohaci.gumunda.titis.application;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

public class DblCellEditor extends AbstractCellEditor
    implements TableCellEditor, ActionListener
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public DblCellEditor()
    {
        m_textfield = new JTextField();
        m_textfield.setHorizontalAlignment(4);
        m_textfield.addActionListener(this);
        DecimalFormat decFormat = (DecimalFormat)NumberFormat.getInstance();
        DecimalFormatSymbols decimalFormatSymbols = decFormat.getDecimalFormatSymbols();
//        decimalFormatSymbols.setDecimalSeparator(',');
//        decimalFormatSymbols.setGroupingSeparator('.');
//        decimalFormatSymbols.setMonetaryDecimalSeparator(',');
        m_decimalFormat = new DecimalFormat("###,##0.00;-###,##0.00", decimalFormatSymbols);
    }

    public Component getTableCellEditorComponent(JTable jTable, Object objValue, boolean bIsSelected, int row, int nColumn)
    {
        if(objValue == null)
            m_dvalue = new Double(0.0D);
        else
        if(objValue instanceof Double)
            m_dvalue = (Double)objValue;
        else
            m_dvalue = new Double(0.0D);
        m_textfield.setText("");
        return m_textfield;
    }

    public Object getCellEditorValue()
    {
        return m_dvalue;
    }

    public boolean isCellEditable(EventObject e)
    {
        if(e instanceof MouseEvent)
            return ((MouseEvent)e).getClickCount() >= 2;
        else
            return true;
    }

    public boolean shouldSelectCell(EventObject eventObject)
    {
        return true;
    }

    public boolean stopCellEditing()
    {
        try
        {
            String strValue = m_textfield.getText();
            if(strValue == null || strValue.equals(""))
                //m_dvalue = new Double(0.0D);
            	m_dvalue = null;
            else
                m_dvalue = new Double(m_decimalFormat.parse(strValue).doubleValue());
        }
        catch(ParseException exception)
        {
            JOptionPane.showMessageDialog(m_textfield, String.valueOf(String.valueOf((new StringBuffer("Masukan tidak dapat diproses: \"")).append(m_textfield.getText()).append("\"\nDikembalikan ke \"").append(m_decimalFormat.format(m_dvalue.doubleValue())).append("\""))), "Kesalahan Input", 0);
        }
        fireEditingStopped();
        return true;
    }

    public void cancelCellEditing()
    {
        fireEditingCanceled();
    }

    public void actionPerformed(ActionEvent event)
    {
        stopCellEditing();
    }

    protected JTextField m_textfield;
    protected DecimalFormat m_decimalFormat;
    protected Double m_dvalue;
}