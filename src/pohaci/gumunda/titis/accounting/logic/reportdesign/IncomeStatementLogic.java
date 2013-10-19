package pohaci.gumunda.titis.accounting.logic.reportdesign;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.Journal;
import pohaci.gumunda.titis.accounting.dbapi.reportdesign.ReportDesignSQL;
import pohaci.gumunda.titis.accounting.entity.reportdesign.Design;
import pohaci.gumunda.titis.accounting.entity.reportdesign.IncomeStatementDesign;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportRow;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class IncomeStatementLogic extends ReportLogic {
	public IncomeStatementLogic(Connection connection, long sessionId) {
		this.connection = connection;
		this.sessionId = sessionId;
	}

	public List getDesignList() {
		GenericMapper mapper = MasterMap
				.obtainMapperFor(IncomeStatementDesign.class);
		mapper.setActiveConn(connection);

		List list = mapper.doSelectAll();
		Iterator iterator = list.iterator();
		ReportDesignSQL sql = new ReportDesignSQL(connection, sessionId);
		while (iterator.hasNext()) {
			IncomeStatementDesign design = (IncomeStatementDesign) iterator
					.next();

			try {
				design.setJournals(sql.getIncomeStatementJournals(design
						.getAutoindex()));

				String rowXML = sql.getIncomeStatementRows(design
						.getAutoindex());
				ReportRow row = createObject(rowXML);
				design.setRootRow(row);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public IncomeStatementDesign getIncomeStatementDesign(
			IncomeStatementDesign design) {
		GenericMapper mapper = MasterMap
				.obtainMapperFor(IncomeStatementDesign.class);
		mapper.setActiveConn(connection);

		ReportDesignSQL sql = new ReportDesignSQL(connection, sessionId);

		IncomeStatementDesign newDesign = (IncomeStatementDesign) mapper
				.doSelectByIndex(new Long(design.getAutoindex()));
		try {
			newDesign.setJournals(sql.getIncomeStatementJournals(design
					.getAutoindex()));
			String rowXML = sql.getIncomeStatementRows(design.getAutoindex());
			ReportRow row = createObject(rowXML);
			newDesign.setRootRow(row);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return newDesign;
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
					.obtainMapperFor(IncomeStatementDesign.class);
			mapper.setActiveConn(connection);

			mapper.doInsert(design);

			ReportDesignSQL sql = new ReportDesignSQL(connection, sessionId);
			Journal[] journals = design.getJournals();
			updateRows(design);
			for (int i = 0; i < journals.length; i++) {
				sql.insertIncomeStatementJournal(design.getAutoindex(),
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

	public Design updateDesign(Design design) throws Exception {
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			connection.setAutoCommit(false);
			trans = connection.getTransactionIsolation();
			connection
					.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			// here
			GenericMapper mapper = MasterMap
					.obtainMapperFor(IncomeStatementDesign.class);
			mapper.setActiveConn(connection);

			mapper.doUpdate(design);

			ReportDesignSQL sql = new ReportDesignSQL(connection, sessionId);
			sql.deleteIncomeStatementJournals(design.getAutoindex());
			Journal[] journals = design.getJournals();
			for (int i = 0; i < journals.length; i++) {
				sql.insertIncomeStatementJournal(design.getAutoindex(),
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

	public void deleteDesign(Design design)
			throws Exception {
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			connection.setAutoCommit(false);
			trans = connection.getTransactionIsolation();
			connection
					.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			// here
			ReportDesignSQL sql = new ReportDesignSQL(connection, sessionId);
			sql.deleteIncomeStatementJournals(design.getAutoindex());
			sql.deleteIncomeStatementRows(design.getAutoindex());

			GenericMapper mapper = MasterMap
					.obtainMapperFor(IncomeStatementDesign.class);
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
			sql.deleteIncomeStatementRows(design.getAutoindex());
			while (iterator.hasNext()) {
				String value = (String) iterator.next();

				sql.insertIncomeStatementRows(design.getAutoindex(), value,
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
}
