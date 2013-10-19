/**
 *
 */
package pohaci.gumunda.titis.accounting.helperutil;

import java.util.Comparator;
import java.util.Date;

import pohaci.gumunda.titis.accounting.entity.PmtOthers;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.impl.sort.ComparableComparator;

/**
 * @author dark-knight
 *
 */
public class PmtOthersTableFormat implements AdvancedTableFormat {

	/* (non-Javadoc)
	 * @see ca.odell.glazedlists.gui.AdvancedTableFormat#getColumnClass(int)
	 */
	public Class getColumnClass(int column) {
		if (column == 0)
			return String.class;
		if (column == 1)
			return Date.class;
		if (column == 2)
			return String.class;
		if (column == 3)
			return String.class;
		if (column == 4)
			return String.class;
		if (column == 5)
			return Double.class;
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.odell.glazedlists.gui.AdvancedTableFormat#getColumnComparator(int)
	 */
	public Comparator getColumnComparator(int column) {
		return new ComparableComparator();
	}

	/* (non-Javadoc)
	 * @see ca.odell.glazedlists.gui.TableFormat#getColumnCount()
	 */
	public int getColumnCount() {
		return 6;
	}

	/* (non-Javadoc)
	 * @see ca.odell.glazedlists.gui.TableFormat#getColumnName(int)
	 */
	public String getColumnName(int column) {
		if (column == 0)
			return "Ref.No.";
		if (column == 1)
			return "Trans.date";
		if (column == 2)
			return "Pay source";
		if (column == 3)
			return "Pay to";
		if (column == 4)
			return "Desc";
		if (column == 5)
			return "Total";
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.odell.glazedlists.gui.TableFormat#getColumnValue(java.lang.Object, int)
	 */
	public Object getColumnValue(Object obj, int column) {
		PmtOthers object = (PmtOthers) obj;

		if (column == 0)
			return object.getReferenceNo();
		if (column == 1)
			return object.getTransactionDate();
		if (column == 2)
			return object.getPaymentSource();
		if (column == 3)
			return object.getPayTo();
		if (column == 4)
			return object.getDescription();
		if (column == 5)
			return new Double(object.getTotal());

		return null;
	}

}
