/**
 *
 */
package pohaci.gumunda.titis.accounting.cgui.reportdesign;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import pohaci.gumunda.titis.accounting.entity.reportdesign.BalanceSheetDesign;
import pohaci.gumunda.titis.accounting.entity.reportdesign.Design;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportGroup;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportRow;
import pohaci.gumunda.titis.accounting.logic.reportdesign.BalanceSheetLogic;

/**
 * @author dark-knight
 *
 */
public class BalanceSheetDesginerPanel extends ReportDesignPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public BalanceSheetDesginerPanel(Connection connection, long sessionid) {
		this.connection = connection;
		this.sessionid = sessionid;
		this.title = "Balance Sheet Rows";
		setDefaultEntity();
		constructComponent();
		init();
	}

	protected void setDefaultEntity() {
		root = new BalanceSheetDesign("Balance Sheet Design");
		designTree = new JTree(
				new BalanceSheetDesign("Balance Sheet Design"));
		designRowsTree = new JTree(new ReportGroup("Balance Sheet Rows", false));
		logic = new BalanceSheetLogic(connection, sessionid);
		rootReportRow = new ReportGroup("Balance Sheet Rows", false);
	}

	protected void doAdd() {
		ReportDesignDlg dlg = new ReportDesignDlg(
				pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
				connection, sessionid, "Balance Sheet Design",
				BalanceSheetDesign.class);
		dlg.setVisible(true);
		if (dlg.getResponse() == JOptionPane.OK_OPTION) {
			DefaultTreeModel model = (DefaultTreeModel) designTree.getModel();

			BalanceSheetDesign design = (BalanceSheetDesign) dlg
					.getDesign();
			design.setRootRow(new ReportGroup("Balance Sheet Rows", false));
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
		BalanceSheetDesign design = (BalanceSheetDesign) getSelectedDesign();
		if (design == designTree.getModel().getRoot())
			return;

		ReportDesignDlg dlg = new ReportDesignDlg(
				pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
				connection, sessionid, "Balance Sheet Design",
				BalanceSheetDesign.class);
		dlg.setDesign(design);
		dlg.setVisible(true);
		if (dlg.getResponse() == JOptionPane.OK_OPTION) {
			DefaultTreeModel model = (DefaultTreeModel) designTree.getModel();

			design = (BalanceSheetDesign) dlg.getDesign();
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
		BalanceSheetDesign design = (BalanceSheetDesign) getSelectedDesign();
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

			ReportRow rootRow = new ReportGroup("Balance Sheet Rows", false);
			DefaultTreeModel modelRow = new DefaultTreeModel(rootRow);
			designRowsTree.setModel(modelRow);

			JOptionPane.showMessageDialog(this, "Successfully deleted");
		}
	}

	/* (non-Javadoc)
	 * @see pohaci.gumunda.titis.accounting.cgui.reportdesign.ReportDesignPanel#updatePositiveBalance(pohaci.gumunda.titis.accounting.entity.reportdesign.Design)
	 */
	protected void updatePositiveBalance(Design design) {
		ReportRow root = design.getRootRow();
		ArrayList list = new ArrayList();
		list.add(root);

		ArrayList allReportRowList = ReportRow.createStandardReportList(list);
		Iterator iterator = allReportRowList.iterator();
		while(iterator.hasNext()){
			ReportRow row = (ReportRow) iterator.next();
			row.setNormalDebit(design.getPositiveBalance()==0);
		}
	}


}
