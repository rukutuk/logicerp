package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import pohaci.gumunda.cgui.BaseTableCellEditor;
import pohaci.gumunda.cgui.BaseTableCellRenderer;
import pohaci.gumunda.cgui.DateCellEditor;
import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProject;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectInstall;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectReceive;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectSettled;
import pohaci.gumunda.titis.accounting.entity.PmtCAProject;
import pohaci.gumunda.titis.accounting.entity.PmtProjectCost;
import pohaci.gumunda.titis.accounting.entity.PmtProjectCostDetail;
import pohaci.gumunda.titis.accounting.entity.PurchaseApPmt;
import pohaci.gumunda.titis.accounting.entity.PurchaseReceipt;
import pohaci.gumunda.titis.accounting.entity.VoucherLoader;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.EmployeeCellEditor;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import pohaci.gumunda.titis.project.cgui.VoucherPanel.VoucherTable;
import pohaci.gumunda.titis.project.cgui.VoucherPanel.VoucherTableModel;

public class SearchVoucherDlg extends JDialog implements ActionListener {

		private static final long serialVersionUID = 1L;
		SearchTable m_searchTable;
		VoucherTable m_voucherTable;
		JButton m_selectBt;
		int m_iResponse = JOptionPane.NO_OPTION;
		JFrame m_mainframe;
		JRadioButton m_andRadioBt, m_orRadioBt;
		JRadioButton m_containsRadioBt, m_matchRadioBt, m_wholeRadioBt;
		JButton m_findBt, m_closeBt, m_clearBt;
		ProjectData m_project;
		Connection m_conn = null;
		long m_sessionid = -1;
		
		VoucherLoader loader;
		Object[] m_voucherList;
		private JComboBox m_statusComboBox;
		
		public SearchVoucherDlg(JFrame owner, String title, Connection conn, long sessionid, VoucherTable table, 
				ProjectData project){
			super(owner, ( title == null ) ? "Search Voucher" : title, true);
			m_project = project;
			m_voucherTable = table;
			m_mainframe = owner;
			m_conn = conn;
			m_sessionid = sessionid;
			setSize(380, 360);
			constructComponent();
		}
				
		void constructComponent() {
			m_statusComboBox = new JComboBox(new Object[]{"Not Submitted","Submitted","Posted","All"});//0,1,3,*
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					dispose();
				}
			});
			
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(criteriaPanel(), BorderLayout.NORTH);
			
		}
		
		JPanel criteriaPanel() {
			m_searchTable = new SearchTable();
			m_andRadioBt = new JRadioButton("and");
			m_andRadioBt.setSelected(true);
			m_orRadioBt = new JRadioButton("or");
			
			m_containsRadioBt = new JRadioButton("Text Contains Criteria");
			m_containsRadioBt.setSelected(true);
			m_matchRadioBt = new JRadioButton("Match Case");
			m_wholeRadioBt = new JRadioButton("Find Whole Words only");
			m_findBt = new JButton("Find");
			m_findBt.addActionListener(this);
			m_closeBt = new JButton("Cancel");
			m_closeBt.addActionListener(this);
			m_clearBt = new JButton("Clear");
			m_clearBt.addActionListener(this);
			
			ButtonGroup bg = new ButtonGroup();
			bg.add(m_andRadioBt);
			bg.add(m_orRadioBt);
			
			ButtonGroup bg2 = new ButtonGroup();
			bg2.add(m_containsRadioBt);
			bg2.add(m_matchRadioBt);
			bg2.add(m_wholeRadioBt);
			
			JPanel centerPanel = new JPanel();
			JPanel criteriaPanel = new JPanel();
			JPanel buttonPanel = new JPanel();
			JPanel operatorPanel = new JPanel();
			JPanel optionPanel = new JPanel();
			JScrollPane scrollPane = new JScrollPane();
			GridBagConstraints gridBagConstraints;
			
			scrollPane.setPreferredSize(new Dimension(100, 120));
			scrollPane.getViewport().add(m_searchTable);
			
			operatorPanel.setLayout(new GridLayout(2, 1));
			operatorPanel.add(m_andRadioBt);
			operatorPanel.add(m_orRadioBt);
			
			optionPanel.setLayout(new GridLayout(3, 1));
			optionPanel.add(m_containsRadioBt);
			optionPanel.add(m_matchRadioBt);
			optionPanel.add(m_wholeRadioBt);
			
			criteriaPanel.setLayout(new GridBagLayout());
			gridBagConstraints = new GridBagConstraints();
			
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.insets = new Insets(1, 0, 2, 0);
			gridBagConstraints.weightx = 1.0;
			criteriaPanel.add(new JLabel("Search Criteria"), gridBagConstraints);
			
			gridBagConstraints.gridy = 1;
			gridBagConstraints.insets = new Insets(1, 0, 5, 0);
			criteriaPanel.add(scrollPane, gridBagConstraints);
			
			gridBagConstraints.gridy = 2;
			gridBagConstraints.insets = new Insets(1, 0, 2, 0);
			criteriaPanel.add(new JLabel("Search Operator"), gridBagConstraints);
			
			gridBagConstraints.gridy = 3;
			gridBagConstraints.insets = new Insets(1, 0, 5, 0);
			criteriaPanel.add(operatorPanel, gridBagConstraints);
			
			gridBagConstraints.gridy = 4;
			gridBagConstraints.insets = new Insets(1, 0, 2, 0);
			criteriaPanel.add(new JLabel("Option"), gridBagConstraints);
			
			gridBagConstraints.gridy = 5;
			criteriaPanel.add(optionPanel, gridBagConstraints);
			
			gridBagConstraints.gridy = 6;
			criteriaPanel.add(buttonPanel, gridBagConstraints);
			
			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			buttonPanel.add(m_findBt);
			buttonPanel.add(m_clearBt);
			buttonPanel.add(m_closeBt);
			
			centerPanel.setLayout(new BorderLayout());
			centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
			centerPanel.add(criteriaPanel, BorderLayout.NORTH);
			centerPanel.add(buttonPanel, BorderLayout.SOUTH);
			
			return centerPanel;
		}
		
		public void setVisible( boolean flag ){
			Rectangle rc = m_mainframe.getBounds();
			Rectangle rcthis = getBounds();
			setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
					(int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
					(int)rcthis.getWidth(), (int)rcthis.getHeight());
			
			super.setVisible(flag);
		}
		
		public int getResponse() {
			return m_iResponse;
		}
		
		void find() {	  
			String criteria = "";
		    try {
		    	criteria = getCriterion();
		    }
		    catch(Exception ex) {
		      JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
                      JOptionPane.WARNING_MESSAGE);
		      return;
		    }

		    try {
		    	DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
		    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		    	
		    	GenericMapper mapper = MasterMap.obtainMapperFor(PmtCAProject.class);
	  			mapper.setActiveConn(m_conn);
	  	
	  			String strSelect = IDBConstants.ATTR_PROJECT + " = (select " + IDBConstants.ATTR_AUTOINDEX + 
	  				" from " + " projectdata where " + IDBConstants.ATTR_CODE + " = '" + m_project.getCode() + "')";
	  		  	
	  			VoucherTableModel model = (VoucherTableModel) m_voucherTable.getModel();
	  			model.setRowCount(0);
	  	
	  			System.err.println(strSelect + criteria);
	  				List list = mapper.doSelectWhere(strSelect + criteria);
	  		  		
	  			Iterator iter = list.iterator();
	  			int i=1;
	  			while(iter.hasNext()){
	  				PmtCAProject voucher = (PmtCAProject) iter.next();
	  				model.addRow(new Object[] {String.valueOf(i++), voucher.getReferenceNo(), dateFormat.format(voucher.getTransactionDate()),
	  				voucher.getDescription(), getStatus(voucher.getStatus()), voucher.getEmpApproved().igetFullName(),dateFormat.format(voucher.getDateApproved()),
	  				voucher.getCurrency().getSymbol()+ " " +decimalFormat.format(voucher.getAmount())});
	  			}
	  	
	  			String str = "" ;
	  			mapper = MasterMap.obtainMapperFor(PmtCAIOUProject.class);
	  			mapper.setActiveConn(m_conn);
	  			list = mapper.doSelectWhere(strSelect + criteria);
	  			iter = list.iterator();
	  			while(iter.hasNext()){
	  				PmtCAIOUProject voucher = (PmtCAIOUProject) iter.next();
	  				model.addRow(new Object[]{String.valueOf(i++), voucher.getReferenceNo(), dateFormat.format(voucher.getTransactionDate()),
	  					voucher.getDescription(), getStatus(voucher.getStatus()), voucher.getEmpApproved().igetFullName(), dateFormat.format(voucher.getDateApproved()),
	  					voucher.getCurrency().getSymbol()+ " " +decimalFormat.format(voucher.getAmount())});
	  			}
	  			
	  			mapper = MasterMap.obtainMapperFor(PmtCAIOUProject.class);
	  			mapper.setActiveConn(m_conn);
	  			list = mapper.doSelectWhere(strSelect);
	  			iter = list.iterator();
	  			while(iter.hasNext()){
	  				PmtCAIOUProject voucher = (PmtCAIOUProject) iter.next();
	  				
	  				GenericMapper tmpMapper = MasterMap.obtainMapperFor(PmtCAIOUProjectInstall.class);
		  			tmpMapper.setActiveConn(m_conn);
		  			str = IDBConstants.ATTR_PMT_CA_IOU_PROJECT + " = " + voucher.getIndex();
		  			List tmpList = tmpMapper.doSelectWhere(str + criteria);
		  			Iterator tmpIter = tmpList.iterator();
		  			while(tmpIter.hasNext()){
		  			  	PmtCAIOUProjectInstall install = (PmtCAIOUProjectInstall) tmpIter.next();
				  		model.addRow(new Object[]{String.valueOf(i++), install.getReferenceNo(), 
		  			  		dateFormat.format(install.getTransactionDate()),install.getDescription(), 
		  			  		getStatus(install.getStatus()), install.getEmpApproved().igetFullName(), 
		  			  		dateFormat.format(install.getDateApproved()), 
		  			  		install.getCurrency().getSymbol()+ " " + decimalFormat.format(install.getAmount())});
		  			}
		  			GenericMapper tmpMapper1 = MasterMap.obtainMapperFor(PmtCAIOUProjectReceive.class);
	  			  	tmpMapper1.setActiveConn(m_conn);
	  				str = IDBConstants.ATTR_PMT_CA_IOU_PROJECT + " = " + voucher.getIndex();
	    			System.err.println(str + criteria);
	  			  	List tmpList1 = tmpMapper1.doSelectWhere(str + criteria);
	  			  	Iterator tmpIter1 = tmpList1.iterator();
	  			  	while(tmpIter1.hasNext()){
	  			  		PmtCAIOUProjectReceive receive = (PmtCAIOUProjectReceive) tmpIter1.next();
	  			  		model.addRow(new Object[]{String.valueOf(i++), receive.getReferenceNo(), 
	  			  			dateFormat.format(receive.getTransactionDate()), receive.getDescription(), 
	  			  			getStatus(receive.getStatus()), receive.getEmpApproved().igetFullName(),
	  			  			dateFormat.format(receive.getDateApproved()), 
	  			  			receive.getCurrency().getSymbol()+ " " + decimalFormat.format(receive.getAmount())});
	  			  	}
	  			  	//	  			PmtCAIOUProjectSettled
	  			  	GenericMapper tmpMapper2 = MasterMap.obtainMapperFor(PmtCAIOUProjectSettled.class);
	  			  	tmpMapper2.setActiveConn(m_conn);
	  			  	//str = IDBConstants.ATTR_AUTOINDEX + " = ( select " + IDBConstants.ATTR_AUTOINDEX + " from " + IDBConstants.TABLE_PMT_CA_IOU_PROJECT + " where " + strSelect + ")" ;
	  				str = IDBConstants.ATTR_PMT_CA_IOU_PROJECT + " = " + voucher.getIndex();
	    			System.err.println(str + criteria);
	  			  	List tmpList2 = tmpMapper2.doSelectWhere(str + criteria);
	  			  	Iterator tmpIter2 = tmpList2.iterator();
	  			  	while(tmpIter2.hasNext()){
	  			  			PmtCAIOUProjectSettled settle = (PmtCAIOUProjectSettled) tmpIter2.next();
	  			  	  		model.addRow(new Object[]{String.valueOf(i++), settle.getReferenceNo(), 
	  			  			dateFormat.format(settle.getTransactionDate()), settle.getDescription(), 
	  			  			getStatus(settle.getStatus()), settle.getEmpApproved().igetFullName(), 
	  			  			dateFormat.format(settle.getDateApproved()),
	  			  			settle.getCurrency().getSymbol()+ " " + decimalFormat.format(settle.getAmount())});
	  			  	}
	  			  	
	  			  	
	  			}
  			  		  			 
	  			//PmtProjectCost
	  			mapper = MasterMap.obtainMapperFor(PmtProjectCost.class);
	  		  	mapper.setActiveConn(m_conn);
	  		  	System.err.println(strSelect + criteria);
	  			list = mapper.doSelectWhere(strSelect + criteria);
	  		  	iter = list.iterator(); 	
	  		  	while(iter.hasNext()){
	  		  		PmtProjectCost voucher = (PmtProjectCost) iter.next();
	  		  		
	  		  		GenericMapper tmpMapper = MasterMap.obtainMapperFor(PmtProjectCostDetail.class);
	  			  	tmpMapper.setActiveConn(m_conn);
	  			  	String tmpQuery = IDBConstants.TABLE_PMT_PROJECT_COST + " = " + voucher.getIndex();
	  			  	System.err.println(tmpQuery);
	  			  	List tmpList = tmpMapper.doSelectWhere(tmpQuery);
	  		  		Iterator tmpIter = tmpList.iterator();
	  		  		double amount = 0;
	  		  		while(tmpIter.hasNext()){
	  		  			PmtProjectCostDetail tmpV = (PmtProjectCostDetail) tmpIter.next();
	  		  			amount = amount + tmpV.getaccValue();
	  		  			
	  		  		}
	  		  		model.addRow(new Object[]{String.valueOf(i++), voucher.getReferenceNo(), dateFormat.format(voucher.getTransactionDate()),
	  		  				voucher.getDescription(), getStatus(voucher.getStatus()), voucher.getEmpApproved().igetFullName(), dateFormat.format(voucher.getDateApproved()),
	  		  				voucher.getCurrency().getSymbol()+ " " + decimalFormat.format(amount)});
	  		  	}
	  		  	
	  		  	//PurchaseApPmt
	  		  	mapper = MasterMap.obtainMapperFor(PurchaseReceipt.class);
	  		  	mapper.setActiveConn(m_conn);
	  		  	list = mapper.doSelectWhere(strSelect);
	  		  	iter = list.iterator(); 
	  		  	while(iter.hasNext()){
	  		  		PurchaseReceipt purchase = (PurchaseReceipt)iter.next();
	  		  		
	  		  		GenericMapper tmpMapper = MasterMap.obtainMapperFor(PurchaseApPmt.class);
	  		  		tmpMapper.setActiveConn(m_conn);
	  		  		str = IDBConstants.ATTR_PURCHASE_RECEIPT + " = " + purchase.getIndex();
	  		  		System.err.println(str+ criteria);
	  		  		List tmpList = tmpMapper.doSelectWhere(str+ criteria);
	  		  		Iterator tmpIter = tmpList.iterator();
	  		  		while(tmpIter.hasNext()){
	  		  			PurchaseApPmt voucher = (PurchaseApPmt) tmpIter.next();
	  		  			model.addRow(new Object[]{String.valueOf(i++), voucher.getReferenceNo(), 
	  		  				dateFormat.format(voucher.getTransactionDate()),voucher.getDescription(), 
	  		  				getStatus(voucher.getStatus()), voucher.getEmpApproved().igetFullName(), 
	  		  				dateFormat.format(voucher.getDateApproved()), 
	  		  				voucher.getAppMtCurr().getSymbol()+ " " + decimalFormat.format(voucher.getAppMtAmount())});
	  		  		}
	  		  	}
	  		  	
	  			
		    }
		    catch(Exception ex) {
		      JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
		                                    JOptionPane.WARNING_MESSAGE);
		      ex.printStackTrace();
		    }
		}
		
		private String getStatus(long status){
			  String  strStatus = "";
			  if (status==0)
				  strStatus = "Not Submitted";
			  else if (status==1 || status==2)
				  strStatus = "Submitted";
			  else 
				  strStatus = "Posted";
			  return strStatus;
		  }
		
		void clear() {
			m_searchTable.clear();
		}
		
		public void actionPerformed(ActionEvent e) {		
			if(e.getSource() == m_findBt) { 
				find();
			}else if(e.getSource() == m_clearBt) {
				m_searchTable.clear();
			}else if(e.getSource() == m_closeBt) {
				dispose();
			}
		}
		
		public String getCriterion(){
			String operator, equality , criteria = "",temp="";
	        int row = 0;      
	        boolean first = true;
	        if(m_andRadioBt.isSelected())
	          operator = " and ";
	        else
	          operator = " or ";
	        
	        if(m_containsRadioBt.isSelected()){
	        	equality = " like ";
	        }
	        else equality = " = ";

	        String value = "";
	        // Voucher No
	        value = (String) m_searchTable.getValueAt(row++, 1);
	        if(!value.equals("")){
	        	if(m_containsRadioBt.isSelected())
	          	  	value = "%" + value + "%";
	        	if (first){       	  
	            	temp = "";
	            	first = false;
	            }else {
	            	temp = operator;        	
	            }
	        	       	
	        	criteria += temp + IDBConstants.ATTR_REFERENCE_NO +  equality + "'" + value + "'";
	        }
	        
	        java.util.Date date = null;
	        java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("dd-MM-yyyy"),
	        sf2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
	       
	        value = (String) m_searchTable.getValueAt(row++, 1);
	        System.err.println(value);
	        try {
	        	if(!value.equals("")) {
	        		date = sf.parse(value);
	        		value = sf2.format(date);
	        	}
	        }
	        catch(Exception ex) {
	        	ex.getStackTrace();
	        }
	        
	        // Voucher Date      
	        if(!value.equals("")){    	  
	        	if (first){       	  
	            	temp = "";
	            	first = false;
	            }else {
	            	temp = operator;        	
	            }        	
	            
	            criteria += temp + IDBConstants.ATTR_TRANSACTION_DATE + " = '" + value + "'";
	        }
	        
	        //Description
	        value = (String) m_searchTable.getValueAt(row++, 1);
	        if(!value.equals("")){
	        	if(m_containsRadioBt.isSelected())
	          	  	value = "%" + value + "%";
	        	if (first){       	  
	            	temp = "";
	            	first = false;
	            }else {
	            	temp = operator;        	
	            }        	
	        	criteria += temp + IDBConstants.ATTR_DESCRIPTION + equality + " '" + value.toLowerCase() + "'";
	        }
	        
	        //Status
	        value = (String) m_searchTable.getValueAt(row++, 1);
	        if(!value.equals("All")){
	        	if(value.equals("Submitted")){
		        	value = "1 or " + IDBConstants.ATTR_STATUS + " = 2";
		        }else if(value.equals("Not Submitted")){
		        	value = "0";
		        }else if(value.equals("Posted"))
		        	value = "3";   
	        	if (first){       	  
	            	temp = "";
	            	first = false;
	            }else {
	            	temp = operator;        	
	            }        	
	        	criteria += temp + "( " + IDBConstants.ATTR_STATUS + " = " + value + ")";
	        }
	        
	        //Approved By
	        Object tmp = m_searchTable.getValueAt(row++, 1);
	        value = "";
	        if(!tmp.equals(""))
	        	value = tmp.toString().substring(0, tmp.toString().indexOf(" "));
	        System.err.println(value);
	        if(!value.equals("")){
	        	if (first){       	  
	            	temp = "";
	            	first = false;
	            }else {
	            	temp = operator;        	
	            }        	
	        	criteria += temp + IDBConstants.ATTR_EMP_APPROVED +  " = ( select " + IDBConstants.ATTR_AUTOINDEX +
	        		" from " + IDBConstants.TABLE_EMPLOYEE + " where " + IDBConstants.ATTR_EMP_NO + " = '" +
	        		value + "')";
	        }
	        
	        //Submit Date 
	        value = (String)m_searchTable.getValueAt(row++, 1);
	        try {
	        	if(!value.equals("")) {
	        		date = sf.parse(value);
	        		value = sf2.format(date);
	        	}
	        }
	        catch(Exception ex) {
	        	ex.printStackTrace();
	        } 
	        if(!value.equals("")){    	  
	        	if (first){       	  
	            	temp = "";
	            	first = false;
	            }else {
	            	temp = operator;        	
	            }       	
	            criteria += temp + IDBConstants.ATTR_DATE_APPROVED + " = '" + value + "'";
	        }
	        String status = (String)m_searchTable.getValueAt(3, 1);
	        if( (status == "All" && m_orRadioBt.isSelected()) || criteria.equals(""))
	        	return "";
	        else return " and (" + criteria + ")"; 
			
		}
		
		/**
		 *
		 */
		public class SearchTable extends JTable {

			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
			public SearchTable() {			
				
				SearchTableModel model = new SearchTableModel();			
				model.addColumn("Attribute");
				model.addColumn("Description");			
				
				model.addRow(new Object[]{"Voucher No", ""});
				model.addRow(new Object[]{"Voucher Date", ""});			
				model.addRow(new Object[]{"Description", ""});
				model.addRow(new Object[]{"Status", "All"});
				model.addRow(new Object[]{"Approved By", ""});
				model.addRow(new Object[]{"Approval Date", ""});			
										
				setModel(model);
				getColumnModel().getColumn(0).setPreferredWidth(140);
				getColumnModel().getColumn(0).setMaxWidth(140);
				getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
				
			}
			
			public TableCellEditor getCellEditor(int row, int col) {
				if( col == 1){
					if(row == 4 )
						return new EmployeeCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
								m_conn, m_sessionid);
					if(row == 1 || row == 5)
						return new DateCellEditor(GumundaMainFrame.getMainFrame());
					if(row == 3 )
						return new DefaultCellEditor(m_statusComboBox);		
				}
				return new BaseTableCellEditor();
			}
						
			public void stopCellEditing() {
				TableCellEditor editor;
				if((editor = getCellEditor()) != null)
					editor.stopCellEditing();
			}
			
			public void clear() {
				stopCellEditing();
				int row = getRowCount();
				for(int i = 0; i < row; i ++){
					if(i==3) 
						setValueAt("All", i, 1);				
					else setValueAt("", i, 1);
				}
			}   
			
		}
		
		class SearchTableModel extends DefaultTableModel {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int col) {
				if(col == 0)
					return false;
				return true;
			}
		}
}
