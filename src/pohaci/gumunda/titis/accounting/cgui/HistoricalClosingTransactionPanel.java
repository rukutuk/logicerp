/**
 * 
 */
package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JDialog;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import pohaci.gumunda.titis.accounting.entity.ClosingTransaction;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

/**
 * @author dark-knight
 * 
 */
public class HistoricalClosingTransactionPanel extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel buttonPanel = null;

	private JScrollPane scrollPane = null;

	private JTable table = null;

	private JButton okButton = null;

	private DefaultTableModel dataModel;

	private Connection connection;
	
	private Frame owner = null;

	/**
	 * This is the default constructor
	 * 
	 * @param title
	 * @param owner
	 */
	public HistoricalClosingTransactionPanel(Frame owner, String title,
			Connection connection) {
		
		super(owner, title, true);
		this.connection = connection;
		this.owner = owner;
		initialize();
		initializeData();
	}

	private void initializeData() {
		GenericMapper mapper = MasterMap
				.obtainMapperFor(ClosingTransaction.class);
		mapper.setActiveConn(this.connection);

		List list = mapper.doSelectAll();
		Iterator iterator = list.iterator();

		getDataModel().setRowCount(0);
		int no = 0;
		while (iterator.hasNext()) {
			ClosingTransaction ct = (ClosingTransaction) iterator.next();
			getDataModel().addRow(
					new Object[] { new Integer(++no), ct.getTransactionDate(),
							ct.getPeriodFrom(), ct.getPeriodTo() });
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(329, 270);
		this.setContentPane(getJContentPane());
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
			jContentPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(
					5, 5, 5, 5));
			jContentPane.add(getButtonPanel(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(getScrollPane(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.RIGHT);
			buttonPanel = new JPanel();
			buttonPanel.setLayout(flowLayout);
			buttonPanel.add(getOkButton(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes scrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTable());
		}
		return scrollPane;
	}

	/**
	 * This method initializes table
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getTable() {
		if (table == null) {
			table = new JTable() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			table.setModel(getDataModel());
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

			table.getColumnModel().getColumn(0).setPreferredWidth(30);
			table.getColumnModel().getColumn(1).setPreferredWidth(100);
			table.getColumnModel().getColumn(2).setPreferredWidth(100);
		}
		return table;
	}

	private DefaultTableModel getDataModel() {
		if (dataModel == null) {
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("No");
			model.addColumn("Closing Date");
			model.addColumn("Period From");
			model.addColumn("Period To");

			dataModel = model;
		}
		return dataModel;
	}

	/**
	 * This method initializes okButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("OK");
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doOK();
				}
			});
		}
		return okButton;
	}

	private void doOK() {
		setVisible(false);
	}
	
	public void setVisible( boolean flag ){
	    Rectangle rc = owner.getBounds();
	    Rectangle rcthis = getBounds();
	    setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
	              (int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
	              (int)rcthis.getWidth(), (int)rcthis.getHeight());

	    super.setVisible(flag);
	  }

} //  @jve:decl-index=0:visual-constraint="10,10"
