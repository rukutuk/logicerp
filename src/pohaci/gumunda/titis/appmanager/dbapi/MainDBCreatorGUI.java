/**
 * 
 */
package pohaci.gumunda.titis.appmanager.dbapi;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.pohaci.titis.testconnection.MyConnection;

import pohaci.cgui.TabelModel;
import pohaci.gumunda.aas.dbapi.ConnectionManager;


/**
 * @author dark-knight
 *
 */
public class MainDBCreatorGUI extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTable m_table;
	JButton m_btCreate = new JButton ("Create"),
	m_btInit = new JButton("Init"),
	m_btDrop = new JButton("Drop"),
	m_btdeInit = new JButton("DeInit");
	ThisTableModel tModel = new ThisTableModel();

	Connection m_conn = null;
	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	JButton m_btCheck = new JButton("Check all"), m_btUnCheck = new JButton("Uncheck all");

	public MainDBCreatorGUI() {
		super("Application Manager DBCreator");

		setSize(275,500);
		setLocation( ( dim.width / 2 ) - ( getWidth() / 2 ), ( dim.height / 2 ) - ( getHeight() / 2 ) );
		construct();
		try{
			//ConnectionManager m_connectionManager = new ConnectionManager("sampurna");
			MyConnection m_connectionManager = new MyConnection();
			m_conn = m_connectionManager.getConnection();
			setVisible(true);
		}catch(Exception ex){
			javax.swing.JOptionPane.showMessageDialog(null,ex.toString());
			System.err.println(ex);
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		try {
			javax.swing.UIManager.setLookAndFeel("com.digitprop.tonic.TonicLookAndFeel");
			javax.swing.plaf.FontUIResource f = new javax.swing.plaf.FontUIResource("Tahoma", 0, 11);
			java.util.Enumeration keys = javax.swing.UIManager.getDefaults().keys();
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				Object value = javax.swing.UIManager.get(key);
				if (value instanceof javax.swing.plaf.FontUIResource)
					javax.swing.UIManager.put(key, f);
			}

		} catch (Exception ex) {
			System.out.println(ex);
		}
		new MainDBCreatorGUI();
	}

	void construct(){
		JPanel p = new JPanel();
		p.add(m_btCheck); m_btCheck.addActionListener(this);
		p.add(m_btUnCheck); m_btUnCheck.addActionListener(this);
		p.setBorder(BorderFactory.createEtchedBorder());

		JPanel pcheck = new JPanel();
		pcheck.add(p);

		m_table = new JTable();
		m_table.setModel(tModel);
		m_table.getColumnModel().getColumn(1).setPreferredWidth(20);
		m_table.getColumnModel().getColumn(1).setMinWidth(20);
		m_table.getColumnModel().getColumn(1).setMaxWidth(20);

		JScrollPane scroll = new JScrollPane(m_table);
		m_table.getTableHeader().setReorderingAllowed(false);

		JPanel pbt = new JPanel();
		pbt.add( m_btCreate ); m_btCreate.addActionListener(this);
		pbt.add( m_btInit ); m_btInit.addActionListener(this);
		pbt.add( m_btdeInit ); m_btdeInit.addActionListener(this);
		pbt.add( m_btDrop ); m_btDrop.addActionListener(this);

		JPanel centerpanel = new JPanel( new GridLayout( 2, 1 ) );
		centerpanel.add( pcheck);
		centerpanel.add( pbt );
		centerpanel.setBorder(BorderFactory.createEtchedBorder());

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(centerpanel,"South");
		getContentPane().add(scroll,"Center");
		setData();
	}

	public void processWindowEvent(WindowEvent e){
		if(e.getID() == WindowEvent.WINDOW_CLOSING ) System.exit(0);
	}

	boolean isOk(int row){
		Boolean bl = (Boolean) m_table.getValueAt(row,1);
		return bl.booleanValue();
	}

	public void actionPerformed(ActionEvent e){
		if( e.getSource() == m_btCreate ) {
			try{
				m_conn.setAutoCommit(false);
				Statement stm = m_conn.createStatement();
				if (isOk(0))
					DBCreatorSAP.createUserDetailTable(stm);
				if (isOk(1))
					DBCreatorSAP.createRoleTable(stm);
				if (isOk(2))
					DBCreatorSAP.createFunctionTable(stm);
				if (isOk(3))
					DBCreatorSAP.createFunctionStructureTable(stm);
				if (isOk(4))
					DBCreatorSAP.createRoleMappingTable(stm);
				if (isOk(5))
					DBCreatorSAP.createUserRoleTable(stm);
				

				m_conn.commit();
				m_conn.setAutoCommit(true);
			}
			catch(Exception ex){
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, ex.toString());
				try{
					m_conn.rollback(); m_conn.setAutoCommit(true);} catch(Exception exc){}
			}
		}
		else if( e.getSource() == m_btDrop ){
			try{
				m_conn.setAutoCommit(false);
				Statement stm = m_conn.createStatement();

				if (isOk(0))
					DBCreatorSAP.deleteUserDetailTable(stm);
				if (isOk(1))
					DBCreatorSAP.deleteRoleTable(stm);
				if (isOk(2))
					DBCreatorSAP.deleteFunctionTable(stm);
				if (isOk(3))
					DBCreatorSAP.deleteFunctionStructureTable(stm);
				if (isOk(4))
					DBCreatorSAP.deleteRoleMappingTable(stm);
				if (isOk(5))
					DBCreatorSAP.deleteUserRoleTable(stm);
				
				m_conn.commit();
				m_conn.setAutoCommit(true);
			}
			catch(Exception ex){
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, ex.toString());
				try{
					m_conn.rollback(); m_conn.setAutoCommit(true);} catch(Exception exc){}
			}
		}
		else if(e.getSource() == m_btInit ){
			try{
				//Statement stm = m_conn.createStatement();
				m_conn.setAutoCommit(false);

				m_conn.commit();
				m_conn.setAutoCommit(true);
			}
			catch(Exception ex){
				JOptionPane.showMessageDialog(null,ex.toString());
				try{
					m_conn.rollback(); m_conn.setAutoCommit(true);
				} catch(Exception exc){}
			}
		}
		else if(e.getSource() == m_btdeInit ){
			try{
				//Statement stm = m_conn.createStatement();
				m_conn.setAutoCommit(false);

				m_conn.commit();
				m_conn.setAutoCommit(true);
			}
			catch(Exception ex){
				JOptionPane.showMessageDialog(null,ex.toString());
				try{
					m_conn.rollback(); m_conn.setAutoCommit(true);
				}
				catch(Exception exc){}
			}
		}
		else if( e.getSource() == m_btCheck ) {
			for( int i = 0; i < m_table.getRowCount(); i++ )
				m_table.setValueAt( new Boolean("true"), i, 1 );
		}
		else if(e.getSource() == m_btUnCheck ) {
			for( int i = 0; i < m_table.getRowCount(); i++ )
				m_table.setValueAt( new Boolean("false"), i, 1 );
		}
	}

	void setData(){
		tModel.addRow(new Object[]{"User Detail", new Boolean (true)});
		tModel.addRow(new Object[]{"Role", new Boolean(true)});
		tModel.addRow(new Object[]{"Function", new Boolean(true)});
		tModel.addRow(new Object[]{"Function Structure", new Boolean(true)});
		tModel.addRow(new Object[]{"Role Mapping", new Boolean(true)});
		tModel.addRow(new Object[]{"User Role", new Boolean(true)});
	}

	class ThisTableModel extends TabelModel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ThisTableModel(){
			addColumn("Table");
			addColumn("Pilih");

		}

		public Class getColumnClass(int columnindex){
			if ( columnindex == 1) return Boolean.class;
			return super.getColumnClass(columnindex);
		}
	}
}
