package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import pohaci.gumunda.cgui.BaseTableCellRenderer;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.entity.VariableAccountSetting;

public class TransactionPeriodOpenDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JFrame m_mainFrame = null;
	private Connection m_conn;
	private long m_sessionId = -1;
	
	private JPanel m_prevPanel = null;
	private JPanel m_selectPanel = null;
	private JPanel m_tablePanel = null;
	private JButton m_openBtn = null;
	private JButton m_prevSelectBtn = null;
	private JButton m_submitBtn = null;
	private JButton m_cancelBtn = null;
	private JTextField m_fromSelectDate = null;
	private JTextField m_toSelectDate = null;
	private JTextField m_fromOpenDate = null;
	private JTextField m_toOpenDate = null;
	private JScrollPane m_scrollPane = null;
	private JTable m_table = null;
	private JTable m_MTTable = null;
	private DefaultTableModel m_dataModel = null;
		
	private String m_status = "";
	private boolean flag = false;
	
	public TransactionPeriodOpenDialog(JFrame frame, String title,Connection conn, long sessionId,
		String from, String to, JTable table){
		super(frame, ( title == null ) ? "Open Transaction Period" : title, true);
		this.m_mainFrame = frame;
		this.m_conn = conn;
		this.m_sessionId = sessionId;
		this.m_fromOpenDate = new JTextField(from);
		this.m_toOpenDate = new JTextField(to);
		this.m_fromSelectDate = new JTextField();
		this.m_toSelectDate = new JTextField();
		this.m_MTTable = table;
		flag = true;
		if(this.m_MTTable.getRowCount() > 1){
			int row = this.m_MTTable.getSelectedRow();
			if(row>0){
				String status = (String) m_MTTable.getValueAt(row-1,3);
				if(status.equals("close")){
					this.m_fromSelectDate = new JTextField((String) this.m_MTTable.getValueAt(row-1,1));
					this.m_toSelectDate = new JTextField((String) this.m_MTTable.getValueAt(row-1,2));
				}
				else {
					flag = false;
					JOptionPane.showMessageDialog(this,"Status of transaction period before must closed");
				}
			}
		}
		//setSize(550, 450);
		setSize(300,210);
		setTableHeader();
		clearRows();
		constructComponent();
	}
	
	public void constructComponent(){
		JLabel fromSelectLbl = new JLabel("From Date");
		fromSelectLbl.setPreferredSize(new Dimension(70,20));
		JLabel toSelectLbl = new JLabel("To Date");
		JLabel fromOpenLbl = new JLabel("From Date");
		fromOpenLbl.setPreferredSize(new Dimension(70,20));
		JLabel toOpenLbl = new JLabel("To Date");
		
		this.m_prevSelectBtn = new JButton("Select");
		this.m_prevSelectBtn.addActionListener(this);
		this.m_openBtn = new JButton("Open");
		this.m_openBtn.addActionListener(this);
		this.m_submitBtn = new JButton("Submit");
		this.m_submitBtn.addActionListener(this);
		this.m_cancelBtn = new JButton("Cancel");
		this.m_cancelBtn.addActionListener(this);
		
		this.m_fromSelectDate.setPreferredSize(new Dimension(100,20));
		this.m_fromOpenDate.setPreferredSize(new Dimension(100,20)); 
		
		
		this.m_fromSelectDate.setEditable(false);
		this.m_toSelectDate.setEditable(false);
		this.m_fromOpenDate.setEditable(false);
		this.m_toOpenDate.setEditable(false);
		
		this.m_prevPanel = new JPanel();
		this.m_selectPanel = new JPanel();
		this.m_tablePanel = new JPanel();
		this.m_scrollPane = new JScrollPane();
		this.m_scrollPane.setPreferredSize(new Dimension(100, 230));
		this.m_scrollPane.getViewport().add(this.m_table);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(this.m_submitBtn);
		buttonPanel.add(this.m_cancelBtn);
		
		this.m_tablePanel.setLayout(new BorderLayout());
		//this.m_tablePanel.add(this.m_scrollPane,BorderLayout.NORTH);
		this.m_tablePanel.add(buttonPanel,BorderLayout.SOUTH);
		
		setBorder(this.m_prevPanel,"Previous Period");
		//this.m_prevPanel.setPreferredSize(new Dimension(100,75));
		this.m_prevPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(1, 6, 1, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.m_prevPanel.add(fromSelectLbl, gridBagConstraints);
				
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new Insets(2, 6, 0, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.m_prevPanel.add(toSelectLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		this.m_prevPanel.add(this.m_prevSelectBtn , gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		this.m_prevPanel.add(this.m_fromSelectDate , gridBagConstraints);	
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		this.m_prevPanel.add(this.m_toSelectDate , gridBagConstraints);	
		
		JPanel tempPanel2= new JPanel();		
		JPanel tempPanel = new JPanel();
		tempPanel.setPreferredSize(new Dimension(100,75));
		tempPanel.setLayout(new GridLayout(1,2));
		
		gridBagConstraints = new GridBagConstraints();		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		tempPanel.add(m_prevPanel,gridBagConstraints);
		
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.fill = GridBagConstraints.REMAINDER;
		tempPanel.add(tempPanel2,gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		this.m_selectPanel.setLayout(new GridBagLayout());
		setBorder(this.m_selectPanel,"Selected Period");
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(1, 6, 1, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.m_selectPanel.add(fromOpenLbl, gridBagConstraints);
				
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new Insets(2, 6, 0, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.m_selectPanel.add(toOpenLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		this.m_selectPanel.add(this.m_fromOpenDate , gridBagConstraints);	
		
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(1, 3, 0, 0);
		//this.m_selectPanel.add(this.m_openBtn , gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		this.m_selectPanel.add(this.m_toOpenDate , gridBagConstraints);
		
		JPanel tempPanel4= new JPanel();		
		JPanel tempPanel3 = new JPanel();
		tempPanel3.setPreferredSize(new Dimension(100,75));
		tempPanel3.setLayout(new GridLayout(1,2));
		
		gridBagConstraints = new GridBagConstraints();		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		tempPanel3.add(m_selectPanel,gridBagConstraints);
		
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.fill = GridBagConstraints.REMAINDER;
		tempPanel3.add(tempPanel4,gridBagConstraints);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(m_prevPanel, BorderLayout.NORTH);// tempPanel
		getContentPane().add(m_selectPanel, BorderLayout.CENTER);// tempPanel3
		getContentPane().add(m_tablePanel, BorderLayout.SOUTH);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.m_prevSelectBtn){
			TransactionPeriodSelectDialog selectDlg = new TransactionPeriodSelectDialog(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), 
					"Select Transaction Period", m_conn, m_sessionId, this.m_MTTable);
			selectDlg.setVisible(true);
			this.m_fromSelectDate.setText(selectDlg.getFromDate());
			this.m_toSelectDate.setText(selectDlg.getToDate());
		}
		else if(e.getSource() == this.m_openBtn){
			
		}
		else if(e.getSource() == this.m_submitBtn){
			//if(balance != 0)
				m_status = "open";
			dispose();
		}
		else if(e.getSource() == this.m_cancelBtn)
			dispose();
	}
	
	public void setVisible( boolean flag ){
		Rectangle rc = m_mainFrame.getBounds();
		Rectangle rcthis = getBounds();
		setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
				(int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
				(int)rcthis.getWidth(), (int)rcthis.getHeight());
		super.setVisible(flag);
	}
	
	private void setTableHeader(){
		this.m_table = new JTable(){
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		this.m_dataModel = new DefaultTableModel();
		this.m_dataModel.addColumn("Account No");
		this.m_dataModel.addColumn("Account Name");
		this.m_dataModel.addColumn("Debit");
		this.m_dataModel.addColumn("Kredit");
				
		this.m_table.setModel(this.m_dataModel);
		this.m_table.getColumnModel().getColumn(0).setPreferredWidth(100);
		this.m_table.getColumnModel().getColumn(0).setMaxWidth(100);
		this.m_table.getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
	}

	private void setBorder(JPanel panel,String theme){
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), theme ,
				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
	}
	
	/*private void doView() {
		clearRows();
		Date dateFrom = null,dateTo = null;
		try {
			dateFrom = m_dateFormat.parse(this.m_fromOpenDate.getText());
			dateTo = m_dateFormat.parse(this.m_toOpenDate.getText());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		double value = 0;	
		List list = ClosingTransaction.igetAccount(this.m_conn);
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Account account = (Account) iterator.next();
			account.isetStatus(Account.DESCRIPTION);

			BigDecimal valueBD = ClosingTransaction.igetDebitValue(
					this.m_conn, account, dateFrom, dateTo);
			value = valueBD.doubleValue();
			if (value > 0) {
				addRow(new Object[] { account.getCode(), account, null,
						m_desimalFormat.format(new Double(value)) });
				m_credit += value;
				System.err.println("Kredit : " + m_credit);
				
			} else if (value < 0) {
				value = -value;
				addRow(new Object[] { account.getCode(), account,
						m_desimalFormat.format(new Double(value)), null });
				m_debit += value;
			}
		}
		
		setIncomeSummary();
		setBalance();
	}
	
	private void addRow(Object[] objects) {
		int oldRow = lastRow;
		lastRow++;
		getDataModel().insertRow(oldRow, objects);
	}
	
	private void setIncomeSummary() {
		int rowIncomeSummary = getDataModel().getRowCount() - 3;
		double debit = getTotalValue(rowIncomeSummary - 1, 2);
		double credit = getTotalValue(rowIncomeSummary - 1, 3);
		balance = m_debit - m_credit;

		if (balance > 0) { // debit > credit
			getDataModel().setValueAt(m_desimalFormat.format(new Double(balance)), rowIncomeSummary, 3);
			m_debit = balance;
		} else {
			getDataModel()
					.setValueAt(m_desimalFormat.format(new Double(-balance)), rowIncomeSummary, 2);
			m_debit = -balance;
		}
	}
	private void setBalance() {
		int rowBalance = getDataModel().getRowCount() - 1;
		double debit = getTotalValue(rowBalance - 1, 2);
		double credit = getTotalValue(rowBalance - 1, 3);

		getDataModel().setValueAt(m_desimalFormat.format(new Double(m_debit)), rowBalance, 2);
		getDataModel().setValueAt(m_desimalFormat.format(new Double(m_credit)), rowBalance, 3);
	}
	
	private double getTotalValue(int maxRow, int col) {
		double val = 0;
		for (int i = 0; i < maxRow + 1; i++) {
			Object obj = getDataModel().getValueAt(i, col);
			if (obj instanceof Double) {
				Double dbl = (Double) obj;
				val += dbl.doubleValue();
			}
		}
		return val;
	}*/
	private void clearRows() {
		VariableAccountSetting vas = null;
		vas = VariableAccountSetting.createVariableAccountSetting(m_conn, m_sessionId, IConstants.ATTR_VARS_INCOME_SUMMARY);
		
		getDataModel().setRowCount(0);
		getDataModel().addRow(
				new Object[] { vas.getAccount().getCode(), vas.getAccount(), new Double(0),
						new Double(0) });
		getDataModel().addRow(new Object[] { null, null, null, null });
		getDataModel().addRow(
				new Object[] { null, "TOTAL", new Double(0), new Double(0) });
	}
	
	private DefaultTableModel getDataModel() {
		if (m_dataModel == null) {
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("Account No");
			model.addColumn("Account Name");
			model.addColumn("Debit");
			model.addColumn("Credit");

			m_dataModel = model;
		}
		return m_dataModel;
	}
	
	public String getStatus(){
		return m_status;
	}
	
	public Unit getDefaultUnit(){
		Unit unit = DefaultUnit.createDefaultUnit(m_conn, m_sessionId);
		return unit;
		
		/*AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
		Unit unit = null;
		try {
			DefaultUnit[] dUnit = logic.getAllDefaultUnit(m_sessionId, IDBConstants.MODUL_ACCOUNTING);
			if(dUnit.length !=0){
				unit = logic.getUnitByDescription(dUnit[0].getUnit());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unit;*/
	}
	
	public boolean getFlag(){
		return flag;
	}
}
