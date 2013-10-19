/**
 *
 */
package pohaci.gumunda.titis.accounting.entity.reportdesign;

import java.util.ArrayList;

import pohaci.gumunda.titis.application.NumberRounding;

/**
 * @author dark-knight
 *
 */
public class ReportLink extends ReportRow {

	private ReportRow link = null;
	private Design designLink = null;

	public Design getDesignLink() {
		return designLink;
	}

	public void setDesignLink(Design designLink) {
		this.designLink = designLink;
	}

	public ReportLink(String label, boolean normalDebit) {
		super(label, normalDebit);
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public void calculate(ReportContext ctx) {
		// dianggap pake report context yang sama
		if (calculated)
			return;

		CalculateStrategy strategy = new CalculateStrategy();
        ArrayList list = new ArrayList();
        list.add(link);
        //System.out.println("CTX: start:" + ctx.reportStartDate + " end:" + ctx.reportEndDate);

       /* IncomeStatementReportContext context = new IncomeStatementReportContext();
		context.setReportTitle(ctx.getReportTitle());
		context.setLanguage(ctx.getLanguage());
		context.setPeriodType(ctx.getPeriodType());
		context.setReportStartDate(ctx.getReportStartDate());
		context.setReportEndDate(ctx.getReportEndDate());
		context.setComparativeStartDate(ctx.getComparativeStartDate());
		context.setComparativeEndDate(ctx.getComparativeEndDate());
		context.setViewComparative(ctx.isViewComparative());
		context.setShowZeroValue(ctx.isShowZeroValue());

		context.setReportPeriodLabel(ctx.getReportPeriodLabel());
		context.setComparativePeriodLabel(ctx.getComparativePeriodLabel());

		context.setJournals(ctx.getJournals());
		context.setConnection(ctx.getConnection());
		context.setAccountingSQLSAP(ctx.getAccountingSQLSAP());*/

		strategy.calculate(ctx, list);


		//value = link.value.multiply(new BigDecimal(-1));
		//compValue = link.compValue.multiply(new BigDecimal(-1));

		NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
        value = link.value;
        value = nr.round(value);
        compValue = link.compValue;
        compValue = nr.round(compValue);

		calculated = true;
	}

	public Integer getCode() {
		return new Integer(1);
	}

	public ReportRow getLink() {
		return link;
	}

	public void setLink(ReportRow link) {
		this.link = link;
	}

}
