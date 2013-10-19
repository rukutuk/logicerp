package pohaci.gumunda.titis.application;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class SearchPanel extends JPanel {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JButton m_findBt, m_clearBt, m_cancelBt;

  public SearchPanel() {
    constructComponent();
  }

  void constructComponent() {
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    m_findBt = new JButton("Find");
    buttonPanel.add(m_findBt);
    m_clearBt = new JButton("Clear");
    buttonPanel.add(m_clearBt);
    m_cancelBt = new JButton("Cancel");
    buttonPanel.add(m_cancelBt);
  }
}