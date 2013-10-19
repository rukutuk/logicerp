package pohaci.gumunda.titis.accounting.cgui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class StateTemplateEntity {
	protected transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	protected static boolean allowNumberRounding = true;
	
	public static abstract class State {
		public static final short NEW = -1;
		public static final short SAVED = 0;
		public static final short SUBMITTED = 1;
		public static final short VERIFIED = 2;
		public static final short POSTED = 3;
		abstract boolean allowEdit();
		abstract boolean allowSubmit();
		abstract boolean allowDelete();
	}

	public static class NewState extends State {
		//boolean allowEdit() {return true;}
		boolean allowEdit() {return false;}
		boolean allowSubmit() {return false;}
		boolean allowDelete() {return false;}
	}

	public static class SavedState extends State {
		boolean allowEdit() {return true;}
		boolean allowSubmit() {return true;}
		boolean allowDelete() {return true;}
	}

	public static class SubmittedState extends State {
		boolean allowEdit() {return false;}
		boolean allowSubmit() {return false;}
		boolean allowDelete() {return false;}
	}

	public static class PostedState extends State {
		boolean allowEdit() {return false;}
		boolean allowSubmit() {return false;}
		boolean allowDelete() {return false;}
	}

	public static State createState(short status)
	{
		switch (status)
		{
		case State.NEW: return new NewState();
		case State.SAVED: return new SavedState();
		case State.SUBMITTED: return new SubmittedState();
		case State.POSTED: return new PostedState();
		}
		return new NewState();
	}

	protected short status;

	public StateTemplateEntity.State getState() { return state; }

	protected StateTemplateEntity.State state = StateTemplateEntity.createState(StateTemplateEntity.State.NEW);
	protected Transaction trans;
	private Currency baseCurrency;

	public final void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public final void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public final PropertyChangeListener[] getPropertyChangeListeners() {
		return propertyChangeSupport.getPropertyChangeListeners();
	}

	public final PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
		return propertyChangeSupport.getPropertyChangeListeners(propertyName);
	}

	public final boolean hasListeners(String propertyName) {
		return propertyChangeSupport.hasListeners(propertyName);
	}

	public final void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public final void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
	}

	public final void firePropertyChange(PropertyChangeEvent evt) {
		propertyChangeSupport.firePropertyChange(evt);
	}

	public final void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	public final void firePropertyChange(String propertyName, int oldValue, int newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	public final void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	public final short getStatus() {
		return status;
	}

	public final String statusInString() {
		switch (status) {
		case State.SUBMITTED:
			return "Submitted";
		case State.VERIFIED:
			return "Submitted";
		case State.POSTED:
			return "Posted";
		default:
			return "Not Submitted";
		}
	}
	
	//i think our cust need this methode
	public static String status2String(short argStatus) {
		switch (argStatus) {
		case State.SUBMITTED:
			return "Submitted";
		case State.VERIFIED:
			return "Submitted";
		case State.POSTED:
			return "Posted";
		default:
			return "Not Submitted";
		}
	}
	
	public void setStatus(short status) {
		this.status = status;
		StateTemplateEntity.State oldState = state;
		this.state = StateTemplateEntity.createState(status);
		this.statusInString();
		System.out.println("status = "+this.getStatus());
		propertyChangeSupport.firePropertyChange("state",oldState,state);
	}

	public Transaction getTrans() {
		return trans;
	}

	public void setTrans(Transaction trans) {
		this.trans = trans;
	}
	protected void beforeSave() {
		
	}
	public void submit(long sessionId,java.sql.Connection conn) throws Exception {
		throw new RuntimeException(" >>>>>>>>>>>>>>>    >>>  > > Logika submit belum dibuat! ");
	}
	//public abstract void submit(long sessionId,java.sql.Connection conn)throws Exception;
	public abstract long getIndex();
	public abstract void setIndex(long index);

	public void isetBaseCurrency(Currency baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public Currency igetBaseCurrency() {
		return baseCurrency;
	}
	
}
