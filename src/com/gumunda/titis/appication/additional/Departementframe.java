package com.gumunda.titis.appication.additional;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import pohaci.gumunda.aas.dbapi.ConnectionManager;
import pohaci.gumunda.cgui.GumundaMainFrame;

public class Departementframe extends  GumundaMainFrame implements ActionListener{
	/**
	 * 
	 */
	static JComboBox m_combotable;
	JButton m_ok;
	JButton m_update;
	JButton m_close;
	Connection m_conn = null;
	
	private static final long serialVersionUID = 1L;
	JPanel m_centerPanel = new JPanel();
	TransactionTable m_table;
	JPanel m_tablePanel;
	SimpleDateFormat format; 
	private static final String[] m_tableName = {"pmtothers","memjournalstrd","memjournalnonstrd",
		"expensesheet","pmtoperationalcost","PayrollPmtTax21Unit"};
	private static final String[] m_tableDetail = {"pmtothersdetail","memjournalstrddet","memjournalnonstrddet",
		"expensesheetdetail","pmtoperationalcostdetail","PayrollPmtTax21Unitdet"};
	private static final String[] m_tableIdDetail = {"pmtothers","memjournalstrd","memjournalnonstrd",
		"expensesheet","pmtoperationalcost","PayrollPmtTax21Unit"};
	
	public Departementframe() {
		setConn();
		setSize(800, 500);
		setResizable(false);
		format = new SimpleDateFormat("dd-MM-yyyy");
		constructComponents();
	}
	
	void adjustPosition() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(dim.width, dim.height );
	}
	
	void constructComponents() {
		m_table = new TransactionTable();
		m_ok = new JButton("OK");
		m_update = new JButton("Update");
		m_close = new JButton("Close");
		
		m_combotable = new JComboBox(m_tableName);
		
		m_ok.addActionListener(this);
		m_update.addActionListener(this);		
		m_close.addActionListener(this);
		
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
		leftPanel.add(m_combotable, gridBagConstraints);	
		
		gridBagConstraints.gridx = 1;
		leftPanel.add(new JLabel(" "), gridBagConstraints);
		
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
		rightPanel.add(m_close, gridBagConstraints);
		
		
		
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
		
		northLeftPanel.setPreferredSize(new Dimension(300, 40)); // size northLeftPanel
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
	
	boolean isOk(int row){
		Boolean bl = (Boolean) m_table.getValueAt(row,6);
		return bl.booleanValue();
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_ok) {			
			onOk();
		} 
		else if (e.getSource() == m_update){
			onUpdate();
			onOk();
		}
		else if (e.getSource()==m_close){
			try {
				m_conn.close();
				System.exit(0);
				dispose();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
		}
	}
	
	private void onUpdate() {
		try {
			setConn();
			int row = m_table.getRowCount();
			for (int i=0;i<row;i++){
				if (isOk(i)){
					String idrow = (String) m_table.getValueAt(i,0);
					String iddept= (String) m_table.getValueAt(i,1);
					m_conn.setAutoCommit(false);
					Statement stm = m_conn.createStatement();
					updateData(stm,iddept,idrow);
					m_conn.commit();
				}
			}
		} catch (SQLException e1) {
			try {
				m_conn.rollback();
				m_conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
	}
	
	private void onOk() {
		try {
			setConn();
			Statement stm = m_conn.createStatement();
			m_conn.setAutoCommit(false);
			m_table.removeRow();
			m_table.setData(stm);
			
		} catch (SQLException e1) {
		}
	}
	
	public static void updateData(Statement stm,String iddept,String idrow) {
		try {
			int val =   m_combotable.getSelectedIndex();
			String query ="update " + m_tableDetail[val] + " set department=" + iddept + " where " +m_tableName[val]+ "=" + idrow;
			stm.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private class TransactionTable extends JTable{
		private static final long serialVersionUID = 1L;
		DefaultTableModel model = new buttomTabbleRcvOthersModel();
		protected TransactionTable() {
			model.addColumn("Row Id");
			model.addColumn("Departemnt Id");
			model.addColumn("Reference No");
			model.addColumn("Tanggal");
			model.addColumn("Descripsi");
			model.addColumn("Department");
			model.addColumn("Pilih");
			setModel(model);    			
			getColumnModel().getColumn(0).setPreferredWidth(50);
			getColumnModel().getColumn(0).setMaxWidth(50);
			getColumnModel().getColumn(1).setPreferredWidth(90);
			getColumnModel().getColumn(1).setMaxWidth(90);
			getColumnModel().getColumn(2).setPreferredWidth(90);
			getColumnModel().getColumn(2).setMaxWidth(90);
			getColumnModel().getColumn(3).setPreferredWidth(90);
			getColumnModel().getColumn(3).setMaxWidth(90);
			getColumnModel().getColumn(5).setPreferredWidth(100);
			getColumnModel().getColumn(5).setMaxWidth(100);
			getColumnModel().getColumn(6).setPreferredWidth(40);
			getColumnModel().getColumn(6).setMaxWidth(40);
			
		}
		
		public Class getColumnClass(int columnindex){
			if ( columnindex == 6) return Boolean.class;
			return super.getColumnClass(columnindex);
		}
		
		void setData(Statement stm){
			ResultSet rs;
			try {
				String table =  (String) m_combotable.getSelectedItem();
				String nullproject ="";
				if (table.equals(m_tableName[2]) || table.equals(m_tableName[3])){
					nullproject = "and project is null";
				}
				String query ="select a.autoindex id,a.transactiondate,a.referenceno,a.description,b.autoindex,b.name from " + table + " a left join organization b " +
				"on a.department=b.autoindex where a.department is not null and a.departmentgroup=0 " + nullproject + " order by a.autoindex";
				System.out.println(query);
				rs = stm.executeQuery(query);
				while (rs.next()) { 
					String iddept="";
					String dept="";
					if (rs.getString("autoindex")!=null){
						iddept = rs.getString("autoindex");
						dept = rs.getString("name");
					}
					if(!isDepartement(rs.getString("ID"),iddept)){
						model.addRow(new Object[]{rs.getString("ID"),iddept,rs.getString("Referenceno"),
								format.format(rs.getDate("transactiondate")),
								rs.getString("Description"),dept,new Boolean (false)});
					}
				}				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
		
		public void removeRow(){
			/*int row = getRowCount();
			//for (int i=0;i<row;i++){
			for (int i=0;i<row;i++){
				model.removeRow(i);
			}*/
			int row = getRowCount()-1;
			for (int i=row;i>=0;i--){
				model.removeRow(i);
			}
		}		
	}
	
	class buttomTabbleRcvOthersModel extends DefaultTableModel{
		private static final long serialVersionUID = 1L;
		
		public boolean isCellEditable(int row, int col) {			
			if(col == 0 || col==1 || col==2  ||  col==4 || col==5)
				return false;
			return true;
		}
	}
	
	public boolean isDepartement(String idrow,String iddept){
		boolean cek=false;
		try {
			
			int index = m_combotable.getSelectedIndex();
			Statement stm = m_conn.createStatement();
			m_conn.setAutoCommit(false);
			String query ="select * from " + m_tableDetail[index] + " where " + m_tableIdDetail[index] + "=" + idrow + " and  (department<>" + iddept +" or department is null)"; 
			ResultSet rs = stm.executeQuery(query);
			if (rs.next())
				cek = false;
			else
				cek = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cek;
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

	public void init() {
		// TODO Auto-generated method stub
	}

	public void deInit() {
		// TODO Auto-generated method stub
	}
}
