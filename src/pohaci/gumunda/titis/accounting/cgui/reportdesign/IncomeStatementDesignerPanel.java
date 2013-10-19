package pohaci.gumunda.titis.accounting.cgui.reportdesign;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.sql.Connection;
import pohaci.gumunda.titis.accounting.entity.reportdesign.Design;
import pohaci.gumunda.titis.accounting.entity.reportdesign.IncomeStatementDesign;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportGroup;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportRow;
import pohaci.gumunda.titis.accounting.logic.reportdesign.IncomeStatementLogic;

public class IncomeStatementDesignerPanel extends ReportDesignPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public IncomeStatementDesignerPanel(Connection connection, long sessionid) {
		this.connection = connection;
		this.sessionid = sessionid;
		this.title = "Income Statement Rows";
		setDefaultEntity();
		constructComponent();
		init();
	}

	protected void setDefaultEntity() {
		root = new IncomeStatementDesign("Income Statement Design");
		designTree = new JTree(new IncomeStatementDesign(
		"Income Statement Design"));
		designRowsTree = new JTree(new ReportGroup("Income Statement Rows",
				false));
		logic = new IncomeStatementLogic(connection, sessionid);
		rootReportRow = new ReportGroup("Income Statement Rows", false);
	}


	protected void doEdit() {
		if (getSelectedDesign() == null) {
			JOptionPane.showMessageDialog(this, "Please select the design");
			return;
		}
		IncomeStatementDesign design = (IncomeStatementDesign) getSelectedDesign();
		if (design == designTree.getModel().getRoot())
			return;

		ReportDesignDlg dlg = new ReportDesignDlg(
				pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
				connection, sessionid, "Income Statement Design",
				IncomeStatementDesign.class);
		dlg.setDesign(design);
		dlg.setVisible(true);
		if (dlg.getResponse() == JOptionPane.OK_OPTION) {
			DefaultTreeModel model = (DefaultTreeModel) designTree.getModel();

			design = (IncomeStatementDesign) dlg.getDesign();
			updatePositiveBalance(design);
			updateEntity(design);

			Design root = (Design) model.getRoot();

			designTree.setModel(new DefaultTreeModel(root));

			JOptionPane.showMessageDialog(this, "Successfully updated");
		}
	}

	protected void doAdd() {
		ReportDesignDlg dlg = new ReportDesignDlg(
				pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
				connection, sessionid, "Income Statement Design",
				IncomeStatementDesign.class);
		dlg.setVisible(true);
		if (dlg.getResponse() == JOptionPane.OK_OPTION) {
			DefaultTreeModel model = (DefaultTreeModel) designTree.getModel();

			IncomeStatementDesign design = (IncomeStatementDesign) dlg
					.getDesign();
			design.setRootRow(new ReportGroup("Income Statement Rows", false));
			insertEntity(design);

			Design root = (Design) model.getRoot();
			root.add(design);

			designTree.setModel(new DefaultTreeModel(root));

			JOptionPane.showMessageDialog(this, "Successfully saved");
		}
	}

	protected void doDelete() {
		if (getSelectedDesign() == null) {
			JOptionPane.showMessageDialog(this, "Please select the design");
			return;
		}
		IncomeStatementDesign design = (IncomeStatementDesign) getSelectedDesign();
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

			ReportRow rootRow = new ReportGroup("Income Statement Rows", false);
			DefaultTreeModel modelRow = new DefaultTreeModel(rootRow);
			designRowsTree.setModel(modelRow);

			JOptionPane.showMessageDialog(this, "Successfully deleted");
		}
	}

}