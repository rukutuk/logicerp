package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import pohaci.gumunda.cgui.BaseTableCellEditor;
import pohaci.gumunda.cgui.BaseTableCellRenderer;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.VariableAccountSetting;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class VariableAccountPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection m_conn;
	private long m_sessionid;
	private VariableAccountTable m_table;
	private JButton m_editBt;
	private JButton m_saveBt;
	private JButton m_cancelBt;
	private boolean m_editable;
	private Hashtable m_entities;
	private GenericMapper m_mapper;
	private String[] m_vars;
	//private boolean m_new;

	public VariableAccountPanel(Connection conn, long sessionid) {
	    m_conn = conn;
	    m_sessionid = sessionid;
	    
	    m_mapper = MasterMap.obtainMapperFor(VariableAccountSetting.class);
	    m_mapper.setActiveConn(m_conn);
	    
	   // m_new = false;

	    m_vars = IConstants.ATTR_VARIABLES;
	    
	    constructComponent();
	    initData();
	  }

	private void initData() {
		doLoad();
		if(m_entities.size()==0){
			//m_new = true;
		}
		entity2gui();
	}
	
	private void entity2gui(){
		// get the variable:		
		DefaultTableModel model = (DefaultTableModel) m_table.getModel();
		model.setRowCount(0);
		for(int i=0; i<m_vars.length; i++){
			VariableAccountSetting vas = (VariableAccountSetting) m_entities.get(m_vars[i]);
			Account acct = null;
			if(vas!=null)
				acct = vas.getAccount();
			model.addRow(new Object[]{m_vars[i], acct});
		}
	}

	private void doLoad() {
		List list = m_mapper.doSelectAll();
		Hashtable tbl = new Hashtable(list.size());
		
		Iterator iter = list.iterator();
		while(iter.hasNext()){
			VariableAccountSetting vas = (VariableAccountSetting) iter.next();
			tbl.put(vas.getVariable(), vas);
		}
		
		m_entities = tbl;
	}

	private void constructComponent() {
	    JPanel buttonPanel = new JPanel();
	    JPanel centerPanel = new JPanel();

	    m_table = new VariableAccountTable();
	    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
	   
	    m_editBt = new JButton("Edit");
	    m_editBt.addActionListener(this);
	    buttonPanel.add(m_editBt);
	    m_saveBt = new JButton("Save");
	    m_saveBt.addActionListener(this);
	    buttonPanel.add(m_saveBt);
	    m_cancelBt = new JButton("Cancel");
	    m_cancelBt.addActionListener(this);;
	    buttonPanel.add(m_cancelBt);
	   
	    m_editBt.setEnabled(true);
	    m_saveBt.setEnabled(false);
	    m_cancelBt.setEnabled(false);

	    centerPanel.setLayout(new BorderLayout());
	    centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
	    centerPanel.add(new JScrollPane(m_table), BorderLayout.CENTER);
	    centerPanel.add(buttonPanel, BorderLayout.SOUTH);

	    setLayout(new BorderLayout());
	    add(centerPanel, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==m_editBt){
			m_editable = true;
			updateButton();
		}
		if(e.getSource()==m_saveBt){
			doSave();
			m_editable = false;
			updateButton();
		}
		if(e.getSource()==m_cancelBt){
			doCancel();
			m_editable = false;
			updateButton();
		}
	}
	
	private void updateButton() {
		m_editBt.setEnabled(!m_editable);
		m_saveBt.setEnabled(m_editable);
		m_cancelBt.setEnabled(m_editable);
	}

	private void doCancel() {
		doLoad();
		entity2gui();
	}

	private void doSave() {
		gui2entity();
		
		for(int i=0; i<m_vars.length; i++){
			VariableAccountSetting vas = (VariableAccountSetting) m_entities.get(m_vars[i]);
			//if(m_new)
			if(vas.getIndex()==0)
				m_mapper.doInsert(vas);
			else
				m_mapper.doUpdate(vas);
		}
	}

	private void gui2entity() {
		Hashtable newEntities = new Hashtable(m_vars.length);
		for(int i=0; i<m_vars.length; i++){
			Object obj = m_table.getValueAt(i, 1);
			Account acct = null;
			if(obj instanceof Account)
				acct = (Account) obj;
			VariableAccountSetting vas = (VariableAccountSetting) m_entities.get(m_vars[i]);
			if(vas==null){
				vas = new VariableAccountSetting();
				vas.setVariable(m_vars[i]);
			}
			vas.setAccount(acct);
			newEntities.put(vas.getVariable(), vas);
		}
		m_entities = newEntities;
	}

	protected class VariableAccountTable extends JTable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public VariableAccountTable() {
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("Variable");
			model.addColumn("Account");
			setModel(model);
			
			getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
		}	
		
		public TableCellEditor getCellEditor(int row, int col) {
			if(col==1)
				return new AccountTreeCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
						m_conn, m_sessionid, true);
			return new BaseTableCellEditor();
		}

		public void stopCellEditing() {
			TableCellEditor editor;
			if ((editor = getCellEditor()) != null)
				editor.stopCellEditing();
		}

		public boolean isCellEditable(int row, int column) {
			if(column==0)
				return false;
			if(m_editable)
				return true;;
			return false;
		}
	}
}
