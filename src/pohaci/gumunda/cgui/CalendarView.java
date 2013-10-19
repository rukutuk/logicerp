package pohaci.gumunda.cgui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CalendarView extends JPanel
    implements ActionListener
{	private static final long serialVersionUID = 1L;
	public CalendarView()
    {
        v_date = new Vector();
        maxYear = 2100;
        minYear = 1950;
        m_year = -1;
        m_month = -1;
        m_day = -1;
        setSize(new Dimension(350, 280));
        setLayout(new BorderLayout());
        constructNorth();
        constructSouth();
    }

    void constructNorth()
    {   m_cal = new GregorianCalendar();
        int i_year = m_cal.get(1);
        int i_month = m_cal.get(2);
        m_combo = new JComboBox(m_stringmonth);
        m_combo.setSelectedIndex(i_month);
        m_combo.setPreferredSize(new Dimension(100, 25));
        m_combo.addActionListener(this);
        m_textfield = new JTextField(5);
        m_textfield.addActionListener(this);
        m_textfield.setText(InttoStr(i_year));
        ImageIcon up = new ImageIcon("../images/thinarrowup.gif");
        ImageIcon down = new ImageIcon("../images/thinarrowdown.gif");
        m_buttonUp = new JButton(up);
        m_buttonDown = new JButton(down);
        m_buttonUp.addActionListener(this);
        m_buttonDown.addActionListener(this);
        JPanel m_panel1 = new JPanel();
        m_panel1.setLayout(new GridLayout(2, 1, 0, 1));
        m_panel1.setPreferredSize(new Dimension(20, 25));
        m_panel1.add(m_buttonUp);
        m_panel1.add(m_buttonDown);
        JPanel m_panel2 = new JPanel();
        m_panel2.setLayout(new BorderLayout());
        m_panel2.add(m_textfield, "Center");
        m_panel2.add(m_panel1, "East");
        JPanel m_panel3 = new JPanel();
        m_panel3.add(m_combo);
        m_panel3.add(m_panel2);
        add(m_panel3, "North");
    }

    void constructSouth()
    {
        int i_year = StrtoInt(m_textfield.getText().trim());
        int i_month = m_combo.getSelectedIndex();
        m_cal = new GregorianCalendar();
        m_cal.set(i_year, i_month, 1);
        int dayTotal = m_cal.getActualMaximum(5);
        i_month++;
        Date date = setDateFormat(String.valueOf(String.valueOf((new StringBuffer("01-")).append(InttoStr(i_month)).append("-").append(InttoStr(i_year)))));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        m_year = i_year;
        m_month = i_month;
        m_day = cal.get(7) - 1;
        String m_state = "";
        Iterator it = v_date.iterator();
        do
        {
            if(!it.hasNext())
                break;
            CalendarDate m_cd = (CalendarDate)it.next();
            if(m_cd.getMonth() != m_month || m_cd.getYear() != m_year)
                continue;
            m_state = "*";
            break;
        } while(true);
        if(m_state.trim().equals(""))
        {
            for(int i = 1; i <= dayTotal; i++)
            {
                CalendarDate m_cd = new CalendarDate();
                m_cd.setYear(m_year);
                m_cd.setMonth(m_month);
                m_cd.setDay(i);
                m_cd.setDate(setDateFormat(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(InttoStr(i))))).append("-").append(InttoStr(m_month)).append("-").append(InttoStr(m_year))))));
                m_cd.setOnOff(-1);
                m_cd.setClick(0);
                v_date.addElement(m_cd);
            }

        }
        pd = new PaintDate(this, v_date, m_year, m_month, m_day);
        add(pd, "Center");
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == m_buttonUp && getTextField())
        {
            setUpYear();
            rebuild();
        }
        if(e.getSource() == m_buttonDown && getTextField())
        {
            setDownYear();
            rebuild();
        }
        if(e.getSource() == m_combo && getTextField())
            rebuild();
        if(e.getSource() == m_textfield && getTextField())
            rebuild();
    }

    void rebuild()
    {
        remove(pd);
        repaint();
        revalidate();
        constructSouth();
    }

    void setUpYear()
    {
        int i = StrtoInt(m_textfield.getText());
        if(++i > maxYear)
            i = minYear;
        m_textfield.setText(InttoStr(i));
    }

    void setDownYear()
    {
        int i = StrtoInt(m_textfield.getText());
        if(--i < minYear)
            i = maxYear;
        m_textfield.setText(InttoStr(i));
    }

    public String InttoStr(int value)
    {
        Integer in = new Integer(value);
        String s = in.toString();
        return s;
    }

    public int StrtoInt(String value)
    {
        Integer in = new Integer(value);
        int i = in.intValue();
        return i;
    }

    public Date setDateFormat(String m_string)
    {
        SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyyy");
        ParsePosition pos = new ParsePosition(0);
        Date date = new Date(formater.parse(m_string, pos).getTime());
        return date;
    }

    public boolean getTextField()
    {
        boolean m_flag = true;
        String m_string = m_textfield.getText().trim();
        int i = 0;
        do
        {
            if(i >= m_string.length())
                break;
            char c = m_string.charAt(i);
            if(c != '0' && c != '1' && c != '2' && c != '3' && c != '4' && c != '5' && c != '6' && c != '7' && c != '8' && c != '9')
            {
                m_flag = false;
                break;
            }
            i++;
        } while(true);
        if(m_string.length() == 4 && m_flag)
        {
            m_flag = true;
        } else
        {
            JOptionPane.showMessageDialog(null, "Angka tahun yang dimasukan tidak bisa diproses");
            m_flag = false;
        }
        return m_flag;
    }

    protected Calendar m_cal;
    protected JComboBox m_combo;
    protected JTextField m_textfield;
    protected JButton m_buttonUp;
    protected JButton m_buttonDown;
    protected Vector v_date;
    static final String m_stringmonth[] = {
        "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", 
        "November", "December"
    };
    int maxYear;
    int minYear;
    int m_year;
    int m_month;
    int m_day;
    PaintDate pd;

}