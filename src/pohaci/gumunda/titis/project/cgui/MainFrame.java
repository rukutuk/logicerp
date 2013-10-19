
package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

import pohaci.gumunda.aas.logic.LoginBusinessLogic;
import pohaci.gumunda.titis.accounting.cgui.ExpenseSheetPanel;
import pohaci.gumunda.titis.accounting.cgui.ProjectProfitabilityPanel;
import pohaci.gumunda.titis.accounting.cgui.bypass.ManagerLoginDlg;
import pohaci.gumunda.titis.application.FrameMain;
import pohaci.gumunda.titis.application.InternalFrame;
import pohaci.gumunda.titis.appmanager.entity.ApplicationFunction;
import pohaci.gumunda.titis.appmanager.entity.UserProfile;
import pohaci.gumunda.titis.appmanager.logic.AppManagerLogic;
import pohaci.gumunda.titis.project.cgui.report.ProjectCostPanel;
import pohaci.gumunda.titis.project.cgui.report.RptPersonalUtilization;

public class MainFrame extends FrameMain implements ActionListener {

	private static final long serialVersionUID = 1L;
	protected static final String HELP_FILENAME = "project.chm";
	protected static final String HELP_FOLDER = "..\\helps\\";

	JMenuItem mi_logout = new JMenuItem("Logout");
	JMenuItem mi_exit = new JMenuItem("Exit");

	JMenuItem mi_projectlist = new JMenuItem("Project List");
	JMenuItem mi_monitoring = new JMenuItem("Project Monitoring");
	JMenuItem mi_tsheet = new JMenuItem("Time Sheet Data");
	JMenuItem mi_expensesheet = new JMenuItem("Expense Sheet Data");

	JMenuItem mi_customergroup = new JMenuItem("Customer Group");
	JMenuItem mi_customer = new JMenuItem("Customer Data");

	JMenuItem mi_partnergroup = new JMenuItem("Partner Group");
	JMenuItem mi_partner = new JMenuItem("Partner Data");
	JMenuItem mi_personal = new JMenuItem("Personal");

	JMenuItem mi_activity = new JMenuItem("Activity");

	JMenuItem mi_rep_project_profitability = new JMenuItem("Project Profitability");
	JMenuItem mi_rep_project_cost = new JMenuItem("Project Cost");
	JMenuItem mi_rep_field_allowances = new JMenuItem("Field Allowances");
	JMenuItem mi_rep_personnel_utilization = new JMenuItem("Personal Utilization");
	private Hashtable hashtable = new Hashtable();
	private UserProfile m_userProfile;
	//JMenuItem mi_helpContentsMenu = new JMenuItem("Help Contents");

	private static final boolean MODE_ADMIN = false;

	public MainFrame() {
		super(IConstants.APP_PROJECT);
		setTitle("PROJECT");
		setSize(750, 550);
		//setIconImage(Toolkit.getDefaultToolkit().getImage("../images/titis.gif"));
		setIconImage(Toolkit.getDefaultToolkit().getImage(".."));
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				if(m_sessionid != -1)
					logout();
				System.exit(0);
			}
		});

		init();
		constructComponent();
		createMenu();
		populateMenu(); // JANGAN DIHAPUS MESKI CUMAN DI-COMMENT

		adjustPosition();
		setVisible(true);
		login();

		// kalau ada apa-apa salahin aja aku.... :)
		// sebelumnya, coba dua baris di bawah ini di comment dulu aja...
		if (!MODE_ADMIN) {
			getAllGrantedFunctions();
			enableMenu();
			deleteSeparator();
		}
	}

	//METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
	private void getAllGrantedFunctions() {
		AppManagerLogic logic = new AppManagerLogic(m_conn);
		try {
			ApplicationFunction[] functions = logic.getGrantedFunctionForUser(m_userProfile, "PROJECT");
			hashtable.clear();
			for(int i=0; i<functions.length; i++){
				hashtable.put(functions[i].getPath(), functions[i]);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
	private void deleteSeparator() {
		JMenuBar menuBar = getJMenuBar();
		int menuCount = menuBar.getMenuCount();
		for (int i=0; i<menuCount; i++) {
			JMenu menu = menuBar.getMenu(i);
			if ((menu.isEnabled())&&(menu.isVisible())){
				removeSeparatorInMenu(menu);
			}
		}
	}

	// METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
	private void removeSeparatorInMenu(JMenu menu) {
		int menuCount = menu.getMenuComponentCount();
		for(int i=0; i<menuCount; i++){
			Component menuComponent = menu.getMenuComponent(i);
			if(menuComponent!=null){
				if (menuComponent instanceof JMenu) {
					removeSeparatorInMenu((JMenu)menuComponent);
				} else if (menuComponent instanceof JSeparator) {
					boolean isBeforeEnabled = false;
					if (i - 1 >= 0)
						isBeforeEnabled = checkMenuComponent(menu, i-1);
					boolean isAfterEnabled = false;
					if (i + 1 <= menuCount)
						isAfterEnabled = checkMenuComponent(menu, i+1);

					menuComponent.setEnabled(isBeforeEnabled && isAfterEnabled);
					menuComponent.setVisible(isBeforeEnabled && isAfterEnabled);
				} else {
					// do nothing
				}
			}
		}
	}

	// METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
	private boolean checkMenuComponent(JMenu menu, int i) {
		Component componentChecked = menu.getMenuComponent(i);
		return ((componentChecked.isEnabled())&&(componentChecked.isVisible()));
	}

	// METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
	private void enableMenu() {
		JMenuBar menuBar = getJMenuBar();
		int menuCount = menuBar.getMenuCount();
		for(int i=0; i<menuCount; i++){
			JMenu menu = menuBar.getMenu(i);
			if(!(menu.getText().equalsIgnoreCase("File")||menu.getText().equalsIgnoreCase("Window")||menu.getText().equalsIgnoreCase("Help"))){
				String text = menu.getText();
				String path = text + "\\";

				if(isGranted(path)){
					System.err.println("true: " + path);
					menu.setEnabled(true);
					menu.setVisible(true);
				}
				else{
					System.err.println("false: " + path);
					menu.setEnabled(false);
					menu.setVisible(false);
				}
				enableMenuItems(menu, text);
			}
		}
	}

	// METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
	private boolean isGranted(String path) {
		return hashtable.containsKey(path);
	}

	// METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
	private void enableMenuItems(JMenu menu, String text) {
		int menuCount = menu.getItemCount();
		for(int i=0; i<menuCount; i++){
			JMenuItem menuItem = menu.getItem(i);
			if(menuItem!=null){
				String newText = text + "\\" + menuItem.getText();
				if(menuItem instanceof JMenu){
					String path = newText + "\\";
					if(isGranted(path)){
						System.err.println("true: " + path);
						menuItem.setEnabled(true);
						menuItem.setVisible(true);
					}
					else{
						System.err.println("false: " + path);
						menuItem.setEnabled(false);
						menuItem.setVisible(false);
					}
					enableMenuItems((JMenu) menuItem, newText);
				}else{
					String path = newText + "\\";
					if(isGranted(path)){
						System.err.println("true: " + path);
						menuItem.setEnabled(true);
						menuItem.setVisible(true);
					}
					else{
						System.err.println("false: " + path);
						menuItem.setEnabled(false);
						menuItem.setVisible(false);
					}
				}
			}
		}
	}

	// METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
	private void populateMenu() {
		JMenuBar menuBar = getJMenuBar();
		int menuCount = menuBar.getMenuCount();
		for(int i=0; i<menuCount; i++){
			JMenu menu = menuBar.getMenu(i);
			if(!(menu.getText().equalsIgnoreCase("File")||menu.getText().equalsIgnoreCase("Window")||menu.getText().equalsIgnoreCase("Help"))){
				String text = menu.getText();
				String toPrint =
					"INSERT INTO FUNCTION (APPLICATION, FUNCTIONNAME, TREEPATH) "
					+ "VALUES ('PROJECT', '" + menu.getText() + "', '"
					+ text + "\\\\')" + "\n//";
				System.err.println(toPrint);
				populateMenuItems(menu, text);
			}
		}
	}

	// METHOD INI JANGAN SEKALI-SEKALI DIHAPUS!!!!
	// TERMASUK SQL STATEMENT INSERT BLA..BLA..BLA..
	private void populateMenuItems(JMenu menu, String text) {
		/*Maxdb
		 * insert into functionstructure as
		 select a.autoindex superfunction, b.autoindex subfunction from function a,
		 (select autoindex, application, substr(treepath,1,index(treepath,functionname)-1) parent from function) b
		 where a.treepath=b.parent and a.application=b.application*/
		
		/*Mysql
		 * insert into functionstructure (superfunction, subfunction) select a.autoindex superfunction, b.autoindex subfunction from function a, (select autoindex, application, substring_index(treepath,functionname,1) parent from function) b where a.treepath=b.parent and a.application=b.application
		 * */
		/*insert into userdetail (username, fullname) (
		 select username, username fullname from uaccount)*/
		int menuCount = menu.getItemCount();
		for(int i=0; i<menuCount; i++){
			JMenuItem menuItem = menu.getItem(i);
			if(menuItem!=null){
				String newText = text + "\\" + menuItem.getText();
				if(menuItem instanceof JMenu){
					String toPrint =
						"INSERT INTO FUNCTION (APPLICATION, FUNCTIONNAME, TREEPATH) "
						+ "VALUES ('PROJECT', '" + menuItem.getText() + "', '"
						+ newText + "\\')" + "\n//";
					System.err.println(toPrint.replace("\\", "\\\\"));
					populateMenuItems((JMenu) menuItem, newText);
				}else{
					String toPrint =
						"INSERT INTO FUNCTION (APPLICATION, FUNCTIONNAME, TREEPATH) "
						+ "VALUES ('PROJECT', '" + menuItem.getText() + "', '"
						+ newText + "\\')" + "\n//";
					System.err.println(toPrint.replace("\\", "\\\\"));
				}
			}
		}
	}

	void adjustPosition() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(dim.width, dim.height );
	}

	void login() {
		ManagerLoginDlg logindlg = new ManagerLoginDlg(this, IConstants.APP_PROJECT);
		//logindlg.show();
		logindlg.setVisible(true);
		if(logindlg.m_sessionid == -2)
			System.exit(0);

		m_sessionid = logindlg.m_sessionid;
		m_userProfile = logindlg.getUserProfile();
	}

	void logout() {
		try{
			LoginBusinessLogic loginbl = new LoginBusinessLogic(getConnection());
			loginbl.logout(m_sessionid);
			m_sessionid = -1;
		}
		catch(Exception ex){
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}
	}

	void createMenu() {
		JMenuBar mbar = new JMenuBar();
		setJMenuBar(mbar);

		JMenu mfile = new JMenu("File");
		mbar.add(mfile);
		mi_logout.addActionListener(this);
		mfile.add(mi_logout);
		mi_exit.addActionListener(this);
		mfile.add(mi_exit);

		JMenu mregistration = new JMenu("Registration");
		mbar.add(mregistration);

		mi_projectlist.addActionListener(this);
		mregistration.add(mi_projectlist);

		JMenu mmonitoring = new JMenu("Monitoring");
		mbar.add(mmonitoring);

		mi_monitoring.addActionListener(this);
		mmonitoring.add(mi_monitoring);

		JMenu mtimesheet = new JMenu("Time Sheet");
		mtimesheet.add(mi_tsheet);
		mi_tsheet.addActionListener(this);
		mbar.add(mtimesheet);

		JMenu mexpensesheet = new JMenu("Expense Sheet");
		mexpensesheet.add(mi_expensesheet);
		mi_expensesheet.addActionListener(this);
		mbar.add(mexpensesheet);

		// set menu Master Data
		JMenu mmaster = new JMenu("Master Data");
		mbar.add(mmaster);
		JMenu mcustomer = new JMenu("Customer");
		mmaster.add(mcustomer);
		mi_customergroup.addActionListener(this);
		mcustomer.add(mi_customergroup);
		mcustomer.addSeparator();
		mi_customer.addActionListener(this);
		mcustomer.add(mi_customer);

		JMenu mpartner = new JMenu("Partner");
		mmaster.add(mpartner);

		JMenu mreport = new JMenu("Report");
		mbar.add(mreport);
		mreport.add(mi_rep_project_profitability);
		mi_rep_project_profitability.addActionListener(this);
		mreport.add(mi_rep_project_cost);
		mi_rep_project_cost.addActionListener(this);
		mreport.add(mi_rep_field_allowances);
		mi_rep_field_allowances.addActionListener(this);
		mreport.add(mi_rep_personnel_utilization);
		mi_rep_personnel_utilization.addActionListener(this);

		mi_partnergroup.addActionListener(this);
		mpartner.add(mi_partnergroup);
		mpartner.addSeparator();
		mi_partner.addActionListener(this);
		mpartner.add(mi_partner);

		mi_personal.addActionListener(this);
		mmaster.add(mi_personal);

		mi_activity.addActionListener(this);
		mmaster.add(mi_activity);

		mbar.add(m_windowMenu);

		// i add this lines
		mbar.add(m_helpMenu);
		mi_helpContentsMenu.addActionListener(this);
		m_helpMenu.add(mi_helpContentsMenu);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == mi_logout) {
			m_desktopPane.removeAll();
			m_desktopPane.repaint();
			logout();
			login();
		}
		else if(e.getSource() == mi_exit){
			if(m_sessionid != -1)
				logout();
			deInit();
			System.exit(0);
		}

		else if(e.getSource() == mi_projectlist) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			InternalFrame internalFrame = new InternalFrame("Project List", this);
			internalFrame.setContentPane(new ProjectListPanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 500);
			m_desktopPane.add( internalFrame );

			internalFrame.setVisible( true );
			try {
				internalFrame.setMaximum(true);
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}

			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if(e.getSource() == mi_monitoring) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			InternalFrame internalFrame = new InternalFrame("Project Monitoring", this);
			internalFrame.setContentPane(new ProjectMonitoringPanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 600);
			m_desktopPane.add( internalFrame );

			internalFrame.setVisible( true );
			try {
				internalFrame.setMaximum(true);
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}

			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		// aksi time sheet
		else if(e.getSource() == mi_tsheet) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			InternalFrame internalFrame = new InternalFrame("Project Time Sheet", this);
			internalFrame.setContentPane(new TimeSheetPanel(m_conn, m_sessionid));
			internalFrame.setSize(800, 600);
			m_desktopPane.add( internalFrame );

			internalFrame.setVisible( true );
			try {
				internalFrame.setMaximum(true);
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}

			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		else if(e.getSource() == mi_expensesheet) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			InternalFrame internalFrame = new InternalFrame("Expense Sheet", this);
			internalFrame.setContentPane(new ExpenseSheetPanel(m_conn, m_sessionid));
			internalFrame.setSize(1000, 725);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setMaximum(true);
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		// aksi Master Data > Customer > Customer Group
		else if(e.getSource() == mi_customergroup) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			InternalFrame internalFrame = new InternalFrame("Customer Group", this);
			internalFrame.setContentPane(new CustomerCompanyGroupPanel(m_conn, m_sessionid));
			internalFrame.setSize(500, 400);
			m_desktopPane.add( internalFrame );

			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}

			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		// aksi Master Data > Customer > Customer
		else if(e.getSource() == mi_customer) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			InternalFrame internalFrame = new InternalFrame("Customer", this);
			internalFrame.setContentPane(new CustomerPanel(m_conn, m_sessionid));
			internalFrame.setSize(750, 500);
			m_desktopPane.add( internalFrame );

			internalFrame.setVisible( true );
			try {
				internalFrame.setMaximum(true);
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}

			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		// aksi master data > partner > partner group
		else if(e.getSource() == mi_partnergroup) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			InternalFrame internalFrame = new InternalFrame("Partner Group", this);
			internalFrame.setContentPane(new PartnerCompanyGroupPanel(m_conn, m_sessionid));
			internalFrame.setSize(500, 400);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}

			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		// aksi master data > partner > partner
		else if(e.getSource() == mi_partner) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			InternalFrame internalFrame = new InternalFrame("Partner", this);
			internalFrame.setContentPane(new PartnerPanel(m_conn, m_sessionid));
			internalFrame.setSize(750, 500);
			m_desktopPane.add( internalFrame );

			internalFrame.setVisible( true );
			try {
				internalFrame.setMaximum(true);
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}

			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		// aksi master data >  personal
		else if(e.getSource() == mi_personal) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			InternalFrame internalFrame = new InternalFrame("Personal", this);
			internalFrame.setContentPane(new PersonalPanel(m_conn, m_sessionid));
			internalFrame.setSize(750, 500);
			m_desktopPane.add( internalFrame );

			internalFrame.setVisible( true );
			try {
				internalFrame.setMaximum(true);
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}

			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		// accounting
		// aksi activity
		else if(e.getSource() == mi_activity) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			InternalFrame internalFrame = new InternalFrame("Activity", this);
			internalFrame.setContentPane(new pohaci.gumunda.titis.accounting.cgui.ActivityTreePanel(m_conn, m_sessionid));
			internalFrame.setSize(350, 400);
			m_desktopPane.add( internalFrame );

			internalFrame.setVisible( true );
			try {
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}

			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if (e.getSource() == mi_rep_project_profitability){
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			InternalFrame internalFrame = new InternalFrame(
					"Project Profitability", this);

			internalFrame.setContentPane(new ProjectProfitabilityPanel(
					m_conn, m_sessionid));
			internalFrame.setSize(400, 150);
			m_desktopPane.add(internalFrame);

			internalFrame.setVisible(true);
			try {
				internalFrame.setMaximum(true);
				internalFrame.setSelected(true);
			} catch (Exception exception) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if (e.getSource() == mi_rep_project_cost){
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			InternalFrame internalFrame = new InternalFrame("Project Cost", this);
			// set panel project list panel
			internalFrame.setContentPane(new ProjectCostPanel(m_conn,m_sessionid));
			internalFrame.setSize(700, 400);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setMaximum(true);
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if (e.getSource() == mi_rep_field_allowances){
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			InternalFrame internalFrame = new InternalFrame("Field Allowances", this);
			// set panel project list panel
			internalFrame.setContentPane(new pohaci.gumunda.titis.project.cgui.report.RptFieldAllowances(m_conn,m_sessionid));
			internalFrame.setSize(700, 400);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setMaximum(true);
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		else if (e.getSource() == mi_rep_personnel_utilization){
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			InternalFrame internalFrame = new InternalFrame("Personal Utilization", this);
			internalFrame.setContentPane(new RptPersonalUtilization(m_conn,m_sessionid));
			internalFrame.setSize(700, 400);
			m_desktopPane.add( internalFrame );
			internalFrame.setVisible( true );
			try {
				internalFrame.setMaximum(true);
				internalFrame.setSelected( true );
			}
			catch( Exception exception ) {
			}
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		}

		else if(e.getSource() == mi_helpContentsMenu) {
			try {
				String file = HELP_FOLDER + HELP_FILENAME;
				Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", file});
				//process.waitFor();
			} catch (Exception ext) {
				System.out.println(ext.getMessage());
			}
		}
	}

	public static void main(String[] args) {
		try {
			javax.swing.UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.Plastic3DLookAndFeel" );
			//javax.swing.UIManager.setLookAndFeel("com.digitprop.tonic.TonicLookAndFeel");
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
		new MainFrame();
	}

}

