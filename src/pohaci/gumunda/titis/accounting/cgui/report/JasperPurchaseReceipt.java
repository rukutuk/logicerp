package pohaci.gumunda.titis.accounting.cgui.report;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.PurchaseReceipt;
import pohaci.gumunda.titis.accounting.entity.PurchaseReceiptItem;
import pohaci.gumunda.titis.accounting.logic.MoneyTalk;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class JasperPurchaseReceipt {
	JasperReport m_jreport = null;
	JasperPrint m_jprint = null;
	Map m_param = null;

	public JasperPurchaseReceipt(PurchaseReceipt entity,Connection conn) {
		try {
			String filename = "PrchsReceipt.jrxml";
			//String filename = "ViewPurchaseReceipt.jrxml";
			DecimalFormat formatDesimal = new DecimalFormat("#,###.00");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
			Map parameters = new HashMap();
			DefaultTableModel model = new DefaultTableModel();

			javax.swing.ImageIcon image = new javax.swing.ImageIcon("../images/TS.gif");
			parameters.put("param_logo", image.getImage());
			parameters.put("param_no", String.valueOf(entity.getReferenceNo()));
			parameters.put("param_date", dateFormat.format(entity.getTransactionDate()));
			parameters.put("param_supplier", String.valueOf(entity.getSupplier()));
			parameters.put("param_client", String.valueOf(entity.getProject().getCustomer()));
			parameters.put("param_work_description", String.valueOf(entity.getProject().getWorkDescription()));
			parameters.put("param_ipc_no", entity.getProject().getIPCNo());
			parameters.put("param_due_date", dateFormat.format(entity.getDuedate()));
			parameters.put("param_unit_code", String.valueOf(entity.getProject().getUnit()));
			parameters.put("param_activity_code", String.valueOf(entity.getProject().getActivity()));
			parameters.put("param_department_code", String.valueOf(entity.getProject().getDepartment()));

			if (entity.getEmpOriginator()!=null){
				String empOriginator = entity.getEmpOriginator().getFirstName() + " " +
					entity.getEmpOriginator().getMidleName() + " " + entity.getEmpOriginator().getLastName();
				parameters.put("param_nama_originator", empOriginator);
			}else
				parameters.put("param_nama_originator", "");

			parameters.put("param_jabatan_originator", entity.getJobTitleOriginator());

			if (entity.getDateOriginator()!=null)
				parameters.put("param_date_originator", dateFormat.format(entity.getDateOriginator()));
			else
				parameters.put("param_date_originator", "");

			if (entity.getEmpApproved()!=null){
				String empApproved = entity.getEmpApproved().getFirstName() + " " +
					entity.getEmpApproved().getMidleName() + " " + entity.getEmpApproved().getLastName();
				parameters.put("param_nama_approver", empApproved);
			}else
				parameters.put("param_nama_approver", "");

			parameters.put("param_jabatan_approver", entity.getJobTitleApproved());

			if (entity.getDateApproved()!=null){
				parameters.put("param_date_approver", dateFormat.format(entity.getDateApproved()));
			}else
				parameters.put("param_date_approver", "");

			if (entity.getEmpReceived()!=null){
				String empReceived = entity.getEmpReceived().getFirstName() + " " +
					entity.getEmpReceived().getMidleName() + " " + entity.getEmpReceived().getLastName();
				parameters.put("param_nama_receiver", empReceived);
			}else
				parameters.put("param_nama_receiver", "");

			parameters.put("param_jabatan_receiver", entity.getJobTitleReceived());

			if (entity.getDateReceived()!=null)
				parameters.put("param_date_receiver", dateFormat.format(entity.getDateReceived()));
			else
				parameters.put("param_date_receiver", "");

			model.addColumn("field1");
			model.addColumn("field2");
			model.addColumn("field3");
			model.addColumn("field4");
			model.addColumn("field5");
			model.addColumn("field6");
			model.addColumn("field7");
			model.addColumn("status");
			//System.err.println("panj :" + entity.getPurchaseDetail().length);

			/* for(int i = 0; i < 3; i ++) {
			 model.addRow(new Object[]{
			 //String.valueOf(i+1), objects[i], objects[i], objects[i], objects[i], objects[i], new Integer(1)});
			  String.valueOf(i+1), "", "", "", "", "", new Integer(1)});
			  }*/

			GenericMapper mapper2=MasterMap.obtainMapperFor(PurchaseReceiptItem.class);
			mapper2.setActiveConn(conn);
			List rs=mapper2.doSelectWhere(IDBConstants.ATTR_PURCHASE_RECEIPT+"="+entity.getIndex());
			PurchaseReceiptItem temp = null;
			double subTotal = 0;
			for(int i=0;i<rs.size();i++){
				temp=(PurchaseReceiptItem)rs.get(i);
				subTotal += temp.getAmount();
				model.addRow(new Object[]{
						String.valueOf(i+1), temp.getDescription(), temp.getSpecification(), formatDesimal.format(temp.getUnitPrice()),
						formatDesimal.format(temp.getQty()),entity.getApCurr().toString(),
						formatDesimal.format(temp.getAmount()),new Integer(1)});
			}
			double vat = (entity.getVatPercent()/100) * subTotal;
			model.addRow(new Object[]{"SUBTOTAL", "", "", "", "",entity.getApCurr().toString(), formatDesimal.format(subTotal), new Integer(2)});
			model.addRow(new Object[]{"VAT 10%", "", "", "", "",entity.getApCurr().toString(), formatDesimal.format(vat), new Integer(2)});
			model.addRow(new Object[]{"TOTAL", "", "", "", "",entity.getApCurr().toString(), formatDesimal.format(subTotal+vat), new Integer(2)});

			
				String moneyTalk = MoneyTalk.say((subTotal+vat),entity.getApCurr(),
					MoneyTalk.SENTENCE_CASE,MoneyTalk.PRESERVE_CURRENCY, false);
			model.addRow(new Object[]{"Amount in word : " + moneyTalk , "", "", "", "", "","", new Integer(3)});

			JRTableModelDataSource ds = new JRTableModelDataSource(model);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, ds);
			m_jprint = jasperPrint;
			m_param = parameters;

			PrintingViewer view = new PrintingViewer(jasperPrint);
			view.setTitle("Purchase Receipt");
			view.setVisible(true);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public JasperPrint getJasperPrint(){
		return m_jprint;
	}

	public Map getParameters(){
		return m_param;
	}

	public JasperReport getJasperReport(){
		return m_jreport;
	}


}