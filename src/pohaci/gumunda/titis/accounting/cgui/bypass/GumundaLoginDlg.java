package pohaci.gumunda.titis.accounting.cgui.bypass;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public abstract class GumundaLoginDlg extends JDialog
    implements ActionListener{
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	protected JTextField m_tusername;
    protected JPasswordField m_password;
    JButton m_ok;
    JButton m_cancel;
    public long m_sessionid;
    private static boolean adminMode = true;
    public GumundaLoginDlg(Frame owner)
        throws HeadlessException{
    	super(owner, "Authentication", true);
        m_tusername = new JTextField();
        m_password = new JPasswordField();

        if(adminMode){
        	m_tusername.setText("admin");
        	m_password.setText("admin");
        	//m_password.setText("T1s4s4dm1n");
        }

        m_ok = new JButton("OK");
        m_cancel = new JButton("Cancel");
        m_sessionid = -1L;
        constructComponent();
        setSize(300, 150);
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }

        });
        Rectangle rect = owner.getBounds();
        adjustDialogPlace(rect);
    }

    public GumundaLoginDlg(Dialog owner){
        super(owner, "Otentifikasi", true);
        m_tusername = new JTextField();
        m_password = new JPasswordField();
        //m_ok = new JButton("Ok");
        //m_cancel = new JButton("Batal");
        m_ok = new JButton("OK");
        m_cancel = new JButton("Cancel");
        m_sessionid = -1L;
        constructComponent();
        setSize(300, 150);
        Rectangle rect = owner.getBounds();
        adjustDialogPlace(rect);
    }

    void adjustDialogPlace(Rectangle ownerrect){
        Dimension dim = getSize();
        setBounds((ownerrect.width / 2 - dim.width / 2) + ownerrect.x, (ownerrect.height / 2 - dim.height / 2) + ownerrect.y, dim.width, dim.height);
    }

    void constructComponent()    {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel loginpanel = new JPanel();
        loginpanel.setLayout(gbl);
        getContentPane().setLayout(new BoxLayout(getContentPane(), 0));
        JLabel lusername = new JLabel("Username ");
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
        JLabel lpassword = new JLabel("Password ");
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
        //btnpanel.setBorder(new EtchedBorder());
        JPanel formpanel = new JPanel();
        formpanel.setLayout(new BorderLayout());
        formpanel.add(loginpanel, "Center");
        formpanel.add(btnpanel, "South");
        JPanel imagepanel = new JPanel();
        getContentPane().add(imagepanel);
        getContentPane().add(formpanel);
    }

    public abstract void onOk();

    public void actionPerformed(ActionEvent e){
    	if(e.getSource() == m_ok) {
    		onOk();
    		System.out.println("sessionid = ".concat(String.valueOf(String.valueOf(m_sessionid))));
    		if(m_sessionid != (long)-1){
    			dispose();
    		} else{
    			/*JOptionPane.showMessageDialog(this,
    					"Username atau Password invalid\nAtau anda tidak memiliki hak untuk menggunakan a" +
    					"plikasi ini"
    					, "Perhatian", 2);*/
    			JOptionPane.showMessageDialog(this,
    					"Invalid username or password.\n" +
    					"You have no access to use this application.",
    					"Information", 2);
    			m_password.setText("");
    		}
    	} else
    		if(e.getSource() == m_cancel){
    			m_sessionid = -2L;
    			dispose();
    		}
    }
}


