package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import pohaci.gumunda.cgui.BaseTableCellRenderer;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;

public class ManageTransactionPeriodPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private Connection m_conn = null;
	private long m_sessionId = -1;
	
	private JButton m_saveBtn = null;
	private JButton m_addBtn = null;
	private JButton m_editBtn = null;
	private JButton m_deleteBtn = null;
	private JButton m_openBtn = null;
	private JButton m_closeBtn = null;
	private JPanel m_unitPanel = null;
	private JPanel m_buttonPanel = null;
	private JPanel m_transactionPanel = null;
	
	private JTable m_table = null;
	private DefaultTableModel m_dataModel = null;
	private JScrollPane m_scrollPane = null;
	private UnitPicker m_unitPicker = null;

	//private SimpleDateFormat m_dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	//private SimpleDateFormat m_dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		
	public ManageTransactionPeriodPanel(Connection connection, long sessionId){
		this.m_conn = connection;
		this.m_sessionId = sessionId;
		this.m_table = new JTable(){
			private static final long serialVersionUID = -2516427721936211352L;

			public boolean isCellEditable(int row, int column) {
				return false;
		}};
		
		setTableHeader();
		constructComponent();
		viewTransactionPeriod();
	}
	
	public void constructComponent(){
		JLabel unitLbl = new JLabel("Default Unit ");
		
		this.m_saveBtn = new JButton("Save");
		this.m_saveBtn.addActionListener(this);
		this.m_addBtn = new JButton("Add");
		this.m_addBtn.addActionListener(this);
		this.m_deleteBtn = new JButton("Delete");
		this.m_deleteBtn.addActionListener(this);
		this.m_editBtn = new JButton("Edit");
		this.m_editBtn.addActionListener(this);
		this.m_closeBtn = new JButton("Close");
		this.m_closeBtn.addActionListener(this);
		this.m_openBtn = new JButton("Open");
		this.m_openBtn.addActionListener(this);
		
		this.m_unitPicker = new UnitPicker(m_conn,m_sessionId);						
		setDefaultUnit();
		
		this.m_unitPanel = new JPanel();		
		this.m_buttonPanel = new JPanel();
		this.m_transactionPanel = new JPanel();
		this.m_scrollPane = new JScrollPane();
		//this.m_scrollPane.setPreferredSize(new Dimension(150, 200));
		this.m_scrollPane.setPreferredSize(new Dimension(150, 300));
		this.m_scrollPane.getViewport().add(this.m_table);
		
		setBorder(this.m_unitPanel,"Default Setting");
		
		this.m_buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.m_buttonPanel.add(this.m_addBtn);
		this.m_buttonPanel.add(this.m_editBtn);
		this.m_buttonPanel.add(this.m_deleteBtn);
		this.m_buttonPanel.add(this.m_closeBtn);
		//this.m_buttonPanel.add(this.m_openBtn);
		this.m_buttonPanel.add(this.m_scrollPane);
		setBorder(this.m_buttonPanel,"Transaction Period");
		
		this.m_unitPanel.setPreferredSize(new Dimension(100,100));
		this.m_unitPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(1, 6, 1, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.m_unitPanel.add(unitLbl, gridBagConstraints);
				
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new Insets(2, 6, 0, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.m_unitPanel.add(this.m_saveBtn, gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		this.m_unitPanel.add(this.m_unitPicker , gridBagConstraints);
		
		JPanel tempPanel2= new JPanel();		
		JPanel temppanel = new JPanel();
		temppanel.setPreferredSize(new Dimension(100,100));
		temppanel.setLayout(new GridLayout(1,2));
		
		gridBagConstraints = new GridBagConstraints();		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		temppanel.add(m_unitPanel,gridBagConstraints);
		
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.fill = GridBagConstraints.REMAINDER;
		temppanel.add(tempPanel2,gridBagConstraints);
		
		this.m_transactionPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.insets = new Insets(10, 3, 1, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.m_transactionPanel.add(this.m_buttonPanel, gridBagConstraints);
			
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;		
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		this.m_transactionPanel.add(this.m_scrollPane, gridBagConstraints);	
		
		setLayout(new BorderLayout());
		//add(temppanel, BorderLayout.NORTH);
		add(this.m_transactionPanel, BorderLayout.NORTH);
	}
	
	private void setBorder(JPanel panel,String theme){
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), theme ,
				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
	}
	public void actionPerformed(ActionEvent e) {
		int row = -1;
		Date from = null;
		Date to = null;
		if(e.getSource() == m_addBtn || e.getSource() == m_editBtn){
			if(e.getSource() == m_editBtn){
				row = m_table.getSelectedRow();
				if(row != -1){
					from = (Date) m_table.getValueAt(row,1); 
					to = (Date) m_table.getValueAt(row,2);
					String status = (String) m_table.getValueAt(row,3);  
					if(!status.equals("Closed")){
						TransactionPeriodAddDialog editTrans = new TransactionPeriodAddDialog(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
							"Edit Transaction Period", m_conn, m_sessionId, m_table, from, to);
						editTrans.setVisible(true);
						if(editTrans.isValid())
							editRow(editTrans);
					}
					else JOptionPane.showMessageDialog(this,"This transaction period was closed. Status must be empty to be edited");
				}
			}
			else if(e.getSource() == m_addBtn){
				TransactionPeriodAddDialog addTrans = new TransactionPeriodAddDialog(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
						"Add Transaction Period", m_table, m_conn, m_sessionId);
				addTrans.setVisible(true);
				if(addTrans.isValid())
					addTransactionPeriod(addTrans);
			}
		}
		else if(e.getSource() == this.m_deleteBtn){
			row = m_table.getSelectedRow();
			if(row != -1){
				String status = (String) m_table.getValueAt(row,3);
				from = (Date) m_table.getValueAt(row,1); 
				to = (Date) m_table.getValueAt(row,2);
				if(status.equals("Empty"))
					deleteRow();
				else JOptionPane.showMessageDialog(this,"Status must be Empty");
			}
		}
		else if(e.getSource() == this.m_closeBtn){
			row = m_table.getSelectedRow();
			if(row != -1){
				String status = (String) m_table.getValueAt(row, 3);
				if(!status.equals("Closed")){
					from = (Date) m_table.getValueAt(row,1); 
					to = (Date) m_table.getValueAt(row,2);
					TransactionPeriodCloseDialog closeDialog = new TransactionPeriodCloseDialog(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
							"Close Transaction Period", m_conn, m_sessionId, from, to);
					closeDialog.setVisible(true);
					if(closeDialog.getStatus().equals("Closed"))
						closeRow();
				}
				else JOptionPane.showMessageDialog(this,"This transaction period was closed");
			}
		}
		/*else if(e.getSource() == this.m_openBtn){
			row = m_table.getSelectedRow();
			if(row != -1){
				String status = (String) m_table.getValueAt(row, 3);
				if(!status.equals("close")){
					from = (Date) m_table.getValueAt(row,1);
					to = (Date) m_table.getValueAt(row,2);
					TransactionPeriodOpenDialog openDialog = new TransactionPeriodOpenDialog(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
							"Open Transaction Period", m_conn, m_sessionId, from, to, m_table);
					if(openDialog.getFlag())
						openDialog.setVisible(true);
					if(openDialog.getFlag() && openDialog.getStatus().equals("open"))
						openRow();
					
				}
				else JOptionPane.showMessageDialog(this,"This transaction period was closed");
			}
		}*/
	}
	
	private void setTableHeader(){
		m_dataModel = new DefaultTableModel();
		m_dataModel.addColumn("No");
		m_dataModel.addColumn("From Date");
		m_dataModel.addColumn("To Date");
		m_dataModel.addColumn("Status");
		
		m_table.setModel(m_dataModel);
		m_table.getColumnModel().getColumn(0).setPreferredWidth(20);
		m_table.getColumnModel().getColumn(0).setMaxWidth(20);
		m_table.getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
	}
	
	public void addTransactionPeriod(TransactionPeriodAddDialog dlg){
		String status = "Empty";
		if(dlg.getFromDate()!= null && dlg.getToDate()!= null){
			TransactionPeriod tPeriod = new TransactionPeriod(dlg.getFromDate(), dlg.getToDate(), status);;
			if(isAvailable(tPeriod)){
				AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
				try {
					logic.createTransactionPeriod(m_sessionId,IDBConstants.MODUL_ACCOUNTING,tPeriod);
				} catch (Exception e) {
					e.printStackTrace();
				}
				viewTransactionPeriod();
			}
			else JOptionPane.showMessageDialog(this,"Data Sudah Ada");
		}
	}
	
	public void editRow(TransactionPeriodAddDialog dlg){
		int row = m_table.getSelectedRow();
		
		if(dlg.getFromDate()!= null && dlg.getToDate()!= null){
			AccountingSQLSAP sql = new AccountingSQLSAP();
			int index = 0;
			try {
				index = sql.getIndexTransactonPeriod(getSeletedFromDate(), getSeletedToDate(), m_conn);
				if(index!=0){
					TransactionPeriod tPeriod = new TransactionPeriod(index, dlg.getFromDate(), dlg.getToDate(), 
						(String) m_table.getValueAt(row, 3));
					sql.updateTransactionPeriod(index, tPeriod, m_conn);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			viewTransactionPeriod();
		}
	}
	
	public void closeRow(){
		int row = m_table.getSelectedRow();
		Date from =(Date) m_table.getValueAt(row,1);
		Date to = (Date) m_table.getValueAt(row,2);
		
		if(from!= null && to!= null){
			AccountingSQLSAP sql = new AccountingSQLSAP();
			int index = 0;
			try {
				index = sql.getIndexTransactonPeriod(getSeletedFromDate(), getSeletedToDate(), m_conn);
				if(index!=0){
					sql.updateStatusTransactionPeriod(index, "Closed", m_conn);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			viewTransactionPeriod();
		}
	}
	
	public void openRow(){
		int row = m_table.getSelectedRow();
		String from =(String) m_table.getValueAt(row,1);
		String to = (String) m_table.getValueAt(row,2);
		
		if(from!= null && to!= null){
			AccountingSQLSAP sql = new AccountingSQLSAP();
			int index = 0;
			try {
				index = sql.getIndexTransactonPeriod(getSeletedFromDate(), getSeletedToDate(), m_conn);
				if(index!=0){
					sql.updateStatusTransactionPeriod(index, "open", m_conn);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			viewTransactionPeriod();		
		}
	}
	
	public void viewTransactionPeriod(){
		AccountingSQLSAP logic = new AccountingSQLSAP();
		TransactionPeriod[] tPeriod;
		m_dataModel.setRowCount(0);
		try {
			tPeriod = logic.getAllTransactonPeriod(m_conn);
			int j=0;
			for(int i = 0; i < tPeriod.length; i ++) {
		        m_dataModel.addRow(new Object[]{
		        		String.valueOf(++j),tPeriod[i].getStartDate(),
						tPeriod[i].getEndDate(), tPeriod[i].getStatus()});
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteRow(){
		int row = m_table.getSelectedRow();
		String status = (String) m_table.getValueAt(row,3); 
		if(status.equalsIgnoreCase("empty")){
			AccountingSQLSAP sql = new AccountingSQLSAP();
			int index = 0;
			try {
				index = row + 1;
				index = sql.getIndexTransactonPeriod(getSeletedFromDate(), getSeletedToDate(), m_conn);
				if(index!=0){
					TransactionPeriod tPeriod = new TransactionPeriod(index);
					sql.deleteTransactionPeriod(tPeriod, m_conn);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			viewTransactionPeriod();
		}
	}
		
	public Date getSeletedFromDate(){
		int row = m_table.getSelectedRow();
		Date dtFrom =  (Date) m_table.getValueAt(row,1);;
		
		return dtFrom;
	}
	
	public Date getSeletedToDate(){
		int row = m_table.getSelectedRow();
		
		Date dtTo = (Date) m_table.getValueAt(row,2);;
		
		return dtTo;
	}
	
	/*Untuk mengecek apakah transaction period telah ada dalam database*/
	public boolean isAvailable(TransactionPeriod tPeriod){
		int index = 0;
		AccountingSQLSAP sql = new AccountingSQLSAP();
		try {
			index = sql.getIndexTransactonPeriod(tPeriod.getStartDate(), tPeriod.getEndDate(), m_conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(index == 0)
			return true;
		else return false;
	}
	
	public void setDefaultUnit(){
		Unit unit = DefaultUnit.createDefaultUnit(m_conn, m_sessionId);
		m_unitPicker.setObject(unit);
		
		
		// moved to DefaultUnit.createDefaultUni(connection, sessionId);
		/*AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
		try {
			DefaultUnit dUnit = logic.getDefaultUnit(m_sessionId, IDBConstants.MODUL_ACCOUNTING);
			if(dUnit!=null){
				Unit unit = dUnit.getUnit();
				m_unitPicker.setObject(unit);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
	}
}
