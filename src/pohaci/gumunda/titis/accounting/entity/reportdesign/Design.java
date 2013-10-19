package pohaci.gumunda.titis.accounting.entity.reportdesign;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import pohaci.gumunda.titis.accounting.cgui.Journal;
import pohaci.gumunda.titis.application.LanguagePack;

public class Design implements TreeNode {

	public static final Enumeration EMPTY_ENUMERATION = new Enumeration() {

		public boolean hasMoreElements() {
			return false;
		}

		public Object nextElement() {
			throw new NoSuchElementException("No more elements");
		}
	};

	private long autoindex;
	
	private Design parent;

	protected Vector children;

	// custom properties
	private String name;

	private String title;

	private String language = "";

	private short positiveBalance;

	private Journal[] journals;
	
	private ReportRow rootRow;

	public ReportRow getRootRow() {
		return rootRow;
	}

	public void setRootRow(ReportRow rootRow) {
		this.rootRow = rootRow;
	}

	public Design(String name) {
		super();
		this.name = name;
	}

	public Design() {
		super();
	}

	public Journal[] getJournals() {
		return journals;
	}

	public void setJournals(Journal[] journals) {
		this.journals = journals;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public short getPositiveBalance() {
		return positiveBalance;
	}

	public void setPositiveBalance(short positiveBalance) {
		this.positiveBalance = positiveBalance;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public TreeNode getChildAt(int childIndex) {
		if (children == null) {
			throw new ArrayIndexOutOfBoundsException("node has no children");
		}
		return (Design) children.elementAt(childIndex);
	}

	public int getChildCount() {
		if (children == null) {
			return 0;
		} else {
			return children.size();
		}
	}

	public TreeNode getParent() {
		return parent;
	}
	
	public void setParent(Design parent) {
		this.parent = parent;
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

	public boolean getAllowsChildren() {
		return true;
	}

	public boolean isLeaf() {
		return (getChildCount() == 0);
	}

	public Enumeration children() {
		if (children == null) {
			return EMPTY_ENUMERATION;
		} else {
			return children.elements();
		}
	}

	public String toString() {
		return (this.name != null ? this.name : "");
	}

	public void add(Design newChild) {
		if (newChild != null && newChild.getParent() == this)
			insert(newChild, getChildCount() - 1);
		else
			insert(newChild, getChildCount());
	}

	public void insert(Design newChild, int childIndex) {
		if (newChild == null) {
			throw new IllegalArgumentException("new child is null");
		} else if (isNodeAncestor(newChild)) {
			throw new IllegalArgumentException("new child is an ancestor");
		}

		Design oldParent = (Design) newChild.getParent();

		if (oldParent != null) {
			oldParent.remove(newChild);
		}
		newChild.setParent(this);
		if (children == null) {
			children = new Vector();
		}
		children.insertElementAt(newChild, childIndex);
	}

	public boolean isNodeAncestor(TreeNode anotherNode) {
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
	
	public void remove(Design aChild) {
		if (aChild == null) {
			throw new IllegalArgumentException("argument is null");
		}

		if (!isNodeChild(aChild)) {
			throw new IllegalArgumentException("argument is not a child");
		}
		remove(getIndex(aChild)); // linear search
	}

	public void remove(int childIndex) {
		Design child = (Design)getChildAt(childIndex);
		children.removeElementAt(childIndex);
		child.setParent(null);
	}

	public long getAutoindex() {
		return autoindex;
	}

	public void setAutoindex(long autoindex) {
		this.autoindex = autoindex;
	}

	public LanguagePack getLanguagePack() {
		if(getLanguage().equals(""))
			return null;
		
		if(getLanguage().equals(LanguagePack.ENGLISH.toString()))
			return LanguagePack.ENGLISH;
		else if(getLanguage().equals(LanguagePack.INDONESIAN.toString()))
			return LanguagePack.INDONESIAN;
		else 
			return null;
	}

	public boolean equals(Object obj) {
		return this.toString().equals(obj.toString());
	}

	/*
	 * public int getDepth() { Object last = null; Enumeration enum_ =
	 * breadthFirstEnumeration();
	 * 
	 * while (enum_.hasMoreElements()) { last = enum_.nextElement(); }
	 * 
	 * if (last == null) { throw new Error("nodes should be null"); }
	 * 
	 * return ((DefaultMutableTreeNode) last).getLevel() - getLevel(); }
	 * 
	 * private int getLevel() { // TODO Auto-generated method stub return 0; }
	 * 
	 * private Enumeration breadthFirstEnumeration() { return new
	 * BreadthFirstEnumeration(this); }
	 */
}
