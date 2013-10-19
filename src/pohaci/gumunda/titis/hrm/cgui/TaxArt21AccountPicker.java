/**
 * 
 */
package pohaci.gumunda.titis.hrm.cgui;

import java.sql.Connection;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.application.AttributeListDlg;
import pohaci.gumunda.titis.application.AttributePicker;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

/**
 * @author irwan
 *
 */
public class TaxArt21AccountPicker extends AttributePicker {

	private AttributeListDlg attributeListDlg;
	private static final long serialVersionUID = 1L;

	public TaxArt21AccountPicker(Connection conn, long sessionid) {
		super(conn, sessionid);
		initComponent();
	    initData();
	}

	private void initData() {
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			Account[] account = logic.getDistinctTaxArt21Account(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA);
			DefaultListModel model = (DefaultListModel) attributeListDlg
					.getAttributeList().getModel();
			for (int i = 0; i < account.length; i++) {
				model.addElement(new Account(account[i], Account.CODE_DESCRIPTION));
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	private void initComponent() {
		attributeListDlg = new AttributeListDlg(
				pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
				"Tax Art 21 Account", m_conn, m_sessionid);
	}

	/* (non-Javadoc)
	 * @see pohaci.gumunda.titis.application.AttributePicker#done()
	 */
	public void done() {
		attributeListDlg.setVisible(true);
		if (attributeListDlg.getResponse() == JOptionPane.OK_OPTION) {
			Object[] object = attributeListDlg.getObject();
			if (object.length > 0)
				setObject(object[0]);
		}
	}
}
