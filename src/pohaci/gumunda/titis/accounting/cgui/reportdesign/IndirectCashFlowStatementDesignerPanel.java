/**
 *
 */
package pohaci.gumunda.titis.accounting.cgui.reportdesign;

import java.sql.Connection;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import pohaci.gumunda.titis.accounting.entity.reportdesign.Design;
import pohaci.gumunda.titis.accounting.entity.reportdesign.IndirectCashFlowStatementDesign;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportGroup;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportRow;
import pohaci.gumunda.titis.accounting.logic.reportdesign.IndirectCashFlowStatementLogic;

/**
 * @author dark-knight
 *
 */
public class IndirectCashFlowStatementDesignerPanel extends ReportDesignPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public IndirectCashFlowStatementDesignerPanel(Connection connection,
			long sessionid) {
		this.connection = connection;
		this.sessionid = sessionid;
		this.title = "Indirect Cash Flow Statement Rows";
		setDefaultEntity();
		constructComponent();
		init();
	}

	protected void setDefaultEntity() {
		root = new IndirectCashFlowStatementDesign("Indirect Cash Flow Design");
		designTree = new JTree(new IndirectCashFlowStatementDesign(
				"Indirect Cash Flow Design"));
		designRowsTree = new JTree(new ReportGroup(
				"Indirect Cash Flow Statement Rows", false));
		logic = new IndirectCashFlowStatementLogic(connection, sessionid);
		rootReportRow = new ReportGroup("Indirect Cash Flow Statement Rows",
				false);
	}

	protected void doAdd() {
		ReportDesignDlg dlg = new ReportDesignDlg(
				pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
				connection, sessionid, "Indirect Cash Flow Statement Design",
				IndirectCashFlowStatementDesign.class);
		dlg.setVisible(true);
		if (dlg.getResponse() == JOptionPane.OK_OPTION) {
			DefaultTreeModel model = (DefaultTreeModel) designTree.getModel();

			IndirectCashFlowStatementDesign design = (IndirectCashFlowStatementDesign) dlg
					.getDesign();
			design.setRootRow(new ReportGroup(
					"Indirect Cash Flow Statement Rows", false));
			insertEntity(design);

			Design root = (Design) model.getRoot();
			root.add(design);

			designTree.setModel(new DefaultTreeModel(root));

			JOptionPane.showMessageDialog(this, "Successfully saved");
		}
	}

	protected void doEdit() {
		if (getSelectedDesign() == null) {
			JOptionPane.showMessageDialog(this, "Please select the design");
			return;
		}
		IndirectCashFlowStatementDesign design = (IndirectCashFlowStatementDesign) getSelectedDesign();
		if (design == designTree.getModel().getRoot())
			return;

		ReportDesignDlg dlg = new ReportDesignDlg(
				pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
				connection, sessionid, "Indirect Cash Flow Statement Design",
				IndirectCashFlowStatementDesign.class);
		dlg.setDesign(design);
		dlg.setVisible(true);
		if (dlg.getResponse() == JOptionPane.OK_OPTION) {
			DefaultTreeModel model = (DefaultTreeModel) designTree.getModel();

			design = (IndirectCashFlowStatementDesign) dlg.getDesign();
			updatePositiveBalance(design);
			updateEntity(design);

			Design root = (Design) model.getRoot();

			designTree.setModel(new DefaultTreeModel(root));

			JOptionPane.showMessageDialog(this, "Successfully updated");
		}
	}

	protected void doDelete() {
		if (getSelectedDesign() == null) {
			JOptionPane.showMessageDialog(this, "Please select the design");
			return;
		}
		IndirectCashFlowStatementDesign design = (IndirectCashFlowStatementDesign) getSelectedDesign();
		if (design == designTree.getModel().getRoot())
			return;

		int response = JOptionPane.showConfirmDialog(this,
				"Are you sure to delete this design", "Delete Confirmation",
				JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			deleteDesign(design);

			DefaultTreeModel model = (DefaultTreeModel) designTree.getModel();
			Design root = (Design) model.getRoot();
			root.remove(design);

			designTree.setModel(new DefaultTreeModel(root));

			ReportRow rootRow = new ReportGroup(
					"Indirect Cash Flow Statement Rows", false);
			DefaultTreeModel modelRow = new DefaultTreeModel(rootRow);
			designRowsTree.setModel(modelRow);

			JOptionPane.showMessageDialog(this, "Successfully deleted");
		}
	}

	protected void doAddRows() {
		ReportRow row = getSelectedRow();
		CashFlowRowDesignDlg dlg = new CashFlowRowDesignDlg(
				pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
				connection, sessionid, row, title, getSelectedDesign()
						.getPositiveBalance() == 0);
		dlg.setVisible(true);
		if (dlg.getResponse() == JOptionPane.OK_OPTION) {
			ReportRow node = (ReportRow) dlg.getReportRow();

			row.add(node);

			setDesignRowsTreeModel(row);
		}
	}

	protected void doEditRows() {
		ReportRow row = getSelectedRow();
		CashFlowRowDesignDlg dlg = new CashFlowRowDesignDlg(
				pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
				connection, sessionid, (ReportRow) row.getParent(), title,
				getSelectedDesign().getPositiveBalance() == 0);
		dlg.setReportRow(row);
		dlg.setVisible(true);
		if (dlg.getResponse() == JOptionPane.OK_OPTION) {
			ReportRow node = (ReportRow) dlg.getReportRow();

			setDesignRowsTreeModel((ReportRow) node.getParent());
		}
	}

}
