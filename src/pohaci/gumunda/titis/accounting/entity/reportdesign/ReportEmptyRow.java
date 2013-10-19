package pohaci.gumunda.titis.accounting.entity.reportdesign;

import java.math.BigDecimal;

public class ReportEmptyRow extends ReportRow {

    private static final long serialVersionUID = 1L;

    public ReportEmptyRow() {
        super("", false);
        allowsChildren = false;
    }
    public ReportEmptyRow(String label) {
        super(label, false);
        allowsChildren = false;
    }
    static BigDecimal zero = BigDecimal.valueOf(0);
    public void calculate(ReportContext ctx) {
        this.value = zero;
        this.compValue = zero;
    }

    public Integer getCode() {
        return new Integer(0);
    }
    
    public String toString(ReportContext ctx) {
        return "";
    }
	public String getLabel() {
		return "";
	}
}
