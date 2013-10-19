package pohaci.gumunda.titis.appmanager.cgui;
import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JList;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.util.Vector;
import javax.swing.JButton;
import pohaci.gumunda.titis.appmanager.entity.Role;
import pohaci.gumunda.titis.appmanager.logic.AppManagerLogic;

public class RoleSelectionDlg extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JScrollPane scrollPane = null;

	private JList list = null;

	private JPanel buttonPanel = null;

	private JButton addButton = null;

	private JButton cancelButton = null;
	
	private Connection connection = null;

	private DefaultListModel listModel;
	
	private Role[] roles = null;
	
	private int response = JOptionPane.CANCEL_OPTION;
	
	private Frame owner = null;
	

	/**
	 * @param owner
	 */
	public RoleSelectionDlg(Frame owner, String title, Connection connection) {
		super(owner, title);
		this.owner = owner;
		this.connection = connection;
		setModal(true);
		initialize();
		initializeDB();
	}

	private void initializeDB() {
		AppManagerLogic logic = new AppManagerLogic(connection);
		try {
			Role[] roles = logic.getRoles();
			for(int i=0; i<roles.length; i++){
				getListModel().addElement(roles[i]);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Failed to initialize data");
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			jContentPane.add(getScrollPane(), BorderLayout.CENTER);
			jContentPane.add(getButtonPanel(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes scrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getList());
		}
		return scrollPane;
	}

	/**
	 * This method initializes list	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getList() {
		if (list == null) {
			list = new JList();
			list.setModel(getListModel());
		}
		return list;
	}

	private DefaultListModel getListModel() {
		if(listModel==null){
			listModel = new DefaultListModel();
		}
		return listModel;
	}

	/**
	 * This method initializes buttonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			buttonPanel = new JPanel();
			buttonPanel.setLayout(flowLayout);
			buttonPanel.add(getAddButton(), null);
			buttonPanel.add(getCancelButton(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes addButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddButton() {
		if (addButton == null) {
			addButton = new JButton();
			addButton.setText("Add");
			addButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onAdd();
				}
			});
		}
		return addButton;
	}

	protected void onAdd() {
		Object[] objects = (Object[]) getList().getSelectedValues();
		Vector vector = new Vector();
		for(int i=0; i<objects.length; i++){
			if(objects[i] instanceof Role){
				vector.addElement((Role)objects[i]);
			}
		}
		Role[] roles = (Role[]) vector.toArray(new Role[vector.size()]);
		
		setRoles(roles);

		setResponse(JOptionPane.OK_OPTION);
		setVisible(false);
	}

	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onCancel();
				}
			});
		}
		return cancelButton;
	}

	protected void onCancel() {
		setResponse(JOptionPane.CANCEL_OPTION);
		setVisible(false);
	}

	public int getResponse() {
		return response;
	}

	private void setResponse(int response) {
		this.response = response;
	}

	public Role[] getRoles() {
		return roles;
	}

	private void setRoles(Role[] roles) {
		this.roles = roles;
	}

	public void setVisible(boolean flag) {
		Rectangle rc = owner.getBounds();
	    Rectangle rcthis = getBounds();
	    setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
	              (int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
	              (int)rcthis.getWidth(), (int)rcthis.getHeight());

	    super.setVisible(flag);
	}

}
