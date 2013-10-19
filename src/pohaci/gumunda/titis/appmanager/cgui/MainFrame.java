/**
 *
 */
package pohaci.gumunda.titis.appmanager.cgui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.pohaci.titis.testconnection.MyConnection;

import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.application.FrameMain;
import pohaci.gumunda.titis.application.InternalFrame;
import pohaci.gumunda.titis.appmanager.entity.ApplicationFunction;
import pohaci.gumunda.titis.appmanager.entity.UserProfile;
import pohaci.gumunda.titis.appmanager.logic.AppManagerLogic;

public class MainFrame extends FrameMain {

	private static final long serialVersionUID = 1L;
	private Connection connection;
	private JMenuBar menuBar;
	private static String app = "Application Manager";

	private JMenu manageMenu;
	private JMenu fileMenu;
	private JMenuItem exitMenuItem;
	private JMenuItem userAdministrationMenuItem;
	private JMenuItem roleAdministrationMenuItem;
	private JMenuItem applicationFunctionMenuItem;
	private UserProfile m_userProfile;
	private boolean MODE_ADMIN = true;
	private Hashtable hashtable = new Hashtable();

	public MainFrame() {
		super(app);
		setTitle(app);
		setSize(750, 600);
		//setIconImage(Toolkit.getDefaultToolkit().getImage("../images/titis.gif"));
		setIconImage(Toolkit.getDefaultToolkit().getImage(".."));
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			}
		});
		databaseInitialize();
		initialize();
		setVisible(true);
		setExtendedState(Frame.MAXIMIZED_BOTH);

		populateMenu();

		//login();

		if (!MODE_ADMIN ) {
			getAllGrantedFunctions();
			enableMenu();
			deleteSeparator();
		}
	}

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

	private boolean checkMenuComponent(JMenu menu, int i) {
		Component componentChecked = menu.getMenuComponent(i);
		return ((componentChecked.isEnabled())&&(componentChecked.isVisible()));
	}

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

	private void enableMenuItems(JMenu menu, String text) {
		int menuCount = menu.getItemCount();
		for(int i=0; i<menuCount; i++){
			JMenuItem menuItem = menu.getItem(i);
			if(menuItem!=null){
				String newText = text + "\\\\" + menuItem.getText();
				if(menuItem instanceof JMenu){
					String path = newText + "\\\\";
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
					String path = newText + "\\\\";
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

	private boolean isGranted(String path) {
		return hashtable.containsKey(path);
	}

	private void getAllGrantedFunctions() {
		AppManagerLogic logic = new AppManagerLogic(m_conn);
		try {
			ApplicationFunction[] functions = logic.getGrantedFunctionForUser(m_userProfile, "APP MANAGER");
			hashtable.clear();
			for(int i=0; i<functions.length; i++){
				hashtable.put(functions[i].getPath(), functions[i]);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void populateMenu() {
		JMenuBar menuBar = getJMenuBar();
		int menuCount = menuBar.getMenuCount();
		for(int i=0; i<menuCount; i++){
			JMenu menu = menuBar.getMenu(i);
			if(!(menu.getText().equalsIgnoreCase("File")||menu.getText().equalsIgnoreCase("Window")||menu.getText().equalsIgnoreCase("Help"))){
				String text = menu.getText();
				String toPrint =
					"INSERT INTO FUNCTION (APPLICATION, FUNCTIONNAME, TREEPATH) "
					+ "VALUES ('APP MANAGER', '" + menu.getText() + "', '"
					+ text + "\\')" + "\n//";
				System.err.println(toPrint.replace("\\", "\\\\"));
				populateMenuItems(menu, text);
			}
		}
	}

	private void populateMenuItems(JMenu menu, String text) {
		int menuCount = menu.getItemCount();
		for(int i=0; i<menuCount; i++){
			JMenuItem menuItem = menu.getItem(i);
			if(menuItem!=null){
				String newText = text + "\\" + menuItem.getText();
				if(menuItem instanceof JMenu){
					String toPrint =
						"INSERT INTO FUNCTION (APPLICATION, FUNCTIONNAME, TREEPATH) "
						+ "VALUES ('APP MANAGER', '" + menuItem.getText() + "', '"
						+ newText + "\\')" + "\n//";
					System.err.println(toPrint.replace("\\", "\\\\"));
					populateMenuItems((JMenu) menuItem, newText);
				}else{
					String toPrint =
						"INSERT INTO FUNCTION (APPLICATION, FUNCTIONNAME, TREEPATH) "
						+ "VALUES ('APP MANAGER', '" + menuItem.getText() + "', '"
						+ newText + "\\')" + "\n//";
					System.err.println(toPrint.replace("\\", "\\\\"));
				}
			}
		}
	}

	private void login() {
		pohaci.gumunda.titis.accounting.cgui.bypass.ManagerLoginDlg logindlg = new pohaci.gumunda.titis.accounting.cgui.bypass.ManagerLoginDlg(this, IConstants.APP_ACCOUNTING);
		logindlg.setVisible(true);
		setVisible(true);
		if(logindlg.m_sessionid == -2)
			System.exit(0);

		m_sessionid = logindlg.m_sessionid;
		m_userProfile = logindlg.getUserProfile();
	}

	public void databaseInitialize() {
		init();
		connection = m_conn;

		try {
			//connectionManager = new ConnectionManager("sampurna");
			MyConnection connectionManager = new MyConnection(); 
			connection = connectionManager.getConnection();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage() + "\n " + "Applicaiton Manager"
					+ " will be closed.");

			System.exit(0);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			javax.swing.UIManager
					.setLookAndFeel("com.jgoodies.plaf.plastic.Plastic3DLookAndFeel");
			// javax.swing.UIManager.setLookAndFeel("com.digitprop.tonic.TonicLookAndFeel");
			javax.swing.plaf.FontUIResource f = new javax.swing.plaf.FontUIResource(
					"Tahoma", 0, 11);
			java.util.Enumeration keys = javax.swing.UIManager.getDefaults()
					.keys();
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				Object value = javax.swing.UIManager.get(key);
				if (value instanceof javax.swing.plaf.FontUIResource)
					javax.swing.UIManager.put(key, f);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		new MainFrame();
	}


	private void initialize() {
		constructComponent();
		setJMenuBar(getJJMenuBar());
	}

	private JMenuBar getJJMenuBar() {
		if(menuBar==null){
			menuBar = new JMenuBar();
			menuBar.add(getFileMenu());
			menuBar.add(getManageMenu());
			menuBar.add(getWindowMenu());
		}
		return menuBar;
	}

	private JMenu getWindowMenu() {
		if(m_windowMenu==null){
			m_windowMenu = new JMenu("Window");
		}
		return m_windowMenu;
	}

	private JMenu getManageMenu() {
		if(manageMenu==null){
			manageMenu = new JMenu("Manage");
			manageMenu.add(getUserAdministrationMenuItem());
			manageMenu.add(getRoleAdministrationMenuItem());
			manageMenu.add(getApplicationFunctionMenuItem());
		}
		return manageMenu;
	}

	private JMenuItem getApplicationFunctionMenuItem() {
		if(applicationFunctionMenuItem==null){
			applicationFunctionMenuItem = new JMenuItem("Application Function Administration");
			applicationFunctionMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getPanel(new ApplicationFunctionPanel(connection), "Application Function Administration", 700, 200, true);
				}
			});
		}
		return applicationFunctionMenuItem;
	}

	private JMenuItem getRoleAdministrationMenuItem() {
		if(roleAdministrationMenuItem==null){
			roleAdministrationMenuItem = new JMenuItem("Role Administration");
			roleAdministrationMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getPanel(new RoleAdministrationPanel(connection), "Role Administration", 700, 200, true);
				}
			});
		}
		return roleAdministrationMenuItem;
	}

	private JMenuItem getUserAdministrationMenuItem() {
		if(userAdministrationMenuItem==null){
			userAdministrationMenuItem = new JMenuItem("User Administration");
			userAdministrationMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getPanel(new UserAdministrationPanel(connection), "User Administration", 700, 600, false);
				}
			});
		}
		return userAdministrationMenuItem;
	}

	protected void getPanel(JPanel panel, String title, int width, int height, boolean isMaximum) {
		setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		InternalFrame internalFrame = new InternalFrame(title, this);
		internalFrame.setContentPane(panel);
		internalFrame.setSize(width, height);
		m_desktopPane.add( internalFrame );
		internalFrame.setVisible( true );

		try {
			internalFrame.setSelected( true );
			internalFrame.setMaximum(isMaximum);
		}
		catch( Exception exception ) {
		}

		setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	private JMenu getFileMenu() {
		if(fileMenu==null){
			fileMenu = new JMenu("File");
			fileMenu.add(getExitMenuItem());
		}
		return fileMenu;
	}

	private JMenuItem getExitMenuItem() {
		if (exitMenuItem == null) {
			exitMenuItem = new JMenuItem("Exit");
			exitMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
		}
		return exitMenuItem;
	}


}
