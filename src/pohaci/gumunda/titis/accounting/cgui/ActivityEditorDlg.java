package pohaci.gumunda.titis.accounting.cgui;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Activity;

public class ActivityEditorDlg extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	JFrame m_mainframe;
	JTextField m_parentTextField, m_codeTextField, m_nameTextField;
	JTextArea m_noteTextArea;
	JButton m_okBt, m_cancelBt;
	
	DefaultMutableTreeNode m_parent = null;
	Activity m_activity = null;
	
	int m_iResponse = JOptionPane.NO_OPTION;
	Connection m_conn = null;
	long m_sessionid = -1;
	
	public ActivityEditorDlg(JFrame mainframe, Connection conn, long sessionid, DefaultMutableTreeNode parent) {
		super(mainframe, "Add Activity", true);
		setSize(400, 220);
		m_mainframe = mainframe;
		m_conn = conn;
		m_sessionid = sessionid;
		m_parent = parent;
		constructComponent();
		initData();
	}
	
	public ActivityEditorDlg(JFrame mainframe, Connection conn, long sessionid, DefaultMutableTreeNode parent, Activity activity) {
		super(mainframe, "Edit Activity", true);
		setSize(400, 220);
		m_mainframe = mainframe;
		m_conn = conn;
		m_sessionid = sessionid;
		m_parent = parent;
		m_activity = activity;
		constructComponent();
		initData();
	}
	
	void constructComponent() {
		JLabel label1 = new JLabel("Parent Name");
		JLabel label2 = new JLabel("Code");
		JLabel label3 = new JLabel("Name");
		JLabel label4 = new JLabel("Description");
		JPanel centerPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		m_parentTextField = new JTextField();
		m_parentTextField.setEditable(false);
		m_codeTextField = new JTextField();
		m_nameTextField = new JTextField();
		m_noteTextArea = new JTextArea();
		m_okBt = new JButton("Save");
		m_okBt.addActionListener(this);
		m_cancelBt = new JButton("Cancel");
		m_cancelBt.addActionListener(this);
		
		centerPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(1, 0, 1 , 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		centerPanel.add(label1, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		centerPanel.add(new JLabel(" "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		centerPanel.add(m_parentTextField, gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		centerPanel.add(label2, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		centerPanel.add(new JLabel(" "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		centerPanel.add(m_codeTextField, gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		centerPanel.add(label3, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		centerPanel.add(new JLabel(" "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		centerPanel.add(m_nameTextField, gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
		centerPanel.add(label4, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		centerPanel.add(new JLabel(" "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.ipady = 40;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		centerPanel.add(new JScrollPane(m_noteTextArea), gridBagConstraints);
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(m_okBt);
		buttonPanel.add(m_cancelBt);
		
		JPanel mergePanel = new JPanel(new BorderLayout());
		mergePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
		mergePanel.add(centerPanel, BorderLayout.CENTER);
		mergePanel.add(buttonPanel, BorderLayout.SOUTH);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(mergePanel, BorderLayout.CENTER);
	}
	
	void initData() {
		if(m_parent != null)
			m_parentTextField.setText(((Activity)m_parent.getUserObject()).getName());
		if(m_activity != null) {
			if(m_activity.getCode() != null)
				m_codeTextField.setText(m_activity.getCode());
			m_nameTextField.setText(m_activity.getName());
			m_noteTextArea.setText(m_activity.getDescription());
		}
	}
	
	void onOK() {
		Activity activity;
		
		try {
			activity = this.getDefineActivity();
			if(m_parent != null)
				activity.setParent((Activity)m_parent.getUserObject());
		}
		catch(Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(),
					"Information", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			if(m_activity == null)
				activity = logic.createActivity(m_sessionid, IDBConstants.MODUL_MASTER_DATA, activity);
			else
				activity = logic.updateActivity(m_sessionid, IDBConstants.MODUL_MASTER_DATA, m_activity.getIndex(), activity);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		m_activity = activity;
		m_iResponse = JOptionPane.OK_OPTION;
		dispose();
	}
	
	public Activity getActivity() {
		return m_activity;
	}
	
	private Activity getDefineActivity() throws Exception {
		java.util.ArrayList list = new java.util.ArrayList();
		String code = m_codeTextField.getText().trim();
		String name = m_nameTextField.getText().trim();
		String note = m_noteTextArea.getText().trim();
		
		if(name.equals(""))
			list.add("Code Activity");
		if(name.equals(""))
			list.add("Name Activity");
		String strexc = "Please insert :\n";
		String[] exception = new String[list.size()];
		list.toArray(exception);
		if(exception.length > 0) {
			for(int i = 0; i < exception.length; i ++)
				strexc += exception[i] + "\n";
			throw new Exception(strexc);
		}
		
		return new Activity(code, name, note);
	}
	
	public int getResponse() {
		return m_iResponse;
	}
	
	public void setVisible( boolean flag ){
		Rectangle rc = m_mainframe.getBounds();
		Rectangle rcthis = getBounds();
		setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
				(int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
				(int)rcthis.getWidth(), (int)rcthis.getHeight());
		super.setVisible(flag);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_okBt) {
			onOK();
		}
		else if(e.getSource() == m_cancelBt) {
			dispose();
		}
	}
}