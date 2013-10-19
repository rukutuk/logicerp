package pohaci.gumunda.titis.application;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import javax.swing.*;

import pohaci.gumunda.titis.hrm.cgui.DefaultWorkingDay;

public class JDayChooser extends JPanel implements ActionListener, KeyListener,
		FocusListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JDayChooser() {

		locale = Locale.getDefault();
		days = new JButton[49];
		selectedDay = null;
		Calendar calendar = Calendar.getInstance(locale);
		today = (Calendar) calendar.clone();
		objecTempVector = new Vector();
		tempVector = new Vector();
		deleteVector = new Vector();
		setLayout(new GridLayout(7, 7));

		for (int y = 0; y < 7; y++) {
			for (int x = 0; x < 7; x++) {
				int index = x + 7 * y;
				if (y == 0) {
					days[index] = new JButton() {
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

					days[index].setBackground(new Color(180, 180, 200));
				} else {
					days[index] = new JButton();
					days[index].addActionListener(this);
					days[index].addKeyListener(this);
					//days[index].addFocusListener(this);
				}
				days[index].setMargin(new Insets(0, 0, 0, 0));
				days[index].setFocusPainted(false);
				add(days[index]);
			}
		}
		init();
		setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		initialized = true;
	}

	protected void init() {
		colorRed = new Color(164, 0, 0);
		colorBlue = new Color(0, 0, 164);
		workingColor = new Color(255, 255, 255);
		JButton testButton = new JButton();
		nonworkingColor = testButton.getBackground();
		selectedColor = new Color(255, 255, 0);

		calendar = Calendar.getInstance(locale);
		calendar.set(Calendar.HOUR_OF_DAY,8);
		int firstDayOfWeek = calendar.getFirstDayOfWeek();
		DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
		dayNames = dateFormatSymbols.getShortWeekdays(); // ambil nama-nama
		// hari
		int day = firstDayOfWeek;

		for (int i = 0; i < 7; i++) {
			days[i].setText(dayNames[day]); // tulis nama hari
			if (day == 1) {
				days[i].setForeground(colorRed); // hari pertama merah
			} else {
				days[i].setForeground(colorBlue); // biru
			}
			if (day < 7) {
				day++;
			} else {
				day -= 6; // bilang aja day = 1.. aneh..
			}
		}
		drawDays();
	}

	protected void drawDays() {
		// if (this!=null)
		// return;
		Calendar tmpCalendar = (Calendar) calendar.clone();
		int firstDayOfWeek = tmpCalendar.getFirstDayOfWeek();
		tmpCalendar.set(Calendar.DAY_OF_MONTH, 1);

		int firstDay = tmpCalendar.get(Calendar.DAY_OF_WEEK) - firstDayOfWeek;
		System.out.println("firstDay = tmpCalendar.get(Calendar.DAY_OF_WEEK) - firstDayOfWeek; ");
		System.out.println("  "+ firstDay+"  =       "+ tmpCalendar.get(Calendar.DAY_OF_WEEK)+" - "+firstDayOfWeek);
	

		if (firstDay < 0) {
			firstDay += 7;
		}
		

		int i;

		for (i = 0; i < firstDay; i++) {
			days[i + 7].setVisible(false);
			days[i + 7].setText("");
		}

		tmpCalendar.add(Calendar.MONTH, 1);
		Date firstDayInNextMonth = tmpCalendar.getTime();
		tmpCalendar.add(Calendar.MONTH, -1);

		Date day = tmpCalendar.getTime();

		int n = 0;
		Color foregroundColor = getForeground();
		while (day.before(firstDayInNextMonth)) {
			days[i + n + 7].setText(Integer.toString(n + 1));
			days[i + n + 7].setVisible(true);
			if (tmpCalendar.get(Calendar.DAY_OF_YEAR) == today
					.get(Calendar.DAY_OF_YEAR)
					&& tmpCalendar.get(Calendar.YEAR) == today
							.get(Calendar.YEAR)) {
				days[i + n + 7].setForeground(colorRed);
			} else {
				days[i + n + 7].setForeground(foregroundColor);
			}

			if (n + 1 == this.day) {
				days[i + n + 7].setBackground(selectedColor);
				selectedDay = days[i + n + 7];
			} else {
				if ((i + n + 7) % 7 == 0
						&& new DefaultWorkingDay().getSunday() == false) {
					days[i + n + 7].setBackground(nonworkingColor);

				} else if ((i + n + 7) % 7 == 6
						&& new DefaultWorkingDay().getSaturday() == false) {
					days[i + n + 7].setBackground(nonworkingColor);

				} else if ((i + n + 7) % 7 == 1
						&& new DefaultWorkingDay().getMonday() == false) {
					days[i + n + 7].setBackground(nonworkingColor);

				} else if ((i + n + 7) % 7 == 2
						&& new DefaultWorkingDay().getTuesday() == false) {
					days[i + n + 7].setBackground(nonworkingColor);

				} else if ((i + n + 7) % 7 == 3
						&& new DefaultWorkingDay().getWednesday() == false) {
					days[i + n + 7].setBackground(nonworkingColor);

				} else if ((i + n + 7) % 7 == 4
						&& new DefaultWorkingDay().getThursday() == false) {
					days[i + n + 7].setBackground(nonworkingColor);

				} else if ((i + n + 7) % 7 == 5
						&& new DefaultWorkingDay().getFriday() == false) {
					days[i + n + 7].setBackground(nonworkingColor);
				}

				else
					days[i + n + 7].setBackground(workingColor);
			}
			n++;
			tmpCalendar.add(Calendar.DATE, 1);
			day = tmpCalendar.getTime();
		}
		for (int k = n + i + 7; k < 49; k++) {
			days[k].setVisible(false);
			days[k].setText("");
		}
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale l) {
		if (!initialized) {
			super.setLocale(l);
		} else {
			locale = l;
			init();
		}
	}

	public void setDay(int d) {
		if (d < 1) { // minimal 1
			d = 1;
		}
		Calendar tmpCalendar = (Calendar) calendar.clone();
		tmpCalendar.set(Calendar.DAY_OF_MONTH, 1); // tgl 1
		tmpCalendar.add(Calendar.MONTH, 1); // bulan berikutnya
		tmpCalendar.add(Calendar.DATE, -1); // hari sebelumnya
		int maxDaysInMonth = tmpCalendar.get(Calendar.DATE); // dapat berapa
		// hari dalam
		// sebulan ini..
		if (d > maxDaysInMonth) { // limit ke maksimal
			d = maxDaysInMonth;
		}
		int oldDay = day; // day sebelum diganti
		day = d;
		tmpCalendar.set(Calendar.DAY_OF_MONTH, day); // tunjuk tanggal yg
		// diberikan pada
		// parameter 'd'
		selectedDate = tmpCalendar.getTime(); // nonsense

		for (int i = 7; i < 49; i++) {
			// days -> array of button-button
			if (days[i].getText().equals(Integer.toString(day))) { // cari hari

				selectedDay = days[i];
				selectedDay.setBackground(selectedColor); // beri warna
			} else {

				if (i % 7 == 0 && new DefaultWorkingDay().getSunday() == false) {
					days[i].setBackground(nonworkingColor);

				} else if (i % 7 == 6
						&& new DefaultWorkingDay().getSaturday() == false) {
					days[i].setBackground(nonworkingColor);

				} else if (i % 7 == 1
						&& new DefaultWorkingDay().getMonday() == false) {
					days[i].setBackground(nonworkingColor);

				} else if (i % 7 == 2
						&& new DefaultWorkingDay().getTuesday() == false) {
					days[i].setBackground(nonworkingColor);

				} else if (i % 7 == 3
						&& new DefaultWorkingDay().getWednesday() == false) {
					days[i].setBackground(nonworkingColor);

				} else if (i % 7 == 4
						&& new DefaultWorkingDay().getThursday() == false) {
					days[i].setBackground(nonworkingColor);

				} else if (i % 7 == 5
						&& new DefaultWorkingDay().getFriday() == false) {
					days[i].setBackground(nonworkingColor);

				} else
					days[i].setBackground(workingColor); // beri warna hari

			}
		}
		firePropertyChange("day", oldDay, day); // beri tahu listener bahwa
		// property berubah
	}

	public int getDay() {
		return day;
	}

	public Date getDate() {
		return selectedDate;
	}

	public void setMonth(int month) {
		calendar.set(Calendar.MONTH, month);
		setDay(day);
		drawDays();
	}

	public void setYear(int year) {
		calendar.set(Calendar.YEAR, year);
		drawDays();
	}

	public void setCalendar(Calendar c) {
		calendar = c;
		drawDays();
	}

	public void setFont(Font font) {
		if (days != null) {
			for (int i = 0; i < 49; i++) {
				days[i].setFont(font);
			}
		}
	}

	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (days != null) {
			for (int i = 7; i < 49; i++) {
				days[i].setForeground(fg);
			}
			drawDays();
		}
	}

	public String getName() {
		return "JDayChooser";
	}

	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		String buttonText = button.getText();
		int day = new Integer(buttonText).intValue();
		setDay(day);
	}

	public void focusGained(FocusEvent e) {
		JButton button = (JButton) e.getSource();
		String buttonText = button.getText();
		if (buttonText != null && !buttonText.equals("")) {
			actionPerformed(new ActionEvent(e.getSource(), 0, null));
		}
	}

	public void focusLost(FocusEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		int offset = e.getKeyCode() == KeyEvent.VK_UP ? -7
				: e.getKeyCode() == KeyEvent.VK_DOWN ? +7
						: e.getKeyCode() == KeyEvent.VK_LEFT ? -1 : e
								.getKeyCode() == KeyEvent.VK_RIGHT ? +1 : 0;

		if (offset != 0) {
			for (int i = getComponentCount() - 1; i >= 0; --i) {
				if (getComponent(i) == selectedDay) {
					i += offset;
					if (i > 7 && i < days.length && days[i].isVisible()) {
						days[i].requestFocus();
						// int day = new Integer(days[i].getText()).intValue();
						// setDay( day );
					}
					break;
				}
			}
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (short i = 0; i < days.length; i++) {
			if (days[i] != null) {
				days[i].setEnabled(enabled);
			}
		}
	}
	public static void main(String[] s) {
		JFrame frame = new JFrame("JCalendar");
		frame.getContentPane().add(new JDayChooser());
		frame.setVisible(true);
	}


	protected JButton days[];

	protected JButton selectedDay;

	protected int day;

	protected Color workingColor;

	protected Color nonworkingColor;

	protected Color selectedColor;

	protected Color colorRed;

	protected Color colorBlue;

	protected String dayNames[];

	protected Calendar calendar;

	protected Calendar today;

	protected Locale locale;

	private boolean initialized = false;

	protected Date selectedDate = null;

	public static Vector objecTempVector, tempVector, deleteVector;
}