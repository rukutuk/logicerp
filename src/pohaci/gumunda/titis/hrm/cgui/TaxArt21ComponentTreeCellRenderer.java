package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

public class TaxArt21ComponentTreeCellRenderer extends JLabel implements
		TreeCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static protected ImageIcon m_headerIcon, m_itemIcon;
	static protected final Color m_selectedBackgroundColor = new Color(156,
			138, 206);

	static {
		try {
			m_headerIcon = new ImageIcon("../images/payrollheader.gif");
			m_itemIcon = new ImageIcon("../images/payrollitem.gif");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Couldn't load images: " + e);
		}
	}

	protected boolean m_selected = false;

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		TreeModel tmodel = tree.getModel();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		String stringValue = tree.convertValueToText(value, selected, expanded,
				leaf, row, hasFocus);
		setText(stringValue);
		setToolTipText(stringValue);

		if (node == tmodel.getRoot())
			setIcon(null);
		else {
			if (node.getUserObject() instanceof TaxArt21Component) {
				if (((TaxArt21Component) node.getUserObject()).isGroup())
					setIcon(m_headerIcon);
				else
					setIcon(m_itemIcon);
			}
		}
		if (hasFocus)
			setForeground(new Color(0, 0, 128));
		else
			setForeground(Color.black);

		m_selected = selected;
		return this;
	}

	public void paint(Graphics g) {
		Color bColor;
		Icon currentI = getIcon();

		if (m_selected)
			bColor = new Color(206, 206, 255);
		else if (getParent() != null)
			bColor = getParent().getBackground();
		else
			bColor = getBackground();

		g.setColor(bColor);
		if (currentI != null && getText() != null) {
			int offset = (currentI.getIconWidth() + getIconTextGap());
			g.fillRect(offset - 1, 0, getWidth() - offset + 1, getHeight());
		} else
			g.fillRect(0, 0, getWidth(), getHeight());

		super.paint(g);
	}
}