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
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

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
import javax.swing.table.TableCellRenderer;

import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.ClosingTransaction;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.entity.VariableAccountSetting;
import pohaci.gumunda.titis.accounting.helper.ReferenceNoGeneratorHelper;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.TransactionPostingLogic;
import pohaci.gumunda.titis.application.FormattedDoubleCellRenderer;
import pohaci.gumunda.titis.application.FormattedStandardCellRenderer;


public class TransactionPeriodCloseDialog extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame m_mainFrame = null;
	private Connection m_conn;
	private long m_sessionId = 0;
	
	private JPanel m_selectPanel = null;
	private JPanel m_tablePanel = null;
	private JButton m_closeBtn = null;
	private JButton m_submitBtn = null;
	private JButton m_cancelBtn = null;
	private JTextField m_fromDate = null;
	private JTextField m_toDate = null;
	private JScrollPane m_scrollPane = null;
	private JTable m_table = null;
	private DefaultTableModel m_dataModel = null;
	
	int lastRow = 0;
	private SimpleDateFormat m_dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	//private SimpleDateFormat m_dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	//private DecimalFormat m_desimalFormat = new DecimalFormat("#,##0.00");
	//private double m_debit = 0;
	//private double m_credit = 0;
	private double balance = 0;
	
	private String m_status = "";
	
	private Currency baseCurrency = null;
	
	private Date from = null;
	private Date to = null;
	
	public TransactionPeriodCloseDialog(JFrame frame, String title,Connection conn, long sessionId,
			Date from, Date to){
		super(frame, ( title == null ) ? "Close Transaction Period" : title, true);
		this.m_mainFrame = frame;
		this.m_conn = conn;
		this.m_sessionId = sessionId;
		this.m_fromDate = new JTextField();
		this.m_toDate = new JTextField();
		m_fromDate.setText(m_dateFormat.format(from));
		m_toDate.setText(m_dateFormat.format(to));
		this.from = from;
		this.to = to;
		
		baseCurrency = BaseCurrency.createBaseCurrency(conn, sessionId);
		setSize(500, 370);
		setTableHeader();
		clearRows();
		constructComponent();
	}
	
	public void constructComponent(){
		JLabel fromOpenLbl = new JLabel("From Date");
		JLabel toOpenLbl = new JLabel("To Date");
		
		this.m_closeBtn = new JButton("Close");
		this.m_closeBtn.addActionListener(this);
		this.m_submitBtn = new JButton("Submit");
		this.m_submitBtn.addActionListener(this);
		this.m_cancelBtn = new JButton("Cancel");
		this.m_cancelBtn.addActionListener(this);
				
		this.m_fromDate.setEditable(false);
		this.m_fromDate.setPreferredSize(new Dimension(100,20));
		this.m_toDate.setEditable(false);
		
		
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
		this.m_tablePanel.add(this.m_scrollPane,BorderLayout.NORTH);
		this.m_tablePanel.add(buttonPanel,BorderLayout.SOUTH);
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		this.m_selectPanel.setLayout(new GridBagLayout());
		setBorder(this.m_selectPanel,"Selected Period");
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(1, 6, 1, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.m_selectPanel.add(fromOpenLbl, gridBagConstraints);
				
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new Insets(1, 6, 1, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.m_selectPanel.add(toOpenLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(1, 3, 1, 0);
		this.m_selectPanel.add(this.m_fromDate , gridBagConstraints);	
		
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(1, 3, 1, 0);
		this.m_selectPanel.add(this.m_closeBtn , gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(1, 3, 1, 0);
		this.m_selectPanel.add(this.m_toDate , gridBagConstraints);
	
		JPanel tempPanel2= new JPanel();		
		JPanel temppanel = new JPanel();
		temppanel.setLayout(new GridLayout(1,2));
		
		gridBagConstraints = new GridBagConstraints();		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		temppanel.add(m_selectPanel,gridBagConstraints);
		
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.fill = GridBagConstraints.REMAINDER;
		temppanel.add(tempPanel2,gridBagConstraints);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(temppanel, BorderLayout.NORTH);
		getContentPane().add(this.m_tablePanel, BorderLayout.SOUTH);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.m_closeBtn){
			doView();
		}
		else if(e.getSource() == this.m_submitBtn){
			submit();
			if(balance != 0)
				m_status = "Closed";
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

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public TableCellRenderer getCellRenderer(int row, int column) {
				if (row < getDataModel().getRowCount() - 1) {
					if ((column > 1)) {
						return new FormattedDoubleCellRenderer(JLabel.RIGHT,
								Font.PLAIN);
					} else
						return super.getCellRenderer(row, column);
				} else {
					if (column == 1)
						return new FormattedStandardCellRenderer(Font.BOLD,
								JLabel.RIGHT);
					else if ((column > 1)) {
						return new FormattedDoubleCellRenderer(JLabel.RIGHT,
								Font.BOLD);
					} else
						return super.getCellRenderer(row, column);
				}
			}

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		this.m_dataModel = new DefaultTableModel();
		this.m_dataModel.addColumn("Account No.");
		this.m_dataModel.addColumn("Account Name");
		this.m_dataModel.addColumn("Debit");
		this.m_dataModel.addColumn("Credit");
				
		this.m_table.setModel(this.m_dataModel);
		this.m_table.getColumnModel().getColumn(0).setPreferredWidth(65);
		this.m_table.getColumnModel().getColumn(1).setPreferredWidth(150);
	}

	private void setBorder(JPanel panel,String theme){
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), theme ,
				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
	}
	
	private void doView() {
		clearRows();
		Date dateFrom = null,dateTo = null;
		dateFrom = this.from;
		dateTo = this.to;

		List list = ClosingTransaction.igetAccount(this.m_conn);
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Account account = (Account) iterator.next();
			account.isetStatus(Account.DESCRIPTION);

			BigDecimal valueBD = ClosingTransaction.igetDebitValue(this.m_conn,
					account, dateFrom, dateTo);
			double value = valueBD.doubleValue();
			System.out.println("account: " + account.toString());
			if (value > 0) {
				addRow(new Object[] { account.getCode(), account, null, new Double(value) });
			} else if (value < 0) {
				value = -value;
				addRow(new Object[] { account.getCode(), account, new Double(value), null });
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
		balance = debit - credit;

		if (balance > 0) { // debit > credit
			getDataModel().setValueAt(new Double(balance), rowIncomeSummary, 3);
		} else if (balance < 0){
			getDataModel()
					.setValueAt(new Double(-balance), rowIncomeSummary, 2);
		}
	}
	
	private void setBalance() {
		int rowBalance = getDataModel().getRowCount() - 1;
		double debit = getTotalValue(rowBalance - 1, 2);
		double credit = getTotalValue(rowBalance - 1, 3);
		//m_debit = m_debit+balance;
		getDataModel().setValueAt(new Double(debit), rowBalance, 2);
		getDataModel().setValueAt(new Double(credit), rowBalance, 3);
		//getDataModel().setValueAt(m_desimalFormat.format(new Double(m_debit)), rowBalance, 2);
		//getDataModel().setValueAt(m_desimalFormat.format(new Double(m_credit)), rowBalance, 3);
	}
	
	private void clearRows() {
		VariableAccountSetting vas = null;
		vas = VariableAccountSetting.createVariableAccountSetting(m_conn, m_sessionId, IConstants.ATTR_VARS_INCOME_SUMMARY);
		
		Account acct = new Account(vas.getAccount(), Account.DESCRIPTION);
		
		getDataModel().setRowCount(0);
		getDataModel().addRow(
				new Object[] { acct.getCode(), acct, null, null });
		getDataModel().addRow(new Object[] { null, null, null, null });
		getDataModel().addRow(
				new Object[] { null, "TOTAL", new Double(0), new Double(0) });

		lastRow = 0;
	}
	
	private DefaultTableModel getDataModel() {
		if (m_dataModel == null) {
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("Account No.");
			model.addColumn("Account Name");
			model.addColumn("Debit");
			model.addColumn("Credit");
			model.addColumn("");
			
			m_dataModel = model;
		}
		return m_dataModel;
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
	}
	
	public String getStatus(){
		return m_status;
	}
	
	public void submit(){		
		int count = m_dataModel.getRowCount();
		double excRate = 1.00;
		double value = 0;
		int balance = 0;
		AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
		Currency baseCurr = this.baseCurrency;;
		Unit unit = getDefaultUnit();
		try {
			if(this.balance != 0){
				Transaction trans = null;
				TransactionDetail detail = null;
				TransactionDetail[] details = null;
				Vector vector = new Vector();
				
				JournalStandardSettingPickerHelper helper =
					new JournalStandardSettingPickerHelper(m_conn,m_sessionId, IDBConstants.MODUL_ACCOUNTING);
				List journalStdList = 
					helper.getJournalStandardSettingWithAccount(IConstants.CLOSING_TRANSACTION);
				JournalStandardSetting jss = (JournalStandardSetting) journalStdList.get(0);
				JournalStandard js = jss.getJournalStandard();
				
				Date transDate = to;
				
				ReferenceNoGeneratorHelper noGenerator = new ReferenceNoGeneratorHelper(
						m_conn, m_sessionId, IDBConstants.MODUL_ACCOUNTING, "");
				String refNo = noGenerator
						.createClosingTransactionReferenceNo(transDate);
				
				trans = new Transaction("Closing Transaction", transDate,
						transDate, transDate, refNo, js.getJournal(), js, (short) 3, unit);
				
				for(int j=0; j<count-2; j++ ){
					Account acc = (Account) m_dataModel.getValueAt(j, 1);
					value = getValue(j);
					balance = getBalance(j);
	
					if(balance!=acc.getBalance())
						value = -value;
					detail = new TransactionDetail(acc, value, baseCurr, excRate, unit, -1);
					vector.add(detail);
				}
				details = (TransactionDetail[]) vector.toArray(new TransactionDetail[vector.size()]);
				trans.setTransactionDetail(details);
				logic.createTransactionData(m_sessionId, IDBConstants.MODUL_ACCOUNTING, trans, details);
				//logic.createTransactionPostedData(m_sessionId, IDBConstants.MODUL_ACCOUNTING, trans, details);
				
				TransactionPostingLogic postLogic = new TransactionPostingLogic(m_conn);
				postLogic.postOnly(m_sessionId, IDBConstants.MODUL_ACCOUNTING, trans, transDate);
			}
			else {
				JOptionPane.showMessageDialog(this, "Please close first");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private int getBalance(int row) {
		int balance = 0;
		Object obj = m_dataModel.getValueAt(row, 2);
		
		if(obj == null || obj.hashCode() == 0)
			balance = 1;
		
		return balance;
	}

	public double getValue(int row){
		double value = 0;
		Double val = null;
		Object obj = m_dataModel.getValueAt(row, 2);
		
		if(obj == null || obj.hashCode() == 0)
			obj = m_dataModel.getValueAt(row, 3);
		
		if(obj instanceof Double) 	
			val = (Double) obj;
		value = val.doubleValue();
		return value;
	}
	
	public Unit getDefaultUnit(){
		Unit unit = DefaultUnit.createDefaultUnit(m_conn, m_sessionId);
		return unit;
		
		// moved to DefaultUnit.createDefaultUni(connection, sessionId);
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
}
