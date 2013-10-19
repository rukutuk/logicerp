package pohaci.gumunda.titis.accounting.cgui.report;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.entity.SalesInvoice;
import pohaci.gumunda.titis.accounting.entity.SalesItem;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.project.cgui.Customer;

public class Invoice {
	public Invoice(SalesInvoice entity,Connection conn, int digit,String moneyLanguage) {
		try {
			String filename = "invoice.jrxml";
			JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

			int pilihdigit = digit;
			String format = "#,##0";

			if (pilihdigit==1)
				format ="#,##0.0";
			else if (pilihdigit==2)
				format ="#,##0.00";
			else if (pilihdigit==3)
				format ="#,##0.000";
			else if (pilihdigit==4)
				format = "#,##0.0000";

			DecimalFormat desimalFormat = new DecimalFormat(format);
			DecimalFormat desimalFormat2 = new DecimalFormat("#,##0");
			DecimalFormat desimalFormat1 = new DecimalFormat("#,##0.00");

			parameters.put("param_logo", "../images/TS.gif");

			if (entity.getTransactionDate()!=null)
				parameters.put("param_invoice_date", dateFormat.format(entity.getTransactionDate()));
			else
				parameters.put("param_invoice_date", "");

			String curr = "Rp";
			Currency cr = null;
			if (entity.getProject()!=null){
				if (entity.getProject().getProjectContract()!=null)
					if (entity.getProject().getProjectContract().getCurrency()!=null) {
						cr = entity.getProject().getProjectContract().getCurrency();
						curr = cr.getSymbol();
					}

				if (entity.getProject().getCustomer()!=null){
					Customer cust = entity.getProject().getCustomer();
					String postcode = "";
					if (cust.getPostCode()!=0)
						postcode =String.valueOf(cust.getPostCode());
					String address = cust.getAddress() + "\n" + cust.getCity() + " " + postcode	 + "\n" +
					cust.getProvince()+ " " + cust.getCountry();
					parameters.put("param_costumer", entity.getProject().getCustomer().getName());
					parameters.put("param_costumer_addrs", address);
				}else{
					parameters.put("param_costumer", "");
					parameters.put("param_costumer_addrs", "");
				}

				parameters.put("param_attention",entity.getAttention());

				if (entity.getProject().getUnit()!=null)
					parameters.put("param_unit_code", entity.getProject().getUnit().toString());
				else
					parameters.put("param_unit_code", "");
				parameters.put("param_job_number", entity.getProject().getIPCNo());
				parameters.put("param_invoice_number", entity.getReferenceNo());
				if (entity.getProject().getDepartment()!=null)
					parameters.put("param_department", entity.getDepartment().toString());
				else
					parameters.put("param_department", "");
				parameters.put("param_order_number", entity.getProject().getPONo());
			}

			if (entity.getBankAccount()!=null){
				String bank = entity.getBankAccount().getName() + "\n" + entity.getBankAccount().getAddress() +"\n" +
				entity.getBankAccount().getAccountNo() + " (" + entity.getBankAccount().getCurrency().getSymbol() + ")";
				parameters.put("param_bank_account", bank);
			}else
				parameters.put("param_bank_account", "");

			if (entity.getEmpAuthorize()!=null){
				String nama = entity.getEmpAuthorize().getFirstName() + " " + entity.getEmpAuthorize().getMidleName() + " " +
				entity.getEmpAuthorize().getLastName();
				parameters.put("param_nama_director", nama);
			}else
				parameters.put("param_nama_director", "");

			if (entity.getJobTitleAuthorize()!=null)
				parameters.put("param_job_title", entity.getJobTitleAuthorize());
			else
				parameters.put("param_job_title", "");

			if (entity.getDateAuthorize()!=null)
				parameters.put("param_date", dateFormat.format(entity.getDateAuthorize()));
			else
				parameters.put("param_date", "");

			parameters.put("param_currency", curr);

			model.addColumn("field0");
			model.addColumn("field1");
			model.addColumn("field2");
			model.addColumn("field3");
			model.addColumn("field4");
			model.addColumn("field5");
			model.addColumn("field6");
			model.addColumn("field7");
			model.addColumn("field8");
			model.addColumn("field9");
			model.addColumn("field10");
			model.addColumn("status");
			model.addRow(new Object[]{"","","","","","","","","","","", new Integer(0)});
			model.addRow(new Object[]{"",entity.getBriefDesc(),"","","","","","","","","", new Integer(0)});
			model.addRow(new Object[]{"","","","","","","","","","","", new Integer(0)});
			int k=0;
			SalesItem[] a = entity.getSalesItem();
			double sales =0;
			for(int i = 0; i < a.length; i ++) {
				k++;
				sales +=a[i].getAmount();
				String desc1 = "";
				String spes1 = "";
				if (a[i].getDescription()!=null)
					desc1 = a[i].getDescription();
				if (a[i].getSpecification()!=null)
					spes1 = a[i].getSpecification();

				String qty = "";
				String kali = "";
				if (a[i].getQty()>0){
					qty = desimalFormat.format(a[i].getQty());
					kali = "X";
				}
				String unitspesial ="";
				String curt = "";
				if (a[i].getUnitPrice()>0){
					unitspesial =desimalFormat.format(a[i].getUnitPrice());
					curt = curr;
				}

				String kaliPerson = "";
				if (a[i].getPersonAmount()>0){
					kaliPerson = "X " + desimalFormat2.format(a[i].getPersonAmount());
				}

				String personDet = "";
				if (a[i].getPersonDesc()!=null)
					if (!a[i].getPersonDesc().equals(""))
						personDet = a[i].getPersonDesc();

				String amt = "";
				String currt = "";
				if (a[i].getAmount()>0){
					amt = desimalFormat1.format(a[i].getAmount());
				    currt = curr;
				}

				model.addRow(new Object[]{a[i].getNumber(),desc1,qty,spes1,
						kali,curt,unitspesial,kaliPerson,personDet,currt,amt, new Integer(1)});
			}
			double downpayment = entity.getDownPaymentAmount();
			parameters.put("param_sub_total", desimalFormat1.format(sales));
			parameters.put("param_downpayment", desimalFormat1.format(downpayment));
			parameters.put("param_total", desimalFormat1.format(sales-downpayment));
			parameters.put("param_vat", desimalFormat1.format(entity.getVatAmount()));
			parameters.put("param_grand_total", desimalFormat1.format((sales-downpayment)+entity.getVatAmount())/*"24,000,000,000.00"*/);

			String currency = cr.getSay();
			String cents = "";
			String language = moneyLanguage;
			//int language = 0;
			/*if (curr.equals("Rp"))
				currency = "Rupiah";
			else
				currency = "US Dollar";*/

		/*	if (moneyLanguage.equals(MoneyTalk.LANGUAGE_INDONESIAN)){
				cents = "Sen";
				language = MoneyTalk.INDONESIAN;
			}else {
				cents = "Cent";
				language = MoneyTalk.ENGLISH;
			}*/
			String moneyTalk = MoneyTalk.say(((sales-downpayment)+entity.getVatAmount()),currency,cents,language,
					MoneyTalk.UPPER_CASE,MoneyTalk.PRESERVE_NONE, false);
			parameters.put("param_say", moneyTalk);

			//model.addRow(new Object[]{"","","","","","","","","GRAND TOTAL",curr, desimalFormat.format((sales-downpayment)+entity.getVatAmount()),new Integer(4)});
			//model.addRow(new Object[]{"AMOUNT IN WORD : " + moneyTalk ,curr,"","","","","",new Integer(3)});
			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
					parameters, ds);

			PrintingViewer view = new PrintingViewer(jasperPrint);
			view.setTitle("Invoice");
			view.setVisible(true);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
