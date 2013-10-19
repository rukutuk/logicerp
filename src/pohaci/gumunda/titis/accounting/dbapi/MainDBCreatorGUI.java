package pohaci.gumunda.titis.accounting.dbapi;

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

import javax.swing.*;

import com.pohaci.titis.testconnection.MyConnection;

import java.sql.*;

import pohaci.cgui.TabelModel;
import pohaci.gumunda.aas.dbapi.ConnectionManager;
import pohaci.gumunda.titis.accounting.cgui.IConstants;

public class MainDBCreatorGUI extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTable m_table;
	JButton m_btCreate = new JButton ("Create"),
	m_btInit = new JButton("Init"),
	m_btDrop = new JButton("Drop"),
	m_btdeInit = new JButton("DeInit");
	ThisTableModel tModel = new ThisTableModel();

	Connection m_conn = null;
	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	JButton m_btCheck = new JButton("Check all"), m_btUnCheck = new JButton("Uncheck all");

	public MainDBCreatorGUI() {
		super("Accounting DBCreator");

		setSize(275,500);
		setLocation( ( dim.width / 2 ) - ( getWidth() / 2 ), ( dim.height / 2 ) - ( getHeight() / 2 ) );
		construct();
		try{
			//ConnectionManager m_connectionManager = new ConnectionManager("sampurna");
			MyConnection m_connectionManager = new MyConnection();
			m_conn = m_connectionManager.getConnection();
			setVisible(true);
			System.out.println("a=================");
		}catch(Exception ex){
			System.out.println("=================");
			javax.swing.JOptionPane.showMessageDialog(null,ex.toString());
			System.err.println(ex);
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		try {
			javax.swing.UIManager.setLookAndFeel("com.digitprop.tonic.TonicLookAndFeel");
			javax.swing.plaf.FontUIResource f = new javax.swing.plaf.FontUIResource("Tahoma", 0, 11);
			java.util.Enumeration keys = javax.swing.UIManager.getDefaults().keys();
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				Object value = javax.swing.UIManager.get(key);
				if (value instanceof javax.swing.plaf.FontUIResource)
					javax.swing.UIManager.put(key, f);
			}

		} catch (Exception ex) {
			System.out.println(ex);
		}
		new MainDBCreatorGUI();
	}

	void construct(){
		JPanel p = new JPanel();
		p.add(m_btCheck); m_btCheck.addActionListener(this);
		p.add(m_btUnCheck); m_btUnCheck.addActionListener(this);
		p.setBorder(BorderFactory.createEtchedBorder());

		JPanel pcheck = new JPanel();
		pcheck.add(p);

		m_table = new JTable();
		m_table.setModel(tModel);
		m_table.getColumnModel().getColumn(1).setPreferredWidth(20);
		m_table.getColumnModel().getColumn(1).setMinWidth(20);
		m_table.getColumnModel().getColumn(1).setMaxWidth(20);

		JScrollPane scroll = new JScrollPane(m_table);
		m_table.getTableHeader().setReorderingAllowed(false);

		JPanel pbt = new JPanel();
		pbt.add( m_btCreate ); m_btCreate.addActionListener(this);
		pbt.add( m_btInit ); m_btInit.addActionListener(this);
		pbt.add( m_btdeInit ); m_btdeInit.addActionListener(this);
		pbt.add( m_btDrop ); m_btDrop.addActionListener(this);

		JPanel centerpanel = new JPanel( new GridLayout( 2, 1 ) );
		centerpanel.add( pcheck);
		centerpanel.add( pbt );
		centerpanel.setBorder(BorderFactory.createEtchedBorder());

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(centerpanel,"South");
		getContentPane().add(scroll,"Center");
		setData();
	}

	public void processWindowEvent(WindowEvent e){
		if(e.getID() == WindowEvent.WINDOW_CLOSING ) System.exit(0);
	}

	boolean isOk(int row){
		Boolean bl = (Boolean) m_table.getValueAt(row,1);
		return bl.booleanValue();
	}

	public void actionPerformed(ActionEvent e){
		if( e.getSource() == m_btCreate ) {
			try{
				m_conn.setAutoCommit(false);
				Statement stm = m_conn.createStatement();
				if ( isOk( 0 ) ) DBCreatorSAP.createAccountTable(stm);
				if ( isOk( 1 ) ) DBCreatorSAP.createAccountStructureTable(stm);
				if ( isOk( 2 ) ) DBCreatorSAP.createCurrencyTable(stm);
				if ( isOk( 3 ) ) DBCreatorSAP.createBankAccountTable(stm);
				if ( isOk( 4 ) ) DBCreatorSAP.createCreditorListTable(stm);
				if ( isOk( 5 ) ) DBCreatorSAP.createCompanyLoanTable(stm);
				if ( isOk( 6 ) ) DBCreatorSAP.createCashAccountTable(stm);

				if ( isOk( 7 ) ) DBCreatorSAP.createJournalTable(stm);

				if ( isOk( 8 ) ) DBCreatorSAP.createJournalStandardTable(stm);
				if ( isOk( 9 ) ) DBCreatorSAP.createJournalStandardStructureTable(stm);
				if ( isOk( 10 ) ) DBCreatorSAP.createJournalStandardAccountTable(stm);
				if ( isOk( 11 ) ) DBCreatorSAP.createExchangeRateTable(stm);
				if ( isOk( 12 ) ) DBCreatorSAP.createActivityTable(stm);
				if ( isOk( 13 ) ) DBCreatorSAP.createActivityStructureTable(stm);
				if ( isOk( 14 ) ) DBCreatorSAP.createUnitTable(stm);

				if ( isOk( 15 ) ) DBCreatorSAP.createBeginningAccountReceivableTable(stm);
				if ( isOk( 16 ) ) DBCreatorSAP.createAccountReceivableDefaultTable(stm);
				if ( isOk( 17 ) ) DBCreatorSAP.createBeginningAccountPayableTable(stm);
				if ( isOk( 18 ) ) DBCreatorSAP.createAccountPayableDefaultTable(stm);

				if ( isOk( 19 ) ) DBCreatorSAP.createJournalStandardSettingTable(stm);
				if ( isOk( 20 ) ) DBCreatorSAP.createSignatureTable(stm);
				if ( isOk( 21 ) ) DBCreatorSAP.createBaseCurrencyTable(stm);

				if ( isOk( 22 ) ) DBCreatorSAP.createWorksheetTable(stm);
				if ( isOk( 23 ) ) DBCreatorSAP.createWorksheetColumnTable(stm);
				if ( isOk( 24 ) ) DBCreatorSAP.createWorksheetJournalTable(stm);
				if ( isOk( 25 ) ) DBCreatorSAP.createBalanceSheetReportTable(stm);

				if ( isOk( 26 ) ) DBCreatorSAP.createTransactionTable(stm);
				if ( isOk( 27 ) ) DBCreatorSAP.createTransactionValueTable(stm);
				if ( isOk( 28 ) ) DBCreatorSAP.createTransactionPostedTable(stm);
				if ( isOk( 29 ) ) DBCreatorSAP.createTransactionValuePostedTable(stm);

				if ( isOk( 30 ) ) DBCreatorSAP.createBeginningBalanceTable(stm);
				if ( isOk( 31 ) ) DBCreatorSAP.createBeginningBalanceSheetDetailTable(stm);
				if ( isOk( 32 ) ) DBCreatorSAP.createBeginningBankDetailTable(stm);
				if ( isOk( 33 ) ) DBCreatorSAP.createBeginningCashDetailTable(stm);

				if ( isOk( 34 ) ) DBCreatorSAP.createReceiveESDiffTable(stm);
				//if ( isOk( 35 ) ) DBCreatorSAP.createReceiveESDiffDetailTable(stm);
				if ( isOk( 35 ) ) DBCreatorSAP.createReceiveUnitCashBankTrnsTable(stm);
				//if ( isOk( 37 ) ) DBCreatorSAP.createReceiveUnitCashBankTrnsDetailTable(stm);
				if ( isOk( 36 ) ) DBCreatorSAP.createReceiveEmpReceivableTable(stm);
				//if ( isOk( 39 ) ) DBCreatorSAP.createReceiveEmpReceivableDetailTable(stm);
				if ( isOk( 37 ) ) DBCreatorSAP.createReceiveLoanTable(stm);
				//if ( isOk( 41 ) ) DBCreatorSAP.createReceiveLoanDetailTable(stm);
				if ( isOk( 38 ) ) DBCreatorSAP.createReceiveOthersTable(stm);
				if ( isOk( 39 ) ) DBCreatorSAP.createReceiveOthersDetailTable(stm);

				if ( isOk( 40 ) ) DBCreatorSAP.createPaymentProjectCostTable(stm);
				if ( isOk( 41 ) ) DBCreatorSAP.createPaymentProjectCostDetailTable(stm);
				if ( isOk( 42 ) ) DBCreatorSAP.createPaymentOperationalCostTable(stm);
				if ( isOk( 43 ) ) DBCreatorSAP.createPaymentOperationalCostDetailTable(stm);
				if ( isOk( 44 ) ) DBCreatorSAP.createCashAdvanceIOUProjectTable(stm);
				if ( isOk( 45 ) ) DBCreatorSAP.createCashAdvanceIOUProjectIstallmentsTable(stm);
				if ( isOk( 46 ) ) DBCreatorSAP.createCashAdvanceIOUProjectSettledTable(stm);
				if ( isOk( 47 ) ) DBCreatorSAP.createCashAdvanceIOUProjectReceiveTable(stm);
				if ( isOk( 48 ) ) DBCreatorSAP.createCashAdvanceIOUOthersTable(stm);
				if ( isOk( 49 ) ) DBCreatorSAP.createCashAdvanceIOUOthersInstallmentsTable(stm);
				if ( isOk( 50 ) ) DBCreatorSAP.createCashAdvanceIOUOthersSettledTable(stm);
				if ( isOk( 51 ) ) DBCreatorSAP.createCashAdvanceIOUOthersReceiveTable(stm);
				if ( isOk( 52 ) ) DBCreatorSAP.createCashAdvanceProjectTable(stm);
				if ( isOk( 53 ) ) DBCreatorSAP.createCashAdvanceOthersTable(stm);
				if ( isOk( 54 ) ) DBCreatorSAP.createPaymentESDifferenceTable(stm);
				//if ( isOk( 57 ) ) DBCreatorSAP.createPaymentESDifferenceDetailTable(stm);
				if ( isOk( 55 ) ) DBCreatorSAP.createPaymentUnitCashBankTransferTable(stm);
				//if ( isOk( 59 ) ) DBCreatorSAP.createPaymentUnitCashBankTransferDetailTable(stm);
				if ( isOk( 56 ) ) DBCreatorSAP.createPaymentEmployeeReceivableTable(stm);
				//if ( isOk( 61 ) ) DBCreatorSAP.createPaymentEmployeeReceivableDetailTable(stm);
				if ( isOk( 57 ) ) DBCreatorSAP.createPaymentLoanTable(stm);
				//if ( isOk( 63 ) ) DBCreatorSAP.createPaymentLoanDetailTable(stm);
				if ( isOk( 58 ) ) DBCreatorSAP.createPaymentOthersTable(stm);
				if ( isOk( 59 ) ) DBCreatorSAP.createPaymentOthersDetailTable(stm);

				if ( isOk( 60 ) ) DBCreatorSAP.createSalesAdvanceTable(stm);
				//if ( isOk( 59 ) ) DBCreatorSAP.createSalesAdvanceDetailTable(stm);
				if ( isOk( 61 ) ) DBCreatorSAP.createSalesInvoiceTable(stm);
				//if ( isOk( 61 ) ) DBCreatorSAP.createSalesInvoiceDetailTable(stm);
				if ( isOk( 62 ) ) DBCreatorSAP.createSalesItemTable(stm);
				if ( isOk( 63 ) ) DBCreatorSAP.createSalesItemDetailTable(stm);
				if ( isOk( 64 ) ) DBCreatorSAP.createSalesARReceivedTable(stm);
				//if ( isOk( 65 ) ) DBCreatorSAP.createSalesARReceivedDetailTable(stm);

				if ( isOk( 65 ) ) DBCreatorSAP.createPurchaseReceiptTable(stm);
				//if ( isOk( 67 ) ) DBCreatorSAP.createPurchaseReceiptDetailTable(stm);
				if ( isOk( 66 ) ) DBCreatorSAP.createPurchaseReceiptItemTable(stm);
				if ( isOk( 67 ) ) DBCreatorSAP.createPurchaseAPPaymentTable(stm);
				//if ( isOk( 70 ) ) DBCreatorSAP.createPurchaseAPPaymentDetailTable(stm);

				if ( isOk( 68 ) ) DBCreatorSAP.createPayrollPaymentSalaryHOTable(stm);
				if ( isOk( 69 ) ) DBCreatorSAP.createPayrollPaymentSalaryHODetailTable(stm);
				//if ( isOk( 70 ) ) DBCreatorSAP.createPayrollPaymentSalaryHOPayableTable(stm);
				if ( isOk( 71 ) ) DBCreatorSAP.createPayrollPaymentSalaryUnitTable(stm);
				if ( isOk( 72 ) ) DBCreatorSAP.createPayrollPaymentSalaryUnitDetailTable(stm);
				if ( isOk( 73 ) ) DBCreatorSAP.createPayrollPaymentTax21HOTable(stm);
				if ( isOk( 74 ) ) DBCreatorSAP.createPayrollPaymentTax21HODetailTable(stm);
				//if ( isOk( 75 ) ) DBCreatorSAP.createPayrollPaymentTax21HOPayableTable(stm);
				if ( isOk( 76 ) ) DBCreatorSAP.createPayrollPaymentTax21UnitTable(stm);
				if ( isOk( 77 ) ) DBCreatorSAP.createPayrollPaymentTax21UnitDetailTable(stm);
				if ( isOk( 78 ) ) DBCreatorSAP.createPayrollPaymentEmployeeInsuranceTable(stm);
				if ( isOk( 79 ) ) DBCreatorSAP.createPayrollPaymentEmployeeInsuranceDetailTable(stm);
				if ( isOk( 80 ) ) DBCreatorSAP.createPayrollPaymentEmployeeInsurancePayableTable(stm);

				if ( isOk( 81 ) ) DBCreatorSAP.createExpenseSheetTable(stm);
				if ( isOk( 82 ) ) DBCreatorSAP.createExpenseSheetDetailTable(stm);

				if ( isOk( 83 ) ) DBCreatorSAP.createMemorialJournalStandardTable(stm);
				if ( isOk( 84 ) ) DBCreatorSAP.createMemorialJournalStandardDetailTable(stm);
				if ( isOk( 85 ) ) DBCreatorSAP.createMemorialJournalNonStandardTable(stm);
				if ( isOk( 86 ) ) DBCreatorSAP.createMemorialJournalNonStandardDetailTable(stm);

				if ( isOk( 87 ) ) DBCreatorSAP.createVariableAccountSetting(stm);
				if ( isOk( 88 ) ) DBCreatorSAP.createSubsidiaryAccountSetting(stm);

				if ( isOk( 89 ) ) DBCreatorSAP.createBeginningAccountPayableTable(stm);
				if ( isOk( 90 ) ) DBCreatorSAP.createBeginningAccountReceivableTable(stm);
				if ( isOk( 91 ) ) DBCreatorSAP.createBeginningEmployeeReceivableTable(stm);
				if ( isOk( 92 ) ) DBCreatorSAP.createBeginningCashInAdvanceTable(stm);
				if ( isOk( 93 ) ) DBCreatorSAP.createBeginningESDiffTable(stm);
				if ( isOk( 94 ) ) DBCreatorSAP.createBeginningLoanTable(stm);
				if ( isOk( 95 ) ) DBCreatorSAP.createBeginningWorkInProgressDetailTable(stm);
				
				if ( isOk( 96 ) ) DBCreatorSAP.createTrialBalanceJournalTypeSettingTable(stm);
				if ( isOk( 97 ) ) DBCreatorSAP.createTrialBalanceAccountTypeSettingTable(stm);

				if ( isOk( 98 ) ) DBCreatorSAP.createClosingTransactionTable(stm);
				if ( isOk( 99 ) ) DBCreatorSAP.createClosingTransactionDetailTable(stm);
				
				if ( isOk( 100 ) ) DBCreatorSAP.createIncomeStatementDesignTable(stm);
				if ( isOk( 101 ) ) DBCreatorSAP.createIncomeStatementJournalTable(stm);
				if ( isOk( 102 ) ) DBCreatorSAP.createIncomeStatementRowsTable(stm);
				
				if ( isOk( 103 ) ) DBCreatorSAP.createBalanceSheetDesignTable(stm);
				if ( isOk( 104 ) ) DBCreatorSAP.createBalanceSheetJournalTable(stm);
				if ( isOk( 105 ) ) DBCreatorSAP.createBalanceSheetRowsTable(stm);
				
				if ( isOk( 106 ) ) DBCreatorSAP.createIndirectCashFlowStatementDesignTable(stm);
				if ( isOk( 107 ) ) DBCreatorSAP.createIndirectCashFlowStatementJournalTable(stm);
				if ( isOk( 108 ) ) DBCreatorSAP.createIndirectCashFlowStatementRowsTable(stm);

				if ( isOk( 109 ) ) DBCreatorSAP.createDefaultUnitTable(stm); 
				if ( isOk( 110 ) ) DBCreatorSAP.createTransactionPeriodTable(stm);
				if ( isOk( 111 ) ) DBCreatorSAP.createPayrollDeptValueTable(stm);
				if ( isOk( 112 ) ) DBCreatorSAP.createTaxArt21DeptValueTable(stm);
				
				m_conn.commit();
				m_conn.setAutoCommit(true);
			}
			catch(Exception ex){
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, ex.toString());
				try{
					m_conn.rollback(); m_conn.setAutoCommit(true);} catch(Exception exc){}
			}
		}
		else if( e.getSource() == m_btDrop ){
			try{
				m_conn.setAutoCommit(false);
				Statement stm = m_conn.createStatement();

				if ( isOk( 0 ) ) DBCreatorSAP.deleteAccountTable(stm);
				if ( isOk( 1 ) ) DBCreatorSAP.deleteAccountStructureTable(stm);
				if ( isOk( 2 ) ) DBCreatorSAP.deleteCurrencyTable(stm);
				if ( isOk( 3 ) ) DBCreatorSAP.deleteBankAccountTable(stm);
				if ( isOk( 4 ) ) DBCreatorSAP.deleteCreditorListTable(stm);
				if ( isOk( 5 ) ) DBCreatorSAP.deleteCompanyLoanTable(stm);
				if ( isOk( 6 ) ) DBCreatorSAP.deleteCashAccountTable(stm);

				if ( isOk( 7 ) ) DBCreatorSAP.deleteJournalTable(stm);

				if ( isOk( 8 ) ) DBCreatorSAP.deleteJournalStandardTable(stm);
				if ( isOk( 9 ) ) DBCreatorSAP.deleteJournalStandardStructureTable(stm);
				if ( isOk( 10 ) ) DBCreatorSAP.deleteJournalStandardAccountTable(stm);
				if ( isOk( 11 ) ) DBCreatorSAP.deleteExchangeRateTable(stm);
				if ( isOk( 12 ) ) DBCreatorSAP.deleteActivityTable(stm);
				if ( isOk( 13 ) ) DBCreatorSAP.deleteActivityStructureTable(stm);
				if ( isOk( 14 ) ) DBCreatorSAP.deleteUnitTable(stm);

				if ( isOk( 15 ) ) DBCreatorSAP.deleteBeginningAccountReceivableTable(stm);
				if ( isOk( 16 ) ) DBCreatorSAP.deleteAccountReceivableDefaultTable(stm);
				if ( isOk( 17 ) ) DBCreatorSAP.deleteBeginningAccountPayableTable(stm);
				if ( isOk( 18 ) ) DBCreatorSAP.deleteAccountPayableDefaultTable(stm);

				if ( isOk( 19 ) ) DBCreatorSAP.deleteJournalStandardSettingTable(stm);
				if ( isOk( 20 ) ) DBCreatorSAP.deleteSignatureTable(stm);
				if ( isOk( 21 ) ) DBCreatorSAP.deleteBaseCurrencyTable(stm);

				if ( isOk( 22 ) ) DBCreatorSAP.deleteWorksheetTable(stm);
				if ( isOk( 23 ) ) DBCreatorSAP.deleteWorksheetColumnTable(stm);
				if ( isOk( 24 ) ) DBCreatorSAP.deleteWorksheetJournalTable(stm);
				if ( isOk( 25 ) ) DBCreatorSAP.deleteBalanceSheetReportTable(stm);

				if ( isOk( 26 ) ) DBCreatorSAP.deleteTransactionTable(stm);
				if ( isOk( 27 ) ) DBCreatorSAP.deleteTransactionValueTable(stm);
				if ( isOk( 28 ) ) DBCreatorSAP.deleteTransactionPostedTable(stm);
				if ( isOk( 29 ) ) DBCreatorSAP.deleteTransactionValuePostedTable(stm);

				if ( isOk( 30 ) ) DBCreatorSAP.deleteBeginningBalanceTable(stm);
				if ( isOk( 31 ) ) DBCreatorSAP.deleteBeginningBalanceSheetDetailTable(stm);
				if ( isOk( 32 ) ) DBCreatorSAP.deleteBeginningBankDetailTable(stm);
				if ( isOk( 33 ) ) DBCreatorSAP.deleteBeginningCashDetailTable(stm);

				if ( isOk( 34 ) ) DBCreatorSAP.deleteReceiveESDiffTable(stm);
				//if ( isOk( 35 ) ) DBCreatorSAP.deleteReceiveESDiffDetailTable(stm);
				if ( isOk( 35 ) ) DBCreatorSAP.deleteReceiveUnitCashBankTrnsTable(stm);
				//if ( isOk( 37 ) ) DBCreatorSAP.deleteReceiveUnitCashBankTrnsDetailTable(stm);
				if ( isOk( 36 ) ) DBCreatorSAP.deleteReceiveEmpReceivableTable(stm);
				//if ( isOk( 39 ) ) DBCreatorSAP.deleteReceiveEmpReceivableDetailTable(stm);
				if ( isOk( 37 ) ) DBCreatorSAP.deleteReceiveLoanTable(stm);
				//if ( isOk( 41 ) ) DBCreatorSAP.deleteReceiveLoanDetailTable(stm);
				if ( isOk( 38 ) ) DBCreatorSAP.deleteReceiveOthersTable(stm);
				if ( isOk( 39 ) ) DBCreatorSAP.deleteReceiveOthersDetailTable(stm);

				if ( isOk( 40 ) ) DBCreatorSAP.deletePaymentProjectCostTable(stm);
				if ( isOk( 41 ) ) DBCreatorSAP.deletePaymentProjectCostDetailTable(stm);
				if ( isOk( 42 ) ) DBCreatorSAP.deletePaymentOperasionalCostTable(stm);
				if ( isOk( 43 ) ) DBCreatorSAP.deletePaymentOperasionalCostDetailTable(stm);
				if ( isOk( 44 ) ) DBCreatorSAP.deleteCashAdvanceIOUProjectTable(stm);
				if ( isOk( 45 ) ) DBCreatorSAP.deleteCashAdvanceIOUProjectIstallmentsTable(stm);
				if ( isOk( 46 ) ) DBCreatorSAP.deleteCashAdvanceIOUProjectSettledTable(stm);
				if ( isOk( 47 ) ) DBCreatorSAP.deleteCashAdvanceIOUProjectReceiveTable(stm);
				if ( isOk( 48 ) ) DBCreatorSAP.deleteCashAdvanceIOUOthersTable(stm);
				if ( isOk( 49 ) ) DBCreatorSAP.deleteCashAdvanceIOUOthersInstallmentsTable(stm);
				if ( isOk( 50 ) ) DBCreatorSAP.deleteCashAdvanceIOUOthersSettledTable(stm);
				if ( isOk( 51 ) ) DBCreatorSAP.deleteCashAdvanceIOUOthersReceiveTable(stm);
				if ( isOk( 52 ) ) DBCreatorSAP.deleteCashAdvanceProjectTable(stm);
				if ( isOk( 53 ) ) DBCreatorSAP.deleteCashAdvanceOthersTable(stm);
				if ( isOk( 54 ) ) DBCreatorSAP.deletePaymentESDifferenceTable(stm);
				//if ( isOk( 57 ) ) DBCreatorSAP.deletePaymentESDifferenceDetailTable(stm);
				if ( isOk( 55 ) ) DBCreatorSAP.deletePaymentUnitCashBankTransferTable(stm);
				//if ( isOk( 59 ) ) DBCreatorSAP.deletePaymentUnitCashBankTransferDetailTable(stm);
				if ( isOk( 56 ) ) DBCreatorSAP.deletePaymentEmployeeReceivableTable(stm);
				//if ( isOk( 61 ) ) DBCreatorSAP.deletePaymentEmployeeReceivableDetailTable(stm);
				if ( isOk( 57 ) ) DBCreatorSAP.deletePaymentLoanTable(stm);
				//if ( isOk( 63 ) ) DBCreatorSAP.deletePaymentLoanDetailTable(stm);
				if ( isOk( 58 ) ) DBCreatorSAP.deletePaymentOthersTable(stm);
				if ( isOk( 59 ) ) DBCreatorSAP.deletePaymentOthersDetailTable(stm);

				if ( isOk( 60 ) ) DBCreatorSAP.deleteSalesAdvanceTable(stm);
				//if ( isOk( 59 ) ) DBCreatorSAP.deleteSalesAdvanceDetailTable(stm);
				if ( isOk( 61 ) ) DBCreatorSAP.deleteSalesInvoiceTable(stm);
				//if ( isOk( 61 ) ) DBCreatorSAP.deleteSalesInvoiceDetailTable(stm);
				if ( isOk( 62 ) ) DBCreatorSAP.deleteSalesItemTable(stm);
				if ( isOk( 63 ) ) DBCreatorSAP.deleteSalesItemDetailTable(stm);
				if ( isOk( 64 ) ) DBCreatorSAP.deleteSalesARReceivedTable(stm);
				//if ( isOk( 65 ) ) DBCreatorSAP.deleteSalesARReceivedDetailTable(stm);

				if ( isOk( 65 ) ) DBCreatorSAP.deletePurchaseReceiptTable(stm);
				//if ( isOk( 67 ) ) DBCreatorSAP.deletePurchaseReceiptDetailTable(stm);
				if ( isOk( 66 ) ) DBCreatorSAP.deletePurchaseReceiptItemTable(stm);
				if ( isOk( 67 ) ) DBCreatorSAP.deletePurchaseAPPaymentTable(stm);
				//if ( isOk( 70 ) ) DBCreatorSAP.deletePurchaseAPPaymentDetailTable(stm);

				if ( isOk( 68 ) ) DBCreatorSAP.deletePayrollPaymentSalaryHOTable(stm);
				if ( isOk( 69 ) ) DBCreatorSAP.deletePayrollPaymentSalaryHODetailTable(stm);
				//if ( isOk( 70 ) ) DBCreatorSAP.deletePayrollPaymentSalaryHOPayableTable(stm);
				if ( isOk( 71 ) ) DBCreatorSAP.deletePayrollPaymentSalaryUnitTable(stm);
				if ( isOk( 72 ) ) DBCreatorSAP.deletePayrollPaymentSalaryUnitDetailTable(stm);
				if ( isOk( 73 ) ) DBCreatorSAP.deletePayrollPaymentTax21HOTable(stm);
				if ( isOk( 74 ) ) DBCreatorSAP.deletePayrollPaymentTax21HODetailTable(stm);
				//if ( isOk( 75 ) ) DBCreatorSAP.deletePayrollPaymentTax21HOPayableTable(stm);
				if ( isOk( 76 ) ) DBCreatorSAP.deletePayrollPaymentTax21UnitTable(stm);
				if ( isOk( 77 ) ) DBCreatorSAP.deletePayrollPaymentTax21UnitDetailTable(stm);
				if ( isOk( 78 ) ) DBCreatorSAP.deletePayrollPaymentEmployeeInsuranceTable(stm);
				if ( isOk( 79 ) ) DBCreatorSAP.deletePayrollPaymentEmployeeInsuranceDetailTable(stm);
				if ( isOk( 80 ) ) DBCreatorSAP.deletePayrollPaymentEmployeeInsurancePayableTable(stm);

				if ( isOk( 81 ) ) DBCreatorSAP.deleteExpenseSheetTable(stm);
				if ( isOk( 82 ) ) DBCreatorSAP.deleteExpenseSheetDetailTable(stm);

				if ( isOk( 83 ) ) DBCreatorSAP.deleteMemorialJournalStandardTable(stm);
				if ( isOk( 84 ) ) DBCreatorSAP.deleteMemorialJournalStandardDetailTable(stm);
				if ( isOk( 85 ) ) DBCreatorSAP.deleteMemorialJournalNonStandardTable(stm);
				if ( isOk( 86 ) ) DBCreatorSAP.deleteMemorialJournalNonStandardDetailTable(stm);

				if ( isOk( 87 ) ) DBCreatorSAP.deleteVariableAccountSetting(stm);
				if ( isOk( 88 ) ) DBCreatorSAP.deleteSubsidiaryAccountSetting(stm);

				if ( isOk( 89 ) ) DBCreatorSAP.deleteBeginningAccountPayableTable(stm);
				if ( isOk( 90 ) ) DBCreatorSAP.deleteBeginningAccountReceivableTable(stm);
				if ( isOk( 91 ) ) DBCreatorSAP.deleteBeginningEmployeeReceivableTable(stm);
				if ( isOk( 92 ) ) DBCreatorSAP.deleteBeginningCashInAdvanceTable(stm);
				if ( isOk( 93 ) ) DBCreatorSAP.deleteBeginningESDiffTable(stm);
				if ( isOk( 94 ) ) DBCreatorSAP.deleteBeginningLoanTable(stm);
				if ( isOk( 95 ) ) DBCreatorSAP.deleteBeginningWorkInProgressDetailTable(stm);
				
				if ( isOk( 96 ) ) DBCreatorSAP.deleteTrialBalanceJournalTypeSettingTable(stm);
				if ( isOk( 97 ) ) DBCreatorSAP.deleteTrialBalanceAccountTypeSettingTable(stm);
				
				if ( isOk( 98 ) ) DBCreatorSAP.deleteClosingTransactionTable(stm);
				if ( isOk( 99 ) ) DBCreatorSAP.deleteClosingTransactionDetailTable(stm);
				
				if ( isOk( 100 ) ) DBCreatorSAP.deleteIncomeStatementDesignTable(stm);
				if ( isOk( 101 ) ) DBCreatorSAP.deleteIncomeStatementJournalTable(stm);
				if ( isOk( 102 ) ) DBCreatorSAP.deleteIncomeStatementRowsTable(stm);
				
				if ( isOk( 103 ) ) DBCreatorSAP.deleteBalanceSheetDesignTable(stm);
				if ( isOk( 104 ) ) DBCreatorSAP.deleteBalanceSheetJournalTable(stm);
				if ( isOk( 105 ) ) DBCreatorSAP.deleteBalanceSheetRowsTable(stm);
				
				if ( isOk( 106 ) ) DBCreatorSAP.deleteIndirectCashFlowStatementDesignTable(stm);
				if ( isOk( 107 ) ) DBCreatorSAP.deleteIndirectCashFlowStatementJournalTable(stm);
				if ( isOk( 108 ) ) DBCreatorSAP.deleteIndirectCashFlowStatementRowsTable(stm);
				
				if ( isOk( 109 ) ) DBCreatorSAP.deleteDefaultUnitTable(stm);
				if ( isOk( 110 ) ) DBCreatorSAP.deleteTransactionPeriodTable(stm);
				if ( isOk( 111 ) ) DBCreatorSAP.deletePayrollDeptValueTable(stm);
				if ( isOk( 112 ) ) DBCreatorSAP.deleteTaxArt21DeptValueTable(stm);

				m_conn.commit();
				m_conn.setAutoCommit(true);
			}
			catch(Exception ex){
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, ex.toString());
				try{
					m_conn.rollback(); m_conn.setAutoCommit(true);} catch(Exception exc){}
			}
		}
		else if(e.getSource() == m_btInit ){
			try{
				Statement stm = m_conn.createStatement();
				m_conn.setAutoCommit(false);

				initApplicationTable(stm);
				initModulTable(stm);

				InitAccount.initKodeRekeningAktiva(m_conn);
				InitAccount.initKodeRekeningKewajiban(m_conn);
				InitAccount.initKodeRekeningEkuitas(m_conn);
				InitAccount.initKodeRekeningPendapatan(m_conn);
				InitAccount.initKodeRekeningBeban(m_conn);
				InitAccount.initKodeRekeningPendapatanDanBeban(m_conn);

				m_conn.commit();
				m_conn.setAutoCommit(true);
			}
			catch(Exception ex){
				JOptionPane.showMessageDialog(null,ex.toString());
				try{
					m_conn.rollback(); m_conn.setAutoCommit(true);
				} catch(Exception exc){}
			}
		}
		else if(e.getSource() == m_btdeInit ){
			try{
				Statement stm = m_conn.createStatement();
				m_conn.setAutoCommit(false);

				deInitModulTable(stm);
				deInitApplicationTable(stm);

				m_conn.commit();
				m_conn.setAutoCommit(true);
			}
			catch(Exception ex){
				JOptionPane.showMessageDialog(null,ex.toString());
				try{
					m_conn.rollback(); m_conn.setAutoCommit(true);
				}
				catch(Exception exc){}
			}
		}
		else if( e.getSource() == m_btCheck ) {
			for( int i = 0; i < m_table.getRowCount(); i++ )
				m_table.setValueAt( new Boolean("true"), i, 1 );
		}
		else if(e.getSource() == m_btUnCheck ) {
			for( int i = 0; i < m_table.getRowCount(); i++ )
				m_table.setValueAt( new Boolean("false"), i, 1 );
		}
	}

	void setData(){
		tModel.addRow(new Object[]{"Account", new Boolean (true)});
		tModel.addRow(new Object[]{"Account Structure", new Boolean (true)});
		tModel.addRow(new Object[]{"Currency", new Boolean (true)});
		tModel.addRow(new Object[]{"Bank Account", new Boolean (true)});
		tModel.addRow(new Object[]{"Creditor List", new Boolean (true)});
		tModel.addRow(new Object[]{"Loan", new Boolean (true)});
		tModel.addRow(new Object[]{"Cash Account", new Boolean (true)});

		tModel.addRow(new Object[]{"Journal", new Boolean (true)});

		tModel.addRow(new Object[]{"Journal Standard", new Boolean (true)});
		tModel.addRow(new Object[]{"Journal Standard Structure", new Boolean (true)});
		tModel.addRow(new Object[]{"Journal Standard Account", new Boolean (true)});
		tModel.addRow(new Object[]{"Exchange Rate", new Boolean (true)});

		tModel.addRow(new Object[]{"Activity", new Boolean (true)});
		tModel.addRow(new Object[]{"Activity Structure", new Boolean (true)});
		tModel.addRow(new Object[]{"Unit", new Boolean (true)});

		tModel.addRow(new Object[]{"Beginning Account Receivable", new Boolean (true)});
		tModel.addRow(new Object[]{"Account Receivable Default", new Boolean (true)});
		tModel.addRow(new Object[]{"Beginning Account Payable", new Boolean (true)});
		tModel.addRow(new Object[]{"Account Payable Default", new Boolean (true)});

		tModel.addRow(new Object[]{"Standard Journal Setting", new Boolean (true)});
		tModel.addRow(new Object[]{"Signature", new Boolean (true)});
		tModel.addRow(new Object[]{"Base Currency", new Boolean (true)});

		tModel.addRow(new Object[]{"Worksheet", new Boolean (true)});
		tModel.addRow(new Object[]{"Worksheet Column", new Boolean (true)});
		tModel.addRow(new Object[]{"Worksheet Journal", new Boolean (true)});
		tModel.addRow(new Object[]{"Balance Sheet", new Boolean (true)});

		tModel.addRow(new Object[]{"Transaction", new Boolean (true)});
		tModel.addRow(new Object[]{"Transaction Value", new Boolean (true)});
		tModel.addRow(new Object[]{"Transaction Posted", new Boolean (true)});
		tModel.addRow(new Object[]{"Transaction Value Posted", new Boolean (true)});

		tModel.addRow(new Object[]{"Beginning Balance", new Boolean (true)});
		tModel.addRow(new Object[]{"Beginning Balance Sheet Detail", new Boolean (true)});
		tModel.addRow(new Object[]{"Beginning Bank Detail", new Boolean (true)});
		tModel.addRow(new Object[]{"Beginning Cash Detail", new Boolean (true)});
		
		tModel.addRow(new Object[]{"Expense Sheet Difference Receive", new Boolean (true)});
		//tModel.addRow(new Object[]{"Expense Sheet Difference Receive Detail", new Boolean (true)});
		tModel.addRow(new Object[]{"Unit Bank Cash Transfer Receive", new Boolean (true)});
		// tModel.addRow(new Object[]{"Unit Bank Cash Transfer Receive Detail", new Boolean (true)});
		tModel.addRow(new Object[]{"Employee Receivable Receive", new Boolean (true)});
		// tModel.addRow(new Object[]{"Employee Receivable Receive Detail", new Boolean (true)});
		tModel.addRow(new Object[]{"Company Loan Receive", new Boolean (true)});
		// tModel.addRow(new Object[]{"Company Loan Receive Detail", new Boolean (true)});
		tModel.addRow(new Object[]{"Others Receive", new Boolean (true)});
		tModel.addRow(new Object[]{"Others Receive Detail", new Boolean (true)});

		tModel.addRow(new Object[]{"Project Cost Payment", new Boolean (true)});
		tModel.addRow(new Object[]{"Project Cost Payment Detail", new Boolean (true)});
		tModel.addRow(new Object[]{"Operational Cost Payment", new Boolean (true)});
		tModel.addRow(new Object[]{"Operational Cost Payment Detail", new Boolean (true)});
		tModel.addRow(new Object[]{"Cash Advance I Owe U Project ", new Boolean (true)});
		tModel.addRow(new Object[]{"Cash Advance I Owe U Project Installments ", new Boolean (true)});
		tModel.addRow(new Object[]{"Cash Advance I Owe U Project Setlled ", new Boolean (true)});
		tModel.addRow(new Object[]{"Cash Advance I Owe U Project Receive ", new Boolean (true)});
		tModel.addRow(new Object[]{"Cash Advance I Owe U Others ", new Boolean (true)});
		tModel.addRow(new Object[]{"Cash Advance I Owe U Others Installments ", new Boolean (true)});
		tModel.addRow(new Object[]{"Cash Advance I Owe U Others Settled ", new Boolean (true)});
		tModel.addRow(new Object[]{"Cash Advance I Owe U Others Receive ", new Boolean (true)});
		tModel.addRow(new Object[]{"Cash Advance Project ", new Boolean (true)});
		tModel.addRow(new Object[]{"Cash Advance Others ", new Boolean (true)});
		tModel.addRow(new Object[]{"Expense Sheet Difference Payment ", new Boolean (true)});
		//tModel.addRow(new Object[]{"Expense Sheet Difference Payment Detail ", new Boolean (true)});
		tModel.addRow(new Object[]{"Unit Cash Bank Transfer Payment ", new Boolean (true)});
		//tModel.addRow(new Object[]{"Unit Cash Bank Transfer Payment Detail", new Boolean (true)});
		tModel.addRow(new Object[]{"Employee Receivable Payment ", new Boolean (true)});
		//tModel.addRow(new Object[]{"Employee Receivable Payment Detail", new Boolean (true)});
		tModel.addRow(new Object[]{"Loan Payment ", new Boolean (true)});
		//tModel.addRow(new Object[]{"Loan Payment Detail ", new Boolean (true)});
		tModel.addRow(new Object[]{"Others Payment ", new Boolean (true)});
		tModel.addRow(new Object[]{"Others Payment Detail ", new Boolean (true)});

		tModel.addRow(new Object[]{"Sales Advance ", new Boolean (true)});
		//tModel.addRow(new Object[]{"Sales Advance Detail ", new Boolean (true)});
		tModel.addRow(new Object[]{"Sales Invoice ", new Boolean (true)});
		//tModel.addRow(new Object[]{"Sales Invoice Detail ", new Boolean (true)});
		tModel.addRow(new Object[]{"Sales Invoice Item ", new Boolean (true)});
		tModel.addRow(new Object[]{"Sales Invoice Item Detail", new Boolean (true)});
		tModel.addRow(new Object[]{"Sales AR Received ", new Boolean (true)});
		//tModel.addRow(new Object[]{"Sales AR Received Detail ", new Boolean (true)});

		tModel.addRow(new Object[]{"Purchase Receipt ", new Boolean (true)});
		//tModel.addRow(new Object[]{"Purchase Receipt Detail", new Boolean (true)});
		tModel.addRow(new Object[]{"Purchase Receipt Item", new Boolean (true)});
		tModel.addRow(new Object[]{"Purchase AP Payment ", new Boolean (true)});
		//tModel.addRow(new Object[]{"Purchase AP Payment Detail ", new Boolean (true)});

		tModel.addRow(new Object[]{"Payroll Payment Salary HO ", new Boolean (true)});
		tModel.addRow(new Object[]{"Payroll Payment Salary HO Detail", new Boolean (true)});
		tModel.addRow(new Object[]{"Payroll Payment Salary HO Payable", new Boolean (true)});
		tModel.addRow(new Object[]{"Payroll Payment Salary Unit ", new Boolean (true)});
		tModel.addRow(new Object[]{"Payroll Payment Salary Unit Detail ", new Boolean (true)});
		tModel.addRow(new Object[]{"Payroll Payment Tax 21 HO ", new Boolean (true)});
		tModel.addRow(new Object[]{"Payroll Payment Tax 21 HO Detail", new Boolean (true)});
		tModel.addRow(new Object[]{"Payroll Payment Tax 21 HO Payable", new Boolean (true)});
		tModel.addRow(new Object[]{"Payroll Payment Tax 21 Unit ", new Boolean (true)});
		tModel.addRow(new Object[]{"Payroll Payment Tax 21 Unit Detail ", new Boolean (true)});
		tModel.addRow(new Object[]{"Payroll Payment Employee Insurance ", new Boolean (true)});
		tModel.addRow(new Object[]{"Payroll Payment Employee Insurance Detail", new Boolean (true)});
		tModel.addRow(new Object[]{"Payroll Payment Employee Insurance Payable", new Boolean (true)});

		tModel.addRow(new Object[]{"Expense Sheet", new Boolean (true)});
		tModel.addRow(new Object[]{"Expense Sheet Detail", new Boolean (true)});

		tModel.addRow(new Object[]{"Memorial Journal Standard", new Boolean (true)});
		tModel.addRow(new Object[]{"Memorial Journal Standard Detail", new Boolean (true)});
		tModel.addRow(new Object[]{"Memorial Journal Non Standard", new Boolean (true)});
		tModel.addRow(new Object[]{"Memorial Journal Non Standard Detail", new Boolean (true)});

		tModel.addRow(new Object[]{"Variable Account Setting", new Boolean(true)});
		tModel.addRow(new Object[]{"Subsidiary Account Setting", new Boolean(true)});
		
		/*
		 * 
     if ( isOk( 89 ) ) DBCreatorSAP.createBeginningAccountPayableTable(stm);
        if ( isOk( 90 ) ) DBCreatorSAP.createBeginningAccountReceivableTable(stm);
        if ( isOk( 91 ) ) DBCreatorSAP.createBeginningEmployeeReceivableTable(stm);
        if ( isOk( 92 ) ) DBCreatorSAP.createBeginningCashInAdvanceTable(stm);
        if ( isOk( 93 ) ) DBCreatorSAP.createBeginningESDiffTable(stm);
		 */
		tModel.addRow(new Object[]{"Beginning Account Payable", new Boolean(true)});
		tModel.addRow(new Object[]{"Beginning Account Receivable", new Boolean(true)});
		tModel.addRow(new Object[]{"Beginning Employee Receivable", new Boolean(true)});
		tModel.addRow(new Object[]{"Beginning Cash Advance", new Boolean(true)});
		tModel.addRow(new Object[]{"Beginning ES Difference", new Boolean(true)});
		tModel.addRow(new Object[]{"Beginning Loan", new Boolean(true)});
		tModel.addRow(new Object[]{"Beginning Work In Progress", new Boolean(true)});

		tModel.addRow(new Object[]{"Trial Balance Journal Type Setting", new Boolean(true)});
		tModel.addRow(new Object[]{"Trial Balance Account Type Setting", new Boolean(true)});
	
		tModel.addRow(new Object[]{"Closing Transaction", new Boolean(true)});
		tModel.addRow(new Object[]{"Closing Transaction Detail", new Boolean(true)});
		
		tModel.addRow(new Object[]{"Income Statement Design", new Boolean(true)});
		tModel.addRow(new Object[]{"Income Statement Journal", new Boolean(true)});
		tModel.addRow(new Object[]{"Income Statement Rows", new Boolean(true)});
		
		tModel.addRow(new Object[]{"Balance Sheet Design", new Boolean(true)});
		tModel.addRow(new Object[]{"Balance Sheet Journal", new Boolean(true)});
		tModel.addRow(new Object[]{"Balance Sheet Rows", new Boolean(true)});

		tModel.addRow(new Object[]{"Indirect Cash Flow Statement Design", new Boolean(true)});
		tModel.addRow(new Object[]{"Indirect Cash Flow Statement Journal", new Boolean(true)});
		tModel.addRow(new Object[]{"Indirect Cash Flow Statement Rows", new Boolean(true)});

		tModel.addRow(new Object[]{"Default Unit", new Boolean(true)});
		tModel.addRow(new Object[]{"Transaction Period", new Boolean(true)});
		tModel.addRow(new Object[]{"Payroll Department Value", new Boolean(true)});
		tModel.addRow(new Object[]{"Payroll Taxt Art Department Value", new Boolean(true)});

	}

	class ThisTableModel extends TabelModel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ThisTableModel(){
			addColumn("Table");
			addColumn("Pilih");

		}

		public Class getColumnClass(int columnindex){
			if ( columnindex == 1) return Boolean.class;
			return super.getColumnClass(columnindex);
		}
	}

	static void initApplicationTable(Statement stm) throws SQLException{
		stm.executeUpdate("INSERT INTO " + pohaci.gumunda.aas.dbapi.IDBConstants.TABLE_APPLICATION + " VALUES('" +
				IConstants.APP_ACCOUNTING + "')");
		System.out.println("init application ok ");
	}

	static void deInitApplicationTable(Statement stm) throws SQLException{
		stm.executeUpdate("DELETE FROM " + pohaci.gumunda.aas.dbapi.IDBConstants.TABLE_APPLICATION + " WHERE " +
				pohaci.gumunda.aas.dbapi.IDBConstants.ATT_NAME + " = '" + IConstants.APP_ACCOUNTING + "'");
		System.out.println("deinit application ok ");

	}

	static  void initModulTable(Statement stm) throws SQLException{
		stm.executeUpdate("INSERT INTO " + pohaci.gumunda.aas.dbapi.IDBConstants.TABLE_MODUL + " values('" +
				IDBConstants.MODUL_MASTER_DATA + "')");
		System.out.println("init modul ok ");
	}

	static void deInitModulTable(Statement stm) throws SQLException{
		stm.executeUpdate("DELETE FROM " + pohaci.gumunda.aas.dbapi.IDBConstants.TABLE_MODUL + " WHERE " +
				pohaci.gumunda.aas.dbapi.IDBConstants.ATT_NAME + " = '" + IDBConstants.MODUL_MASTER_DATA + "'");
		System.out.println("deinit modul ok ");
	}
}