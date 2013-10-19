package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

public class TaxArt21PayrollComponentSelectionDlg extends JDialog implements
		ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JFrame m_mainframe;
	AllowenceMultiplierTable m_table;
	JButton m_addBt, m_editBt, m_saveBt, m_cancelBt, m_deleteBt;
	Connection m_conn = null;
	long m_sessionid = -1;
	boolean m_new = false, m_edit = false;
	int m_editedIndex = -1;
	private Vector saveObjectVector;
	private TaxArt21AdapterPayrollComponentTree PayrollComponentTree;

	public TaxArt21PayrollComponentSelectionDlg(JFrame owner, Connection conn,
			long sessionid) {
		super(owner, "Payroll Component", true);
		setSize(300, 400);
		m_mainframe = owner;
		m_conn = conn;
		m_sessionid = sessionid;

		constructComponent();
		initData();
	}

	void constructComponent() {
		JPanel buttonPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		saveObjectVector = new Vector();
//		saveVector = new Vector();

		m_table = new AllowenceMultiplierTable(m_conn, m_sessionid);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		m_addBt = new JButton("Add");
		m_addBt.addActionListener(this);
		buttonPanel.add(m_addBt);
		m_editBt = new JButton("Edit");
		m_editBt.addActionListener(this);
		buttonPanel.add(m_editBt);
		m_deleteBt = new JButton("Delete");
		m_deleteBt.addActionListener(this);
		buttonPanel.add(m_deleteBt);
		m_saveBt = new JButton("Save");
		m_saveBt.addActionListener(this);
		buttonPanel.add(m_saveBt);
		m_cancelBt = new JButton("Cancel");
		m_cancelBt.addActionListener(this);
		buttonPanel.add(m_cancelBt);

		m_addBt.setEnabled(true);
		m_editBt.setEnabled(false);
		m_saveBt.setEnabled(false);
		m_cancelBt.setEnabled(false);
		m_deleteBt.setEnabled(false);

		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
		centerPanel.add(new JScrollPane(m_table), BorderLayout.CENTER);
		centerPanel.add(buttonPanel, BorderLayout.SOUTH);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(centerPanel, BorderLayout.CENTER);
		setSize(300, 250);
	}

	public void initData() {
		DefaultTableModel model = (DefaultTableModel) m_table.getModel();
		model.setRowCount(0);

		try {

//			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
//			AllowenceMultiplier[] multiplier = logic.getAllAllowenceMultiplier(
//					m_sessionid, IDBConstants.MODUL_MASTER_DATA);

			for (int i = 0; i < saveObjectVector.size(); i++) {
				PayrollComponent paycomp = (PayrollComponent) saveObjectVector
						.get(i);
				model.addRow(new Object[] { paycomp.getDescription(),
						paycomp.getAccount().getCode()
						 });
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	public void setInitialData(Vector v) {
		saveObjectVector = v;
	}

	public void setVisible(boolean flag) {
		Rectangle rc = m_mainframe.getBounds();
		Rectangle rcthis = getBounds();
		setBounds((int) (rc.getWidth() - rcthis.getWidth()) / 2 + rc.x,
				(int) (rc.getHeight() - rcthis.getHeight()) / 2 + rc.y,
				(int) rcthis.getWidth(), (int) rcthis.getHeight());

		super.setVisible(flag);
	}

	void onNew() {
		m_new = true;

		DefaultTableModel model = (DefaultTableModel) m_table.getModel();
		model.addRow(new Object[] {});
		m_editedIndex = model.getRowCount() - 1;
		m_table.getSelectionModel().setSelectionInterval(m_editedIndex,
				m_editedIndex);
		// m_table.setValueAt(new Float(0.0), m_editedIndex, 2);

		m_addBt.setEnabled(false);
		m_editBt.setEnabled(false);
		m_saveBt.setEnabled(true);
		m_cancelBt.setEnabled(true);
		m_deleteBt.setEnabled(false);
	}

	void onEdit() {
		m_edit = true;
		m_editedIndex = m_table.getSelectedRow();
		String m_multiplier = (String) m_table.getValueAt(m_editedIndex,0);
		System.out.println(" m_editedIndex = "+m_editedIndex +", m_multiplier = "+m_multiplier );

		m_addBt.setEnabled(false);
		m_editBt.setEnabled(false);
		m_saveBt.setEnabled(true);
		m_cancelBt.setEnabled(true);
		m_deleteBt.setEnabled(false);
	}

	void onSave() {
		m_table.stopCellEditing();
		java.util.ArrayList list = new java.util.ArrayList();
		DefaultTableModel model = (DefaultTableModel) m_table.getModel();
		// Object objValue = model.getValueAt(m_editedIndex, 0);
//		Account account = null;
		Object objValue = PayrollComponentTree.getObject();
		PayrollComponent paycomp = null;
		if (objValue instanceof PayrollComponent)
			paycomp = (PayrollComponent) objValue;

		String desc = (String) model.getValueAt(m_editedIndex, 0);
		if (desc == null || desc.equals(""))
			list.add("P");

		String acc = (String) model.getValueAt(m_editedIndex, 1);
		if (acc == null || acc.equals(""))
			list.add("Account");

		String strexc = "Please insert :\n";
		String[] exception = new String[list.size()];
		list.toArray(exception);
		if (exception.length > 0) {
			for (int i = 0; i < exception.length; i++)
				strexc += exception[i] + "\n";
			JOptionPane.showMessageDialog(this, strexc, "Information",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		try {
//			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);

			if (m_new) {
				saveObjectVector.add(paycomp);
				writeToConsole();
				firePropertyChange("payComp", paycomp, null);
			} else if (m_edit) {
				saveObjectVector.setElementAt(paycomp,m_editedIndex);
				//saveObjectVector.add(paycomp);
				writeToConsole();
				firePropertyChange("payComp", paycomp, null);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		m_new = false;
		m_edit = false;
		m_addBt.setEnabled(true);
		m_editBt.setEnabled(true);
		m_saveBt.setEnabled(false);
		m_cancelBt.setEnabled(false);
		m_deleteBt.setEnabled(true);
	}

	void onCancel() {
		m_table.stopCellEditing();
		if (!pohaci.gumunda.titis.application.Misc.getConfirmation())
			return;

		DefaultTableModel model = (DefaultTableModel) m_table.getModel();
		if (m_new) {
			m_new = false;
			model.removeRow(m_editedIndex);
			writeToConsole();
			// firePropertyChange("payComp",paycomp,null);
		} else if (m_edit) {
			m_edit = false;
			//m_table.updateAllowenceMultiplier(m_multiplier, m_editedIndex);
			m_addBt.setEnabled(true);
			m_editBt.setEnabled(true);
			m_saveBt.setEnabled(false);
			m_cancelBt.setEnabled(false);
			m_deleteBt.setEnabled(true);
		}
	}

	void onDelete() {

		DefaultTableModel model = (DefaultTableModel) m_table.getModel();
		m_editedIndex = m_table.getSelectedRow();
		String akun = (String) model.getValueAt(m_editedIndex, 1);

		PayrollComponent Paycomp = null;
		/*
		 * AllowenceMultiplier multiplier = (AllowenceMultiplier) model
		 * .getValueAt(m_editedIndex, 0);
		 * 
		 * if (!pohaci.gumunda.titis.application.Misc.getConfirmation()) return;
		 * try { HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
		 * logic.deleteAllowenceMultiplier(m_sessionid,
		 * IDBConstants.MODUL_MASTER_DATA, multiplier.getIndex()); } catch
		 * (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(),
		 * "Warning", JOptionPane.WARNING_MESSAGE); return; }
		 */
		for (int i = 0; i < saveObjectVector.size(); i++) {
			Paycomp = (PayrollComponent) saveObjectVector.get(i);
			if (Paycomp.getAccount().getCode().equals(akun)) {
				break;
			}
		}

		saveObjectVector.removeElement(Paycomp);
		firePropertyChange("payComp", Paycomp, null);
		model.removeRow(m_editedIndex);
		writeToConsole();
	}

	private void writeToConsole() {
		for (int i = 0; i < saveObjectVector.size(); i++) {
			System.out.println(saveObjectVector.get(i));
		}
	}

	public void clearVector() {
		saveObjectVector.clear();
	}

	public Vector getSaveVector() {
		return saveObjectVector;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_addBt) {
			onNew();
		} else if (e.getSource() == m_editBt) {
			onEdit();
		} else if (e.getSource() == m_saveBt) {
			onSave();
		} else if (e.getSource() == m_cancelBt) {
			onCancel();
		} else if (e.getSource() == m_deleteBt) {
			onDelete();
		}
	}

	class AllowenceMultiplierTable extends JTable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private Connection m_conn;

		private long m_sessionid;

		public AllowenceMultiplierTable(Connection m_conn, long m_sessionid) {
			this.m_conn = m_conn;
			this.m_sessionid = m_sessionid;

			AllowenceMultiplierTableModel model = new AllowenceMultiplierTableModel();
			model.addColumn("Payroll Component");
			model.addColumn("Account");
			// model.addColumn("Multiplier");
			setModel(model);

			getColumnModel().getColumn(1).setPreferredWidth(80);
			getColumnModel().getColumn(1).setMaxWidth(80);
			// getColumnModel().getColumn(2).setCellRenderer(new
			// FloatCellRenderer(JLabel.CENTER));
			getSelectionModel().addListSelectionListener(model);
		}

		public TableCellEditor getCellEditor(int row, int col) {
			if (col == 0) {
				PayrollComponentTree = new TaxArt21AdapterPayrollComponentTree(
						m_mainframe, m_conn, m_sessionid);
				PayrollComponentTree.getTableCellEditorComponent(m_table,
						m_table.getSelectedRow(), 0);
				return PayrollComponentTree;
			} else
				return super.getCellEditor();
		}

		public void stopCellEditing() {
			TableCellEditor editor;
			if ((editor = getCellEditor()) != null)
				editor.stopCellEditing();
		}

		public void updateAllowenceMultiplier(AllowenceMultiplier multiplier,
				int row) {
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.removeRow(row);
			model.insertRow(row, new Object[] { multiplier,
					multiplier.getDescription(),
					new Float(multiplier.getMuliplier()) });
			getSelectionModel().setSelectionInterval(row, row);
		}
	}

	/**
	 * 
	 */
	class AllowenceMultiplierTableModel extends DefaultTableModel implements
			ListSelectionListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int col) {
			if ((m_new || m_edit) && row == m_editedIndex)
				return true;
			return false;
		}

		public void valueChanged(ListSelectionEvent e) {

			if (!e.getValueIsAdjusting()) {
				int iRowIndex = ((ListSelectionModel) e.getSource())
						.getMinSelectionIndex();

				if (m_new || m_edit) {
					if (iRowIndex != m_editedIndex)
						m_table.getSelectionModel().setSelectionInterval(
								m_editedIndex, m_editedIndex);
				} else {
					if (iRowIndex != -1) {
						m_addBt.setEnabled(true);
						m_editBt.setEnabled(true);
						m_saveBt.setEnabled(false);
						m_cancelBt.setEnabled(false);
						m_deleteBt.setEnabled(true);
					} else {
						m_addBt.setEnabled(true);
						m_editBt.setEnabled(false);
						m_saveBt.setEnabled(false);
						m_cancelBt.setEnabled(false);
						m_deleteBt.setEnabled(false);
					}
				}
			}
		}
	}

}