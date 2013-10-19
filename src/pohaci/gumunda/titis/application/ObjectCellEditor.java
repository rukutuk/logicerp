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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

public abstract class ObjectCellEditor extends DefaultCellEditor implements
		ActionListener {
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

	public ObjectCellEditor(JFrame owner, Connection conn, long sessionid) {
		super(new JTextField());
		m_owner = owner;
		m_conn = conn;
		m_sessionid = sessionid;
		m_browseBt.addActionListener(this);
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		m_model =  table.getModel();
		m_row = row;
		m_col = column;
		m_object = value;
		return m_browseBt;
	}

	public Component getTableCellEditorComponent(JTable table, int row,
			int column) {
		m_model = table.getModel();
		m_row = row;
		m_col = column;
		return m_browseBt;
	}

	public Object getObject() {
		return m_object;
	}

	public void setObject(Object object) {
		m_object = object;		
		m_model.setValueAt(m_object, m_row, m_col);
	}

	public abstract void done();

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_browseBt) {
			stopCellEditing();
			done();			
		}
	}
}