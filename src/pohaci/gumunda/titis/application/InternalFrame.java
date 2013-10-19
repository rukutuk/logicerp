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
import pohaci.gumunda.cgui.GumundaInternalFrame;

public class InternalFrame extends GumundaInternalFrame {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
FrameMain m_frame;

  public InternalFrame(String title, FrameMain frame) {
    setTitle(title);
    setFrameIcon(new ImageIcon("../images/group.gif"));
    m_frame = frame;

    addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
      public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
      }
      public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
        m_frame.updateMenuInternalList();
      }
      public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
        m_frame.updateMenuInternalList();
      }
      public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
      }
      public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
      }
      public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
        m_frame.updateMenuInternalList();
      }
      public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
      }
    });
  }
}