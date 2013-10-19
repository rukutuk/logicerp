package com.gumunda.titis.appication.additional;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import pohaci.gumunda.aas.dbapi.ConnectionManager;

public class TransactionPostedReferencenoupdateframe extends JDialog implements ActionListener{
	/**
	 * 
	 */
	JButton m_update;
	JButton m_close;
	JButton m_delete;
	Connection m_conn = null;
	
	private static final long serialVersionUID = 1L;
	JPanel m_centerPanel = new JPanel();
	TransactionTable m_table;
	JPanel m_tablePanel;
	SimpleDateFormat format; 
	JOptionPane m_submit;
	JOptionPane m_post;
	String m_trans;
	String m_desc="";
	JFrame m_mainframe;
	Parameter m_paramaeter;
	String m_sign;
	
/*	public TransactionPostedReferencenoupdateframe(JFrame owner,Connection conn,String tablename,ArrayList list) {
		//super(owner, null, true);
		m_conn=conn;
		m_mainframe=owner;
		m_tablename=tablename;
		//setListToString(list);
		setConn();
		setSize(750, 450);
		setResizable(false);
		format = new SimpleDateFormat("dd-MM-yyyy");
		constructComponents();
		onOk();
	}*/
	
	String[] m_tablename;
	public TransactionPostedReferencenoupdateframe(JFrame owner,Connection conn,Parameter parameter,String[] tablename,String sign) {
		//super(owner, null, true);
		m_conn=conn;
		m_mainframe=owner;
		m_paramaeter = parameter;
		m_sign=sign;
		m_tablename =tablename;
		setSize(750, 450);
		setTitle(m_sign);
		setResizable(false);
		format = new SimpleDateFormat("dd-MM-yyyy");
		constructComponents();
		onOk();
	}
	
	void constructComponents() {
		m_table = new TransactionTable();
		m_update = new JButton("Update");
		m_close = new JButton("Close");
		m_delete = new JButton("Delete");
		
		m_update.addActionListener(this);		
		m_close.addActionListener(this);
		m_delete.addActionListener(this);
		
		JPanel centerPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		JPanel northRightPanel = new JPanel();
		JPanel northLeftPanel = new JPanel(); 		   
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints = new GridBagConstraints();
		
		rightPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;	
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		rightPanel.add(m_update, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;		
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		rightPanel.add(new JLabel("  "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;		
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		rightPanel.add(m_delete, gridBagConstraints);	
		
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;		
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		rightPanel.add(new JLabel("  "), gridBagConstraints);
		
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 1;		
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		rightPanel.add(m_close, gridBagConstraints);	
		
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;		
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		rightPanel.add(new JLabel("  "), gridBagConstraints);
		
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 1;		
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		rightPanel.add(m_close, gridBagConstraints);
		
		
		
		northLeftPanel.setLayout(new BorderLayout());
		//northLeftPanel.add(leftPanel,BorderLayout.NORTH);	    
		
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
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_update){
			onUpdate();
			onOk();
		}
		else if (e.getSource()==m_delete){
			//if (JOptionPane.showMessageDialog(null,refno +" :Referenceno Sudah ada")
			//onDelete();
			onOk();
		}
		else if (e.getSource()==m_close){
			dispose();
		}
	}
	
	private void onDelete() {
		try {
			int row = m_table.getSelectedRow();
			String id = (String) m_table.getValueAt(row,0);
			m_conn.setAutoCommit(false);
			Statement stm = m_conn.createStatement();
			String query ="delete from " + m_sign + " where autoindex=" + id;
			updateData(stm,query);			
			stm.close();
			m_conn.commit();
		} catch (SQLException e1) {
			try {
				m_conn.rollback();
				m_conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
	}
	
	private void onUpdate() {
		try {
			int row = m_table.getRowCount();
			for (int i=0;i<row;i++){
				String id = (String) m_table.getValueAt(i,0);
				String refno = (String) m_table.getValueAt(i,2);				
				m_conn.setAutoCommit(false);
				Statement stm = m_conn.createStatement();
				if (!m_paramaeter.getReferenceno().equals(refno)){
					if (!isExistFront(stm,refno) && !isExist(stm,refno)){
						String query ="update " + m_sign + " set referenceno='" + refno + "' where autoindex=" + id;
						updateData(stm,query);
					}else{
						JOptionPane.showMessageDialog(null,refno +" :Referenceno Sudah ada");
					}
				}
				stm.close();
			}
			m_conn.commit();
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
				
				String query="select * from " + m_sign + " where referenceno='" + m_paramaeter.getReferenceno() + "' order by transactiondate";
				setData(query);
			
		} catch (SQLException e1) {
		}
	}
	
	private void setData(String query) throws SQLException {
		Statement stm = m_conn.createStatement();
		m_conn.setAutoCommit(false);
		m_table.removeRow();
		m_table.setData(stm,query);
		stm.close();
	}
	
	public static void updateData(Statement stm,String query) {
		try {
			
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
			model.addColumn("Transactioncode");
			model.addColumn("Reference No");
			model.addColumn("Tanggal");
			model.addColumn("Descripsi");
			if (m_sign.equals("AccTransaction"))
				model.addColumn("void");
			setModel(model);    			
			getColumnModel().getColumn(0).setPreferredWidth(50);
			getColumnModel().getColumn(0).setMaxWidth(50);
			getColumnModel().getColumn(1).setPreferredWidth(60);
			getColumnModel().getColumn(1).setMaxWidth(60);
			getColumnModel().getColumn(2).setPreferredWidth(120);
			getColumnModel().getColumn(2).setMaxWidth(120);
			getColumnModel().getColumn(3).setPreferredWidth(70);
			getColumnModel().getColumn(3).setMaxWidth(70);
			
		}
		
		
		void setData(Statement stm,String query){
			ResultSet rs;
			try {
				rs = stm.executeQuery(query);
				while (rs.next()) { 
					if (m_sign.equals("AccTransaction"))
						model.addRow(new Object[]{rs.getString("autoindex"),rs.getString("transactioncode"),rs.getString("Referenceno"),
								format.format(rs.getDate("transactiondate")),rs.getString("Description"),rs.getString("void")});
					else
						model.addRow(new Object[]{rs.getString("autoindex"),rs.getString("transactioncode"),rs.getString("Referenceno"),
								format.format(rs.getDate("transactiondate")),rs.getString("Description")});
				}				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
		
		public void removeRow(){
			int row = getRowCount()-1;
			for (int i=row;i>=0;i--){
				model.removeRow(i);
			}
		}		
	}
	
	public boolean isExist(Statement stm,String refno){
		boolean exist = false;
		String query ="select * from " + m_sign + " where referenceno='" + refno + "' order by autoindex";
		System.out.println(query);
		ResultSet rs;
		try {
			rs = stm.executeQuery(query);
			if (rs.next()){
				exist = true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exist;
	}
	
	public boolean isExistFront(Statement stm,String refnoup){
		boolean exist = false;
		
		try {
			for (int i=0;i<m_tablename.length;i++){
				String query ="select * from " + m_tablename[i] + " where referenceno='" + refnoup + "' order by autoindex";
				ResultSet rs;
				rs = stm.executeQuery(query);
				if (rs.next()){
					exist = true;
					return exist;
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return exist;
	}
	
	/*public void setListToString(ArrayList list){
		for (int i=0;i<list.size();i++){
			String[] data = (String[])list.get(i);
			m_refno = data[0];
			m_trans=data[2];
			if (i==(list.size()-1)){
				m_desc = m_desc+ "'" + data[1] + "'";
			}else{
				m_desc = m_desc+ "'" + data[1] + "'" + ",";
			}
		}
	}*/
	
	
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
			if(col == 0 || col==1 || col==3 || col==4 || col==5)
				return false;
			return true;
		}
	}
	
	
}
