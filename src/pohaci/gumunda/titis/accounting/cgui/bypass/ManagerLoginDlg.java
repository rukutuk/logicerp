package pohaci.gumunda.titis.accounting.cgui.bypass;

import javax.swing.JOptionPane;
import pohaci.gumunda.aas.logic.LoginBusinessLogic;
import pohaci.gumunda.titis.accounting.cgui.bypass.GumundaLoginDlg;
import pohaci.gumunda.titis.appmanager.entity.UserProfile;
import pohaci.gumunda.titis.appmanager.logic.AppManagerLogic;
import pohaci.gumunda.cgui.GumundaMainFrame;

public class ManagerLoginDlg extends GumundaLoginDlg{
	private static final long serialVersionUID = 1L;
	GumundaMainFrame m_mainFrame;
	String m_app;
	// i tambahin ye
	private UserProfile userProfile;
	
	public ManagerLoginDlg(GumundaMainFrame frame, String app){
		super(frame);
		m_mainFrame = frame;
		m_app = app;
	}
	
	public void onOk(){
		String username = super.m_tusername.getText();
		char passwd[] = super.m_password.getPassword();		
		try{  
			LoginBusinessLogic loginbl = new LoginBusinessLogic(m_mainFrame.getConnection());			
			super.m_sessionid = loginbl.simpleLogin(username, passwd, m_app);
			
			// tambah dikit gpp kali ye...
			AppManagerLogic logic = new AppManagerLogic(m_mainFrame.getConnection());
			userProfile = logic.getUserProfilByUsername(username);
		}
		catch(Exception ex){
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Attention", 2);
			ex.printStackTrace();
		}
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}
}
