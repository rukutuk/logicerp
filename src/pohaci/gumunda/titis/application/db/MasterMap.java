package pohaci.gumunda.titis.application.db;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.Iterator;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningAccountPayable;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningAccountReceivable;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalance;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalanceSheetEntry;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBankDetail;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningCashAdvance;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningCashDetail;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningEmpReceivable;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningEsDiff;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningLoan;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningWorkInProgress;
import pohaci.gumunda.titis.accounting.cgui.CompanyLoan;
import pohaci.gumunda.titis.accounting.cgui.CreditorList;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.Journal;
import pohaci.gumunda.titis.accounting.cgui.JournalStandard;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardAccount;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSetting;
import pohaci.gumunda.titis.accounting.cgui.Signature;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.cgui.TransactionDetail;
import pohaci.gumunda.titis.accounting.cgui.TransactionTemplateEntity;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayroll;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.hrm.cgui.Overtime;
import pohaci.gumunda.titis.hrm.cgui.OvertimeMultiplier;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21Submit;
import pohaci.gumunda.titis.project.cgui.Customer;
import pohaci.gumunda.titis.project.cgui.Partner;
import pohaci.gumunda.titis.project.cgui.ProjectContract;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import pohaci.gumunda.titis.accounting.entity.*;
import pohaci.gumunda.titis.accounting.entity.reportdesign.BalanceSheetDesign;
import pohaci.gumunda.titis.accounting.entity.reportdesign.IncomeStatementDesign;
import pohaci.gumunda.titis.accounting.entity.reportdesign.IndirectCashFlowStatementDesign;

public class MasterMap {
	static Hashtable map; // key: clazz.getName, value: GenericMapper
	static Hashtable lookupMap = new Hashtable();
	public static Object queryForEntity(Class clazz, Object id, Connection c){
		GenericMapper m=obtainMapperFor(clazz);
		m.setActiveConn(c);
		return m.doSelectByIndex(id);
	}

	static void setMapper(GenericMapper m, Class clazz){
		if (map.containsKey(clazz.getName()))
			map.remove(clazz.getName());
		map.put(clazz.getName(),m);
	}

	public static GenericMapper obtainMapperFor(Class clazz){
		if (map.containsKey(clazz.getName()))
			return (GenericMapper) (map.get(clazz.getName()));
		System.err.println("Creating on-demand mapper for "+ clazz.getName());
		return new GenericMapper(clazz);
	}

	static {
		map = new Hashtable();
		addMap0();
		addMap1();
		addMap2();
		addMapHRM();
		checkMaps();
	}

	private static void checkMaps(){
		for (Iterator iter = map.values().iterator(); iter.hasNext();) {
			GenericMapper element = (GenericMapper ) iter.next();
			Class c = element.mapping.clazz;
			if (TransactionTemplateEntity.class.isAssignableFrom(c)){
				String key = "trans";
				if (!element.mapping.fields.containsKey(key))
					System.err.println("association "+ DataMapping.stripPkg(c.getName())+"."+key+" not mapped");
				key = "empApproved";
				if (!element.mapping.fields.containsKey(key))
					System.err.println("association "+ DataMapping.stripPkg(c.getName())+"."+key+" not mapped");
				key = "empReceived";
				if (!element.mapping.fields.containsKey(key))
					System.err.println("association "+ DataMapping.stripPkg(c.getName())+"."+key+" not mapped");
				key = "empOriginator";
				if (!element.mapping.fields.containsKey(key))
					System.err.println("association "+ DataMapping.stripPkg(c.getName())+"."+key+" not mapped");
			}
		}
	}

	private static void addMapHRM() {
		addMapOvertime();
		addMapOvertimeMultiplier();
	}

	private static void addMapOvertimeMultiplier() {
		addMap(OvertimeMultiplier.class).mapping.removeField("typeAsString")
		.useMultiArgConstructor(
				new String[] { "index", "type", "hourMin", "hourMax",
				"multiplier" });
	}

	/**
	 *
	 */
	private static void addMapOvertime() {
		addMap(Overtime.class)
		.mapping
		.field("year")
		.setFieldName("periodYear")
		.up()
		.field("month")
		.setFieldName("periodMonth")
		.up()
		.associate("employee")
		.up()
		.associate("multiplier")
		.useRefLookup()
		.up();
	}

	private static GenericMapper addMap(Class class1) {
		GenericMapper gm;
		map.put(class1.getName(),
				gm = new GenericMapper(class1));
		return gm;

	}
	private static void addMap1() { /*----Popi ---*/
		pmtCAOthers();
		pmtCAProject();
		pmtCAIOUProjectSettled();
		pmtCAIOUProjectInstall();
		pmtCAIOUProjectReceive();
		pmtCAIOUProject();
		pmtProjectCost();
		pmtProjectCostDetail();
		pmtESDiff();
		pmtEmpReceivable();
		pmtOperationalCost();
		pmtOperationalCostDetail();
		pmtCAIOUOthers();
		pmtCAIOUOthersInstall();
		pmtCAIOUOthersSettled();
		pmtCAIOUOthersReceive();

		payrollPmtEmpInsDet();
		payrollPmtTax21HoDet();

		rcvESDiff();
		rcvUnitBankCashTrns();
		rcvLoan();
		rcvOthers();
		rcvOthersDetail();
		rcvEmpReceivable();
		//Ini untuk expense sheet
		expenseSheet();
		expenseSheetDetail();

	}
	private static void expenseSheet(){
		addMap(ExpenseSheet.class)
		.mapping
		.removeField("iouEsProjectType")
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		.associate("project")
		//.useRefLookup()
		.up()
		.associate("pmtCaIouProjectSettled")
		//.useRefLookup()
		.up()
		.associate("pmtCaProject")
		//.useRefLookup()
		.up()
		.associate("pmtCaIouOthersSettled")
		//.useRefLookup()
		.up()
		.associate("pmtCaOthers")
		//.useRefLookup()
		.up()
		.associate("department")
		.useRefLookup()
		.up()
		.associate("beginningBalance")
		.up()
		.associate("esOwner")
		.useRefLookup()
		.up()
		.setTableName("ExpenseSheet")
		.associate("trans")
		.up()
		;
	}

	private static void expenseSheetDetail() {
		addMap( ExpenseSheetDetail.class)
		.mapping
		.setTableName("ExpenseSheetDetail")
		.associate("expenseSheet")
		//.useRefLookup()
		.up()
		.associate("account")
		//.useRefLookup()
		.up()
		.associate("department")
		.useRefLookup()
		.up()
		;
	}

	private static void payrollPmtEmpInsDet() {
		addMap(PayrollPmtEmpInsDet.class)
		.mapping
		.associate("payrollPmtEmpIns")
		//.useRefLookup()
		.up()
		.associate("account")
		//.useRefLookup()
		.up()
		.associate("trans")
		.up();
	}

	private static void payrollPmtTax21HoDet() {
		addMap(PayrollPmtTax21HoDet.class)
		.mapping
		.associate("payrollPmtTax21Ho")
		//.useRefLookup()
		.up()
		.associate("account")
		//.useRefLookup()
		.up()
		.associate("trans")
		.up();
	}

	private static void pmtCAIOUOthersSettled() {
		addMap( PmtCAIOUOthersSettled.class)
		.mapping
		.setTableName("PmtCAIOUOthersSettled")
		.removeField("exchangeRate")
		.associate("pmtcaiouothers")
		//.useRefLookup()
		.up()
		.associate("trans")
		//.useRefLookup()
		.up()
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		;
	}

	private static void pmtCAIOUOthersInstall() {
		addMap( PmtCAIOUOthersInstall.class)
		.mapping
		.setTableName("PmtCAIOUOthersInstall")
		.removeField("exchangeRate")
		.associate("pmtcaiouothers")
		//.useRefLookup()
		.up()
		.associate("trans")
		//.useRefLookup()
		.up()
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		;
	}

	private static void pmtCAIOUOthersReceive() {
		addMap( PmtCAIOUOthersReceive.class)
		.mapping
		.setTableName("PmtCAIOUOthersReceive")
		.removeField("bankAccount")
		.removeField("exchangeRate")
		.associate("pmtcaiouothers")
		//.useRefLookup()
		.up()
		.associate("trans")
		//.useRefLookup()
		.up()
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		;
	}

	private static void pmtCAIOUOthers() {
		addMap( PmtCAIOUOthers.class)
		.mapping
		.setTableName("PmtCAIOUOthers")
		.removeField("exchangeRate")
		.removeField("submitDate")
		.associate("department")
		.useRefLookup()
		.up()
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("payTo")
		.useRefLookup()
		.up()
		.associate("unit")
		.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		//.associate("trans")
		//.useRefLookup()
		//.up()
		;
	}

	private static void pmtOperationalCostDetail() {
		addMap( PmtOperationalCostDetail.class)
		.mapping
		.associate("pmtOperationalCost")
		//.useRefLookup()
		.up()
		.associate("account")
		//.useRefLookup()
		.up()
		.associate("department")
		.useRefLookup()
		.up()
		;
	}

	private static void pmtOperationalCost() {
		addMap( PmtOperationalCost.class)
		.mapping
		.setTableName("PmtOperationalCost")
		.removeField("amount")
		.removeField("sourceBank")
		.associate("unit")
		.useRefLookup()
		.up()
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("department")
		.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		/*.associate("payTo")
		 .useRefLookup()
		 .up()*/
		.associate("trans")
		//.useRefLookup()
		.up();
	}

	private static void pmtEmpReceivable() {
		addMap( PmtEmpReceivable.class)
		.mapping
		.setTableName("PmtEmpReceivable")
		.removeField("sourceBank")
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("payTo")
		//.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		.associate("unit")
		.useRefLookup()
		.up()
		.associate("trans")
		.up();
	}

	private static DataMapping pmtESDiff() {
		return addMap(PmtESDiff.class)
		.mapping
		.removeField("sourceBank")
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("payTo")
		.useRefLookup()
		.up()
		.associate("esNo")
		.up()
		.associate("beginningBalance")
		.up()
		.associate("unit")
		.useRefLookup()
		.up()
		.associate("trans")
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		;
	}

	private static void pmtProjectCostDetail() {
		addMap( PmtProjectCostDetail.class)
		.mapping
		.setTableName("PmtProjectCostDetail")
		.associate("pmtProjectCost")
		//.useRefLookup()
		.up()
		.associate("account")
		//.useRefLookup()
		.up();
	}

	private static void pmtProjectCost() {
		addMap( PmtProjectCost.class)
		.mapping.setTableName("PmtProjectCost")
		.removeField("amount")
		.removeField("sourceBank")
		.associate("trans")
		.up()
		.associate("cashAccount")
		.up()
		.associate("bankAccount")
		.up()
		.associate("empApproved")
		.up()
		.associate("empOriginator")
		.up()
		.associate("empReceived")
		.up()
		.associate("unit")
		.useRefLookup()
		.up()
		.associate("project")
		//.useRefLookup()
		.up()
		//.removeField("total")
		;
	}

	private static void rcvEmpReceivable() {
		addMap( RcvEmpReceivable.class)
		.mapping.setTableName("RcvEmpReceivable")
		.removeField("bank")
		.removeField("paymentSource")
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("beginningBalance")
		.up()
		.associate("empReceivable")
		//.useRefLookup()
		.up()
		.associate("unit")
		.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("trans")
		// masak semua transaksi diload? berat euy..
		//.useRefLookup()
		.up();
	}

	private static void rcvOthers() {
		addMap(RcvOthers.class)
		.mapping.setTableName("RcvOthers")
		.removeField("sourceBank")
		.removeField("amount")
		.removeField("rcvOthersDetail")
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("journal")
		//.useRefLookup()
		.up()
		.associate("trans")
		//.useRefLookup()
		.up()
		.associate("department")
		.useRefLookup()
		.up()
		.associate("unit")
		.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up();
	}

	private static void rcvOthersDetail() {
		addMap( RcvOthersDetail.class)
		.mapping.setTableName("RcvOthersDetail")
		.associate("receiveothers")
		//.useRefLookup()
		.up()
		.associate("account")
		//.useRefLookup()
		.up();
	}

	private static void rcvLoan() {
		addMap( RcvLoan.class)
		.mapping.setTableName("RcvLoan")
		.removeField("sourceBank")
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("unit")
		.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("trans")
		//.useRefLookup()
		.up()
		.associate("companyLoan")
		//.useRefLookup()
		.up();
	}

	private static void rcvUnitBankCashTrns() {
		addMap(RcvUnitBankCashTrns.class)
		.mapping.setTableName("RcvUnitBankCashTrns")
		.removeField("sourceBank")
		.associate("trans")
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		//.associate("rcvUnitCode")
		//.useRefLookup()
		//.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("rcvUnitCode")
		.useRefLookup()
		.up();
	}

	private static void rcvESDiff() {
		addMap(RcvESDiff.class)
		.mapping.setTableName("RCVESDIFF")
		.removeField("bank")
		.removeField("paymentSource")
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("esNo")
		//.useRefLookup()
		.up()
		.associate("beginningBalance")
		.up()
		.associate("unit")
		.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("trans")
		//.useRefLookup()
		.up();
	}

	private static void pmtCAIOUProject() {
		addMap(PmtCAIOUProject.class)
		.mapping
		.setTableName("PmtCAIOUProject")
		.removeField("exchangeRate")
		.removeField("submitDate")
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("department")
		.useRefLookup()
		.up()
		.associate("payTo")
		.useRefLookup()
		.up()
		.associate("unit")
		.useRefLookup()
		.up()
		.associate("project")
		//.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		//.associate("trans")
		//.useRefLookup()
		//.up()
		;
	}

	private static void pmtCAIOUProjectInstall() {
		addMap(PmtCAIOUProjectInstall.class)
		.mapping
		.setTableName("PmtCAIOUProjectInstall")
		.removeField("exchangeRate")
		.associate("pmtcaiouproject")
		//.useRefLookup()
		.up()
		.associate("trans")
		//.useRefLookup()
		.up()
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up();
	}

	private static void pmtCAIOUProjectReceive() {
		addMap( PmtCAIOUProjectReceive.class)
		.mapping
		.setTableName("PmtCAIOUProjectReceive")
		.removeField("bankAccount")
		.removeField("exchangeRate")
		.associate("pmtcaiouproject")
		//.useRefLookup()
		.up()
		.associate("trans")
		//.useRefLookup()
		.up()
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up();
	}

	private static void pmtCAIOUProjectSettled() {
		addMap(PmtCAIOUProjectSettled.class)
		.mapping
		.setTableName("PmtCAIOUProjectSettled")
		.removeField("exchangeRate")
		.removeField("transactiondate")
		.associate("trans")
		//.useRefLookup()
		.up()
		.associate("pmtcaiouproject")
		//.useRefLookup()
		.up()
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up();
	}

	private static void pmtCAProject() {
		addMap(PmtCAProject.class)
		.mapping
		.setTableName("PmtCAProject")
		.removeField("bank")
		.removeField("sourceBank")
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("unit")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		.associate("payTo")
		.useRefLookup()
		.up()
		.associate("trans")
		//.useRefLookup()
		.up()
		.associate("project")
		//.useRefLookup()
		.up();
	}

	private static void pmtCAOthers() {
		addMap(PmtCAOthers.class)
		.mapping.setTableName("PmtCAOthers")
		.removeField("bank")
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("unit")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		.associate("payTo")
		.up()
		.associate("department")
		.useRefLookup()
		.up()
		.associate("trans")
		//.useRefLookup()
		.up();
	}

	private static void addMap2() { /*----Nunung ---*/
		addMap(Activity.class)
		.mapping
		.useMultiArgConstructor(new String[]{
				"index","code","name","description"
		});

		addMap(Organization.class)
		.mapping
		.useMultiArgConstructor(new String[]{
				"index","code","name","description"});

		addMap(PmtLoan.class)
		.mapping
		.removeField("sourceBank")
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("loanReceipt")
		//.useRefLookup()
		.up()
		.associate("beginningBalance")
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		.associate("trans")
		//.useRefLookup()
		.up()
		.associate("payTo")
		.useRefLookup()
		.up()
		.associate("unit")
		.useRefLookup()
		.up();

		addMap(PmtOthers.class)
		.mapping
		.removeField("amount")
		.removeField("rcvPmtOthers")
		.removeField("sourceBank")
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("journal")
		//.useRefLookup()
		.up()
		.associate("unit")
		.useRefLookup()
		.up()
		.associate("department")
		.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("trans")
		//.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up();

		addMap(PmtOthersDetail.class)
		.mapping
		.associate("pmtOthers")
		//.useRefLookup()
		.up()
		.associate("account")
		//.useRefLookup()
		.up()
		.associate("department")
		.useRefLookup()
		.up();

		addMap(SalesAdvance.class)
		.mapping
		.removeField("exchangeRate")
		.removeField("currency")
		.associate("trans")
		.up()
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("project")
		//.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		.associate("unit")
		.useRefLookup()
		.up();

		addMap(SalesReceived.class)
		.mapping.setTableName("SALESARRECEIVED")
		.removeField("currency")
		.removeField("exchangeRate")
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		.associate("unit")
		.useRefLookup()
		.up()
		.associate("trans")
		//.useRefLookup()
		.up()
		.associate("invoice")
		.up()
		.associate("beginningBalance")
		.up();

		addMap(SalesInvoice.class)
		.mapping.setTableName("SALESINVOICE")
		//.removeField("NMarital")
		//.removeField("vatAmount")
		.removeField("currency")
		.removeField("dateApproved")
		.removeField("jobTitleReceived")
		.removeField("jobTitleOriginator")
		.removeField("jobTitleApproved")
		.removeField("dateOriginator")
		.removeField("dateReceived")
		.removeField("exchangeRate")
		.removeField("salesItem")
		.associate("trans")
		.up()
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("project")
		//.useRefLookup()
		.up()
		.associate("empAuthorize")
		.useRefLookup()
		.up()
		.associate("unit")
		.useRefLookup()
		.up()
		.associate("customer")
		//.useRefLookup()
		.up()
		.associate("department")
		.useRefLookup()
		.up()
		.associate("activity")
		.useRefLookup()
		.up()
		/*.associate("empApproved")
		 .useRefLookup()
		 .up()
		 .associate("empReceived")
		 .useRefLookup()
		 .up()
		 .associate("empOriginator")
		 .useRefLookup()
		 .up()*/
		.associate("salesAdvance")
		//.useRefLookup()
		.up()
		.associate("beginningBalance")
		.up();

		addMap(SalesItem.class);

		addMap(SalesItemDetail.class);

		addMap(Partner.class)
		.mapping.setTableName("PARTNER")
		.field("postCode")
		.setFieldName("POSTALCODE").up()
		.field("address")
		.setFieldName("STREET").up()
		.removeField("companyToString")
		.useMultiArgConstructor(new String[] {
				"index","code", "name", "address", "city",
				"postCode", "province", "country",
				"phone1", "phone2", "fax1", "fax2",
				"email", "website"
		});


		addMap(PurchaseReceipt.class)
		.mapping
		.removeField("currency")
		.removeField("exchangeRate")
		.associate("supplier")
		//.useRefLookup()
		.up()
		.associate("trans")
		//.useRefLookup()
		.up()
		.associate("project")
		//.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		.associate("unit")
		.useRefLookup()
		.up();

		addMap(PurchaseReceiptItem.class)
		.mapping
		.associate("purchaseReceipt")
		//.useRefLookup()
		.up();
		/*.associate("unit")
		 .useRefLookup()
		 .up();*/

		addMap(PurchaseApPmt.class)
		.mapping
		.removeField("currency")
		.removeField("exchangeRate")
		.removeField("sourceBank")
		.associate("cashAccount")
		.useRefLookup()
		.up()
		.associate("trans")
		//.useRefLookup()
		.up()
		.associate("beginningBalance")
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("purchaseReceipt")
		//.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		.associate("unit")
		.useRefLookup()
		.up();

		addMap(AccountPayable.class)
		.mapping
		.associate("partner")
		//.useRefLookup()
		.up()
		.associate("account")
		//.useRefLookup()
		.up();


		addMap(PayrollPmtSlryHo.class)
		.mapping
		.removeField("currency")
		.removeField("exchangeRate")
		.removeField("sourceBank")
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		.associate("trans")

		.up()
		.associate("unit")
		.useRefLookup()
		.up();

		// belum bisa ditest karena datanya masih belum complete
		addMap(PayrollPmtSlryHoPay.class)
		//	 Class TRANSACTIONPOSTED tanya keirwan
		.mapping
		.associate("payrollPmtSlryHo")
		//.useRefLookup()
		.up();


		addMap(PayrollPmtSlryUnit.class)
		.mapping
		.removeField("currency")
		.removeField("exchangeRate")
		.removeField("sourceBank")
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("unit")
		.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("trans")
		//.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		.associate("department")
		.useRefLookup()
		.up();

		addMap(PayrollPmtSlryUnitDet.class)
		.mapping
		.associate("payrollPmtSlryUnit")
		//.useRefLookup()
		.up()
		.associate("account")
		//.useRefLookup()
		.up()
		.associate("department")
		.useRefLookup()
		.up();;

		addMap(PayrollPmtSlryHoDet.class)
		.mapping
		.associate("payrollPmtSlryHo")
		.up()
		.associate("trans")
		.up()
		.associate("account")
		//.useRefLookup()
		.up();


		addMap(PayrollPmtTax21Ho.class)
		.mapping
		.removeField("currency")
		.removeField("exchangeRate")
		.removeField("sourceBank")
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		.associate("trans")
		//.useRefLookup()
		.up()
		.associate("unit")
		.useRefLookup()
		.up();

		// 	belum bisa ditest karena datanya masih belum complete
		addMap(PayrollPmtTax21HoPay.class)
		.mapping
		//	 associate dengan TRANSACTIONPOSTED
		.associate("payrollPmtTax21Ho")
		//.useRefLookup()
		.up();

		addMap(PayrollPmtTax21Unit.class)
		.mapping
		.removeField("currency")
		.removeField("exchangeRate")
		.removeField("sourceBank")
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("unit")
		.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("trans")
		//.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		.associate("department")
		.useRefLookup()
		.up();

		addMap(PayrollPmtTax21UnitDet.class)
		.mapping
		.associate("payrollPmtTax21Unit")
		//.useRefLookup()
		.up()
		.associate("account")
		//.useRefLookup()
		.up()
		.associate("department")
		.useRefLookup()
		.up();

		addMap(PayrollPmtEmpInsurance.class)
		.mapping
		.removeField("sourceBank")
		.removeField("currency")
		.removeField("exchangeRate")
		.associate("cashAccount")
		//.useRefLookup()
		.up()
		.associate("bankAccount")
		//.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("empReceived")
		.useRefLookup()
		.up()
		.associate("trans")
		//.useRefLookup()
		.up()
		.associate("unit")
		.useRefLookup()
		.up();

		// belum bisa dites karena datanya belom complete
		addMap(PayrollPmtEmpInsPay.class)
		.mapping
		// associate dengan TRANSACTIONPOSTED
		.associate("payrollPmtEmpIns")
		//.useRefLookup()
		.up();

		/*addMap(ExpenseSheet.class)
		 .mapping
		 .removeField("jobTitleReceived")
		 .removeField("dateReceived")
		 .removeField("iouEsProjectType")

		 .associate("pmtCaIouProject")
		 .useRefLookup()
		 .up()
		 .associate("pmtCaProject")
		 .useRefLookup()
		 .up()
		 .associate("pmtCaIouOthers")
		 .useRefLookup()
		 .up()
		 .associate("pmtCaOthers")
		 .useRefLookup()
		 .up()
		 .associate("empApproved")
		 .useRefLookup()
		 .up()
		 .associate("empOriginator")
		 .useRefLookup()
		 .up()
		 .associate("empReceived")
		 .useRefLookup()
		 .up()
		 .associate("esOwner")
		 .useRefLookup()
		 .up()
		 .associate("trans")
		 .useRefLookup()
		 .up()
		 .associate("trans")
		 .useRefLookup()
		 .up()
		 .associate("project")
		 //.useRefLookup()
		  .up();


		  addMap(ExpenseSheetDetail.class)
		  .mapping
		  .associate("expenseSheet")
		  .up()
		  .associate("account")
		  .useRefLookup()
		  .up();*/

		addMap(MemJournalStrd.class)
		.mapping
		.removeField("currency")
		.removeField("jobTitleReceived")
		.removeField("exchangeRate")
		.removeField("dateReceived")
		.associate("unit")
		.useRefLookup()
		.up()
		.associate("department")
		.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("project")
		//.useRefLookup()
		.up()
		.associate("trans")
		//.useRefLookup()
		.up()
		.associate("transactionCode")
		//.useRefLookup()
		.up();

		addMap(MemJournalStrdDet.class)
		.mapping
		//.removeField("")
		.associate("memJournalStrd")
		//.useRefLookup()
		.up()
		.associate("account")
		//.useRefLookup()
		.up()
		.associate("department")
		.useRefLookup()
		.up();

		addMap(MemJournalNonStrd.class)
		.mapping
		.removeField("currency")
		.removeField("jobTitleReceived")
		.removeField("exchangeRate")
		.removeField("dateReceived")
		.removeField("submitType")
		.associate("journal")
		.useRefLookup()
		.up()
		.associate("project")
		//.useRefLookup()
		.up() // masih belom beres
		.associate("unit")
		.useRefLookup()
		.up()
		.associate("department")
		.useRefLookup()
		.up()
		.associate("empOriginator")
		.useRefLookup()
		.up()
		.associate("empApproved")
		.useRefLookup()
		.up()
		.associate("trans")
		.up();

		addMap(MemJournalNonStrdDet.class)
		.mapping
		.associate("memJournalNonStrd")
		//.useRefLookup()
		.up()
		.associate("account")
		//.useRefLookup()
		.up()
		.associate("department")
		.useRefLookup()
		.up();

	}

	private static void addMap0() {
		// ini buat yudhi & irwan
		addTax21Submit();
		addTrialBalanceSetting();
		addClosingTransaction();
		addClosingTransactionDetail();
		addIncomeStatementDesign();
		addBalanceSheetDesign();
		addCashFlowDesign();

		exchangeRate();

		addMap(Employee.class)
		.mapping
		.removeField("qualification")
		.removeField("qualificationToString")
		.removeField("employment")
		.removeField("education")
		.removeField("certification")
		.removeField("family")
		.removeField("account")
		.removeField("retirement")
		.removeField("mobPhone1")
		.removeField("jobTitleName")
		.removeField("department")
		.field("NSex")
		.setFieldName("SEX")
		.up()
		.field("NReligion")
		.setFieldName("RELIGION")
		.up()
		.field("NMarital")
		.setFieldName("MARITAL")
		.up()
		.field("NArt21")
		.setFieldName("ART21")
		.up()
		;

		addMap(Customer.class)
		.mapping
		.field("postCode")
		.setFieldName("POSTALCODE").up()
		.removeField("companyToString")
		.field("address")
		.setFieldName("STREET").up()
		.useMultiArgConstructor(new String[] {"index",
				"code", "name", "address", "city",
				"postCode", "province", "country",
				"phone1", "phone2", "fax1", "fax2",
				"email", "website"
		});
		/*addMap(RcvUnitBankCashTrns.class)
		 .mapping
		 .removeField("Bank")
		 .associate("rcvUnitCode")
		 .useRefLookup()
		 .up()
		 .associate("empOriginator")
		 .useRefLookup()
		 .up()
		 .associate("empReceived")
		 .useRefLookup()
		 .up()
		 .associate("empApproved")
		 .useRefLookup()
		 .up()
		 .associate("unit")
		 .useRefLookup()
		 .up()
		 ;*/

		addMap(JournalStandardSetting.class)
		.mapping
		.field("number")
		.setFieldName("ATTRNUMBER")
		.up()
		.associate("journalStandard")
		.setFieldName("JOURNAL")
		.up()
		.useMultiArgConstructor(new String[]{
				"application", "number", "journalStandard", "attribute"});
		addMap(JournalStandard.class)
		.mapping
		.associate("journal")
		//.useRefLookup()
		.up()
		.field("group")
		.setFieldName("ISGROUP")
		.up();
		addMap(Journal.class);

		addMap(Currency.class)
		.mapping
		.removeField("isBase")
		.useMultiArgConstructor(new String[] {"index","symbol",
				"code","description", "say", "cent", "language"});
		//long index, String symbol, String code, String description) {
		addMap(PmtUnitBankCashTrans.class)
		.mapping.setTableName("PmtUnitBankCashTrns")
		.removeField("sourceBank")
		.removeField("receivingBank")
		.associate("trans")
		.up()
		.associate("cashAccount")
		.up()
		.associate("bankAccount")
		.up()
		.associate("rcvCashAccount")
		.up()
		.associate("rcvBankAccount")
		.up()
		.associate("empApproved")
		.up()
		.associate("empOriginator")
		.up()
		.associate("empReceived")
		.up()
		.associate("unit")
		.useRefLookup()
		.up();

		addMap(JournalStandardAccount.class)
		.mapping
		.removeField("balanceAsString")
		.associate("account")
		.up()
		.field("balance")
		.setFieldName("BALANCECODE")
		.up()
		.useMultiArgConstructor(new String[]{"account", "balance",
				"hidden", "calculate"});

		addMap(Account.class)
		.mapping
		.removeField("balanceAsString")
		.removeField("categoryAsString")
		.field("code")
		.setFieldName("ACCOUNTCODE")
		.up()
		.field("name")
		.setFieldName("ACCOUNTNAME")
		.up()
		.field("balance")
		.setFieldName("BALANCECODE")
		.up()
		.field("group")
		.setFieldName("ISGROUP")
		.up()

		.useMultiArgConstructor(
				new String[]{"index", "code",
						"name", "category", "group",
						"balance", "note", "treePath"});


		addMap(Unit.class)
		.mapping
		.useMultiArgConstructor(
				new String[] { "index", "code",
						"description"

				}
		);

		addMap(CashAccount.class)
		.mapping
		.associate("unit")
		.useRefLookup()
		.up()
		.associate("account")
		//.useRefLookup()
		.up()
		/*.useMultiArgConstructor(
		 new String[] {
		 "index","code","unit", "account","unit",""
		 }
		 )*/;


		addMap(BankAccount.class)
		.mapping
		.associate("unit")
		.useRefLookup()
		.up()
		.associate("account")
		//.useRefLookup()
		.up()
		/*.useMultiArgConstructor(
		 new String[] {
		 "index","code","name", "accountNo","currency",
		 "address", "contact", "phone", "unit", "account"
		 }
		 )*/;

		addMap(Transaction.class)
		.mapping
		.setTableName("ACCTRANSACTION")
		.associate("journalStandard")
		.setFieldName("TRANSACTIONCODE")
		//.useRefLookup()
		.up()
		.field("transDate")
		.setFieldName("TRANSACTIONDATE")
		.up()
		.field("verifyDate")
		.setFieldName("VERIFICATIONDATE")
		.up()
		.field("reference")
		.setFieldName("REFERENCENO")
		.up()
		.removeField("transactionDetail")
		.associate("journal")
		//.useRefLookup()
		.up()
		.associate("unit")
		.useRefLookup()
		.up()
		.useMultiArgConstructor(
				new String[]{
						"index", "description", "transDate", "verifyDate",
						"postedDate", "reference", "journal", "journalStandard",
						"status", "unit", "void"
				});

		addMap(TransactionDetail.class)
		.mapping
		.setTableName("TRANSACTIONVALUE")
		.associate("account")
		.up()
		.field("value")
		.setFieldName("ACCVALUE")
		.up()
		.associate("unit")
		.useRefLookup()
		.up()
		.useMultiArgConstructor(
				new String[]{
						"account", "value", "currency", "exchangeRate", "unit", "subsidiaryAccount"
				});
		addMap(TransactionPosted.class)
		.mapping
		.setTableName("TRANSACTIONPOSTED")
		.associate("journalStandard")
		.setFieldName("TRANSACTIONCODE")
		.up()
		.field("transDate")
		.setFieldName("TRANSACTIONDATE")
		.up()
		.field("verifyDate")
		.setFieldName("VERIFICATIONDATE")
		.up()
		.field("reference")
		.setFieldName("REFERENCENO")
		.up()
		.removeField("transactionDetail")
		.associate("journal")
		//	.useRefLookup()
		.up()
		.associate("unit")
		.useRefLookup()
		.up()
		.useMultiArgConstructor(
				new String[]{
						"index", "description", "transDate", "verifyDate",
						"postedDate", "reference", "journal", "journalStandard",
						"status", "unit"
				});

		addMap(TransactionPostedDetail.class)
		.mapping
		.setTableName("TRANSVALUEPOSTED")
		.field("transactionPostedId")
		.setFieldName("TRANSACTIONPOSTED")
		.up()
		.associate("account")
		.up()
		.field("value")
		.setFieldName("ACCVALUE")
		.up()
		.associate("unit")
		.useRefLookup()
		.up()
		.useMultiArgConstructor(
				new String[]{
						"transactionPostedId","account", "value", "currency", "exchangeRate", "unit", "subsidiaryAccount"
				});


		addMap(EmployeePayroll.class)
		.mapping
		.field("month")
		.setFieldName("MONTHPAYROLLSUBMIT")
		.up()
		.field("year")
		.setFieldName("YEARPAYROLLSUBMIT")
		.up()
		.associate("unit")
		.up()
		.associate("trans")
		.up()
		.removeField("transactionDate")
		.removeField("currency")
		.removeField("remarks")
		.removeField("submitDate")
		.removeField("dateApproved")
		.removeField("dateOriginator")
		.removeField("dateReceived")
		.removeField("empApproved")
		.removeField("empOriginator")
		.removeField("empReceived")
		.removeField("jobTitleApproved")
		.removeField("jobTitleOriginator")
		.removeField("jobTitleReceived")
		.removeField("referenceNo")
		.removeField("exchangeRate")
		.removeField("description");

		addMap(ProjectData.class)
		.mapping
		.removeField("indexcust")
		.removeField("indexdept")
		.removeField("indexunit")
		.removeField("validation")
		.removeField("enddate")
		.removeField("custname")
		.removeField("indexact")
		.removeField("act")
		.removeField("startdate")
		//.associateNull("customer").up()
		.associate("customer")
		//.useRefLookup()
		.up()
		//.associateNull("unit").up()
		.associate("unit")
		.useRefLookup().up()
		//.associateNull("activity").up()
		.associate("activity")
		.useRefLookup().up()
		.associateNull("sheet").up()
		//.associateNull("department").up()
		.associate("department")
		.useRefLookup().up()
		.field("file")
		.setFieldName("ATTFILE").up()
		.useMultiArgConstructor(new String[] {
				"index","code","customer","unit", "activity","department",
				"workDescription","regDate","ORNo","PONo","IPCNo",
				"ORDate","PODate","IPCDate","file","sheet"
		});

		addMap(VariableAccountSetting.class)
		.mapping
		.associate("account")
		.up();

		addMap(Signature.class)
		.mapping
		.removeField("typeAsString")
		.removeField("employee")
		.field("app")
		.setFieldName("APPLICATION")
		.up()
		.field("type")
		.setFieldName("SIGNATURE")
		.up()
		.associate("fullEmployee")
		.setFieldName("employee")
		.up()
		.useMultiArgConstructor(new String[]{"app","type","fullEmployee"});

		addMap(ProjectContract.class)
		.mapping
		.field("value")
		.setFieldName("ATTVALUE")
		.up()
		.removeField("pPN")
		.removeField("file")
		.removeField("sheet")
		.removeField("curr")
		.useMultiArgConstructor(new String[]{
				"estimateStartDate", "estimateEndDate", "actualStartDate", "actualEndDate",
				"value", "currency", "validation", "description"
		});
		beginningBalance();
		CustomMapper.install();
	}
	private static void addIncomeStatementDesign() {
		addMap(IncomeStatementDesign.class)
		.mapping
		.removeField("journals")
		.removeField("childAt")
		.removeField("childCount")
		.removeField("parent")
		.removeField("index")
		.removeField("leaf")
		.removeField("nodeAncestor")
		.removeField("allowsChildren")
		.removeField("rootRow")
		;
	}

	private static void addBalanceSheetDesign() {
		addMap(BalanceSheetDesign.class)
		.mapping
		.removeField("journals")
		.removeField("childAt")
		.removeField("childCount")
		.removeField("parent")
		.removeField("index")
		.removeField("leaf")
		.removeField("nodeAncestor")
		.removeField("allowsChildren")
		.removeField("rootRow")
		;
	}

	private static void addCashFlowDesign() {
		addMap(IndirectCashFlowStatementDesign.class)
		.mapping
		.setTableName("cashflowdesign")
		.removeField("journals")
		.removeField("childAt")
		.removeField("childCount")
		.removeField("parent")
		.removeField("index")
		.removeField("leaf")
		.removeField("nodeAncestor")
		.removeField("allowsChildren")
		.removeField("rootRow")
		;
	}

	private static void addClosingTransactionDetail() {
		addMap(ClosingTransactionDetail.class)
		.mapping
		.setTableName("CLOSINGTRANSACTIONDET")
		.associate("account")
		.up();
	}

	private static void addClosingTransaction() {
		addMap(ClosingTransaction.class)
		.mapping
		.associate("trans")
		.up()
		.associate("unit")
		.up();
	}

	private static void addTax21Submit() {
		addMap(TaxArt21Submit.class)
		.mapping
		.associate("unit")
		.up()
		.associate("trans")
		.up()
		.associate("taxAccount")
		.up()
		.removeField("transactionDate")
		.removeField("currency")
		.removeField("remarks")
		.removeField("submitDate")
		.removeField("dateApproved")
		.removeField("dateOriginator")
		.removeField("dateReceived")
		.removeField("empApproved")
		.removeField("empOriginator")
		.removeField("empReceived")
		.removeField("jobTitleApproved")
		.removeField("jobTitleOriginator")
		.removeField("jobTitleReceived")
		.removeField("referenceNo")
		.removeField("exchangeRate")
		.removeField("description");
	}

	private static void addTrialBalanceSetting() {
		addMap(TrialBalanceJournalTypeSetting.class)
		.mapping
		.associate("journal")
		.useRefLookup()
		.up();
		addMap(TrialBalanceAccountTypeSetting.class);
	}
	private static void exchangeRate() {
		addMap(ExchangeRate.class)
		.mapping
		.useMultiArgConstructor(new String[]{"index", "referenceCurrency", "baseCurrency", "exchangeRate",
				"validFrom", "validTo"});

	}

	private static void beginningBalance() {
		addMap(CreditorList.class)
		.mapping
		.useMultiArgConstructor(new String[] {
				"index",
				"code",
				"name",
				"address",
				"province",
				"country",
				"contact",
				"phone",
				"bankAccount",
				"bankName",
				"currency",
				"bankAddress",
				"creditorType"
		});
		addMap(CompanyLoan.class)
		.mapping
		.associate("account").up()
		.associate("unit").up()
		.associate("creditorList").setFieldName("creditor").up()
		.useMultiArgConstructor(new String[] {
				"index", "creditorList", "code", "name",
				"currency", "initial",
				"account", "unit", "remarks"
		});


		addMap(BeginningBalanceSheetEntry.class)
		.mapping
		.associate("account").up();

		addMap(BeginningBankDetail.class)
		.mapping
		.ownedByClass(BeginningBalanceSheetEntry.class).up()
		.associate("account").up()
		.associate("bankAccount");

		addMap(BeginningCashDetail.class)
		.mapping
		.ownedByClass(BeginningBalanceSheetEntry.class).up()
		.associate("account").up()
		.associate("cashAccount");


		addMap(BeginningEmpReceivable.class)
		.mapping
		.ownedByClass(BeginningBalanceSheetEntry.class).up()
		.associate("account").up()
		.associate("employee");

		addMap(BeginningAccountPayable.class)
		.mapping
		.ownedByClass(BeginningBalanceSheetEntry.class).up()
		.associate("partner").up()
		.associate("project").up()
		.associate("account");

		addMap(BeginningAccountReceivable.class)
		.mapping
		.ownedByClass(BeginningBalanceSheetEntry.class).up()
		.associate("account").up()
		.associate("project").up()
		.associate("customer");

		addMap(BeginningCashAdvance.class)
		.mapping
		.ownedByClass(BeginningBalanceSheetEntry.class).up()
		.associate("account").up()
		.associate("project").up()
		.associate("employee");

		addMap(BeginningEsDiff.class)
		.mapping
		.ownedByClass(BeginningBalanceSheetEntry.class).up()
		.associate("account").up()
		.associate("project").up()
		.associate("employee");

		addMap(BeginningLoan.class)
		.mapping
		.ownedByClass(BeginningBalanceSheetEntry.class).up()
		.associate("account").up()
		.associate("companyLoan");

		addMap(BeginningBalance.class)
		.mapping
		.associate("bridgeAccount").up()
		.associate("defaultUnit").up()
		.associate("trans").up()
		.associate("journalStandard").up();

		addMap(BeginningWorkInProgress.class)
		.mapping
		.ownedByClass(BeginningBalanceSheetEntry.class).up()
		.associate("account").up()
		.associate("project").up();
	}
	static Connection lookupConn;
	public static void setPrimaryConnection(Connection c)
	{
		lookupConn = c;
	}
	public static Connection obtainLookupConnection() {
		return lookupConn;
	}
	public static void closeLookupConnection() {
		// do nothing
	}
	public static LookupMap obtainLookupMap(Class associatedClass) {
		if (!lookupMap.containsKey(associatedClass.getName()))
		{
			LookupMap lookupMap2 = new LookupMap(associatedClass);
			lookupMap.put(associatedClass.getName(), lookupMap2);
		}
		return (LookupMap) lookupMap.get(associatedClass.getName());

	}
}
