/**
 *
 */
package pohaci.gumunda.titis.accounting.helperutil;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import pohaci.gumunda.aas.dbapi.ConnectionManager;
import pohaci.gumunda.titis.accounting.entity.PmtProjectCost;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

/**
 * @author dark-knight
 *
 */
public class InvoiceDBUtilFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel mainPanel = null;
	private JPanel centerPanel = null;
	private JPanel southPanel = null;
	private JButton updateTableButton = null;
	private JButton runQueryButton = null;
	private JButton implementButton = null;
	private JScrollPane mainScrollPane = null;
	private JTable mainTable = null;
	private JTextField filterTextField = null;
	private ConnectionManager connectionManager;
	private Connection connection;
	private List list = new ArrayList();


	/**
	 * This method initializes mainPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(new BorderLayout());
			mainPanel.add(getCenterPanel(), BorderLayout.CENTER);
			mainPanel.add(getSouthPanel(), BorderLayout.SOUTH);
		}
		return mainPanel;
	}

	/**
	 * This method initializes centerPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getCenterPanel() {
		if (centerPanel == null) {
			centerPanel = new JPanel();
			centerPanel.setLayout(new BorderLayout());
			centerPanel.add(getMainScrollPane(), BorderLayout.CENTER);
			centerPanel.add(getFilterTextField(), BorderLayout.NORTH);
		}
		return centerPanel;
	}

	/**
	 * This method initializes southPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getSouthPanel() {
		if (southPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			southPanel = new JPanel();
			southPanel.setLayout(flowLayout);
			southPanel.add(getUpdateTableButton(), null);
			southPanel.add(getRunQueryButton(), null);
			southPanel.add(getImplementButton(), null);
		}
		return southPanel;
	}

	/**
	 * This method initializes updateTableButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getUpdateTableButton() {
		if (updateTableButton == null) {
			updateTableButton = new JButton();
			updateTableButton.setText("Update Table Structure");
			updateTableButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					updateTableStructure();
				}
			});
		}
		return updateTableButton;
	}

	/**
	 *
	 */
	private void updateTableStructure() {
		InvoiceSQLUtil sqlUtil = new InvoiceSQLUtil(connection);

		try {
			if (!sqlUtil.ifTotalColumnExists()) {
				sqlUtil.addTotalColumn();

				JOptionPane.showMessageDialog(this, "Column 'TOTAL' has been added");
			} else {
				JOptionPane.showMessageDialog(this, "There is already column 'TOTAL' in table");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error while updating table structure");
			e.printStackTrace();
		}
	}

	/**
	 * This method initializes runQueryButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getRunQueryButton() {
		if (runQueryButton == null) {
			runQueryButton = new JButton();
			runQueryButton.setText("Count Total");
			runQueryButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					countTotal();
				}
			});
		}
		return runQueryButton;
	}

	/**
	 *
	 */
	private void countTotal() {
		InvoiceSQLUtil sqlUtil = new InvoiceSQLUtil(connection);

		try {
			if (sqlUtil.ifTotalColumnExists()) {
				list = sqlUtil.findNoTotalRecords();

				list = sqlUtil.countTotal(list);

				// gambarin
				EventList eventList = GlazedLists.eventList(list);

				TextFilterator filterator = new TextFilterator() {

					public void getFilterStrings(List list, Object obj) {
						PmtProjectCost payment = (PmtProjectCost) obj;
						list.add(payment.getReferenceNo());
						list.add(payment.getTransactionDate().toString());
						list.add(payment.getPaymentSource());
						list.add(payment.getPayTo());
						list.add(payment.getDescription());
					}

				};

				FilterList filterList = new FilterList(eventList,
						new TextComponentMatcherEditor(filterTextField, filterator));
				EventTableModel tableModel = new EventTableModel(
						filterList, new PmtProjectCostTableFormat());

				EventSelectionModel selectionModel = new EventSelectionModel(filterList);
				getMainTable().setModel(tableModel);
				getMainTable().setSelectionModel(selectionModel);
			} else {
				JOptionPane.showMessageDialog(this, "Please add column 'TOTAL' first!");
			}
		} catch (HeadlessException e) {
			JOptionPane.showMessageDialog(this, "Error while counting total");
			e.printStackTrace();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error while counting total");
			e.printStackTrace();
		}
	}

	/**
	 * This method initializes implementButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getImplementButton() {
		if (implementButton == null) {
			implementButton = new JButton();
			implementButton.setText("Update Total");
			implementButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					updateTable();
				}
			});
		}
		return implementButton;
	}

	/**
	 *
	 */
	private void updateTable() {
		InvoiceSQLUtil sqlUtil = new InvoiceSQLUtil(connection);

		try {
			if (sqlUtil.ifTotalColumnExists()) {
				if (list.size()>0) {
					sqlUtil.updateTotal(list);
				} else {
					JOptionPane.showMessageDialog(this, "There is no data to update");
				}
			} else {
				JOptionPane.showMessageDialog(this, "Please add column 'TOTAL' first!");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error while updating table");
			e.printStackTrace();
		}
	}

	/**
	 * This method initializes mainScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getMainScrollPane() {
		if (mainScrollPane == null) {
			mainScrollPane = new JScrollPane();
			mainScrollPane.setViewportView(getMainTable());
		}
		return mainScrollPane;
	}

	/**
	 * This method initializes mainTable
	 *
	 * @return javax.swing.JTable
	 */
	private JTable getMainTable() {
		if (mainTable == null) {
			mainTable = new JTable();
		}
		return mainTable;
	}

	/**
	 * This method initializes filterTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getFilterTextField() {
		if (filterTextField == null) {
			filterTextField = new JTextField();
		}
		return filterTextField;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				InvoiceDBUtilFrame thisClass = new InvoiceDBUtilFrame();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public InvoiceDBUtilFrame() {
		super();
		initialize();
		initDB();

	}

	/**
	 *
	 */
	private void initDB() {
		try {
		      connectionManager = new ConnectionManager("sampurna");
		      connection = connectionManager.getConnection();
		    }
		    catch(Exception ex){
		      JOptionPane.showMessageDialog(this, ex.getMessage() + "\n DBUtil will be closed.");

		      System.exit(0);
		    }
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(453, 326);
		this.setContentPane(getJContentPane());
		this.setTitle("Payment Project Cost DBUtil");
	}

	/**
	 * This method initializes jContentPane
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getMainPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
