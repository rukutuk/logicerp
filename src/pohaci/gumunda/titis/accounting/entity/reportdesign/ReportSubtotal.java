/**
 *
 */
package pohaci.gumunda.titis.accounting.entity.reportdesign;

import java.math.BigDecimal;

import pohaci.gumunda.titis.application.NumberRounding;

public class ReportSubtotal extends ReportRow {
	private static final long serialVersionUID = 1L;
	ReportGroup m_group;
	transient BigDecimal m_groupValue;

	public void calculate(ReportContext ctx)
    {
    	NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
       m_group.calculate(ctx);
       value = BigDecimal.valueOf(0);
       m_groupValue = m_group.value;
       value = m_groupValue;
       value = nr.round(value);
       compValue = m_group.compValue;
       compValue = nr.round(compValue);
    }

	public String toString(ReportingContext ctx) {
		if (normalDebit)
			return ctx.getNumberFormat().format(m_groupValue);
		else
			return ctx.getNumberFormat().format(
					m_groupValue.multiply(BigDecimal.valueOf(-1)));
	}

	public ReportSubtotal(String label, boolean normalDebit, ReportGroup group) {
		super(label, normalDebit);
		m_group = group;
		allowsChildren = false;
	}

	public ReportSubtotal(String label, boolean normalDebit) {
		super(label, normalDebit);
		allowsChildren = false;
	}

	public Integer getCode() {
		return new Integer(2);
	}
}