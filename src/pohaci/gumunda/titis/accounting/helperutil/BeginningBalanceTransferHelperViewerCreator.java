/**
 * 
 */
package pohaci.gumunda.titis.accounting.helperutil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JRViewer;

/**
 * @author dark-knight
 *
 */
public class BeginningBalanceTransferHelperViewerCreator {
	
	private JRViewer viewer = null;
	private Connection connection = null;
	//private Date date;
	

	/**
	 * @param connection
	 */
	public BeginningBalanceTransferHelperViewerCreator(Connection connection, Date date) {
		this.connection = connection;
		//this.date = date;
		setViewer(date);
	}



	/**
	 * @return Returns the viewer.
	 */
	public JRViewer getViewer() {
		return viewer;
	}

	private void setViewer(Date date) {
		String filename = "beginningbalance1.jrxml";
		try {
			JasperReport jasperReport = JasperCompileManager.compileReport("report/" + filename);
			
			List list = new ArrayList();
			
			if (connection != null) {
				if (date != null) {
					BeginningBalanceTransferLogic logic = new BeginningBalanceTransferLogic();
					list = logic.generateList(connection, date);
				}
			}
			
			JRDataSource ds = new JRBeanCollectionDataSource(list);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(), ds);
			
			viewer = new JRViewer(jasperPrint);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	

}
