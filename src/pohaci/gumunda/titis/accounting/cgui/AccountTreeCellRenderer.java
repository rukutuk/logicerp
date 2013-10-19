package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

import pohaci.gumunda.titis.accounting.entity.Account;

public class AccountTreeCellRenderer extends JLabel implements TreeCellRenderer {
	private static final long serialVersionUID = 1L;
	static protected ImageIcon m_debetleafIcon, m_debetgroupIcon, m_creditleafIcon, m_creditgroupIcon;
	static protected final Color m_selectedBackgroundColor = new Color(156, 138, 206);
	static {
		try {
			m_debetleafIcon = new ImageIcon("../images/debetleaf.gif");
			m_debetgroupIcon = new ImageIcon("../images/debetgroup.gif");
			m_creditleafIcon = new ImageIcon("../images/creditleaf.gif");
			m_creditgroupIcon = new ImageIcon("../images/creditgroup.gif");
		}
		catch (Exception e) {
			System.out.println("Couldn't load images: " + e);
		}
	}
	
	protected boolean m_selected = false;
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		TreeModel tmodel = tree.getModel();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
		Account account = null;
		if(node != tmodel.getRoot())
			account = (Account)node.getUserObject();
		String  stringValue = tree.convertValueToText(value, selected,
				expanded, leaf, row, hasFocus);
		setText(stringValue);
		setToolTipText(stringValue);
		if(node == tmodel.getRoot())
			setIcon(null);
		else if(account.isGroup() && account.getBalanceAsString().equals(Account.STR_DEBET))
			setIcon(m_debetgroupIcon);
		else if(!account.isGroup() && account.getBalanceAsString().equals(Account.STR_DEBET))
			setIcon(m_debetleafIcon);
		else if(account.isGroup() && account.getBalanceAsString().equals(Account.STR_CREDIT))
			setIcon(m_creditgroupIcon);
		else if(!account.isGroup() && account.getBalanceAsString().equals(Account.STR_CREDIT))
			setIcon(m_creditleafIcon);
		if(hasFocus)
			setForeground(new Color(0, 0, 128));
		else
			setForeground(Color.black);
		m_selected = selected;
		return this;
	}
	
	public void paint(Graphics g) {
		Color            bColor;
		Icon             currentI = getIcon();
		if(m_selected)
			bColor = new Color(206, 206, 255);
		else if(getParent() != null)
			bColor = getParent().getBackground();
		else
			bColor = getBackground();
		g.setColor(bColor);
		if(currentI != null && getText() != null) {
			int          offset = (currentI.getIconWidth() + getIconTextGap());
			g.fillRect(offset - 1, 0, getWidth() - offset + 1,
					getHeight());
		}
		else
			g.fillRect(0, 0, getWidth(), getHeight());
		super.paint(g);
	}
}