package pohaci.gumunda.titis.accounting.cgui;


/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

public interface ITransactionObserver {

  public void setSelectedTransaction(Transaction trans);

  public void initTransactionViewToNoSelection();

  public boolean isNewTransaction();

  public boolean isTransactionEdited();

}