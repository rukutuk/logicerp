package pohaci.gumunda.titis.accounting.cgui;


public class LookUpAccountPicker 
//extends AttributePicker 
{
	private LookUpAccountPicker()
	{
	
	}
	// class killed by yudhi
	  /*AttributeListDlg m_attrlistDlg = null;
	  CashAccount[] m_cashaccount = null;
	  CashAccount n_cashaccount = null;

	  public LookUpAccountPicker(Connection conn, long sessionid) {
	    super(conn, sessionid);
	    initComponent();
	    initData();
	  }

	  void initComponent() {
	    m_attrlistDlg = new AttributeListDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
	        "Cash Account", m_conn, m_sessionid);
	  }

	  void initData() {
	    try {
	      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
	      CashAccount[] cashaccount = logic.getAllCashAccount(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

	      DefaultListModel model = (DefaultListModel)m_attrlistDlg.getAttributeList().getModel();
	      for(int i = 0; i < cashaccount.length; i ++) {
	        model.addElement(new CashAccount(cashaccount[i]));//, CashAccount.CODE_DESCRIPTION));
	      }
	    }
	    catch(Exception ex) {
	      JOptionPane.showMessageDialog(this, ex.getMessage(),
	                                    "Warning", JOptionPane.WARNING_MESSAGE);
	    }
	  }

	  public void done() {
	    m_attrlistDlg.setVisible(true);
	    if(m_attrlistDlg.getResponse() == JOptionPane.OK_OPTION) {
	      Object[] object = m_attrlistDlg.getObject();
	      if(object.length > 0)
	       setObject(object[0]);
	    }
	  }
	  
	  public CashAccount getCashAccount(){
		 return n_cashaccount;  
	  }*/

	}

