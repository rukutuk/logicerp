/**
 *
 */
package pohaci.gumunda.titis.accounting.entity.reportdesign;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import pohaci.gumunda.titis.application.NumberRounding;

public class ReportGroup extends ReportRow {
	private static final long serialVersionUID = 1L;

	private int groupLevel;

	public void calculate(ReportContext ctx) {
		if (calculated)
			return;
		Iterator iterator = children.iterator();
		NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
		value = BigDecimal.valueOf(0);
		while (iterator.hasNext()) {
			ReportRow a = (ReportRow) iterator.next();
			if (a.value == null) {
				System.err.println("appan tuh?");
				continue;
			}
			value = value.add(a.value);
			value = nr.round(value);
		}
		compValue = BigDecimal.valueOf(0);
		iterator = children.iterator();
		while (iterator.hasNext()) {
			ReportRow a = (ReportRow) iterator.next();
			if (a.compValue == null) {
				System.err.println("appan tuh?");
				continue;
			}
			compValue = compValue.add(a.compValue);
			compValue = nr.round(compValue);
		}
		calculated = true;
	}

	protected void childVisitPostOrder(RowVisitor v) {
		Iterator iterator = children.iterator();
		while (iterator.hasNext()) {
			ReportRow rw = (ReportRow) iterator.next();
			rw.postOrder(v);
		}
	}

	protected void childVisitPreOrder(RowVisitor v) {
		Iterator iterator = children.iterator();
		while (iterator.hasNext()) {
			ReportRow rw = (ReportRow) iterator.next();
			rw.preOrder(v);
		}
	}

	public ReportGroup(String label, boolean normalDebit, Vector children) {
		super(label, normalDebit);
		this.children = children;
		allowsChildren = true;
	}

	public ReportGroup(String label, boolean normalDebit) {
		super(label, normalDebit);
		allowsChildren = true;
	}

	public Integer getCode() {
		if (groupLevel == 0)
			return new Integer(3);
		else
			return new Integer(4);
	}

	public void storeOutput(List list) {
		/*if (!invisible) {
			if (!context.isShowZeroValue()) {
				if (context.isViewComparative()) {
					if (!((value.equals(BigDecimal.valueOf(0))) && (compValue
							.equals(BigDecimal.valueOf(0)))))
						super.storeOutput(list);
				} else {
					if (!value.equals(BigDecimal.valueOf(0)))
						super.storeOutput(list);
				}
			} else
				super.storeOutput(list);
		}*/
		if (!invisible){
			super.storeOutput(list);
		}
	}

	public int getGroupLevel() {
		return groupLevel;
	}

	public void setGroupLevel(int groupLevel) {
		this.groupLevel = groupLevel;
	}
}