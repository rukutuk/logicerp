package pohaci.gumunda.titis.application;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 *  JSpinField is a numeric field with 2 spin buttons to increase or decrease
 *  the value.
 *
 *@author     Kai Toedter
 *@version    1.1.4 07/16/02
 */
public class JSpinField extends JPanel implements CaretListener,
                AdjustmentListener, ActionListener {
        /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		/**
         *  Default JSpinField constructor.
         */
        public JSpinField() {
                super();
                min = 0;
                max = 100;
                value = 0;
                darkGreen = new Color(0, 150, 0);

                setLayout(new BorderLayout());
                textField = new JTextField(Integer.toString(value));
                textField.addCaretListener(this);
                textField.addActionListener(this);
        textField.setHorizontalAlignment( JTextField.RIGHT );
                add(textField, BorderLayout.CENTER);

                scrollBar = new JScrollBar(Adjustable.VERTICAL, 0, 0, 0, 100);
                scrollBar.setPreferredSize(
                                new Dimension(scrollBar.getPreferredSize().width,
                                textField.getPreferredSize().height));
                scrollBar.setMinimum(min);
                scrollBar.setMaximum(max);
                scrollBar.setValue(max + min - value);
                scrollBar.setVisibleAmount(0);

                scrollBar.addAdjustmentListener(this);
                add(scrollBar, BorderLayout.EAST);
        }


        /**
         *  Sets the value attribute of the JSpinField object
         *
         *@param  newValue         The new value value
         *@param  updateTextField  The new value value
         *@param  updateScrollbar  The new value value
         */
        protected void setValue(int newValue, boolean updateTextField,
                        boolean updateScrollbar) {
                int oldValue = value;

                if (newValue < min) {
                        value = min;
                } else if (newValue > max) {
                        value = max;
                } else {
                        value = newValue;
                }

                if (updateTextField) {
                        textField.setText(Integer.toString(value));
                        textField.setForeground(Color.black);
                }

                if (updateScrollbar) {
                        scrollBar.setValue(max + min - value);
                }

                firePropertyChange("value", oldValue, value);
        }


        /**
         *  Sets the value. This is a bound property.
         *
         *@param  newValue  the new value
         *@see              #getValue
         */
        public void setValue(int newValue) {
                setValue(newValue, true, true);
        }


        /**
         *  Returns the value.
         *
         *@return    The value value
         */
        public int getValue() {
                return value;
        }


        /**
         *  Sets the minimum value.
         *
         *@param  newMinimum  the new minimum value
         *@see                #getMinimum
         */
        public void setMinimum(int newMinimum) {
                min = newMinimum;
                scrollBar.setMinimum(min);
        }


        /**
         *  Returns the minimum value.
         *
         *@return    The minimum value
         */
        public int getMinimum() {
                return min;
        }


        /**
         *  Sets the maximum value and adjusts the preferred width.
         *
         *@param  newMaximum  the new maximum value
         *@see                #getMaximum
         */
        public void setMaximum(int newMaximum) {
                max = newMaximum;
                scrollBar.setMaximum(max);

                textField.setPreferredSize(
                                new Dimension(new JTextField(Integer.toString(newMaximum)).getPreferredSize().width,
                                textField.getPreferredSize().height));
        }


        /**
         *  Sets the horizontal alignment of the displayed value.
         */
        public void setHorizontalAlignment(int alignment) {
                textField.setHorizontalAlignment(alignment);
        }


        /**
         *  Returns the maximum value.
         *
         *@return    The maximum value
         */
        public int getMaximum() {
                return max;
        }


        /**
         *  Sets the font property.
         *
         *@param  font  the new font
         */
        public void setFont(Font font) {
                if (textField != null) {
                        textField.setFont(font);
                }
        }


        /**
         *  Sets the foreground color.
         *
         *@param  fg  the new foreground
         */
        public void setForeground(Color fg) {
                if (textField != null) {
                        textField.setForeground(fg);
                }
        }


        /**
         *  After any user input, the value of the textfield is proofed. Depending on
         *  being an integer, the value is colored green or red.
         *
         *@param  e  Description of the Parameter
         */
        public void caretUpdate(CaretEvent e) {
                try {
                        int testValue = Integer.valueOf(textField.getText()).intValue();

                        if ((testValue >= min) && (testValue <= max)) {
                                textField.setForeground(darkGreen);
                                setValue(testValue, false, false);
                        } else {
                                textField.setForeground(Color.red);
                        }
                } catch (Exception ex) {
                        if (ex instanceof NumberFormatException) {
                                textField.setForeground(Color.red);
                        }
                        // Ignore all other exceptions, e.g. illegal state exception
                }
                textField.repaint();
        }


        /**
         *  The 2 buttons are implemented with a JScrollBar.
         *
         *@param  e  Description of the Parameter
         */
        public void adjustmentValueChanged(AdjustmentEvent e) {
                setValue(max + min - e.getValue(), true, false);
        }


        /**
         *  After any user input, the value of the textfield is proofed. Depending on
         *  being an integer, the value is colored green or red. If the textfield is
         *  green, the enter key is accepted and the new value is set.
         *
         *@param  e  Description of the Parameter
         */
        public void actionPerformed(ActionEvent e) {
                if (textField.getForeground().equals(darkGreen)) {
                        setValue(Integer.valueOf(textField.getText()).intValue());
                }
        }


        /**
         *  Enable or disable the JSpinField.
         *
         *@param  enabled  The new enabled value
         */
        public void setEnabled(boolean enabled) {
                super.setEnabled(enabled);
                textField.setEnabled(enabled);
                scrollBar.setEnabled(enabled);
        }


        /**
         *  Creates a JFrame with a JSpinField inside and can be used for testing.
         *
         *@param  s  The command line arguments
         */
        public static void main(String[] s) {
                JFrame frame = new JFrame("JSpinField");
                frame.getContentPane().add(new JSpinField());
                frame.pack();
                frame.setVisible(true);
        }


        /**
         *  the text (number) field
         */
        protected JTextField textField;
        /**
         *  the scrollbar for the spin buttons
         */
        protected JScrollBar scrollBar;
        private Color darkGreen;
        private int min;
        private int max;
        private int value;
}
