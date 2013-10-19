package pohaci.gumunda.titis.application;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class JCalendar extends JPanel implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JCalendar() {
		this(JMonthChooser.NO_SPINNER, new JDayChooser());
	}
	public JCalendar(JDayChooser chooser){
		this(JMonthChooser.NO_SPINNER, chooser);
	}

	public JCalendar(int monthSpinner, JDayChooser chooser) {
		// needed for setFont() etc.
		dayChooser = null;
		monthChooser = null;
		yearChooser = null;

		locale = Locale.getDefault();
		calendar = Calendar.getInstance();

		setLayout(new BorderLayout(3, 3));
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new GridLayout(1, 3));
		monthChooser = new JMonthChooser(monthSpinner);
		yearChooser = new JYearChooser();
		monthChooser.setYearChooser(yearChooser);
		myPanel.add(monthChooser);
		myPanel.add(yearChooser);
		dayChooser = chooser;
		dayChooser.addPropertyChangeListener(this);
		monthChooser.setDayChooser(dayChooser);
		monthChooser.addPropertyChangeListener(this);
		yearChooser.setDayChooser(dayChooser);
		yearChooser.addPropertyChangeListener(this);
		add(myPanel, BorderLayout.NORTH);
		add(dayChooser, BorderLayout.CENTER);
		initialized = true;
	}

	private void setCalendar(Calendar c, boolean update) {
		Calendar oldCalendar = calendar;
		calendar = c;
		if (update) {
			// Thanks to Jeff Ulmer for correcting a bug in the sequence :)
			yearChooser.setYear(c.get(Calendar.YEAR));
			monthChooser.setMonth(c.get(Calendar.MONTH));
			dayChooser.setDay(c.get(Calendar.DATE));
		}
		firePropertyChange("calendar", oldCalendar, calendar);
	}

	public void setCalendar(Calendar c) {
		setCalendar(c, true);
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setLocale(Locale l) {
		if (!initialized) {
			super.setLocale(l);
		} else {
			Locale oldLocale = locale;
			locale = l;
			dayChooser.setLocale(locale);
			monthChooser.setLocale(locale);
			firePropertyChange("locale", oldLocale, locale);
		}
	}

	public Locale getLocale() {
		return locale;
	}

	public void setFont(Font font) {
		super.setFont(font);
		if (dayChooser != null) {
			dayChooser.setFont(font);
			monthChooser.setFont(font);
			yearChooser.setFont(font);
		}
	}

	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (dayChooser != null) {
			dayChooser.setForeground(fg);
			monthChooser.setForeground(fg);
			yearChooser.setForeground(fg);
		}
	}

	public void setBackground(Color bg) {
		super.setBackground(bg);
		if (dayChooser != null) {
			dayChooser.setBackground(bg);
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (calendar != null) {
			Calendar c = (Calendar) calendar.clone();
			if (evt.getPropertyName().equals("day")) {
				c.set(Calendar.DAY_OF_MONTH, ((Integer) evt.getNewValue())
						.intValue());
				setCalendar(c, false);
				System.out.println(" day");

			} else if (evt.getPropertyName().equals("month")) {
				c.set(Calendar.MONTH, ((Integer) evt.getNewValue()).intValue());
				setCalendar(c, false);
				System.out.println(" month ");
				//((HolidayJDayChooser) dayChooser).clear();

			} else if (evt.getPropertyName().equals("year")) {
				c.set(Calendar.YEAR, ((Integer) evt.getNewValue()).intValue());
				setCalendar(c, false);
				System.out.println(" year");
				//((HolidayJDayChooser) dayChooser).clear();

			}
		}
	}

	public String getName() {
		return "JCalendar";
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (dayChooser != null) {
			dayChooser.setEnabled(enabled);
			monthChooser.setEnabled(enabled);
			yearChooser.setEnabled(enabled);
		}
	}
	public void disableMonthYear() {
		monthChooser.setEnabled(false);
		yearChooser.setEnabled(false);
	}
	public void enableMonthYear() {
		monthChooser.setEnabled(true);
		yearChooser.setEnabled(true);
	}

	public JDayChooser getDayChooser() {
		return dayChooser;
	}

	public JMonthChooser getMonthChooser() {
		return monthChooser;
	}

	public JYearChooser getYearChooser() {
		return yearChooser;
	}

	public static void main(String[] s) {
		JFrame frame = new JFrame("JCalendar");
		frame.getContentPane().add(new JCalendar());
		frame.pack();
		frame.setVisible(true);
	}

	protected JYearChooser yearChooser;

	protected JMonthChooser monthChooser;

	public JDayChooser dayChooser;

	private Calendar calendar;

	private Locale locale;

	private boolean initialized = false;
}