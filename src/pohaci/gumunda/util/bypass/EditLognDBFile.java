package pohaci.gumunda.util.bypass;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;


public class EditLognDBFile extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EditLognDBFile() {
		//super("Edit File Login", true);
		m_tusername = new JTextField();
		m_password = new JPasswordField();
		m_ok = new JButton("OK");
		m_cancel = new JButton("Batal");
		setSize(300, 150);
		setResizable(false);
		constructComponents();
		
	}
	
	void constructComponents() {
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		JPanel loginpanel = new JPanel();
		loginpanel.setLayout(gbl);
		getContentPane().setLayout(new BoxLayout(getContentPane(), 0));
		JLabel lusername = new JLabel("Username: ");
		gbc.fill = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbl.setConstraints(lusername, gbc);
		loginpanel.add(lusername);
		gbc.gridx = 1;
		gbc.gridwidth = 0;
		gbc.weightx = 1.0D;
		gbl.setConstraints(m_tusername, gbc);
		loginpanel.add(m_tusername);
		JLabel lpassword = new JLabel("Password: ");
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0.0D;
		gbl.setConstraints(lpassword, gbc);
		loginpanel.add(lpassword);
		gbc.gridx = 1;
		gbc.gridwidth = 0;
		gbc.weightx = 1.0D;
		gbl.setConstraints(m_password, gbc);
		loginpanel.add(m_password);
		JPanel btnpanel = new JPanel();
		btnpanel.add(m_ok);
		btnpanel.add(m_cancel);
		m_ok.addActionListener(this);
		m_cancel.addActionListener(this);
		btnpanel.setBorder(new EtchedBorder());
		JPanel formpanel = new JPanel();
		formpanel.setLayout(new BorderLayout());
		formpanel.add(loginpanel, "Center");
		formpanel.add(btnpanel, "South");
		JPanel imagepanel = new JPanel();
		getContentPane().add(imagepanel);
		getContentPane().add(formpanel);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_ok) {
			String username = m_tusername.getText().trim();
			char cpasswd[] = m_password.getPassword();
			String password = String.valueOf(cpasswd);
			try {
				CreateLoginDBFile createlogin = new CreateLoginDBFile();
				createlogin.create(username, password);
				JOptionPane.showMessageDialog(this, String
						.valueOf(String.valueOf((new StringBuffer("File : "))
								.append("password.txt").append(
								" telah diperbaharui "))));
				dispose();
			} catch (Exception exception) {
			}
		} else if (e.getSource() != m_cancel)
			;
	}
	
	public static void main(String args[]) throws IOException {
		
		
	}
	
	JTextField m_tusername;
	
	JPasswordField m_password;
	
	JButton m_ok;
	
	JButton m_cancel;
	
}
