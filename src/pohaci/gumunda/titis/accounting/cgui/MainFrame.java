
package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.swing.*;

import pohaci.gumunda.aas.logic.LoginBusinessLogic;
import pohaci.gumunda.titis.accounting.cgui.reportdesign.BalanceSheetDesginerPanel;
import pohaci.gumunda.titis.accounting.cgui.reportdesign.IncomeStatementDesignerPanel;
import pohaci.gumunda.titis.accounting.cgui.reportdesign.IndirectCashFlowStatementDesignerPanel;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalanceSheetEntryPanel;
import pohaci.gumunda.titis.application.*;
import pohaci.gumunda.titis.appmanager.entity.ApplicationFunction;
import pohaci.gumunda.titis.appmanager.entity.UserProfile;
import pohaci.gumunda.titis.appmanager.logic.AppManagerLogic;
import pohaci.gumunda.titis.hrm.cgui.AllowanceMultiplierPanel;
import pohaci.gumunda.titis.hrm.cgui.PTKPPanel;
import pohaci.gumunda.titis.hrm.cgui.PayrollCategoryPanel;
import pohaci.gumunda.titis.hrm.cgui.PayrollComponentTreePanel;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21ComponentPanel;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21TariffPanel;
//import pohaci.gumunda.titis.hrm.cgui.report.RptBalance;
import pohaci.gumunda.titis.hrm.cgui.report.RptPaycheques;
import pohaci.gumunda.titis.project.cgui.ProjectListPanel;
import pohaci.gumunda.titis.project.cgui.ProjectMonitoringPanel;
import pohaci.gumunda.titis.project.cgui.report.ProjectCostPanel;
import pohaci.gumunda.titis.accounting.cgui.report.*;

public class MainFrame extends FrameMain implements ActionListener {
	//Paycheques
	private static final long serialVersionUID = 1L;
	protected static final String HELP_FILENAME_MASTER = "accounting_master_data_setting.chm";
	protected static final String HELP_FILENAME_MEMORIAL = "accounting_memorial_closing_posting.chm";
	protected static final String HELP_FILENAME_PAYMENT = "accounting_payment.chm";
	protected static final String HELP_FILENAME_PAYROLL = "accounting_payroll.chm";
	protected static final String HELP_FILENAME_EXPENSE_SHEET = "accounting_expense_sheet.chm";
	protected static final String HELP_FILENAME_OVERVIEW = "accounting_overview.chm";
	protected static final String HELP_FILENAME_SALES_PURCHASE = "accounting_sales_purchase.chm";
	protected static final String HELP_FILENAME_RECEIVE = "accounting_receive.chm";
	protected static final String HELP_FILENAME_PROJECT = "accounting_project.chm";
	protected static final String HELP_FOLDER = "..\\helps\\";

	JMenuItem mi_hlp_master = new JMenuItem("Master Data & Setting");
	JMenuItem mi_hlp_memorial = new JMenuItem("Memorial, Closing, & Transaction Posting");
	JMenuItem mi_hlp_payment = new JMenuItem("Cash/Bank Payment");
	JMenuItem mi_hlp_payroll = new JMenuItem("Payroll");
	JMenuItem mi_hlp_expensesheet = new JMenuItem("Expense Sheet");
	JMenuItem mi_hlp_overview = new JMenuItem("Business Process Overview");
	JMenuItem mi_hlp_salespurchase = new JMenuItem("Sales & Purchase");
	JMenuItem mi_hlp_receive = new JMenuItem("Cash/Bank Receive");
	JMenuItem mi_hlp_project = new JMenuItem("Project");

	JMenuItem mi_logout = new JMenuItem("Logout");
	JMenuItem mi_exit = new JMenuItem("Exit");
	JMenuItem mi_account = new JMenuItem("Chart of Account");
	JMenuItem mi_currency = new JMenuItem("Currency");
	JMenuItem mi_bankaccount = new JMenuItem("Bank Account");
	JMenuItem mi_creditorlist = new JMenuItem("Creditor List");
	JMenuItem mi_bankloan = new JMenuItem("Loan");
	JMenuItem mi_cashaccount = new JMenuItem("Cash Account");
	JMenuItem mi_journal = new JMenuItem("Journal");
	JMenuItem mi_journalstandard = new JMenuItem("Standard Journal");
	JMenuItem mi_exchangerate = new JMenuItem("Exchange Rate");
	JMenuItem mi_payrollcomponent = new JMenuItem("Payroll Component");
	JMenuItem mi_payrollcategory = new JMenuItem("Payroll Category");
	JMenuItem mi_fieldallowancesmultiplier = new JMenuItem("Field Allowance Multiplier");
	JMenuItem mi_tax21component = new JMenuItem("Tax Art 21 Component");
	JMenuItem mi_ptkp = new JMenuItem("PTKP");
	JMenuItem mi_tax21tariff = new JMenuItem("Tax Art 21 Tariff");
	JMenuItem mi_activity = new JMenuItem("Activity Code");
	JMenuItem mi_department = new JMenuItem("Department");
	JMenuItem mi_unit = new JMenuItem("Unit Code");
	JMenuItem mi_customer = new JMenuItem("Customer");
	JMenuItem mi_partner = new JMenuItem("Partner");
	JMenuItem mi_personal = new JMenuItem("Personal");
	JMenuItem mi_beginingbalancesheet = new JMenuItem("Balance Sheet");
	JMenuItem mi_journalsetting = new JMenuItem("Standard Journal Setting");
	JMenuItem mi_balancesheetdesaigner = new JMenuItem("Balance Sheet Designer");
	JMenuItem mi_incomeStatementdesaigner = new JMenuItem("Income Statement Designer");
	JMenuItem mi_indirectCashFlowdesigner = new JMenuItem("Indirect Cash Flow Statement Designer");

	JMenuItem mi_basecurrency = new JMenuItem("Base Currency");
	JMenuItem mi_signature = new JMenuItem("Employee Signature");
	JMenuItem mi_variableAccount = new JMenuItem("Variable Account");
	JMenuItem mi_subsidiryAccount = new JMenuItem("Subsidiary Account");
	JMenuItem mi_balancesheet = new JMenuItem("Balance Sheet");
	JMenuItem mi_cashflow = new JMenuItem("Cash Flow");

	JMenuItem mi_baseunit = new JMenuItem("Base Unit");

	JMenuItem mi_trialbalance = new JMenuItem("Trial Balance");

	JMenuItem mi_expensesheetdifferencereceive = new JMenuItem("Expense Sheet Difference");
	JMenuItem mi_unitbankcashtransferreceive = new JMenuItem("Unit Bank/Cash Transfer");
	JMenuItem mi_employeereceivablereceive = new JMenuItem("Employee Receivables");
	JMenuItem mi_loanreceive = new JMenuItem("Loan");
	JMenuItem mi_othersreceive = new JMenuItem("Others");

	JMenuItem mi_projectcost = new JMenuItem("Project Cost");
	JMenuItem mi_operationalcost = new JMenuItem("Operational Cost");
	JMenuItem mi_cashadvanceiou = new JMenuItem("I Owe You");
	JMenuItem mi_general = new JMenuItem("General");
	JMenuItem mi_expensesheetdifferencepayment = new JMenuItem("Expense Sheet Difference");
	JMenuItem mi_unitbankcashtransferpayment = new JMenuItem("Unit Bank/Cash Transfer");
	JMenuItem mi_employeereceivablepayment = new JMenuItem("Employee Receivables");
	JMenuItem mi_bankloanpayment = new JMenuItem("Loan");
	JMenuItem mi_otherspayment = new JMenuItem("Others");

	JMenuItem mi_paycheque = new JMenuItem("Paycheques");
	JMenuItem mi_mealallowance = new JMenuItem("Meal Allowance");
	JMenuItem mi_transallowance = new JMenuItem("Transportation Allowance");
	JMenuItem mi_overtime = new JMenuItem("Overtime");
	JMenuItem mi_otherallowance = new JMenuItem("Other Allowance");
	JMenuItem mi_insuranceallowance = new JMenuItem("Insurance Allowance");
	JMenuItem mi_taxart21 = new JMenuItem("Tax Art 21");

	JMenuItem mi_prsalaryho=new JMenuItem("Head Office");
	JMenuItem mi_prsalaryunit=new JMenuItem("Unit");
	JMenuItem mi_prtaxho=new JMenuItem("Head Office");
	JMenuItem mi_prtaxunit=new JMenuItem("Unit");
	JMenuItem mi_premployeeinsurance=new JMenuItem("Employee Insurance");

	JMenuItem mi_salesadvance=new JMenuItem("Sales Advanced");
	JMenuItem mi_invoice=new JMenuItem("Invoice");
	JMenuItem mi_salesrcv=new JMenuItem("Sales Receive");

	JMenuItem mi_expensesheet=new JMenuItem("Expense Sheet");

	JMenuItem mi_purchasereceipt = new JMenuItem("Purchase Receipt");
	JMenuItem mi_purchaseaccountpayablepayment = new JMenuItem("Account Payable Payment");

	JMenuItem mi_mjstandard=new JMenuItem("Memorial Journal Standard");
	JMenuItem mi_mjnonstandard=new JMenuItem("Memorial Journal Non Standard");

	JMenuItem mi_projectregistration=new JMenuItem("Project Registration");
	JMenuItem mi_projectmonitoring=new JMenuItem("Project Monitoring");

	JMenuItem mi_posting = new JMenuItem("Transaction Posting");

	JMenuItem mi_neraca = new JMenuItem("Balance Sheet");
	JMenuItem mi_labarugi = new JMenuItem("Income Statement");
	JMenuItem mi_alirankas = new JMenuItem("Indirect Cash Flow Statement");

	JMenuItem mi_genLedger = new JMenuItem("General Ledger");
	JMenuItem mi_accReceivable = new JMenuItem("Account Receivable");
	JMenuItem mi_subsidiaryLedger = new JMenuItem("Subsidiary Ledger");
	JMenuItem mi_empReceivable = new JMenuItem("Employee Receivable");
	JMenuItem mi_trialBalanceRpt = new JMenuItem("Trial Balance");
	JMenuItem mi_cashAdvance = new JMenuItem("Cash Advance");
	JMenuItem mi_accountPayable = new JMenuItem("Account Payable");
	JMenuItem mi_sales = new JMenuItem("Sales");
	JMenuItem mi_purchase = new JMenuItem("Purchase");
	JMenuItem mi_directCashflow = new JMenuItem("Direct Cash Flow Statement");
	JMenuItem mi_projectProfitability = new JMenuItem("Project Profitability");
	JMenuItem mi_paychequerpt = new JMenuItem("Paycheque");
	JMenuItem mi_departmentcost = new JMenuItem("Department Cost");
	JMenuItem mi_projectcostrpt = new JMenuItem("Project Cost");
	JMenuItem mi_projectprofitabilitydetail = new JMenuItem("Project Profitability Detail");

	JMenuItem mi_closing = new JMenuItem("Closing Transaction");

	private UserProfile m_userProfile;
	private Hashtable hashtable = new Hashtable();

	private static final boolean MODE_ADMIN = false;

	public MainFrame() {
		super(IConstants.APP_ACCOUNTING);
		setTitle("ACCOUNTING");
		setSize(750, 600);
		//setIconImage(Toolkit.getDefaultToolkit().getImage("../images/titis.gif"));
		setIconImage(Toolkit.getDefaultToolkit().getImage(".."));
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				if(m_sessionid != -1)
					logout();
				System.exit(0);
			}
		});
		init();
		constructComponent();
		createMenu();
		populateMenu(); // JANGAN DIHAPUS MESKI CUMAN DI-COMMENT

		adjustPosition();
		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
		login();

		// kalau ada apa-apa salahin aja aku.... :)
		// sebelumnya, coba dua baris di bawah ini di comment dulu aja...
		if (!MODE_ADMIN) {
			getAllGrantedFunctions();
			//enableMenu();
			deleteSeparator();
		}
	}

	// METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
	private void getAllGrantedFunctions() {
		AppManagerLogic logic = new AppManagerLogic(m_conn);
		try {
			ApplicationFunction[] functions = logic.getGrantedFunctionForUser(m_userProfile, "ACCOUNTING");
			hashtable.clear();
			for(int i=0; i<functions.length; i++){
				hashtable.put(functions[i].getPath(), functions[i]);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
	private void deleteSeparator() {
		JMenuBar menuBar = getJMenuBar();
		int menuCount = menuBar.getMenuCount();
		for (int i=0; i<menuCount; i++) {
			JMenu menu = menuBar.getMenu(i);
			if ((menu.isEnabled())&&(menu.isVisible())){
				removeSeparatorInMenu(menu);
			}
		}
	}

	// METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
	private void removeSeparatorInMenu(JMenu menu) {
		int menuCount = menu.getMenuComponentCount();
		for(int i=0; i<menuCount; i++){
			Component menuComponent = menu.getMenuComponent(i);
			if(menuComponent!=null){
				if (menuComponent instanceof JMenu) {
					removeSeparatorInMenu((JMenu)menuComponent);
				} else if (menuComponent instanceof JSeparator) {
					boolean isBeforeEnabled = false;
					if (i - 1 >= 0)
						isBeforeEnabled = checkMenuComponent(menu, i-1);
					boolean isAfterEnabled = false;
					if (i + 1 <= menuCount)
						isAfterEnabled = checkMenuComponent(menu, i+1);

					menuComponent.setEnabled(isBeforeEnabled && isAfterEnabled);
					menuComponent.setVisible(isBeforeEnabled && isAfterEnabled);
				} else {
					// do nothing
				}
			}
		}
	}

	// METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
	private boolean checkMenuComponent(JMenu menu, int i) {
		Component componentChecked = menu.getMenuComponent(i);
		return ((componentChecked.isEnabled())&&(componentChecked.isVisible()));
	}

	// METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
	private void enableMenu() {
		JMenuBar menuBar = getJMenuBar();
		int menuCount = menuBar.getMenuCount();
		for(int i=0; i<menuCount; i++){
			JMenu menu = menuBar.getMenu(i);
			if(!(menu.getText().equalsIgnoreCase("File")||menu.getText().equalsIgnoreCase("Window")||menu.getText().equalsIgnoreCase("Help"))){
				String text = menu.getText();
				String path = text + "\\";

				if(isGranted(path)){
					System.err.println("true: " + path);
					menu.setEnabled(true);
					menu.setVisible(true);
				}
				else{
					System.err.println("false: " + path);
					menu.setEnabled(false);
					menu.setVisible(false);
				}
				enableMenuItems(menu, text);
			}
		}
	}

	// METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
	private boolean isGranted(String path) {
		return hashtable.containsKey(path);
	}

	// METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
	private void enableMenuItems(JMenu menu, String text) {
		int menuCount = menu.getItemCount();
		for(int i=0; i<menuCount; i++){
			JMenuItem menuItem = menu.getItem(i);
			if(menuItem!=null){
				String newText = text + "\\" + menuItem.getText();
				if(menuItem instanceof JMenu){
					String path = newText + "\\";
					if(isGranted(path)){
						System.err.println("true: " + path);
						menuItem.setEnabled(true);
						menuItem.setVisible(true);
					}
					else{
						System.err.println("false: " + path);
						menuItem.setEnabled(false);
						menuItem.setVisible(false);
					}
					enableMenuItems((JMenu) menuItem, newText);
				}else{
					String path = newText + "\\";
					if(isGranted(path)){
						System.err.println("true: " + path);
						menuItem.setEnabled(true);
						menuItem.setVisible(true);
					}
					else{
						System.err.println("false: " + path);
						menuItem.setEnabled(false);
						menuItem.setVisible(false);
					}
				}
			}
		}
	}

	// METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
	private void populateMenu() {
		JMenuBar menuBar = getJMenuBar();
		int menuCount = menuBar.getMenuCount();
		for(int i=0; i<menuCount; i++){
			JMenu menu = menuBar.getMenu(i);
			if(!(menu.getText().equalsIgnoreCase("File")||menu.getText().equalsIgnoreCase("Window")||menu.getText().equalsIgnoreCase("Help"))){
				String text = menu.getText();
				String toPrint =
					"INSERT INTO FUNCTION (APPLICATION, FUNCTIONNAME, TREEPATH) "
					+ "VALUES ('ACCOUNTING', '" + menu.getText() + "', '"
					+ text + "\\')" + "\n//";
				System.err.println(toPrint.replace("\\", "\\\\"));
				populateMenuItems(menu, text);
			}
		}
	}

	// METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
	// TERMASUK SQL STATEMENT INSERT BLA..BLA..BLA..
	private void populateMenuItems(JMenu menu, String text) {
		/*insert into functionstructure as
		 select a.autoindex superfunction, b.autoindex subfunction from function a,
		 (select autoindex, application, substr(treepath,1,index(treepath,functionname)-1) parent from function) b
		 where a.treepath=b.parent and a.application=b.application*/
		/*insert into userdetail (username, fullname) (
		 select username, username fullname from uaccount)*/
		int menuCount = menu.getItemCount();
		for(int i=0; i<menuCount; i++){
			JMenuItem menuItem = menu.getItem(i);
			if(menuItem!=null){
				String newText = text + "\\" + menuItem.getText();
				if(menuItem instanceof JMenu){
					String toPrint =
						"INSERT INTO FUNCTION (APPLICATION, FUNCTIONNAME, TREEPATH) "
						+ "VALUES ('ACCOUNTING', '" + menuItem.getText() + "', '"
						+ newText + "\\')" + "\n//";
					System.err.println(toPrint.replace("\\", "\\\\"));
					populateMenuItems((JMenu) menuItem, newText);
				}else{
					String toPrint =
						"INSERT INTO FUNCTION (APPLICATION, FUNCTIONNAME, TREEPATH) "
						+ "VALUES ('ACCOUNTING', '" + menuItem.getText() + "', '"
						+ newText + "\\')" + "\n//";
					System.err.println(toPrint.replace("\\", "\\\\"));
				}
			}
		}
	}

	void adjustPosition() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(dim.width, dim.height );
	}

	void login() {
		pohaci.gumunda.titis.accounting.cgui.bypass.ManagerLoginDlg logindlg = new pohaci.gumunda.titis.accounting.cgui.bypass.ManagerLoginDlg(this, IConstants.APP_ACCOUNTING);
		logindlg.setVisible(true);
		setVisible(true);
		if(logindlg.m_sessionid == -2)
			System.exit(0);

		m_sessionid = logindlg.m_sessionid;
		m_userProfile = logindlg.getUserProfile();
	}



	void logout() {
		try{
			LoginBusinessLogic loginbl = new LoginBusinessLogic(getConnection());
			loginbl.logout(m_sessionid);
			m_sessionid = -1;
		}
		catch(Exception ex){
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}
	}

	void createMenu() {
		JMenuBar mbar = new JMenuBar();
		setJMenuBar(mbar);

		JMenu mfile = new JMenu("File");
		mbar.add(mfile);
		mi_logout.addActionListener(this);
		mfile.add(mi_logout);
		mi_exit.addActionListener(this);
		mfile.add(mi_exit);

		JMenu mtrans = new JMenu("Transaction");
		mbar.add(mtrans);

		JMenu mreceive = new JMenu("Receive");
		mtrans.add(mreceive);
		mi_expensesheetdifferencereceive.addActionListener(this);
		mreceive.add(mi_expensesheetdifferencereceive);
		mi_unitbankcashtransferreceive.addActionListener(this);
		mreceive.add(mi_unitbankcashtransferreceive);
		mi_employeereceivablereceive.addActionListener(this);
		mreceive.add(mi_employeereceivablereceive);
		mi_loanreceive.addActionListener(this);
		mreceive.add(mi_loanreceive);
		mi_othersreceive.addActionListener(this);
		mreceive.add(mi_othersreceive);

		JMenu mpayment = new JMenu("Payment");
		mtrans.add(mpayment);
		mi_projectcost.addActionListener(this);
		mpayment.add(mi_projectcost);
		mi_operationalcost.addActionListener(this);
		mpayment.add(mi_operationalcost);
		JMenu mcashadvance = new JMenu("Cash Advance");
		mpayment.add(mcashadvance);
		mi_cashadvanceiou.addActionListener(this);
		mcashadvance.add(mi_cashadvanceiou);
		mi_general.addActionListener(this);
		mcashadvance.add(mi_general);
		mi_expensesheetdifferencepayment.addActionListener(this);
		mpayment.add(mi_expensesheetdifferencepayment);
		mi_unitbankcashtransferpayment.addActionListener(this);
		mpayment.add(mi_unitbankcashtransferpayment);
		mi_employeereceivablepayment.addActionListener(this);
		mpayment.add(mi_employeereceivablepayment);
		mi_bankloanpayment.addActionListener(this);
		mpayment.add(mi_bankloanpayment);
		mi_otherspayment.addActionListener(this);
		mpayment.add(mi_otherspayment);

		JMenu msales= new JMenu("Sales");
		mtrans.add(msales);
		msales.add(mi_salesadvance);
		mi_salesadvance.addActionListener(this);
		msales.add(mi_invoice);
		mi_invoice.addActionListener(this);
		msales.add(mi_salesrcv);
		mi_salesrcv.addActionListener(this);

		JMenu mpurchase = new JMenu("Purchase");
		mtrans.add(mpurchase);
		mpurchase.add(mi_purchasereceipt);
		mi_purchasereceipt.addActionListener(this);
		mpurchase.add(mi_purchaseaccountpayablepayment);
		mi_purchaseaccountpayablepayment.addActionListener(this);

		JMenu mpayroll = new JMenu("Payroll");
		mtrans.add(mpayroll);
		JMenu mverification = new JMenu("Payroll Verification");
		mpayroll.add(mverification);
		mi_paycheque.addActionListener(this);
		mverification.add(mi_paycheque);

		JMenu mnonpaycheque = new JMenu("Non Paycheque");
		mverification.add(mnonpaycheque);
		mi_mealallowance.addActionListener(this);
		mnonpaycheque.add(mi_mealallowance);
		mi_transallowance.addActionListener(this);
		mnonpaycheque.add(mi_transallowance);
		mi_overtime.addActionListener(this);
		mnonpaycheque.add(mi_overtime);
		JMenu mnonpayment = new JMenu("Non Payment");
		mverification.add(mnonpayment);
		mi_otherallowance.addActionListener(this);
		mnonpayment.add(mi_otherallowance);
		mi_insuranceallowance.addActionListener(this);
		mnonpayment.add(mi_insuranceallowance);

		mi_taxart21.addActionListener(this);
		mverification.add(mi_taxart21);

		JMenu mprpayment=new JMenu("Payroll Payment");
		mpayroll.add(mprpayment);
		JMenu mprpaymentsalary = new JMenu("Salary");
		mprpayment.add(mprpaymentsalary);
		JMenu mprpaymenttax = new JMenu("Tax Art 21");
		mprpayment.add(mprpaymenttax);
		mprpayment.add(mi_premployeeinsurance);
		mi_premployeeinsurance.addActionListener(this);

		mprpaymentsalary.add(mi_prsalaryho);
		mi_prsalaryho.addActionListener(this);
		mprpaymentsalary.add(mi_prsalaryunit);
		mi_prsalaryunit.addActionListener(this);
		mprpaymenttax.add(mi_prtaxho);
		mi_prtaxho.addActionListener(this);
		mprpaymenttax.add(mi_prtaxunit);
		mi_prtaxunit.addActionListener(this);
		mtrans.add(mi_expensesheet);
		mi_expensesheet.addActionListener(this);
		JMenu mmemorial = new JMenu("Memorial");
		mtrans.add(mmemorial);
		mi_mjstandard.addActionListener(this);
		mmemorial.add(mi_mjstandard);
		mi_mjnonstandard.addActionListener(this);
		mmemorial.add(mi_mjnonstandard);

		mi_closing.addActionListener(this);
		mtrans.add(mi_closing);


		JMenu mposting = new JMenu("Posting");
		mbar.add(mposting);
		mposting.add(mi_posting);
		mi_posting.addActionListener(this);
		JMenu mproject = new JMenu("Project");
		mbar.add(mproject);
		mi_projectregistration.addActionListener(this);
		mproject.add(mi_projectregistration);
		mi_projectmonitoring.addActionListener(this);
		mproject.add(mi_projectmonitoring);

		JMenu mreport = new JMenu("Report");
		mbar.add(mreport);
		mreport.add(mi_neraca);
		mreport.add(mi_labarugi);
		mreport.add(mi_alirankas);
		mreport.add(new JSeparator());

		mreport.add(mi_directCashflow);
		mreport.add(mi_trialbalance);
		mreport.add(mi_trialBalanceRpt);
		mreport.add(mi_genLedger);
		mreport.add(mi_subsidiaryLedger);
		mreport.add(new JSeparator());

		mreport.add(mi_sales);
		mreport.add(mi_accReceivable);
		mreport.add(new JSeparator());

		mreport.add(mi_purchase);
		mreport.add(mi_accountPayable);
		mreport.add(new JSeparator());

		mreport.add(mi_empReceivable);
		mreport.add(new JSeparator());

		mreport.add(mi_cashAdvance);
		mreport.add(new JSeparator());
		mreport.add(mi_projectcostrpt);
		mreport.add(mi_projectProfitability);
		mreport.add(mi_projectprofitabilitydetail);
		mreport.add(new JSeparator());
		mreport.add(mi_paychequerpt);
		mreport.add(mi_departmentcost);

		mi_neraca.addActionListener(this);
		mi_labarugi.addActionListener(this);
		mi_genLedger.addActionListener(this);
		mi_alirankas.addActionListener(this);

		mi_accReceivable.addActionListener(this);
		mi_subsidiaryLedger.addActionListener(this);
		mi_empReceivable.addActionListener(this);
		mi_trialBalanceRpt.addActionListener(this);
		mi_cashAdvance.addActionListener(this);
		mi_accountPayable.addActionListener(this);
		mi_sales.addActionListener(this);
		mi_purchase.addActionListener(this);
		mi_directCashflow.addActionListener(this);
		mi_projectProfitability.addActionListener(this);
		mi_projectcostrpt.addActionListener(this);
		mi_paychequerpt.addActionListener(this);
		mi_departmentcost.addActionListener(this);
		mi_projectprofitabilitydetail.addActionListener(this);

		JMenu msetting = new JMenu("Setting");
		mbar.add(msetting);
		JMenu mbalance = new JMenu("Beginning Balance");
		msetting.add(mbalance);
		mi_beginingbalancesheet.addActionListener(this);
		mbalance.add(mi_beginingbalancesheet);
		mi_journalsetting.addActionListener(this);
		msetting.add(mi_journalsetting);
		/*mi_accountreceivable.addActionListener(this);
		 msetting.add(mi_accountreceivable);
		 mi_accountpayable.addActionListener(this);
		 msetting.add(mi_accountpayable);*/
		JMenu mreportdesign = new JMenu("Report Design");
		msetting.add(mreportdesign);
		mi_trialbalance.addActionListener(this);
		mreportdesign.add(mi_trialbalance);
		mreportdesign.add(mi_incomeStatementdesaigner);
		mi_incomeStatementdesaigner.addActionListener(this);
		mreportdesign.add(mi_balancesheetdesaigner);
		mi_balancesheetdesaigner.addActionListener(this);
		mreportdesign.add(mi_indirectCashFlowdesigner);
		mi_indirectCashFlowdesigner.addActionListener(this);


		mi_basecurrency.addActionListener(this);
		msetting.add(mi_basecurrency);
		mi_signature.addActionListener(this);
		msetting.add(mi_signature);
		mi_variableAccount.addActionListener(this);
		msetting.add(mi_variableAccount);
		mi_subsidiryAccount.addActionListener(this);
		msetting.add(mi_subsidiryAccount);
		mi_baseunit.addActionListener(this);
		msetting.add(mi_baseunit);

		JMenu mmaster = new JMenu("Master Data");
		mbar.add(mmaster);
		JMenu maccounting = new JMenu("Accounting");
		mmaster.add(maccounting);
		mi_account.addActionListener(this);
		maccounting.add(mi_account);
		mi_currency.addActionListener(this);
		maccounting.add(mi_currency);
		mi_bankaccount.addActionListener(this);
		maccounting.add(mi_bankaccount);
		mi_creditorlist.addActionListener(this);
		maccounting.add(mi_creditorlist);
		mi_bankloan.addActionListener(this);
		maccounting.add(mi_bankloan);
		mi_cashaccount.addActionListener(this);
		maccounting.add(mi_cashaccount);
		mi_journal.addActionListener(this);
		maccounting.add(mi_journal);
		mi_journalstandard.addActionListener(this);
		maccounting.add(mi_journalstandard);
		mi_exchangerate.addActionListener(this);
		maccounting.add(mi_exchangerate);
		JMenu mpayrollmaster = new JMenu("Payroll");
		mmaster.add(mpayrollmaster);
		mi_payrollcomponent.addActionListener(this);
		mpayrollmaster.add(mi_payrollcomponent);
		mi_payrollcategory.addActionListener(this);
		mpayrollmaster.add(mi_payrollcategory);
		mi_fieldallowancesmultiplier.addActionListener(this);
		mpayrollmaster.add(mi_fieldallowancesmultiplier);
		mi_tax21component.addActionListener(this);
		mpayrollmaster.add(mi_tax21component);
		mi_ptkp.addActionListener(this);
		mpayrollmaster.add(mi_ptkp);
		mi_tax21tariff.addActionListener(this);
		mpayrollmaster.add(mi_tax21tariff);
		mi_activity.addActionListener(this);
		mmaster.add(mi_activity);
		mi_department.addActionListener(this);
		mmaster.add(mi_department);
		mi_unit.addActionListener(this);
		mmaster.add(mi_unit);
		mi_customer.addActionListener(this);
		mmaster.add(mi_customer);
		mi_partner.addActionListener(this);
		mmaster.add(mi_partner);
		mi_personal.addActionListener(this);
		mmaster.add(mi_personal);
		mbar.add(m_windowMenu);

		// BARU:
		mbar.add(m_helpMenu);
		mi_hlp_overview.addActionListener(this);
		m_helpMenu.add(mi_hlp_overview);

		mi_hlp_receive.addActionListener(this);
		m_helpMenu.add(mi_hlp_receive);

		mi_hlp_payment.addActionListener(this);
		m_helpMenu.add(mi_hlp_payment);

		mi_hlp_salespurchase.addActionListener(this);
		m_helpMenu.add(mi_hlp_salespurchase);

		mi_hlp_payroll.addActionListener(this);
		m_helpMenu.add(mi_hlp_payroll);

		mi_hlp_expensesheet.addActionListener(this);
		m_helpMenu.add(mi_hlp_expensesheet);

		mi_hlp_memorial.addActionListener(this);
		m_helpMenu.add(mi_hlp_memorial);

		mi_hlp_project.addActionListener(this);
		m_helpMenu.add(mi_hlp_project);

		mi_hlp_master.addActionListener(this);
		m_helpMenu.add(mi_hlp_master);

		//mi_helpContentsMenu.addActionListener(this); // i change this
		//m_helpMenu.add(mi_helpContentsMenu); // i add this

	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == mi_logout) {
			m_desktopPane.removeAll();
			m_desktopPane.repaint();
			logout();
			login();
		}
		else if(e.getSource() == mi_exit){
			if(m_sessionid != -1)
				logout();
			deInit();
			System.exit(0);
		}

		else if(e.getSource() == mi_account) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Chart of Account", this);
			internalFrame.setContentPane(new AccountTreePanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 700);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_currency) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Currency", this);
			internalFrame.setContentPane(new CurrencyPanel(m_conn, m_sessionid));
			internalFrame.setSize(500, 400);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_bankaccount) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Bank Account", this);
			internalFrame.setContentPane(new BankAccountPanel(m_conn, m_sessionid));
			internalFrame.setSize(1000, 400);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_creditorlist) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Creditor List", this);
			internalFrame.setContentPane(new CreditorListPanel(m_conn, m_sessionid));
			internalFrame.setSize(1100, 400);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_bankloan) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Loan", this);
			internalFrame.setContentPane(new CompanyLoanPanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 400);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_cashaccount) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Cash Account", this);
			internalFrame.setContentPane(new CashAccountPanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 400);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}

			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_activity) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Activity Code", this);
			internalFrame.setContentPane(new ActivityTreePanel(m_conn, m_sessionid));
			internalFrame.setSize(350, 400);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected(true);
			}
			catch(Exception exception) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_department) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Department", this);
			internalFrame.setContentPane(new pohaci.gumunda.titis.hrm.cgui.OrganizationTreePanel(m_conn, m_sessionid));
			internalFrame.setSize(350, 400);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_unit) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Unit Code", this);
			internalFrame.setContentPane(new UnitPanel(m_conn, m_sessionid));
			internalFrame.setSize(500, 400);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_customer) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Customer", this);
			internalFrame.setContentPane(new pohaci.gumunda.titis.project.cgui.CustomerPanel(m_conn, m_sessionid));
			internalFrame.setSize(750, 500);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setMaximum(true);
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_partner) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Partner", this);
			internalFrame.setContentPane(new pohaci.gumunda.titis.project.cgui.PartnerPanel(m_conn, m_sessionid));
			internalFrame.setSize(750, 500);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setMaximum(true);
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_personal) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Personal", this);
			internalFrame.setContentPane(new pohaci.gumunda.titis.project.cgui.PersonalPanel(m_conn, m_sessionid));
			internalFrame.setSize(750, 500);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setMaximum(true);
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_journal) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Journal", this);
			internalFrame.setContentPane(new JournalPanel(m_conn, m_sessionid));
			internalFrame.setSize(350, 400);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_journalstandard) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Standard Journal", this);
			internalFrame.setContentPane(new JournalStandardPanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 500);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_exchangerate) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Exchange Rate", this);
			internalFrame.setContentPane(new ExchangeRatePanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 400);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_payrollcomponent) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Payroll Component", this);
			internalFrame.setContentPane(new PayrollComponentTreePanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 700);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_payrollcategory) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Payroll Category", this);
			internalFrame.setContentPane(new PayrollCategoryPanel(m_conn, m_sessionid));
			internalFrame.setSize(900, 700);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_fieldallowancesmultiplier) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Field Allowances Multiplier", this);
			internalFrame.setContentPane(new AllowanceMultiplierPanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 700);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_tax21component) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Tax Art 21 Component", this);
			internalFrame.setContentPane(new TaxArt21ComponentPanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 700);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_ptkp) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("PTKP", this);
			internalFrame.setContentPane(new PTKPPanel(m_conn, m_sessionid));
			internalFrame.setSize(600, 500);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_tax21tariff) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Tax Art 21 Tariff", this);
			internalFrame.setContentPane(new TaxArt21TariffPanel(m_conn, m_sessionid));
			internalFrame.setSize(600, 500);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		// report designer
		else if(e.getSource() == mi_balancesheet) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Balance Sheet", this);
			internalFrame.setContentPane(new ReportDesignPanel(m_conn, m_sessionid, "Balance Sheet"));
			internalFrame.setSize(800, 725);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_balancesheetdesaigner) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Balance Sheet Designer", this);
			internalFrame.setContentPane(new BalanceSheetDesginerPanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 500);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_incomeStatementdesaigner) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Income Statement Designer", this);
			internalFrame.setContentPane(new IncomeStatementDesignerPanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 500);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_indirectCashFlowdesigner) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Indirect Cash Flow Statement Designer", this);
			internalFrame.setContentPane(new IndirectCashFlowStatementDesignerPanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 500);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		/*else if(e.getSource() == mi_accountreceivable) {
		 setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		 InternalFrame internalFrame = new InternalFrame("Account Receivable", this);
		 internalFrame.setContentPane(new AccountReceivablePanel(m_conn, m_sessionid));
		 internalFrame.setSize(600, 400);
		 m_desktopPane.add( internalFrame );
		 internalFrame.setVisible( true );
		 try {
		 internalFrame.setSelected( true );
		 }
		 catch( Exception exception ) {
		 }
		 setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		 }
		 */
		/*else if(e.getSource() == mi_accountpayable) {
		 setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		 InternalFrame internalFrame = new InternalFrame("Account Payable", this);
		 internalFrame.setContentPane(new AccountPayablePanel(m_conn, m_sessionid));
		 internalFrame.setSize(600, 400);
		 m_desktopPane.add( internalFrame );
		 internalFrame.setVisible( true );
		 try {
		 internalFrame.setSelected( true );
		 }
		 catch( Exception exception ) {
		 }
		 setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		 }*/

		else if(e.getSource() == mi_trialbalance) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Trial Balance", this);
			//internalFrame.setContentPane(new WorksheetDesignPanel(m_conn, m_sessionid));
			internalFrame.setContentPane(new TrialBalanceSettingPanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 400);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_basecurrency) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			BaseCurrencyDlg dlg = new BaseCurrencyDlg(this, m_conn, m_sessionid);
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			dlg.setVisible(true);
		}

		else if(e.getSource() == mi_baseunit) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Base Unit", this);
			internalFrame.setContentPane(new BaseUnitPanel(m_conn, m_sessionid));
			internalFrame.setSize(250, 120);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_signature) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Employee Signature", this);
			internalFrame.setContentPane(new SignaturePanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 725);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_variableAccount) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Variable Account", this);
			internalFrame.setContentPane(new VariableAccountPanel(m_conn, m_sessionid));
			internalFrame.setSize(400, 225);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if (e.getSource()== mi_subsidiryAccount){
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Subsidiary Account Setting", this);
			internalFrame.setContentPane(new AccountSubsidiaryPanel(m_conn, m_sessionid));
			internalFrame.setSize(400, 300);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_journalsetting) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Standard Journal Setting", this);
			internalFrame.setContentPane(new JournalStandardSettingPanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 400);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_beginingbalancesheet) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Beginning Balance Sheet", this);
			internalFrame.setContentPane(new BeginningBalanceSheetEntryPanel(m_conn, m_sessionid));
			internalFrame.setSize(750, 725);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}


		else if(e.getSource() == mi_expensesheetdifferencereceive) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Expense Sheet Differences Receive", this);
			internalFrame.setContentPane(new ReceiveExpenseSheetDifferencePanel(m_conn, m_sessionid));
			internalFrame.setSize(900, 500);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_unitbankcashtransferreceive) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Unit Bank/Cash Transfer Receive", this);
			internalFrame.setContentPane(new ReceiveUnitBankCashTransferPanel(m_conn, m_sessionid));
			internalFrame.setSize(855, 450);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_employeereceivablereceive) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Employee Receivables Receive", this);
			internalFrame.setContentPane(new ReceiveEmployeeReceivablePanel(m_conn, m_sessionid));
			internalFrame.setSize(850, 450);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_loanreceive) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Loan Receive", this);
			internalFrame.setContentPane(new ReceiveLoanPanel(m_conn, m_sessionid));
			internalFrame.setSize(855, 450);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_othersreceive) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Others Receive", this);
			internalFrame.setContentPane(new ReceiveOthersPanel(m_conn, m_sessionid));
			internalFrame.setSize(880, 550);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		}

		else if(e.getSource() == mi_projectcost) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Project Cost Payment", this);
			internalFrame.setContentPane(new PaymentProjectCostPanel(m_conn, m_sessionid));
			internalFrame.setSize(850, 700);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_operationalcost) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Operational Cost Payment", this);
			internalFrame.setContentPane(new PaymentOperationalCostPanel(m_conn, m_sessionid));
			internalFrame.setSize(850, 500);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_expensesheetdifferencepayment) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Expense Sheet Differences Payment", this);
			internalFrame.setContentPane(new PaymentExpenseSheetDifferencePanel(m_conn, m_sessionid));
			internalFrame.setSize(900, 550);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_unitbankcashtransferpayment) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Unit Bank/Cash Transfer Payment", this);
			internalFrame.setContentPane(new PaymentUnitBankCashTransferPanel(m_conn, m_sessionid));
			internalFrame.setSize(855,475);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_employeereceivablepayment) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Employee Receivables Payment", this);
			internalFrame.setContentPane(new PaymentEmployeeReceivablePanel(m_conn, m_sessionid));
			internalFrame.setSize(855, 450);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_bankloanpayment) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Loan Payment", this);
			internalFrame.setContentPane(new PaymentLoanPanel(m_conn, m_sessionid));
			internalFrame.setSize(855, 500);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_otherspayment) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Others Payment", this);
			internalFrame.setContentPane(new PaymentOthersPanel(m_conn, m_sessionid));
			internalFrame.setSize(850, 550);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_paycheque) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Paycheques Verification", this);
			internalFrame.setContentPane(new PayrollPaychequeVerificationPanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 600);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setMaximum(true);
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_mealallowance) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Meal Allowance Verification", this);
			internalFrame.setContentPane(new PayrollMealAllowanceVerificationPanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 600);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setMaximum(true);
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_transallowance) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Transportation Allowance Verification", this);
			internalFrame.setContentPane(new PayrollTransportationAllowanceVerificationPanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 600);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setMaximum(true);
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_overtime) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Overtime Verification", this);
			internalFrame.setContentPane(new PayrollOvertimeVerificationPanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 600);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setMaximum(true);
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_otherallowance) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Other Allowance Verification", this);
			internalFrame.setContentPane(new PayrollOtherAllowanceVerificationPanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 600);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setMaximum(true);
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_insuranceallowance) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Insurance Allowance Verification", this);
			internalFrame.setContentPane(new PayrollInsuranceAllowanceVerificationPanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 600);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setMaximum(true);
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_taxart21) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Tax Art 21 Verification", this);
			internalFrame.setContentPane(new PayrollTaxArt21VerificationPanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 600);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setMaximum(true);
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if (e.getSource()== mi_prsalaryho){
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Salary Payment - Head Office ", this);
			internalFrame.setContentPane(new PayrollSalaryHeadOfficePanel(m_conn, m_sessionid));
			internalFrame.setSize(900, 685);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if (e.getSource()==mi_prsalaryunit){
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Salary Payment - Unit", this);
			internalFrame.setContentPane(new PayrollSalaryUnitPanel(m_conn, m_sessionid));
			internalFrame.setSize(900, 720);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if (e.getSource()==mi_prtaxho){
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Tax Art 21 Payment - Head Office", this);
			internalFrame.setContentPane(new PayrollTaxArt21HeadOfficePanel(m_conn, m_sessionid));
			internalFrame.setSize(900, 720);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if (e.getSource()==mi_prtaxunit){
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Tax Art 21 Payment - Unit", this);
			internalFrame.setContentPane(new PayrollTaxArt21UnitPanel(m_conn, m_sessionid));
			internalFrame.setSize(900, 685);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if (e.getSource()==mi_premployeeinsurance){
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Employee Insurance Payment", this);
			internalFrame.setContentPane(new PayrollEmployeeInsurancePanel(m_conn, m_sessionid));
			internalFrame.setSize(900, 685);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_cashadvanceiou) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Cash Advance I Owe You Payment", this);
			internalFrame.setContentPane(new PaymentCAIOUPanel(m_conn, m_sessionid));
			internalFrame.setSize(850, 510);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_general) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			InternalFrame internalFrame = new InternalFrame("Cash Advance General Payment", this);
			internalFrame.setContentPane(new CashAdvanceGeneralPanel(m_conn, m_sessionid));
			internalFrame.setSize(850, 570);
			m_desktopPane.add( internalFrame );

			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else
			if (e.getSource()==mi_salesadvance){
				setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				InternalFrame internalFrame = new InternalFrame("Sales Advance Receive", this);
				internalFrame.setContentPane(new SalesAdvancedPanel(m_conn, m_sessionid));
				internalFrame.setSize(955, 500);
				m_desktopPane.add( internalFrame );
				internalFrame.setVisible( true );
				try {
					internalFrame.setSelected( true );
				}
				catch( Exception exception ) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			else if (e.getSource()==mi_invoice){
				setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				InternalFrame internalFrame = new InternalFrame("Invoice", this);
				internalFrame.setContentPane(new SalesInvoicePanel(m_conn, m_sessionid));
				internalFrame.setSize(880, 675);
				m_desktopPane.add( internalFrame );
				internalFrame.setVisible( true );
				try {
					internalFrame.setSelected( true );
				}
				catch( Exception exception ) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			else if (e.getSource()==mi_salesrcv){
				setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				InternalFrame internalFrame = new InternalFrame("Account Receivable Received", this);
				internalFrame.setContentPane(new  SalesAccountReceivableReceivePanel(m_conn, m_sessionid));
				internalFrame.setSize(955, 720);
				m_desktopPane.add( internalFrame );
				internalFrame.setVisible( true );
				try {
					internalFrame.setSelected( true );
				}
				catch( Exception exception ) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			else if(e.getSource() == mi_purchasereceipt) {
				setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				InternalFrame internalFrame = new InternalFrame("Purchase Receipt", this);
				internalFrame.setContentPane(new PurchaseReceiptPanel(m_conn, m_sessionid));
				internalFrame.setSize(950, 685);
				m_desktopPane.add( internalFrame );
				internalFrame.setVisible( true );
				try {
					internalFrame.setSelected( true );
				}
				catch( Exception exception ) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			else if(e.getSource() == mi_purchaseaccountpayablepayment) {
				setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				InternalFrame internalFrame = new InternalFrame("Account Payable Payment", this);
				internalFrame.setContentPane(new PurchaseAccountPayablePaymentPanel(m_conn, m_sessionid));
				internalFrame.setSize(950, 710);
				m_desktopPane.add( internalFrame );
				internalFrame.setVisible( true );
				try {
					internalFrame.setSelected( true );
				}
				catch( Exception exception ) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		//end of bagian
			else if(e.getSource() == mi_expensesheet) {
				setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				InternalFrame internalFrame = new InternalFrame("Expense Sheet", this);
				internalFrame.setContentPane(new ExpenseSheetPanel(m_conn, m_sessionid));
				internalFrame.setSize(1130, 725);
				m_desktopPane.add( internalFrame );
				internalFrame.setVisible( true );
				try {
					internalFrame.setSelected( true );
				}
				catch( Exception exception ) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			else if(e.getSource() == mi_mjstandard) {
				setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				InternalFrame internalFrame = new InternalFrame("Memorial Journal Standard", this);
				internalFrame.setContentPane(new MJStandardPanel(m_conn, m_sessionid));
				internalFrame.setSize(850, 725);
				m_desktopPane.add( internalFrame );
				internalFrame.setVisible( true );
				try {
					internalFrame.setSelected( true );
				}
				catch( Exception exception ) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			else if(e.getSource() == mi_mjnonstandard) {
				setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				InternalFrame internalFrame = new InternalFrame("Memorial Journal Non Standard", this);
				internalFrame.setContentPane(new MJNonStandardPanel(m_conn, m_sessionid));
				internalFrame.setSize(850, 725);
				m_desktopPane.add( internalFrame );
				internalFrame.setVisible( true );
				try {
					internalFrame.setMaximum(true);
					internalFrame.setSelected( true );
				}
				catch( Exception exception ) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			else if(e.getSource() == mi_closing) {
				setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				InternalFrame internalFrame = new InternalFrame("Manage Transaction Period", this);
				internalFrame.setContentPane(new ManageTransactionPeriodPanel(m_conn, m_sessionid));
				internalFrame.setSize(545, 400);
				m_desktopPane.add( internalFrame );
				internalFrame.setVisible( true );
				try {
					//internalFrame.setMaximum(true);
					internalFrame.setSelected( true );
				}
				catch( Exception exception ) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}


			else if(e.getSource() == mi_projectregistration) {
				setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				InternalFrame internalFrame = new InternalFrame("Project Registration", this);
				internalFrame.setContentPane(new ProjectListPanel(m_conn, m_sessionid));
				internalFrame.setSize(1020, 725);
				m_desktopPane.add( internalFrame );
				internalFrame.setVisible( true );
				try {
					internalFrame.setSelected( true );
				}
				catch( Exception exception ) {
				}

				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			else if(e.getSource() == mi_projectmonitoring) {
				setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				InternalFrame internalFrame = new InternalFrame("Project Monitoring", this);
				internalFrame.setContentPane(new ProjectMonitoringPanel(m_conn, m_sessionid));
				internalFrame.setSize(1000, 725);
				m_desktopPane.add(internalFrame);
				internalFrame.setVisible( true );
				try {
					internalFrame.setSelected( true );
				}
				catch( Exception exception ) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		//posting
			else if (e.getSource() == mi_posting) {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				InternalFrame internalFrame = new InternalFrame("Transaction Posting", this);
				internalFrame.setContentPane(new PostingPanel(m_conn,m_sessionid));
				internalFrame.setSize(1000, 600);
				m_desktopPane.add(internalFrame);
				internalFrame.setVisible(true);
				try {
					internalFrame.setSelected(true);
				} catch (Exception exception) {
				}
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			else if (e.getSource() == mi_neraca) {
				balanceReport();
			}
			else if (e.getSource() == mi_labarugi){
				incomeStatement();
			}
			else if( (e.getSource() == mi_alirankas)){
				indirectCashFlow();
			}
			else if (e.getSource() == mi_genLedger){
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				InternalFrame internalFrame = new InternalFrame(
						"General Ledger", this);
				internalFrame.setContentPane(new GenLedgerPanel(
						m_conn, m_sessionid));
				internalFrame.setSize(400, 150);
				m_desktopPane.add(internalFrame);

				internalFrame.setVisible(true);
				try {
					internalFrame.setMaximum(true);
					internalFrame.setSelected(true);
				} catch (Exception exception) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			else if (e.getSource() == mi_accReceivable){
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				InternalFrame internalFrame = new InternalFrame(
						"Account Receivable", this);
				internalFrame.setContentPane(new AccountReceivableReport(
						m_conn, m_sessionid));
				internalFrame.setSize(400, 150);
				m_desktopPane.add(internalFrame);

				internalFrame.setVisible(true);
				try {
					internalFrame.setMaximum(true);
					internalFrame.setSelected(true);
				} catch (Exception exception) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			else if (e.getSource() == mi_subsidiaryLedger){
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				InternalFrame internalFrame = new InternalFrame(
						"Subsidiary Ledger", this);
				internalFrame.setContentPane(new SubsidiaryLedgerReport(
						m_conn, m_sessionid));
				internalFrame.setSize(400, 150);
				m_desktopPane.add(internalFrame);

				internalFrame.setVisible(true);
				try {
					internalFrame.setMaximum(true);
					internalFrame.setSelected(true);
				} catch (Exception exception) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			else if (e.getSource() == mi_empReceivable){
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				InternalFrame internalFrame = new InternalFrame(
						"Employee Receivable", this);
				internalFrame.setContentPane(new EmployeeReceivableReport(
						m_conn, m_sessionid));
				internalFrame.setSize(400, 150);
				m_desktopPane.add(internalFrame);

				internalFrame.setVisible(true);
				try {
					internalFrame.setMaximum(true);
					internalFrame.setSelected(true);
				} catch (Exception exception) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			else if (e.getSource() == mi_cashAdvance){
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				InternalFrame internalFrame = new InternalFrame(
						"Cash Advance", this);
				internalFrame.setContentPane(new CashAdvanceReport(
						m_conn, m_sessionid));
				internalFrame.setSize(400, 150);
				m_desktopPane.add(internalFrame);

				internalFrame.setVisible(true);
				try {
					internalFrame.setMaximum(true);
					internalFrame.setSelected(true);
				} catch (Exception exception) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			else if (e.getSource() == mi_accountPayable){
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				InternalFrame internalFrame = new InternalFrame(
						"Account Payable", this);

				internalFrame.setContentPane(new AccountPayableReport(
						m_conn, m_sessionid));
				internalFrame.setSize(400, 150);
				m_desktopPane.add(internalFrame);

				internalFrame.setVisible(true);
				try {
					internalFrame.setMaximum(true);
					internalFrame.setSelected(true);
				} catch (Exception exception) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			else if (e.getSource() == mi_sales){
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				InternalFrame internalFrame = new InternalFrame(
						"Sales", this);

				internalFrame.setContentPane(new SalesReport(
						m_conn, m_sessionid));
				internalFrame.setSize(400, 150);
				m_desktopPane.add(internalFrame);

				internalFrame.setVisible(true);
				try {
					internalFrame.setMaximum(true);
					internalFrame.setSelected(true);
				} catch (Exception exception) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			else if (e.getSource() == mi_purchase){
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				InternalFrame internalFrame = new InternalFrame(
						"Purchase", this);

				internalFrame.setContentPane(new PurchaseReport(
						m_conn, m_sessionid));
				internalFrame.setSize(400, 150);
				m_desktopPane.add(internalFrame);

				internalFrame.setVisible(true);
				try {
					internalFrame.setMaximum(true);
					internalFrame.setSelected(true);
				} catch (Exception exception) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			else if (e.getSource() == mi_directCashflow){
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				InternalFrame internalFrame = new InternalFrame(
						"Direct Cashflow", this);

				internalFrame.setContentPane(new DirectCashflowReport(
						m_conn, m_sessionid));
				internalFrame.setSize(400, 150);
				m_desktopPane.add(internalFrame);

				internalFrame.setVisible(true);
				try {
					internalFrame.setMaximum(true);
					internalFrame.setSelected(true);
				} catch (Exception exception) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			else if (e.getSource() == mi_trialBalanceRpt){
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				InternalFrame internalFrame = new InternalFrame(
						"Trial Balance", this);

				internalFrame.setContentPane(new TrialBalanceReport(
						m_conn, m_sessionid));
				internalFrame.setSize(400, 150);
				m_desktopPane.add(internalFrame);

				internalFrame.setVisible(true);
				try {
					internalFrame.setMaximum(true);
					internalFrame.setSelected(true);
				} catch (Exception exception) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			else if (e.getSource() == mi_projectcostrpt){
				setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				InternalFrame internalFrame = new InternalFrame("Project Cost", this);
				// set panel project list panel
				internalFrame.setContentPane(new ProjectCostPanel(m_conn,m_sessionid));
				internalFrame.setSize(700, 400);
				m_desktopPane.add( internalFrame );
				internalFrame.setVisible( true );
				try {
					internalFrame.setMaximum(true);
					internalFrame.setSelected( true );
				}
				catch( Exception exception ) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			else if (e.getSource() == mi_projectProfitability){
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				InternalFrame internalFrame = new InternalFrame(
						"Project Profitability", this);

				internalFrame.setContentPane(new ProjectProfitabilityPanel(
						m_conn, m_sessionid));
				internalFrame.setSize(400, 150);
				m_desktopPane.add(internalFrame);

				internalFrame.setVisible(true);
				try {
					internalFrame.setMaximum(true);
					internalFrame.setSelected(true);
				} catch (Exception exception) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			else if (e.getSource() == mi_paychequerpt){
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				InternalFrame internalFrame = new InternalFrame(
						"Paycheque", this);

				internalFrame.setContentPane(new RptPaycheques(
						m_conn, m_sessionid));
				internalFrame.setSize(400, 150);
				m_desktopPane.add(internalFrame);

				internalFrame.setVisible(true);
				try {
					internalFrame.setMaximum(true);
					internalFrame.setSelected(true);
				} catch (Exception exception) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			else if (e.getSource() == mi_departmentcost){
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				InternalFrame internalFrame = new InternalFrame(
						"Department Cost", this);

				internalFrame.setContentPane(new DepartmentCostPanel(
						m_conn, m_sessionid));
				internalFrame.setSize(400, 150);
				m_desktopPane.add(internalFrame);

				internalFrame.setVisible(true);
				try {
					internalFrame.setMaximum(true);
					internalFrame.setSelected(true);
				} catch (Exception exception) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			else if (e.getSource() == mi_projectprofitabilitydetail){
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				InternalFrame internalFrame = new InternalFrame(
						"Project Profitability Detail", this);

				internalFrame.setContentPane(new ProjectProfitabilityDetailPanel(
						m_conn, m_sessionid));
				internalFrame.setSize(400, 150);
				m_desktopPane.add(internalFrame);

				internalFrame.setVisible(true);
				try {
					internalFrame.setMaximum(true);
					internalFrame.setSelected(true);
				} catch (Exception exception) {
				}
				setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			else if (e.getSource() == mi_hlp_overview) {
				shell(HELP_FILENAME_OVERVIEW);
			}
			else if (e.getSource() == mi_hlp_receive) {
				shell(HELP_FILENAME_RECEIVE);
			}
			else if (e.getSource() == mi_hlp_payment) {
				shell(HELP_FILENAME_PAYMENT);
			}
			else if (e.getSource() == mi_hlp_salespurchase) {
				shell(HELP_FILENAME_SALES_PURCHASE);
			}
			else if (e.getSource() == mi_hlp_payroll) {
				shell(HELP_FILENAME_PAYROLL);
			}
			else if (e.getSource() == mi_hlp_expensesheet) {
				shell(HELP_FILENAME_EXPENSE_SHEET);
			}
			else if (e.getSource() == mi_hlp_memorial) {
				shell(HELP_FILENAME_MEMORIAL);
			}
			else if (e.getSource() == mi_hlp_project) {
				shell(HELP_FILENAME_PROJECT);
			}
			else if (e.getSource() == mi_hlp_master) {
				shell(HELP_FILENAME_MASTER);
			}
	}

	private void shell(String filename) {
		String fullFilename = HELP_FOLDER + filename;
		try {
			Runtime.getRuntime().exec(
					new String[] { "cmd.exe", "/c", fullFilename });
		} catch (IOException e) {
			try {
				Runtime.getRuntime().exec(
						new String[] { "command.exe", "/c", fullFilename });
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this, "Failed to open help files: " + fullFilename);
			}
		}
	}

	private void indirectCashFlow() {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		InternalFrame internalFrame = new InternalFrame(
				"Indirect Cash Flow Statement", this);

		internalFrame.setContentPane(new IndirectCashFlowReportPanel(m_conn, m_sessionid));
		internalFrame.setSize(400, 150);
		m_desktopPane.add(internalFrame);

		internalFrame.setVisible(true);
		try {
			internalFrame.setMaximum(true);
			internalFrame.setSelected(true);
		} catch (Exception exception) {
		}
		setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	void balanceReport()
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		InternalFrame internalFrame = new InternalFrame(
				"Balance Sheet", this);

		internalFrame.setContentPane(new BalanceSheetReportPanel(m_conn, m_sessionid));
		internalFrame.setSize(400, 150);
		m_desktopPane.add(internalFrame);

		internalFrame.setVisible(true);
		try {
			internalFrame.setMaximum(true);
			internalFrame.setSelected(true);
		} catch (Exception exception) {
		}
		setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		/*BalanceReportDesign.TestBalanceReport test = new BalanceReportDesign.TestBalanceReport();
		 test.conn = this.m_conn;
		 test.sqlSap = new AccountingSQLSAP();
		 try {
		 test.createMockupGraph();
		 test.exec();
		 } catch (SQLException e) {
		 e.printStackTrace();
		 }*/
	}
	void incomeStatement()
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		InternalFrame internalFrame = new InternalFrame(
				"Income Statement", this);

		internalFrame.setContentPane(new IncomeStatementReportPanel(m_conn, m_sessionid));
		internalFrame.setSize(400, 150);
		m_desktopPane.add(internalFrame);

		internalFrame.setVisible(true);
		try {
			internalFrame.setMaximum(true);
			internalFrame.setSelected(true);
		} catch (Exception exception) {
		}
		setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		/*IncomeReportDesign.TestIncomeReport test = new IncomeReportDesign.TestIncomeReport();
		 test.conn = this.m_conn;
		 test.sqlSap = new AccountingSQLSAP();
		 try {
		 test.createMockupGraph();
		 test.exec();
		 } catch (SQLException e) {
		 e.printStackTrace();
		 }*/

	}
	public void createFrameForPanel(JPanel panel, String frameTitle)
	{
		setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		InternalFrame internalFrame = new InternalFrame(frameTitle, this);
		internalFrame.setContentPane(panel);
		internalFrame.setSize(500, 400);
		m_desktopPane.add( internalFrame );
		internalFrame.setVisible( true );
		try {
			internalFrame.setSelected( true );
		}
		catch( Exception exception ) {
		}

		setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	private static MainFrame singleton;

	public static void staticCreateFrameForPanel(JPanel panel,String title)
	{
		singleton.createFrameForPanel(panel,title);
	}

	public static void main(String[] args) {
		try {
			javax.swing.UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.Plastic3DLookAndFeel");
			//javax.swing.UIManager.setLookAndFeel("com.digitprop.tonic.TonicLookAndFeel");
			javax.swing.plaf.FontUIResource f = new javax.swing.plaf.FontUIResource("Tahoma", 0, 11);
			java.util.Enumeration keys = javax.swing.UIManager.getDefaults().keys();
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				Object value = javax.swing.UIManager.get(key);
				if (value instanceof javax.swing.plaf.FontUIResource)
					javax.swing.UIManager.put(key, f);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		singleton = new MainFrame();
	}
}
