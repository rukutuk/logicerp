package pohaci.gumunda.titis.accounting.beginningbalance;

public class BeginningBalanceTransactionCode {
	public static final String TRANSCODES[] = 
	{
		"Account Payable",
		"Account Receivable",
		"Bank",
		"Cash",
		"Cash Advance",
		"Emp receivable",
		"Expense sheet difference",
		"Company Loan",
		"Work in Progress"
	};
	int idx;
	String said;
    public BeginningBalanceTransactionCode(int idx)
    {
    	if (idx <1 || idx > TRANSCODES.length)
    		return;
    	this.said = TRANSCODES[idx-1];
    	this.idx = idx;
    }
    public String toString()
    {
    	return said;
    }
	public int getIdx() {
		return idx;
	}
	public static BeginningBalanceTransactionCode[] getList() {
		BeginningBalanceTransactionCode[] result = new BeginningBalanceTransactionCode[TRANSCODES.length];
		int i;
		for (i=0; i<result.length;i++)
			result[i] = new BeginningBalanceTransactionCode(i+1);
		return result;
	}
}
