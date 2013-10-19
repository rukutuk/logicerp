package pohaci.gumunda.titis.project.dbapi;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.pohaci.titis.testconnection.MyConnection;

import java.sql.*;

import pohaci.cgui.TabelModel;
import pohaci.gumunda.aas.dbapi.ConnectionManager;
import pohaci.gumunda.titis.project.cgui.IConstants;

public class MainDBCreatorGUI extends JDialog implements ActionListener{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JTable m_table;
  JButton m_btCreate = new JButton ("Create"),
  m_btInit = new JButton("Init"),
  m_btDrop = new JButton("Drop"),
  m_btdeInit = new JButton("deInit");
  ThisTableModel tModel = new ThisTableModel();

  Connection m_conn = null;
  Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
  JButton m_btCheck = new JButton("Pilih Semua"), m_btUnCheck = new JButton("Semua Tidak");

  public MainDBCreatorGUI() {
    super((java.awt.Frame)null,"DBCreator",true);

    setSize(400,500);
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

        if ( isOk( 0 ) ) DBCreatorSAP.createCustomerCompanyGroupTable(stm);
        if ( isOk( 1 ) ) DBCreatorSAP.createPartnerCompanyGroupTable(stm);

        if ( isOk( 2 ) ) DBCreatorSAP.createPersonalTable(stm);
        if ( isOk( 3 ) ) DBCreatorSAP.createPersonalHomeTable(stm);
        if ( isOk( 4 ) ) DBCreatorSAP.createPersonalBusinessTable(stm);
        if ( isOk( 5 ) ) DBCreatorSAP.createCustomerTable(stm);
        if ( isOk( 6 ) ) DBCreatorSAP.createCustomerGroupTable(stm);
        if ( isOk( 7 ) ) DBCreatorSAP.createCustomerAddressTable(stm);
        if ( isOk( 8 ) ) DBCreatorSAP.createCustomerContactTable(stm);

        if ( isOk( 9 ) ) DBCreatorSAP.createPartnerTable(stm);
        if ( isOk( 10 ) ) DBCreatorSAP.createPartnerGroupTable(stm);
        if ( isOk( 11 ) ) DBCreatorSAP.createPartnerAddressTable(stm);
        if ( isOk( 12 ) ) DBCreatorSAP.createPartnerContactTable(stm);

        if ( isOk( 13 ) ) DBCreatorSAP.createProjectDataTable(stm);
        if ( isOk( 14 ) ) DBCreatorSAP.createProjectPersonalTable(stm);

        if ( isOk( 15 ) ) DBCreatorSAP.createProjectOrganizationAttachTable(stm);

        if ( isOk( 16 ) ) DBCreatorSAP.createProjectContractTable(stm);
        if ( isOk( 17 ) ) DBCreatorSAP.createContractPaymentTable(stm);

        if ( isOk( 18 ) ) DBCreatorSAP.createProjectPartnerTable(stm);
        if ( isOk( 19 ) ) DBCreatorSAP.createProjectPartnerContactTable(stm);

        if ( isOk( 20 ) ) DBCreatorSAP.createProjectClientContactTable(stm);
        if ( isOk( 21 ) ) DBCreatorSAP.createProjectLocationTable(stm);

        if ( isOk( 22 ) ) DBCreatorSAP.createProjectProgressTable(stm);
        if ( isOk( 23 ) ) DBCreatorSAP.createProjectNotesTable(stm);

        if ( isOk( 24 ) ) DBCreatorSAP.createTimeSheetTable(stm);
        if ( isOk( 25 ) ) DBCreatorSAP.createTimeSheetDetailTable(stm);

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

        if ( isOk( 0 ) ) DBCreatorSAP.deleteCustomerCompanyGroupTable(stm);
        if ( isOk( 1 ) ) DBCreatorSAP.deletePartnerCompanyGroupTable(stm);

        if ( isOk( 2 ) ) DBCreatorSAP.deletePersonalTable(stm);
        if ( isOk( 3 ) ) DBCreatorSAP.deletePersonalHomeTable(stm);
        if ( isOk( 4 ) ) DBCreatorSAP.deletePersonalBusinessTable(stm);
        if ( isOk( 5 ) ) DBCreatorSAP.deleteCustomerTable(stm);
        if ( isOk( 6 ) ) DBCreatorSAP.deleteCustomerGroupTable(stm);
        if ( isOk( 7 ) ) DBCreatorSAP.deleteCustomerAddressTable(stm);
        if ( isOk( 8 ) ) DBCreatorSAP.deleteCustomerContactTable(stm);

        if ( isOk( 9 ) ) DBCreatorSAP.deletePartnerTable(stm);
        if ( isOk( 10 ) ) DBCreatorSAP.deletePartnerGroupTable(stm);
        if ( isOk( 11 ) ) DBCreatorSAP.deletePartnerAddressTable(stm);
        if ( isOk( 12 ) ) DBCreatorSAP.deletePartnerContactTable(stm);

        if ( isOk( 13 ) ) DBCreatorSAP.deleteProjectDataTable(stm);
        if ( isOk( 14 ) ) DBCreatorSAP.deleteProjectPersonalTable(stm);

        if ( isOk( 15 ) ) DBCreatorSAP.deleteProjectOrganizationAttachTable(stm);

        if ( isOk( 16 ) ) DBCreatorSAP.deleteProjectContractTable(stm);
        if ( isOk( 17 ) ) DBCreatorSAP.deleteContractPaymentTable(stm);

        if ( isOk( 18 ) ) DBCreatorSAP.deleteProjectPartnerTable(stm);
        if ( isOk( 19 ) ) DBCreatorSAP.deleteProjectPartnerContactTable(stm);

        if ( isOk( 20 ) ) DBCreatorSAP.deleteProjectClientContactTable(stm);
        if ( isOk( 21 ) ) DBCreatorSAP.deleteProjectLocationTable(stm);

        if ( isOk( 22 ) ) DBCreatorSAP.deleteProjectProgressTable(stm);
        if ( isOk( 23 ) ) DBCreatorSAP.deleteProjectNotesTable(stm);

        if ( isOk( 24 ) ) DBCreatorSAP.deleteTimeSheetTable(stm);
        if ( isOk( 25 ) ) DBCreatorSAP.deleteTimeSheetDetailTable(stm);

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
        Statement stm = m_conn.createStatement();
        m_conn.setAutoCommit(false);

        initApplicationTable(stm);
        initModulTable(stm);

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
        Statement stm = m_conn.createStatement();
        m_conn.setAutoCommit(false);

        deInitModulTable(stm);
        deInitApplicationTable(stm);

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
    tModel.addRow(new Object[]{"Customer Company Group", new Boolean (true)});
    tModel.addRow(new Object[]{"Partner Company Group", new Boolean (true)});

    tModel.addRow(new Object[]{"Personal", new Boolean (true)});
    tModel.addRow(new Object[]{"Personal Home", new Boolean (true)});
    tModel.addRow(new Object[]{"Personal Business", new Boolean (true)});


    tModel.addRow(new Object[]{"Customer", new Boolean (true)});
    tModel.addRow(new Object[]{"Customer Group", new Boolean (true)});
    tModel.addRow(new Object[]{"Customer Address", new Boolean (true)});
    tModel.addRow(new Object[]{"Customer Contact", new Boolean (true)});

    tModel.addRow(new Object[]{"Partner", new Boolean (true)});
    tModel.addRow(new Object[]{"Partner Group", new Boolean (true)});
    tModel.addRow(new Object[]{"Partner Address", new Boolean (true)});
    tModel.addRow(new Object[]{"Partner Contact", new Boolean (true)});

    tModel.addRow(new Object[]{"Project Data", new Boolean (true)});
    tModel.addRow(new Object[]{"Project Personal", new Boolean (true)});
    tModel.addRow(new Object[]{"Project Organization Attachment", new Boolean (true)});

    tModel.addRow(new Object[]{"Project Contract", new Boolean (true)});
    tModel.addRow(new Object[]{"Contract Payment", new Boolean (true)});

    tModel.addRow(new Object[]{"Project Partner", new Boolean (true)});
    tModel.addRow(new Object[]{"Project Partner Contact", new Boolean (true)});

    tModel.addRow(new Object[]{"Project Client Contact", new Boolean (true)});
    tModel.addRow(new Object[]{"Project Location", new Boolean (true)});

    tModel.addRow(new Object[]{"Project Progress", new Boolean (true)});
    tModel.addRow(new Object[]{"Project Notes", new Boolean (true)});

    tModel.addRow(new Object[]{"Time Sheet", new Boolean (true)});
    tModel.addRow(new Object[]{"Time Sheet Detail", new Boolean (true)});
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
      if ( columnindex == 1) return Boolean.class;{
    	  return super.getColumnClass(columnindex);   	
    	  
      }
    }
  }

  static void initApplicationTable(Statement stm) throws SQLException{
    stm.executeUpdate("INSERT INTO " + pohaci.gumunda.aas.dbapi.IDBConstants.TABLE_APPLICATION + " VALUES('" +
                      IConstants.APP_PROJECT + "')");
    System.out.println("init application ok ");
  }

  static void deInitApplicationTable(Statement stm) throws SQLException{
    stm.executeUpdate("DELETE FROM " + pohaci.gumunda.aas.dbapi.IDBConstants.TABLE_APPLICATION + " WHERE " +
                      pohaci.gumunda.aas.dbapi.IDBConstants.ATT_NAME + " = '" + IConstants.APP_PROJECT + "'");
    System.out.println("deinit application ok ");

  }

  static  void initModulTable(Statement stm) throws SQLException{
    stm.executeUpdate("INSERT INTO " + pohaci.gumunda.aas.dbapi.IDBConstants.TABLE_MODUL + " values('" +
                      IDBConstants.MODUL_PROJECT_MANAGEMENT + "')");
    System.out.println("init modul ok ");
  }

  static void deInitModulTable(Statement stm) throws SQLException{
    stm.executeUpdate("DELETE FROM " + pohaci.gumunda.aas.dbapi.IDBConstants.TABLE_MODUL + " WHERE " +
                      pohaci.gumunda.aas.dbapi.IDBConstants.ATT_NAME + " = '" + IDBConstants.MODUL_PROJECT_MANAGEMENT + "'");
    System.out.println("deinit modul ok ");
  }
}