package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import pohaci.gumunda.titis.application.JCalendar;
import pohaci.gumunda.titis.application.JDayChooser;

public class HolidayJCalendar extends JCalendar {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public HolidayJCalendar(int monthSpinner, JDayChooser chooser) {
    super(monthSpinner, chooser);
  }
}