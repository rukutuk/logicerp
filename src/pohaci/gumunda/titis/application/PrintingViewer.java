package pohaci.gumunda.titis.application;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import java.awt.event.*;

public class PrintingViewer extends JasperViewer {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public PrintingViewer(String str, boolean flag) throws Exception{
    super(str, flag);
  }

  public PrintingViewer(JasperPrint jasperprint) throws Exception {
    super(jasperprint,false);
  }	

  
  public void processWindowEvent(WindowEvent e){
    if( e.getID() == WindowEvent.WINDOW_CLOSING )
      dispose();
  }
}
