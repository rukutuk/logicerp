/**
 * 
 */
package pohaci.gumunda.titis.accounting.cgui.reportdesign;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportAccountValue;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportAltAccountValue;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportEmptyRow;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportGroup;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportLink;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportRow;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportSubtotal;

/**
 * @author dark-knight
 * 
 */
public class TreeableReportRowTreeCellRenderer extends DefaultTreeCellRenderer
		implements TreeCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static ImageIcon groupOpenedIcon, groupClosedIcon, valueIcon,
			altValueIcon, subtotalIcon, emptyIcon, linkIcon;
	//private static final Color m_selectedBackgroundColor = new Color(156, 138, 206);

	static {
		try {
			groupOpenedIcon = new ImageIcon("../images/folder.gif");
			groupClosedIcon = new ImageIcon("../images/folderok.gif");
			valueIcon = new ImageIcon("../images/dokumenok.gif");
			subtotalIcon = new ImageIcon("../images/dokumenadd.gif");
			emptyIcon = new ImageIcon("../images/dokumen.gif");
			altValueIcon = new ImageIcon("../images/document_preferences.gif");
			linkIcon = new ImageIcon("../images/document_connection.gif");
		} catch (Exception e) {
			System.out.println("Couldn't load images: " + e);
		}
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		ReportRow node = (ReportRow) value;
		super.getTreeCellRendererComponent(tree, value, selected, expanded,
				leaf, row, hasFocus);

		if (node instanceof ReportGroup) {
			if (expanded)
				setIcon(groupOpenedIcon);
			else
				setIcon(groupClosedIcon);
		} else if (node instanceof ReportAltAccountValue) {
			setIcon(altValueIcon);
		} else if (node instanceof ReportAccountValue) {
			setIcon(valueIcon);
		} else if (node instanceof ReportSubtotal) {
			setIcon(subtotalIcon);
		} else if (node instanceof ReportEmptyRow) {
			setIcon(emptyIcon);
		} else if (node instanceof ReportLink) {
			setIcon(linkIcon);
		} else {
			System.err.println("unknown type");
		}

		setDisabledIcon(getIcon());
		return this;
	}

}
