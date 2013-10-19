package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;

public class BaseUnitDlg extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection m_conn = null;
	long m_sessionId = -1;
	
	private JFrame m_mainframe = null;
	private JPanel m_mergePanel = null; 
	private JPanel m_unitPanel = null;
	private JPanel m_buttonPanel = null;
	private JButton m_saveBtn = null;
	private JButton m_cancelBtn = null;
	private UnitPicker m_unitPicker = null;
	
	public BaseUnitDlg(JFrame frame, Connection conn, long sessionId) {
		super(frame, "Base Unit", true);
		setSize(400, 120);
		m_mainframe = frame;
		m_conn = conn;
		m_sessionId = sessionId;
		
		constructComponent();
	}

	public void constructComponent(){
		JLabel unitLbl = new JLabel("Default Unit ");
		this.m_unitPicker = new UnitPicker(m_conn,m_sessionId);						
		setDefaultUnit();
		
		this.m_saveBtn = new JButton("Save");
		this.m_saveBtn.addActionListener(this);
		this.m_cancelBtn = new JButton("Cancel");
		this.m_cancelBtn.addActionListener(this);
		
		this.m_buttonPanel = new JPanel();		
		this.m_buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.m_buttonPanel.add(m_saveBtn);
		this.m_buttonPanel.add(m_cancelBtn);
		
		this.m_unitPanel = new JPanel();
		this.m_unitPanel.setPreferredSize(new Dimension(100,100));
		this.m_unitPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(1, 3, 1, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.m_unitPanel.add(unitLbl, gridBagConstraints);
						
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		this.m_unitPanel.add(this.m_unitPicker , gridBagConstraints);
		
		this.m_mergePanel = new  JPanel();
		this.m_mergePanel.setLayout(new BorderLayout());
		this.m_mergePanel.add(m_unitPanel, BorderLayout.CENTER);
		this.m_mergePanel.add(m_buttonPanel, BorderLayout.SOUTH);
		
		getContentPane().add(this.m_mergePanel, BorderLayout.CENTER);
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
		if(e.getSource() == m_saveBtn){
			if(this.m_unitPicker.getUnit() != null){
				AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
				try {
					DefaultUnit[] Unit = logic.getAllDefaultUnit(m_sessionId, IDBConstants.MODUL_ACCOUNTING);
					if(Unit.length == 0)
						saveDefaultUnit();
					else {
						Unit unit = this.m_unitPicker.getUnit();
						DefaultUnit dUnit = new DefaultUnit(unit);
						logic.updateDefaultUnit(1, dUnit, m_sessionId, IDBConstants.MODUL_ACCOUNTING);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			dispose();
		}
		else if(e.getSource() == m_cancelBtn){
			dispose();
		}
		
	}
	
	public void setDefaultUnit(){
		Unit unit = DefaultUnit.createDefaultUnit(m_conn, m_sessionId);
		m_unitPicker.setObject(unit);
		
		// moved to DefaultUnit.createDefaultUni(connection, sessionId);
		
		/*AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
		try {
			DefaultUnit dUnit = logic.getDefaultUnit(m_sessionId, IDBConstants.MODUL_ACCOUNTING);
			if(dUnit != null){
				Unit unit = dUnit.getUnit();
				m_unitPicker.setObject(unit);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
	}
	
	public void saveDefaultUnit(){
		Unit unit = this.m_unitPicker.getUnit();
		DefaultUnit dUnit = new DefaultUnit(unit);
		AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			try {
				logic.createDefaultUnit(m_sessionId,IDBConstants.MODUL_ACCOUNTING,dUnit);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
