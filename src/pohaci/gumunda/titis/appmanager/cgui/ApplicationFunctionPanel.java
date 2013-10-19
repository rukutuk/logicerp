/**
 * 
 */
package pohaci.gumunda.titis.appmanager.cgui;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.sql.Connection;

import javax.swing.JScrollPane;
import javax.swing.JTree;

/**
 * @author irwan
 *
 */
public class ApplicationFunctionPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane = null;
	private JTree tree = null;
	private Connection connection = null;

	/**
	 * This is the default constructor
	 */
	public ApplicationFunctionPanel(Connection connection) {
		this.connection = connection;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(getScrollPane(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes scrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTree());
		}
		return scrollPane;
	}

	/**
	 * This method initializes tree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getTree() {
		if (tree == null) {
			tree = new FunctionTree(connection);
		}
		return tree;
	}
}
