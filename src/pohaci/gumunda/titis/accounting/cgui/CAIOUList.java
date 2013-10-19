package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthers;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersInstall;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProject;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectInstall;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class CAIOUList extends JList {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Connection m_conn = null;

	Object objnya;

	long m_sessionid = -1;

	// PmtCAIOUOthers[] m_employee = new PmtCAIOUOthers[0];
	// private GenericMapper m_entityMapper;
	private double amountnya;

	public CAIOUList(Connection conn, long sessionid, Object obj) {
		m_conn = conn;
		m_sessionid = sessionid;
		objnya = obj;
		setModel(new DefaultListModel());
		initData(obj);
	}

	void initData(Object obj) {
		GenericMapper mapnya;
		Object[] listData;
		long indexParent = 0;
		if (obj instanceof PmtCAIOUOthers) {
			mapnya = MasterMap.obtainMapperFor(PmtCAIOUOthersInstall.class);
			indexParent = ((PmtCAIOUOthers) objnya).getIndex();
		} else if (obj instanceof PmtCAIOUProject) {
			mapnya = MasterMap.obtainMapperFor(PmtCAIOUProjectInstall.class);
			indexParent = ((PmtCAIOUProject) objnya).getIndex();
		} else
			mapnya = MasterMap.obtainMapperFor(PmtCAIOUProjectInstall.class);
		mapnya.setActiveConn(m_conn);

		listData = mapnya.doSelectAll().toArray();
		DefaultListModel model = (DefaultListModel) getModel();
		model.clear();
		amountnya = 0;
		for (int i = 0; i < listData.length; i++) {
 			if (obj instanceof PmtCAIOUOthers) {
				PmtCAIOUOthersInstall data = (PmtCAIOUOthersInstall) listData[i];
				if (data.getPmtcaiouothers()!=null)
					if (data.getPmtcaiouothers().getIndex() == indexParent) {
						model.addElement(data);
						amountnya = amountnya + data.getAmount();
					}
			} else if (obj instanceof PmtCAIOUProject) {
				PmtCAIOUProjectInstall data = (PmtCAIOUProjectInstall) listData[i];
				System.out.println(i + " "
						+ data.getPmtcaiouproject().getIndex());
				if (data.getPmtcaiouproject().getIndex() == indexParent) {
					model.addElement(data);
					amountnya = amountnya + data.getAmount();
				}
			}

		}

	}

	public double getAmountnya() {
		return amountnya;
	}

}
