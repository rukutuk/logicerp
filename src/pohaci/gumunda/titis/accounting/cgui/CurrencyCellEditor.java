package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import javax.swing.*;

import java.sql.Connection;

import pohaci.gumunda.titis.application.*;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;

public class CurrencyCellEditor extends AttributeCellEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CurrencyCellEditor(JFrame owner, String title, Connection conn,
			long sessionid) {
		super(owner, title, conn, sessionid);
	}

	public void initData() {
		DefaultListModel model = this.getAttributeListModel();
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			Currency[] object = logic.getAllCurrency(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA);
			for (int i = 0; i < object.length; i++)
				model.addElement(new Currency(object[i], Currency.DESCRIPTION));

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(m_owner, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	//Override
	public void done() {
		m_attrdlg.setVisible(true);
		if (m_attrdlg.getResponse() == JOptionPane.OK_OPTION) {
			Object[] object = m_attrdlg.getObject();
			if (object.length > 0)
				m_model.setValueAt(new Currency((Currency) object[0],
						Currency.SYMBOL), m_row, m_col);
		}
	}
}