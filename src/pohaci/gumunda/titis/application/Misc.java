package pohaci.gumunda.titis.application;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class Misc {
  public static final String CONTAINTS_CRITERIA = "Text Containts Criteria";
  public static final String MATCH_CASE = "Match Case";
  public static final String FIND_WHOLE_WORDS_ONLY = "Find Whole Words Only";

  public static boolean getConfirmation() {
    Object[] options = {"Yes", "No"};
    if(JOptionPane.showOptionDialog(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
                                    "Are you sure ? ","Confirmation",
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0])
                                    == JOptionPane.NO_OPTION)
      return false;
    return true;
  }

  public static void hasSaved(Component component, String message) {
    JOptionPane.showMessageDialog(component, message + " has been saved");
  }

  public static String getCriteria(String attribute, String value, String criteria) {
    String statement = "", equality = "";
    if(criteria.equals(CONTAINTS_CRITERIA)) {
      equality = " LIKE ";
      value = "%" + value + "%";
      statement = "UPPER(" + attribute + ")" + equality + "'" + value.toUpperCase() + "'";
    }
    else if(criteria.equals(MATCH_CASE)) {
      equality = " LIKE ";
      value = "%" + value + "%";
      statement = attribute + equality + "'" + value + "'";
    }
    else {
      equality = " = ";
      statement = "UPPER(" + attribute + ")" + equality + "'" + value.toUpperCase() + "'";
    }

    return statement;
  }

  public static void setGridBagConstraints(JComponent container, JComponent component, GridBagConstraints gridBagConstraints,
                             int gridx, int gridy, int fill, int gridwidth, int gridheight,
                             double weightx, double weighty, Insets insets) {

    gridBagConstraints.gridx = gridx;
    gridBagConstraints.gridy = gridy;
    if(fill > -1)
      gridBagConstraints.fill = fill;
    gridBagConstraints.gridwidth = gridwidth;
    gridBagConstraints.gridheight = gridheight;
    gridBagConstraints.weightx = weightx;
    gridBagConstraints.weighty = weighty;
    if(insets != null)
      gridBagConstraints.insets = insets;
    container.add(component, gridBagConstraints);
  }
}
