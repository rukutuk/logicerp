package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.sql.Connection;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.AttributeListDlg;
import pohaci.gumunda.titis.application.AttributePicker;

public class CurrencyPicker extends AttributePicker {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
AttributeListDlg m_attrlistDlg = null;

  public CurrencyPicker(Connection conn, long sessionid) {
    super(conn, sessionid);
    initComponent();
    initData();
  }

  public CurrencyPicker() {
	this(null,0);
  }

void initComponent() {
    m_attrlistDlg = new AttributeListDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        "Currency", m_conn, m_sessionid);
  }

  void initData() {
    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      Currency[] currency = logic.getAllCurrency(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      DefaultListModel model = (DefaultListModel)m_attrlistDlg.getAttributeList().getModel();
      for(int i = 0; i < currency.length; i ++) {
        model.addElement(new Currency(currency[i], Currency.DESCRIPTION));
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
      {   Currency currency = (Currency)object[0];
      	  currency = new Currency(currency,Currency.SYMBOL);
      	  //Perubahan cok gung 22 Mei 2007
//      	  try
//      	  {
//      		  // tak pindah ke sqlnya. yudhi
/*      	   Currency currencybase= (new AccountingSQLSAP().getBaseCurrency(m_conn));
      	   if (currencybase.getCode().compareTo(currency.getCode())==0)
      	     currency.setIsBase(true);
      	   else
      	     currency.setIsBase(false);*/
//      	  }
//      	  catch (Exception ex)
//    	  {
//    	 
//    	  }
      	  setObject(currency);
      }
    }
  }
}
