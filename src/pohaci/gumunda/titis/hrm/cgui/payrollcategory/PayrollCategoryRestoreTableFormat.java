package pohaci.gumunda.titis.hrm.cgui.payrollcategory;

import java.util.Comparator;
import java.util.Date;

import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.impl.sort.ComparableComparator;

public class PayrollCategoryRestoreTableFormat implements AdvancedTableFormat {

	public Class getColumnClass(int column) {
		if (column == 0)
			return String.class;
		if (column == 1)
			return String.class;
		if (column == 2)
			return Date.class;
		return Object.class;
	}

	public Comparator getColumnComparator(int column) {
		return new ComparableComparator();
	}

	public int getColumnCount() {
		return 3;
	}

	public String getColumnName(int column) {
		if (column == 0)
			return "Period";
		if (column == 1)
			return "Description";
		if (column == 2)
			return "Creation Date";

		return null;
	}

	public Object getColumnValue(Object object, int column) {
		PayrollCategoryBackupMaster master = (PayrollCategoryBackupMaster) object;

		if (column == 0)
			return master.getPeriod();
		if (column == 1)
			return master.getDescription();
		if (column == 2)
			return master.getWorkingDate();
		return null;
	}

}
