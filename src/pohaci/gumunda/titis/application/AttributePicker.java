package pohaci.gumunda.titis.application;

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

public abstract class AttributePicker extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JTextField m_objectTextField;
	public JButton m_browseBt = new JButton("...");
	public Connection m_conn = null;
	public long m_sessionid = -1;
	protected Object m_object = null;
	public AttributePicker(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		m_browseBt.setPreferredSize(new Dimension(22, 18));
		m_browseBt.addActionListener(this);

		m_objectTextField = new JTextField() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void addMouseListener(MouseListener l) {
			}

			public boolean isFocusTraversable() {
				return false;
			}
		};
		setLayout(new BorderLayout(3, 1));
		add(m_objectTextField, BorderLayout.CENTER);
		add(m_browseBt, BorderLayout.EAST);
		
	}

	public void setEnabled(boolean enable) {
		super.setEnabled(enable);
		m_objectTextField.setEnabled(enable);
		m_browseBt.setEnabled(enable);
	}

	public Object getObject() {
		return m_object;
	}

	public void setObject(Object object) {
		Object oldObject = m_object;
		m_object = object;
		if (m_object != null)
			m_objectTextField.setText(getCaption(object));
		else
			m_objectTextField.setText("");
		firePropertyChange("object",oldObject,m_object);
	}
	protected String getCaption(Object object) {
		return object.toString();
	}
	public abstract void done();

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_browseBt) {
			done();
		}
	}
	
	//Tambahan
	public void hapusActionListenerBrowse(){
		m_browseBt.removeActionListener(this);
	}
}