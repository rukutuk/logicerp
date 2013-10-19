package pohaci.gumunda.titis.application;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

public abstract class AttributeCellEditor extends DefaultCellEditor implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public JFrame m_owner;
  JButton m_browseBt = new JButton("Browse...");
  public TableModel m_model = null;
  public int m_row, m_col;

  public Connection m_conn = null;
  public long m_sessionid = -1;
  Object m_object = null;

  public AttrListDlg m_attrdlg = null;
  
  // i add this
  protected short m_type = -1;

  public AttributeCellEditor(JFrame owner, String title, Connection conn, long sessionid) {
    super(new JTextField());
    m_owner = owner;
    m_conn = conn;
    m_sessionid = sessionid;
    m_browseBt.addActionListener(this);

    constructComponent(title);
  }
  
  /**
   * I tried to add this constructor
   * @param title
   */
  public AttributeCellEditor(JFrame owner, String title, Connection conn,
			long sessionid, short type) {
		super(new JTextField());
		m_owner = owner;
		m_conn = conn;
		m_sessionid = sessionid;
		m_browseBt.addActionListener(this);
		m_type = type;
		
		constructComponent(title);
	}
  
  void constructComponent(String title) {
    m_attrdlg = new AttrListDlg(m_owner, title, m_conn, m_sessionid);
    m_attrdlg.initData123();
  }

  public Component getTableCellEditorComponent(JTable table, Object value,
      boolean isSelected, int row, int column){
    m_model = table.getModel();
    m_row = row;
    m_col = column;
    return m_browseBt;
  }

  public abstract void initData();

  public Object getObject() {
    return m_object;
  }

  public void setObject(Object object) {
    m_object = object;
  }

  public JList getAttributeList() {
    return m_attrdlg.getAttributeList();
  }

  public DefaultListModel getAttributeListModel() {
    return (DefaultListModel)m_attrdlg.getAttributeList().getModel();
  }

  public void done() {
    m_attrdlg.setVisible(true);
    if(m_attrdlg.getResponse() == JOptionPane.OK_OPTION) {
      Object[] object = m_attrdlg.getObject();
      if(object.length > 0)
        m_model.setValueAt(object[0], m_row, m_col);
    }
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_browseBt){
      stopCellEditing();
      done();
    }
  }

  /**
   *
   */
  public class AttrListDlg extends AttributeListDlg {
	  /**
	   * 
	   */
	  private static final long serialVersionUID = 1L;
	  
	  public AttrListDlg(JFrame owner, String title, Connection conn, long sessionid) {
		  super(owner, title, conn, sessionid);
	  }
	  
	  void initData123() {
		  initData();
	  }
  }
}