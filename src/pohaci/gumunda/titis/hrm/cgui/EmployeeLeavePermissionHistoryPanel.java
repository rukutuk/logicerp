package pohaci.gumunda.titis.hrm.cgui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;

import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class EmployeeLeavePermissionHistoryPanel extends JPanel implements ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	Connection m_conn = null;
	long m_sessionid = -1;

	JButton m_filterBt, m_refreshBt;
	JLabel m_labelTotal, m_labelDays, m_labelDeduction;

	JTextField m_totalTextField, m_daysTextField, m_deductionTextField;

	int m_totalCount = 0;
	int m_totalDays = 0;
	int m_totalDeduction = 0;

	HistoryTable m_table = null;

	Employee m_employee = null;

	public EmployeeLeavePermissionHistoryPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		constructComponent();
		initData();
	}


	private void constructComponent() {
		m_table = new HistoryTable();
		m_filterBt = new JButton(new ImageIcon("../images/filter2.gif"));
	    m_filterBt.addActionListener(this);
	    m_refreshBt = new JButton(new ImageIcon("../images/refresh.gif"));
	    m_refreshBt.addActionListener(this);

	    m_labelTotal = new JLabel("Total proposed: ");
	    m_labelDays = new JLabel("Total days: ");
	    m_labelDeduction = new JLabel("Total days deducting annual leave right: ");

	    m_totalTextField = new JTextField(3);
	    m_totalTextField.setEditable(false);
	    m_totalTextField.setHorizontalAlignment(JTextField.RIGHT);

	    m_daysTextField = new JTextField(3);
	    m_daysTextField.setEditable(false);
	    m_daysTextField.setHorizontalAlignment(JTextField.RIGHT);

	    m_deductionTextField = new JTextField(3);
	    m_deductionTextField.setEditable(false);
	    m_deductionTextField.setHorizontalAlignment(JTextField.RIGHT);

	    JToolBar northbar = new JToolBar();

	    northbar.setFloatable(false);
	    northbar.setLayout(new FlowLayout(FlowLayout.LEFT));
	    GridBagConstraints gridBagConstraints = new GridBagConstraints();
	    gridBagConstraints.gridx = 0;
	    gridBagConstraints.gridy = 0;
	    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
	    northbar.add(m_filterBt, gridBagConstraints);

	    gridBagConstraints.gridx = 1;
	    northbar.add(m_refreshBt, gridBagConstraints);

	    gridBagConstraints.gridx = 2;
	    northbar.add(new JLabel("  "), gridBagConstraints);

	    gridBagConstraints.gridx = 3;
	    northbar.add(m_labelTotal, gridBagConstraints);

	    gridBagConstraints.gridx = 4;
	    northbar.add(m_totalTextField, gridBagConstraints);

	    gridBagConstraints.gridx = 5;
	    northbar.add(new JLabel("  "), gridBagConstraints);

	    gridBagConstraints.gridx = 6;
	    northbar.add(m_labelDays, gridBagConstraints);

	    gridBagConstraints.gridx = 7;
	    northbar.add(m_daysTextField, gridBagConstraints);

	    gridBagConstraints.gridx = 8;
	    northbar.add(new JLabel("  "), gridBagConstraints);

	    gridBagConstraints.gridx = 9;
	    northbar.add(m_labelDeduction, gridBagConstraints);

	    gridBagConstraints.gridx = 10;
	    northbar.add(m_deductionTextField, gridBagConstraints);

	    gridBagConstraints.gridx = 11;
	    northbar.add(new JLabel("(based on proposed date)"), gridBagConstraints);

	    setLayout(new BorderLayout());
	    setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
	    add(northbar, BorderLayout.NORTH);
	    add(new JScrollPane(m_table), BorderLayout.CENTER);
	}

	private void initData(){
		DefaultTableModel model = (DefaultTableModel) m_table.getModel();
		model.setRowCount(0);

		HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);

		if(m_employee!=null){
			try {
				EmployeeLeavePermissionHistory[] leavepermissions =
					logic.getEmployeeLeavePermissionHistory(m_sessionid, IDBConstants.MODUL_MASTER_DATA,
							m_employee.getIndex());

				viewData(leavepermissions);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void viewData(EmployeeLeavePermissionHistory[] leavepermissions) {
		DefaultTableModel model = (DefaultTableModel) m_table.getModel();
		model.setRowCount(0);

		m_totalCount = 0;
		m_totalDays = 0;
		m_totalDeduction = 0;

		if(leavepermissions!=null){
			if(leavepermissions.length>0){
				for(int i=0; i<leavepermissions.length; i++){
					String deduction = "";
					if(leavepermissions[i].isAnnualLeaveDeduction())
						deduction = "Yes";
					else
						deduction = "No";
					model.addRow(new Object[]{
						new Integer(i+1),
						leavepermissions[i].getType(),
						leavepermissions[i].getReason(),
						deduction,
						leavepermissions[i].getPropose(),
						leavepermissions[i].getFrom(),
						leavepermissions[i].getTo(),
						new Integer(leavepermissions[i].getDays()),
						leavepermissions[i].getReplaced(),
						leavepermissions[i].getChecked(),
						leavepermissions[i].getCheckeddate(),
						leavepermissions[i].getApproved(),
						leavepermissions[i].getApproveddate()
						}
					);
					m_totalCount += 1;
					m_totalDays += leavepermissions[i].getDays();
					if(leavepermissions[i].isAnnualLeaveDeduction()){
						m_totalDeduction += leavepermissions[i].getDays();
					}
				}
			}
		}

		// show total
		m_totalTextField.setText(String.valueOf(m_totalCount));
		m_daysTextField.setText(String.valueOf(m_totalDays));
		m_deductionTextField.setText(String.valueOf(m_totalDeduction));
	}

	public void setEmployee(Employee employee){
		m_employee = employee;
		initData();
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==m_filterBt){
			filter();
		}else if(e.getSource()==m_refreshBt){
			refresh();
		}
	}

	private void filter() {
		SearchEmployeeLeavePermissionHistoryDlg dlg = null;
		dlg = new SearchEmployeeLeavePermissionHistoryDlg(
				GumundaMainFrame.getMainFrame(), this, m_conn, m_sessionid);
		dlg.setVisible(true);

	}

	private void refresh() {
		initData();
	}

	/**
	 *
	 */
	class HistoryTable extends JTable {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		public HistoryTable() {
			HistoryTableModel model = new HistoryTableModel();
			model.addColumn("No");
			model.addColumn("Type");
			model.addColumn("Reason");
			model.addColumn("Deduction");
			model.addColumn("Proposed Date");
			model.addColumn("Form");
			model.addColumn("To");
			model.addColumn("Days #");
			model.addColumn("Replaced By");
			model.addColumn("Checked By");
			model.addColumn("Checked Date");
			model.addColumn("Approved By");
			model.addColumn("Approved Date");
			setModel(model);
		}
	}

	/**
	 *
	 */
	public class HistoryTableModel extends DefaultTableModel {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int col) {
			return false;
		}
	}

}


