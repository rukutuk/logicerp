package pohaci.gumunda.titis.accounting.cgui;

public abstract class TransactionCode {

	protected Transaction trans;
	private short status = -1;

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public Transaction getTrans() {
		return trans;
	}

	public void setTrans(Transaction transaction) {
		this.trans = transaction;
	}
}
