package pohaci.gumunda.titis.application;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.border.BevelBorder;

import com.pohaci.titis.testconnection.MyConnection;

import pohaci.gumunda.aas.dbapi.ConnectionManager;
import pohaci.gumunda.cgui.GumundaMainFrame;

public class FrameMain extends GumundaMainFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private ConnectionManager m_connectmgr = null;
	MyConnection m_connectmgr = null;
	private String m_app = "";

	public JDesktopPane m_desktopPane;
	public JMenu m_windowMenu = new JMenu("Window");
	public JMenu m_helpMenu = new JMenu("Help");
	public JMenuItem mi_helpContentsMenu = new JMenuItem("Help Contents"); // i add this
	private ButtonGroup m_groupRadioMenuItem = new ButtonGroup();

	public FrameMain(String app) {
		m_app = app;
	}

	public void constructComponent() {
		m_desktopPane = new JDesktopPane();
		m_desktopPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(m_desktopPane, BorderLayout.CENTER);
	}

	public void init() {
		try {
			//m_connectmgr = new ConnectionManager("sampurna");
			m_connectmgr = new MyConnection();
			m_conn = m_connectmgr.getConnection();

		}
		catch(Exception ex){
			JOptionPane.showMessageDialog(this, ex.getMessage() + "\n " + m_app + " will be closed.");

			System.exit(0);
		}
	}

	public void deInit() {
		if(m_connectmgr != null) {
			try {
				m_connectmgr.getConnection().close();
				m_connectmgr = null;
			}
			catch(Exception exception) {}
		}
	}

	public void updateMenuInternalList() {
		Component[] menus = m_windowMenu.getMenuComponents();
		int i = 0;
		for (int k = 0; k < menus.length ; ++k) {
			if (menus[k] instanceof CheckBoxMenuItem) {
				m_windowMenu.remove((JMenuItem)menus[k]);
				m_groupRadioMenuItem.remove((JMenuItem)menus[k]);
			}
		}

		JInternalFrame[] frames = m_desktopPane.getAllFrames();
		for (int k=0; k<frames.length; ++k) {
			CheckBoxMenuItem menuItem = new CheckBoxMenuItem((InternalFrame)frames[k],frames[k].getTitle());
			menuItem.setText( (i+1) + ". " + menuItem.getFrame().getTitle() );
			menuItem.setMnemonic((int)(((i+1)+"").charAt(0)) );
			menuItem.setFont(new java.awt.Font("Tahoma", 0, 11));
			menuItem.updateUI();
			menuItem.setSelected(frames[i].isSelected());
			m_windowMenu.insert(menuItem,  m_windowMenu.getMenuComponentCount());
			menuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jMenuWindowListActionPerformed(evt);
				}
			});
			m_groupRadioMenuItem.add(menuItem);
			i++;
		}
	}

	public void jMenuWindowListActionPerformed(java.awt.event.ActionEvent evt) {
		if (evt.getSource() != null &&  evt.getSource() instanceof CheckBoxMenuItem) {
			CheckBoxMenuItem menuItem = (CheckBoxMenuItem)evt.getSource();
			try {
				menuItem.getFrame().setSelected(true);
			}
			catch (Exception ex){}
		}
	}
}