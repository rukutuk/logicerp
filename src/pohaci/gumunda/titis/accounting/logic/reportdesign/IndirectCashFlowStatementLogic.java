/**
 * 
 */
package pohaci.gumunda.titis.accounting.logic.reportdesign;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.Journal;
import pohaci.gumunda.titis.accounting.dbapi.reportdesign.ReportDesignSQL;
import pohaci.gumunda.titis.accounting.entity.reportdesign.Design;
import pohaci.gumunda.titis.accounting.entity.reportdesign.IndirectCashFlowStatementDesign;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportRow;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

/**
 * @author dark-knight
 * 
 */
public class IndirectCashFlowStatementLogic extends ReportLogic {

	public IndirectCashFlowStatementLogic(Connection connection, long sessionId) {
		this.connection = connection;
		this.sessionId = sessionId;
	}

	public List getDesignList() {
		GenericMapper mapper = MasterMap
				.obtainMapperFor(IndirectCashFlowStatementDesign.class);
		mapper.setActiveConn(connection);

		List list = mapper.doSelectAll();
		Iterator iterator = list.iterator();
		ReportDesignSQL sql = new ReportDesignSQL(connection, sessionId);
		while (iterator.hasNext()) {
			IndirectCashFlowStatementDesign design = (IndirectCashFlowStatementDesign) iterator.next();

			try {
				design.setJournals(sql.getIndirectCashFlowStatementJournals(design
						.getAutoindex()));

				String rowXML = sql.getIndirectCashFlowStatementRows(design.getAutoindex());
				ReportRow row = createObject(rowXML);
				design.setRootRow(row);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public Design updateDesign(Design design) throws Exception {
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			connection.setAutoCommit(false);
			trans = connection.getTransactionIsolation();
			connection
					.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			// here
			GenericMapper mapper = MasterMap
					.obtainMapperFor(IndirectCashFlowStatementDesign.class);
			mapper.setActiveConn(connection);

			mapper.doUpdate(design);

			ReportDesignSQL sql = new ReportDesignSQL(connection, sessionId);
			sql.deleteIndirectCashFlowStatementJournals(design.getAutoindex());
			Journal[] journals = design.getJournals();
			for (int i = 0; i < journals.length; i++) {
				sql.insertIndirectCashFlowStatementJournal(design.getAutoindex(),
						journals[i].getIndex());
			}

			updateRows(design);

			connection.commit();
			connection.setAutoCommit(true);
			connection.setTransactionIsolation(trans);
			return design;
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
				connection.setAutoCommit(true);
			} catch (Exception e) {
			}
			throw new Exception(ex.getMessage());
		} finally {
			connection.setAutoCommit(true);
		}
	}

	public Design insertDesign(Design design) throws Exception {
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			connection.setAutoCommit(false);
			trans = connection.getTransactionIsolation();
			connection
					.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			// here
			GenericMapper mapper = MasterMap
					.obtainMapperFor(IndirectCashFlowStatementDesign.class);
			mapper.setActiveConn(connection);

			mapper.doInsert(design);

			ReportDesignSQL sql = new ReportDesignSQL(connection, sessionId);
			Journal[] journals = design.getJournals();
			updateRows(design);
			for (int i = 0; i < journals.length; i++) {
				sql.insertIndirectCashFlowStatementJournal(design.getAutoindex(),
						journals[i].getIndex());
			}

			connection.commit();
			connection.setAutoCommit(true);
			connection.setTransactionIsolation(trans);
			return design;
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
				connection.setAutoCommit(true);
			} catch (Exception e) {
			}
			throw new Exception(ex.getMessage());
		} finally {
			connection.setAutoCommit(true);
		}
	}

	public void deleteDesign(Design design) throws Exception {
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			connection.setAutoCommit(false);
			trans = connection.getTransactionIsolation();
			connection
					.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			// here
			ReportDesignSQL sql = new ReportDesignSQL(connection, sessionId);
			sql.deleteIndirectCashFlowStatementJournals(design.getAutoindex());
			sql.deleteIndirectCashFlowStatementRows(design.getAutoindex());

			GenericMapper mapper = MasterMap
					.obtainMapperFor(IndirectCashFlowStatementDesign.class);
			mapper.setActiveConn(connection);

			mapper.doDelete(design);

			connection.commit();
			connection.setAutoCommit(true);
			connection.setTransactionIsolation(trans);
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
				connection.setAutoCommit(true);
			} catch (Exception e) {
			}
			throw new Exception(ex.getMessage());
		} finally {
			connection.setAutoCommit(true);
		}
	}

	public void updateRows(Design design) throws Exception {
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			connection.setAutoCommit(false);
			trans = connection.getTransactionIsolation();
			connection
					.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			ReportRow root = design.getRootRow();
			String rootXML = createXML(root);

			List parseList = parseString255(rootXML);

			Iterator iterator = parseList.iterator();
			int sequence = 0;
			ReportDesignSQL sql = new ReportDesignSQL(connection, sessionId);
			sql.deleteIndirectCashFlowStatementRows(design.getAutoindex());
			while (iterator.hasNext()) {
				String value = (String) iterator.next();

				sql.insertIndirectCashFlowStatementRows(design.getAutoindex(), value,
						sequence);

				sequence++;
			}

			connection.commit();
			connection.setAutoCommit(true);
			connection.setTransactionIsolation(trans);
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
				connection.setAutoCommit(true);
			} catch (Exception e) {
			}
			throw new Exception(ex.getMessage());
		} finally {
			connection.setAutoCommit(true);
		}
	}

	public IndirectCashFlowStatementDesign getIndirectCashFlowStatementDesign(
			IndirectCashFlowStatementDesign design) {
		GenericMapper mapper = MasterMap
				.obtainMapperFor(IndirectCashFlowStatementDesign.class);
		mapper.setActiveConn(connection);

		ReportDesignSQL sql = new ReportDesignSQL(connection, sessionId);

		IndirectCashFlowStatementDesign newDesign = (IndirectCashFlowStatementDesign) mapper
				.doSelectByIndex(new Long(design.getAutoindex()));
		try {
			newDesign.setJournals(sql.getIndirectCashFlowStatementJournals(design
					.getAutoindex()));
			String rowXML = sql.getIndirectCashFlowStatementRows(design.getAutoindex());
			ReportRow row = createObject(rowXML);
			newDesign.setRootRow(row);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return newDesign;
	}

}
