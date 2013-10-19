package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.io.Serializable;

public class Journal implements Serializable{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected long m_index = 0;
  protected String m_name = "";

  public Journal(String name) {
    m_name = name;
  }

  public Journal(long index, String name) {
    m_index = index;
    m_name = name;
  }
  
  public Journal(){
	  
  }

  public long getIndex() {
    return m_index;
  }
  
  public void setIndex(long index) {
	  m_index = index;
  }

  public String getName() {
    return m_name;
  }
  
  public void setName(String name){
	  m_name = name;
  }

  public String toString() {
    return m_name;
  }
}