package pohaci.gumunda.titis.accounting.entity.reportdesign;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import pohaci.gumunda.titis.accounting.cgui.reportdesign.TreeableReportRow;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;

public abstract class ReportRow extends TreeableReportRow implements
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String label;
	protected transient BigDecimal value;
	protected transient BigDecimal compValue;
	protected boolean normalDebit;
	protected transient boolean calculated = false;
	transient ReportContext context;
	abstract public void calculate(ReportContext ctx);
	abstract public Integer getCode();
	protected int textSize;
	protected boolean bold;
	protected boolean underline;
	protected boolean italic;
	protected transient String displayLabel;
	protected int indent;
	protected boolean viewValue;
	protected int alignment;
	protected boolean usedStyleInValue;
	protected boolean invisible;
	private static boolean m_excel;
	protected boolean negatePositiveBalance;

	/**
	 * @return the negatePositiveBalance
	 */
	public boolean isNegatePositiveBalance() {
		return negatePositiveBalance;
	}
	/**
	 * @param negatePositiveBalance the negatePositiveBalance to set
	 */
	public void setNegatePositiveBalance(boolean negatePositiveBalance) {
		this.negatePositiveBalance = negatePositiveBalance;
	}
	public boolean isUsedStyleInValue() {
		return usedStyleInValue;
	}

	public void setUsedStyleInValue(boolean usedStyleInValue) {
		this.usedStyleInValue = usedStyleInValue;
	}

	public String getDebit() {
		return getDebit(context);
	}

	public String getCredit() {
		return getCredit(context);
	}

	String getDebit(ReportContext ctx) {
		if (value.compareTo(BigDecimal.valueOf(0)) >= 0)
			return ctx.getNumberFormat().format(value);
		return "";
	}

	String getCredit(ReportContext ctx) {
		if (value.compareTo(BigDecimal.valueOf(0)) < 0)
			return ctx.getNumberFormat().format(value.multiply(BigDecimal.valueOf(-1)));
		return "";
	}

	public String getValueStr() {
		if(isViewValue())
			return toString(context);
		return "";
	}

	public String getCompValueStr() {
		if(context.viewComparative)
			if(isViewValue())
				return toCompString(context);
		return "";
	}

	public String toCompString(ReportContext ctx) {
		if (!negatePositiveBalance) {
			if (normalDebit)
				return ctx.getNumberFormat().format(compValue);
			else
				return ctx.getNumberFormat().format(
						compValue.multiply(BigDecimal.valueOf(-1)));
		} else {
			if (!normalDebit)
				return ctx.getNumberFormat().format(compValue);
			else
				return ctx.getNumberFormat().format(
						compValue.multiply(BigDecimal.valueOf(-1)));
		}
	}

	public String getDisplayLabel() {
		return this.label;
	}

	public String toString(ReportContext ctx) {
		if (!negatePositiveBalance) {
			if (normalDebit)
				return ctx.getNumberFormat().format(value);
			else
				return ctx.getNumberFormat().format(
						value.multiply(BigDecimal.valueOf(-1)));
		} else {
			if (!normalDebit)
				return ctx.getNumberFormat().format(value);
			else
				return ctx.getNumberFormat().format(
						value.multiply(BigDecimal.valueOf(-1)));
		}
	}

	public final void preOrder(RowVisitor v) {
		v.visit(this);
		childVisitPreOrder(v);
	}

	public final void postOrder(RowVisitor v) {
		childVisitPostOrder(v);
		v.visit(this);
	}

	protected void childVisitPreOrder(RowVisitor v) {
	}

	protected void childVisitPostOrder(RowVisitor v) {
	}

	protected ReportRow(String label, boolean normalDebit) {
		super();
		this.label = label;
		this.normalDebit = normalDebit;
	}

	public void storeOutput(List list) {
		list.add(this);
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

	public boolean isSmallSize() {
		return textSize == 0;
	}

	public boolean isLargeSize() {
		return textSize >= 2;
	}

	public boolean isMediumSize() {
		return textSize == 1;
	}

	public boolean isUnderline() {
		return underline;
	}

	public void setUnderline(boolean underline) {
		this.underline = underline;
	}

	public boolean isItalic() {
		return italic;
	}

	public void setItalic(boolean italic) {
		this.italic = italic;
	}

	public int getIndent() {
		return indent;
	}

	public void setIndent(int indent) {
		this.indent = indent;
	}

	public ReportContext getContext() {
		return context;
	}

	public void setContext(ReportContext context) {
		this.context = context;
	}

	public String getLabel() {
		if(this.alignment==0) // left
			return (space(this.indent) + label);
		else
			return (label + space(this.indent));
	}

	private String space(int num){
		String spc = "";
		num *= 2;
		for(int i=0; i<num; i++){
			spc += " ";
		}
		return spc;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String toString()
    {
        return (this.label==null?"":label);
    }

	public boolean isViewValue() {
		return viewValue;
	}

	public void setViewValue(boolean invisible) {
		this.viewValue = invisible;
	}

	public int getAlignment() {
		return alignment;
	}

	public void setAlignment(int alignment) {
		this.alignment = alignment;
	}

	public static final List initializeReportData(ReportContext context, List rootList, AccountingSQLSAP sql, Connection connection, boolean excel) {
		m_excel = excel;
		calculateAll(context, rootList, sql, connection);
		final ArrayList reportRows = createReportList(rootList);
		return reportRows;
	}

	public static ArrayList createReportList(List rootList) {
		final ArrayList reportRows = new ArrayList();
		RowVisitor collectRows = new RowVisitor() {
			public void visit(ReportRow rw) {
				rw.storeOutput(reportRows);
			};
		};
		for (Iterator iter = rootList.iterator(); iter.hasNext();) {
			ReportRow element = (ReportRow) iter.next();
			element.preOrder(collectRows);
		}
		return reportRows;
	}

	public static ArrayList createStandardReportList(List rootList) {
		final ArrayList reportRows = new ArrayList();
		RowVisitor collectRows = new RowVisitor() {
			public void visit(ReportRow rw) {
				rw.storeToList(reportRows);
			};
		};
		for (Iterator iter = rootList.iterator(); iter.hasNext();) {
			ReportRow element = (ReportRow) iter.next();
			element.preOrder(collectRows);
		}
		return reportRows;
	}

	public void storeToList(List list) {
		list.add(this);
	}

	private static void calculateAll(ReportContext context, List rootList, AccountingSQLSAP sql, Connection connection) {
		CalculateStrategy strategy = new CalculateStrategy();
        contextInit(context, sql, connection);
        strategy.calculate(context, rootList);
	}

	private static void contextInit(ReportContext context, AccountingSQLSAP sql, Connection connection) {
		context.setAccountingSQLSAP(sql);
		context.setConnection(connection);
		context.setNumberFormat(NumberFormat.getInstance());
		DecimalFormat dformat = (DecimalFormat) context.getNumberFormat();
		dformat.setMinimumFractionDigits(2);
		dformat.setMaximumFractionDigits(2);
        if(m_excel){
        	dformat.setGroupingUsed(false);
        }
        else{
        	dformat.setNegativePrefix("(");
            dformat.setNegativeSuffix(")");
        }
	}

	static class CalculateStrategy {
        public void calculate(final ReportContext ctx, List rootRowList){
            RowVisitor v = new RowVisitor() {
                public void visit(ReportRow rw) {
                    rw.setContext(ctx);
                    rw.calculate(ctx);
                }
            };
            for (Iterator iter = rootRowList.iterator(); iter.hasNext();) {
                ReportRow rootRow = (ReportRow) iter.next();
                rootRow.postOrder(v);
                rootRow.postOrder(v);
            }
        }
    }

	public boolean isInvisible() {
		return invisible;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	public boolean isNormalDebit() {
		return normalDebit;
	}

	public void setNormalDebit(boolean normalDebit) {
		this.normalDebit = normalDebit;
	}

}