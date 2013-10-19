package pohaci.gumunda.titis.accounting.cgui.reportdesign;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

import javax.swing.tree.TreeNode;

public abstract class TreeableReportRow implements TreeNode {
	
	public static final Enumeration EMPTY_ENUMERATION = new Enumeration() {
		public boolean hasMoreElements() {
			return false;
		}
		public Object nextElement() {
			throw new NoSuchElementException("No more elements");
		}
	};

	protected Vector children = new Vector();
	private TreeNode parent;
	protected boolean allowsChildren;
	public Enumeration children() {
		if (children == null) {
			return EMPTY_ENUMERATION;
		} else {
			return children.elements();
		}
	}

	public boolean getAllowsChildren() {
		return allowsChildren;
	}

	public TreeNode getChildAt(int childIndex) {
		if (children == null) {
			throw new ArrayIndexOutOfBoundsException("node has no children");
		}
		return (TreeableReportRow) children.elementAt(childIndex);
	}

	public int getChildCount() {
		if (children == null) {
			return 0;
		} else {
			return children.size();
		}
	}

	public int getIndex(TreeNode aChild) {
		if (aChild == null) {
			throw new IllegalArgumentException("argument is null");
		}
		if (!isNodeChild(aChild)) {
			return -1;
		}
		return children.indexOf(aChild); // linear search
	}

	private boolean isNodeChild(TreeNode aNode) {
		boolean retval;
		if (aNode == null) {
			retval = false;
		} else {
			if (getChildCount() == 0) {
				retval = false;
			} else {
				retval = (aNode.getParent() == this);
			}
		}
		return retval;
	}

	public TreeNode getParent() {
		return parent;
	}

	public boolean isLeaf() {
		return (getChildCount() == 0);
	}
	
	public void add(TreeableReportRow newChild) {
		if (newChild != null && newChild.getParent() == this)
			insert(newChild, getChildCount() - 1);
		else
			insert(newChild, getChildCount());
	}

	public void insert(TreeableReportRow newChild, int childIndex) {
		if (newChild == null) {
			throw new IllegalArgumentException("new child is null");
		} else if (isNodeAncestor(newChild)) {
			throw new IllegalArgumentException("new child is an ancestor");
		}

		TreeableReportRow oldParent = (TreeableReportRow) newChild.getParent();

		if (oldParent != null) {
			oldParent.remove(newChild);
		}
		newChild.setParent(this);
		if (children == null) {
			children = new Vector();
		}
		children.insertElementAt(newChild, childIndex);
	}

	public void setParent(TreeableReportRow parent) {
		this.parent = parent;
	}

	public void remove(TreeableReportRow aChild) {
		if (aChild == null) {
			throw new IllegalArgumentException("argument is null");
		}

		if (!isNodeChild(aChild)) {
			throw new IllegalArgumentException("argument is not a child");
		}
		remove(getIndex(aChild)); // linear search
	}

	public void remove(int childIndex) {
		TreeableReportRow child = (TreeableReportRow)getChildAt(childIndex);
		children.removeElementAt(childIndex);
		child.setParent(null);
	}

	public boolean isNodeAncestor(TreeableReportRow anotherNode) {
		if (anotherNode == null) {
			return false;
		}
		TreeNode ancestor = this;
		do {
			if (ancestor == anotherNode) {
				return true;
			}
		} while ((ancestor = ancestor.getParent()) != null);
		return false;
	}
	
	
}
