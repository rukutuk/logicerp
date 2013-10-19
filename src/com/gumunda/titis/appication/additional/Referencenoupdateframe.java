package com.gumunda.titis.appication.additional;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import pohaci.gumunda.aas.dbapi.ConnectionManager;
import pohaci.gumunda.cgui.GumundaMainFrame;

public class Referencenoupdateframe extends GumundaMainFrame implements ActionListener{
	/**
	 * 
	 */
	static JComboBox m_combotable;
	JButton m_ok;
	JButton m_update;
	JButton m_delete;
	JButton m_close;
	JButton m_transaction;
	JButton m_posted;
	JButton m_entity;
	Connection m_conn = null;
	
	private static final long serialVersionUID = 1L;
	JPanel m_centerPanel = new JPanel();
	TransactionTable m_table;
	JPanel m_tablePanel;
	SimpleDateFormat format;
	
	private static final String[] m_choose = {"entity","transaction"};
	private static final String[] m_tableName = {"PmtProjectCost","PmtOperationalCost","memjournalnonstrd","RcvESDiff","RcvUnitBankCashTrns","RcvEmpReceivable","Rcvloan","Rcvothers",
		"PmtCAIOUProject","PmtCAIOUProjectInstall","PmtCAIOUProjectReceive","PmtCAIOUProjectSettled",
		"PmtCAIOUOthers","PmtCAIOUOthersInstall","PmtCAIOUOthersReceive","PmtCAIOUOthersSettled","pmtcaproject","pmtcaothers","PmtESDiff","PmtUnitBankCashTrns","PmtEmpReceivable",
		"PmtLoan","PmtOthers","SalesAdvance","SalesInvoice","SalesARReceived","PurchaseApPmt","PurchaseReceipt","PayrollPmtEmpInsurance","PayrollPmtSlryHo",
		"PayrollPmtSlryUnit","PayrollPmtTax21Ho","PayrollPmtTax21Unit","ExpenseSheet","MemJournalStrd"};
	JOptionPane m_submit;
	JOptionPane m_post;
	
	public Referencenoupdateframe() {
		setConn();
		setResizable(false);
		setTitle("Menu Utama");
		format = new SimpleDateFormat("dd-MM-yyyy");
		constructComponents();
	}
	
	/*void adjustPosition() {
	 Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	 setSize(dim.width, dim.height );
	 }*/
	
	void constructComponents() {
		m_table = new TransactionTable();
		m_table.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if (e.getClickCount()==2){
					/*int i = m_table.getSelectedRow();
					 String status = (String) m_table.getValueAt(i,4);
					 String refno = (String) m_table.getValueAt(i,2);
					 String trans =  (String) m_table.getValueAt(i,1);
					 int intStatus=Integer.parseInt(status);
					 if (intStatus==3){						
					 int val =   m_combotable.getSelectedIndex();						
					 TransactionPostedReferencenoupdateframe frame =new TransactionPostedReferencenoupdateframe(GumundaMainFrame.getMainFrame(),
					 m_conn,m_choose[val],getValueFromAcctransaction( refno,trans));
					 frame.setVisible(true);
					 }*/
				}
			}
		});
		m_ok = new JButton("OK");
		m_update = new JButton("Update");
		m_close = new JButton("Close");
		m_transaction = new JButton("Transaction");
		m_posted = new JButton("Posted");
		m_delete = new JButton("Delete");
		
		m_combotable = new JComboBox(m_choose);
		
		m_ok.addActionListener(this);
		m_update.addActionListener(this);		
		m_close.addActionListener(this);
		m_transaction.addActionListener(this);
		m_posted.addActionListener(this);		
		m_delete.addActionListener(this);
		
		JLabel tableLbl = new JLabel("Table Name");		
		
		
		JPanel centerPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		JPanel northRightPanel = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel northLeftPanel = new JPanel(); 		   
		
		leftPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(10, 6, 0, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		leftPanel.add(tableLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		gridBagConstraints.insets = new Insets(10, 3, 1, 3);
		gridBagConstraints.gridwidth=2;
		leftPanel.add(m_combotable, gridBagConstraints);
		
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.insets = new Insets(10, 3, 1, 3);
		gridBagConstraints.gridwidth=1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		leftPanel.add(m_transaction, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		leftPanel.add(m_posted, gridBagConstraints);
		
		gridBagConstraints.gridx = 2;		
		leftPanel.add(m_close, gridBagConstraints);
		
		rightPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;	
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		rightPanel.add(m_ok, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;		
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		rightPanel.add(new JLabel("  "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;		
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		rightPanel.add(m_update, gridBagConstraints);
		

		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;		
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		rightPanel.add(new JLabel("  "), gridBagConstraints);
		
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 1;		
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		rightPanel.add(m_delete, gridBagConstraints);
		
		northLeftPanel.setLayout(new BorderLayout());
		northLeftPanel.add(leftPanel,BorderLayout.NORTH);	    
		
		northRightPanel.setLayout(new BorderLayout());
		northRightPanel.add(rightPanel,BorderLayout.NORTH);
		m_tablePanel = new JPanel();	 
		m_tablePanel.setLayout(new BorderLayout());
		m_tablePanel.setBorder(BorderFactory.createEtchedBorder());		
		m_tablePanel.add(new JScrollPane(m_table), BorderLayout.CENTER);
		
		centerPanel.setLayout(new GridBagLayout());// menentukan cartesius x,y
		gridBagConstraints = new GridBagConstraints(); 
		
		northLeftPanel.setPreferredSize(new Dimension(300, 100)); // size northLeftPanel
		gridBagConstraints.gridx = 0; 
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL; // mengikuti perbesaran component horizontal
		centerPanel.add(northLeftPanel, gridBagConstraints); // centerpanel diincludkan kompenent menurut gridbag
		
		gridBagConstraints.gridx = 1;
		gridBagConstraints.anchor = GridBagConstraints.NORTH; // meletakkan posisi keatas 
		centerPanel.add(northRightPanel, gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER; // mengisi sisa
		centerPanel.add(new JPanel(), gridBagConstraints);
		
		// panel buat mendorong agar keatas.	    
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH; // perbesarannya secara horisontal dan vertikal
		centerPanel.add(m_tablePanel, gridBagConstraints);
		getContentPane().setLayout(new BorderLayout());		
		getContentPane().add(centerPanel);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_ok) {			
			onOk();
		} 
		else if (e.getSource() == m_update){
			//onUpdate();
			//onOk();
		}
		else if (e.getSource() == m_transaction){
			int row =m_table.getSelectedRow();
			if (row>=0){				
				TransactionPostedReferencenoupdateframe frame =new TransactionPostedReferencenoupdateframe(GumundaMainFrame.getMainFrame(),m_conn,getParameter(row),m_tableName,"AccTransaction");
				frame.setVisible(true);
			}
		}
		else if (e.getSource() == m_posted){
			int row =m_table.getSelectedRow();
			if (row>=0){				
				TransactionPostedReferencenoupdateframe frame =new TransactionPostedReferencenoupdateframe(GumundaMainFrame.getMainFrame(),m_conn,getParameter(row),m_tableName,"TransactionPosted");
				frame.setVisible(true);
			}
		}
		else if (e.getSource()==m_close){
			try {
				System.exit(0);
				m_conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
		}
	}
	
	private void onOk() {
		try {
			String strChoose = (String)m_combotable.getSelectedItem();
			if (strChoose.equals(m_choose[1])){
				Statement stm = m_conn.createStatement();
				m_conn.setAutoCommit(false);
				m_table.removeRow();
				m_table.setDataTransaction(stm);
				stm.close();	
			}
		} catch (SQLException e1) {
		}
	}
	
	public Parameter getParameter(int row){		
		return new Parameter(
				(String)m_table.getValueAt(row,1),
				(String)m_table.getValueAt(row,2),
				(String)m_table.getValueAt(row,3),
				(String)m_table.getValueAt(row,4),
				(String)m_table.getValueAt(row,5));
	}
	
	
	private class TransactionTable extends JTable{
		private static final long serialVersionUID = 1L;
		DefaultTableModel model = new buttomTabbleRcvOthersModel();
		protected TransactionTable() {
			model.addColumn("Row Id");
			model.addColumn("Trans");
			model.addColumn("Reference No");
			model.addColumn("Status");
			model.addColumn("Tanggal");
			model.addColumn("Descripsi");
			model.addColumn("Entity");
			model.addColumn("Reference update");
			setModel(model);    			
			getColumnModel().getColumn(0).setPreferredWidth(50);
			getColumnModel().getColumn(0).setMaxWidth(50);
			getColumnModel().getColumn(1).setPreferredWidth(50);
			getColumnModel().getColumn(1).setMaxWidth(50);
			getColumnModel().getColumn(2).setPreferredWidth(120);
			getColumnModel().getColumn(2).setMaxWidth(120);
			getColumnModel().getColumn(3).setPreferredWidth(60);
			getColumnModel().getColumn(3).setMaxWidth(60);
			getColumnModel().getColumn(4).setPreferredWidth(80);
			getColumnModel().getColumn(4).setMaxWidth(80);
			getColumnModel().getColumn(6).setPreferredWidth(120);
			getColumnModel().getColumn(6).setMaxWidth(120);
			
		}
		
		void setDataTransaction(Statement stm){
			ResultSet rs;
			try {
				for (int i=0;i<m_tableName.length;i++){
					String query = getQuery(i);
					rs = stm.executeQuery(query);
					while (rs.next()) { 
						model.addRow(new Object[]{rs.getString("autoindex"),rs.getString("trans"),rs.getString("Referenceno"),
								rs.getString("status"),
								format.format(rs.getDate("transactiondate")),rs.getString("Description"),m_tableName[i],rs.getString("Referenceno")});
					}				
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
		
		private String getQuery(int i) {
			String query ="select * from " + m_tableName[i] + " where referenceno in(select referenceno from (select referenceno,count(*) jumlah,void  from acctransaction where void is false group by referenceno,void) where jumlah>1) and status>0 order by transactiondate asc";
			return query;
		}
		
		public void removeRow(){
			int row = getRowCount()-1;
			for (int i=row;i>=0;i--){
				model.removeRow(i);
			}
		}		
	}
	
	public void setConn(){
		try {
			ConnectionManager m_connectionManager = new ConnectionManager("sampurna");
			m_conn = m_connectionManager.getConnection();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	class buttomTabbleRcvOthersModel extends DefaultTableModel{
		private static final long serialVersionUID = 1L;
		
		public boolean isCellEditable(int row, int col) {			
			if(col == 0 || col==1 || col==2  || col==3  || col==4 || col==5 || col==6 )
				return false;
			return true;
		}
	}
	
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	public void deInit() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
