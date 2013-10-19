package pohaci.gumunda.titis.accounting.cgui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ActivityCellEditor extends DefaultCellEditor implements ActionListener {

    private static final long serialVersionUID = 1L;
    JFrame m_owner;
	JButton m_browseBt = new JButton("Browse...");
  	DefaultTableModel m_model = null;
  	int m_row, m_col;

  	public Connection m_conn = null;
  	public long m_sessionid = -1;
  	Object m_object = null;
	
	public ActivityCellEditor(JFrame owner, Connection conn, long sessionid) {
		super(new JTextField());
	    m_owner = owner;
	    m_conn = conn;
	    m_sessionid = sessionid;
	    m_browseBt.addActionListener(this);
	}
	
	public Component getTableCellEditorComponent(JTable table, Object value,
	      boolean isSelected, int row, int column){
	    m_model = (DefaultTableModel)table.getModel();
	    m_row = row;
	    m_col = column;
	    return m_browseBt;
	}
	
	void done() {
		ActivityTreeDlg dlg = new ActivityTreeDlg(m_owner, m_conn, m_sessionid);
		dlg.setVisible(true);
		if(dlg.getResponse() == JOptionPane.OK_OPTION) {
			Object object = dlg.getActivity();
			m_model.setValueAt(object, m_row, m_col);
		}
	}

	public void actionPerformed(ActionEvent e) {
		 if(e.getSource() == m_browseBt){
		      stopCellEditing();
		      done();
		    }
	}
}
