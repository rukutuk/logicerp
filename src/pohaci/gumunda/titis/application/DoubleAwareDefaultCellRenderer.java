package pohaci.gumunda.titis.application;

import java.awt.Component;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DoubleAwareDefaultCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;
    private static NumberFormat defaultNumFormat;
    public static NumberFormat numberFormat()
    {
        if (defaultNumFormat==null)
        {
            defaultNumFormat = NumberFormat.getInstance();
            DecimalFormat dformat = (DecimalFormat) defaultNumFormat;
            dformat.setMinimumFractionDigits(2);
        }
        return defaultNumFormat;
    }
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if ((value instanceof Double) || (value instanceof BigDecimal) || (value instanceof Float))
        {
            Number valNum = (Number)value;
            value = numberFormat().format(valNum);
            this.setHorizontalAlignment(JLabel.TRAILING);
        }
        else
            this.setHorizontalAlignment(JLabel.LEADING);
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                row, column);
    }
    

}
