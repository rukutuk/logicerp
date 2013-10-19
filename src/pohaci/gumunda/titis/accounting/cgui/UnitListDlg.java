package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import javax.swing.*;

import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Unit;

public class UnitListDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  JFrame m_mainframe;
  JList m_list;
  JButton m_okBt, m_cancelBt;

  int m_iResponse = JOptionPane.NO_OPTION;
  Unit m_unit = null;

  public UnitListDlg(JFrame owner, Connection conn, long sessionid) {
    super(owner, "Unit", true);
    setSize(350, 350);
    m_mainframe = owner;
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
    initData();
  }

  void constructComponent() {
    m_list = new JList(new DefaultListModel());
    m_okBt = new JButton("OK");
    m_okBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(m_okBt);
    buttonPanel.add(m_cancelBt);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(new JScrollPane(m_list), BorderLayout.CENTER);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
  }

  void initData() {
    DefaultListModel model = (DefaultListModel)m_list.getModel();
    try {
      AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
      Unit[] unit = logic.getAllUnit(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      for(int i = 0; i < unit.length; i ++)
        model.addElement(new Unit(unit[i], Unit.CODE_DESCRIPTION));
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);

    }
  }

  public void setVisible( boolean flag ){
    Rectangle rc = m_mainframe.getBounds();
    Rectangle rcthis = getBounds();
    setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
              (int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
              (int)rcthis.getWidth(), (int)rcthis.getHeight());

    super.setVisible(flag);
  }

  void onOK() {
    m_unit = (Unit)m_list.getSelectedValue();
    if(m_unit == null) {
      JOptionPane.showMessageDialog(this, "Unit not yet been selected.");
      return;
    }

    m_iResponse = JOptionPane.OK_OPTION;
    dispose();
  }

  public Unit getUnit() {
    return m_unit;
  }

  public int getResponse() {
    return m_iResponse;
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_okBt) {
      onOK();
    }
    else if(e.getSource() == m_cancelBt) {
      dispose();
    }
  }
  
  public void mouseClicked(MouseEvent e){
	  if(e.getClickCount()== 2){
		  onOK();
	  }
  }
  
  
}