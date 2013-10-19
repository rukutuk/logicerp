package pohaci.gumunda.titis.accounting.cgui;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import net.sf.jasperreports.view.JRViewer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

import pohaci.gumunda.titis.accounting.cgui.reportdesign.ReportDesignDlg;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.BalanceSheetTreeLeft;
import pohaci.gumunda.titis.accounting.entity.BalanceSheetTreeRight;
import pohaci.gumunda.titis.accounting.entity.reportdesign.Design;
import pohaci.gumunda.titis.application.*;

public class BalanceSheetDesignerPanel extends JPanel  implements ActionListener, TreeSelectionListener  {
	private static final long serialVersionUID = 1L;
	BalanceSheetTreeLeft m_list;
	BalanceSheetTreeRight m_list2;
	Account m_account;
	UnitPicker m_unitPicker;	
	JRViewer m_jrv;
	Connection m_conn = null;
	long m_sessionid = -1;	
	PeriodStartEnd m_periodStartEnd;
	JPanel m_centerPanel = new JPanel();
	JButton m_btnAdd,m_btnDelete;
	boolean m_root = false;
	
	JMenuItem mi_add, mi_edit, mi_delete,mi_values;
	JPopupMenu m_popupMenu;
	
	public BalanceSheetDesignerPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		constructComponent();
	}	
	
	void constructComponent() {
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);  		
		
		m_btnAdd = new JButton("Add");
		m_btnAdd.addActionListener(this);
		m_btnDelete = new JButton("Delete");
		m_btnDelete.addActionListener(this);
		
		JPanel listPanel = new JPanel();
		JPanel listPanel2 = new JPanel();
		
		JPanel titlePanel = new JPanel();		
		JPanel buttonPanel = new JPanel();		
		
		m_periodStartEnd = new PeriodStartEnd("Period");
		m_unitPicker = new UnitPicker(m_conn,m_sessionid);		
		
		m_list = new BalanceSheetTreeLeft(m_conn,m_sessionid);
		m_list.addTreeSelectionListener(this);
		
		m_popupMenu = new JPopupMenu();
		mi_add = new JMenuItem("Add");
		mi_add.addActionListener(this);
		m_popupMenu.add(mi_add);
		mi_edit = new JMenuItem("Edit");
		mi_edit.addActionListener(this);
		m_popupMenu.add(mi_edit);
		mi_delete = new JMenuItem("Delete");
		mi_delete.addActionListener(this);		
		m_popupMenu.add(mi_delete);		
		mi_values = new JMenuItem("Values...");
		mi_values.addActionListener(this);
		m_popupMenu.addSeparator();
		m_popupMenu.add(mi_values);
		m_list2 = new BalanceSheetTreeRight(m_conn,m_sessionid);
		m_list2.addMouseListener(new TreeMouseAdapter());
		
		buttonPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(3, 3, 3, 3);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		buttonPanel.add(m_btnAdd, gridBagConstraints);		
		
		gridBagConstraints.gridx = 1;		
		buttonPanel.add(m_btnDelete, gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		buttonPanel.add(new JLabel(""), gridBagConstraints);
		
		listPanel.setLayout(new BorderLayout());
		listPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
		listPanel.add(buttonPanel, BorderLayout.NORTH);
		listPanel.add(new JScrollPane(m_list), BorderLayout.CENTER);
		
		JLabel titleLbl = new JLabel("Balance Sheet Labels");		
		titlePanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(3, 3, 3, 3);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		titlePanel.add(new JLabel(""), gridBagConstraints);
		gridBagConstraints.gridy = 1;
		titlePanel.add(titleLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		titlePanel.add(new JLabel(""), gridBagConstraints);
		
		listPanel2.setLayout(new BorderLayout());
		listPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10,5, 10));
		listPanel2.add(titlePanel, BorderLayout.NORTH);
		listPanel2.add(new JScrollPane(m_list2), BorderLayout.CENTER);		
		
		splitPane.setLeftComponent(listPanel);
		splitPane.setRightComponent(listPanel2);
		splitPane.setDividerLocation(200);
		
		setLayout(new BorderLayout());
		add(splitPane, BorderLayout.CENTER);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_btnAdd){
			ReportDesignDlg dlg = new ReportDesignDlg(
					pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
					m_conn, m_sessionid, "Balance Sheet Design", Design.class);
			dlg.setVisible(true);	
		}else if (e.getSource()==m_btnDelete){	
			if (m_strList.equals("")){
				JOptionPane.showMessageDialog(this,"pilih data");
			}else{
				System.err.println(m_strList);
			}
		}else if (e.getSource()==mi_add){
			System.err.println("add");
		}else if (e.getSource()==mi_edit){
			BalanceSheetDesignAccountDlg dlg = new BalanceSheetDesignAccountDlg(
					pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
					m_conn, m_sessionid);
			dlg.setVisible(true);	
		}else if (e.getSource()==mi_values){			
			BalanceSheetDesignValuesDlg dlg = new BalanceSheetDesignValuesDlg(
					pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
					m_conn, m_sessionid);
			dlg.setVisible(true);	
		}
	}
	
	DefaultMutableTreeNode m_node = null;
	public void valueChanged(TreeSelectionEvent e) {
		//if (e.getSource()==m_list){
			TreePath path = e.getNewLeadSelectionPath();
			if(path != null) {
				m_node = (DefaultMutableTreeNode)path.getLastPathComponent();
				setSelectedObject(m_node.getUserObject());
			}
		/*}else if(e.getSource()==m_list2){
			TreePath path = e.getNewLeadSelectionPath();
			if(path != null) {
				m_node = (DefaultMutableTreeNode)path.getLastPathComponent();
				setSelectedObject(m_node.getUserObject());
			}
		}*/
	}
	
	String m_strList = "";
	void setSelectedObject(Object obj) {
		if (obj instanceof String) {
			String node = (String) obj;
			m_strList = node;
		}else{
			//System.err.println("account");
		}
	}
	
	class TreeMouseAdapter extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			if(e.isPopupTrigger()){
				DefaultTreeModel model = (DefaultTreeModel)m_list2.getModel();
				TreePath path = m_list2.getSelectionPath();
				if(path != null){
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
					if(node == model.getRoot()) {
						mi_add.setEnabled(true);
						mi_edit.setEnabled(false);
						mi_delete.setEnabled(false);
					}
					else {
						if(((Account)node.getUserObject()).isGroup()) {
							mi_add.setEnabled(true);
							mi_edit.setEnabled(true);
							mi_delete.setEnabled(true);
						}
						else {
							mi_add.setEnabled(false);
							mi_edit.setEnabled(true);
							mi_delete.setEnabled(true);
						}
					}
					Rectangle rectangle = m_list2.getPathBounds(path);
					if( rectangle.contains(e.getPoint()))
						m_popupMenu.show(m_list2, e.getX(), e.getY());
				}
			}
		}
	}
}