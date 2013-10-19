/**
 * 
 */
package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pohaci.gumunda.cgui.GumundaMainFrame;

/**
 * @author dark-knight
 *
 */
public class JournalStandardPicker extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextField journalStandardTextField;
	JButton browseButton = new JButton("...");

	Connection conn = null;
	long sessionid = -1;
	JournalStandard journalStandard = null;
	
	public JournalStandardPicker(Connection conn, long sessionid) {
		this.conn = conn;
		this.sessionid = sessionid;
		
		initComponents();
	}
	
	public JournalStandardPicker() {
		initComponents();
	}
	
	private void initComponents() {
		browseButton.setPreferredSize(new Dimension(22, 18));
		browseButton.addActionListener(this);

		journalStandardTextField = new JTextField() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void addMouseListener(MouseListener l) {
			}

			public boolean isFocusTraversable() {
				return false;
			}
		};

	    setLayout(new BorderLayout(3, 1));
	    add(journalStandardTextField, BorderLayout.CENTER);
	    add(browseButton, BorderLayout.EAST);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==browseButton){
			JournalStandardTreeDlg dlg = 
				new JournalStandardTreeDlg(GumundaMainFrame.getMainFrame(), 
						conn, sessionid);
			dlg.setVisible(true);
			
			if(dlg.getResponse()==JOptionPane.OK_OPTION){
			
				JournalStandard oldJournalStandard = this.journalStandard;
				JournalStandard newJournalStandard = dlg.getJournalStandard();
				if(oldJournalStandard==null)
					oldJournalStandard = new JournalStandard();
				if(newJournalStandard==null)
					newJournalStandard = new JournalStandard();
				
				this.setJournalStandard(dlg.getJournalStandard());
				if((oldJournalStandard!=null)&&(newJournalStandard!=null)){
					if(newJournalStandard!=oldJournalStandard){
						firePropertyChange("journalStandard", oldJournalStandard, newJournalStandard);
					}
				}
			} else {
				
				JournalStandard oldJournalStandard = this.journalStandard;
				JournalStandard newJournalStandard = null;
				if(oldJournalStandard==null)
					oldJournalStandard = new JournalStandard();
				
				this.setJournalStandard(dlg.getJournalStandard());
				if((oldJournalStandard!=null)){
					if(newJournalStandard!=oldJournalStandard){
						firePropertyChange("journalStandard", oldJournalStandard, newJournalStandard);
					}
				}
				
			}
		}
	}
	
	public void setEnabled(boolean enable) {
		super.setEnabled(enable);
		journalStandardTextField.setEditable(enable);
		browseButton.setEnabled(enable);
	}

	public JournalStandard getJournalStandard() {
		return journalStandard;
	}

	public void setJournalStandard(JournalStandard journalStandard) {
		this.journalStandard = journalStandard;
		if(this.journalStandard!=null)
			journalStandardTextField.setText(this.journalStandard.getCode());
		else
			journalStandardTextField.setText("");
	}

	public void init(Connection conn2, long sessionid2) {
		this.conn = conn2;
		this.sessionid = sessionid2;		
	}
	
}
