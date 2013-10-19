package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PaychequeLabelPicker extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JTextField m_labelTextField;
  JButton m_browseBt = new JButton("...");

  Connection m_conn = null;
  long m_sessionid = -1;
  PaychequeLabel m_label = null;

  public PaychequeLabelPicker(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    m_browseBt.setPreferredSize(new Dimension(22, 18));
    m_browseBt.addActionListener(this);

    m_labelTextField = new JTextField() {
      /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	public void addMouseListener(
          MouseListener l) { }

      public boolean isFocusTraversable() {
        return false;
      }
    };

    setLayout(new BorderLayout(3, 1));
    add(m_labelTextField, BorderLayout.CENTER);
    add(m_browseBt, BorderLayout.EAST);
  }

  public void setEnabled(boolean enable) {
    super.setEnabled(enable);
    m_labelTextField.setEnabled(enable);
    m_browseBt.setEnabled(enable);
  }

  public PaychequeLabel getPaychequeLabel() throws Exception {
    return m_label;
  }

  public void setPaychequeLabel(PaychequeLabel label) {
    m_label = label;
    if(m_label != null)
      m_labelTextField.setText(m_label.toString());
    else
      m_labelTextField.setText("");
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_browseBt) {
      PaychequeLabelTreeDlg dlg = new PaychequeLabelTreeDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
          "Paycheque Label", m_conn, m_sessionid);
      dlg.setVisible(true);

      setPaychequeLabel(dlg.getPaychequeLabel());
    }
  }
}