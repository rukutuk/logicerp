package pohaci.gumunda.titis.application;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import javax.swing.*;

public class CheckBoxMenuItem extends JCheckBoxMenuItem {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private InternalFrame m_frame;

  public CheckBoxMenuItem(InternalFrame frame, String text) {
    super(text);
    m_frame = frame;
  }

  public InternalFrame getFrame() {
    return m_frame;
  }

  public void setFrame(InternalFrame frame) {
    m_frame = frame;
  }
}