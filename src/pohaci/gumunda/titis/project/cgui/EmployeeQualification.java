/*
 * EmployeeQualification.java
 *
 * Created on March 3, 2007, 2:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package pohaci.gumunda.titis.project.cgui;

/**
 *
 * @author nunung
 */
public class EmployeeQualification {
    long m_employeeid;
    long m_qualification;
    String m_code = " ";
    String m_name  = "";
    
    /**
     * Creates a new instance of EmployeeQualification
     */
    public EmployeeQualification(long employeeid, long qualification ,String code) {        
        m_employeeid = employeeid;
        m_qualification = qualification;
        m_code = code;                
    }
    public EmployeeQualification(long employeeid, long qualification ,String code ,String name) {        
        m_employeeid = getEmployeeid();
        m_qualification = getQualification();
        m_code = getCode();       
        m_name = name;
    }
    
    public long getEmployeeid(){
        return m_employeeid;
    }
    
    public long getQualification(){
        return m_qualification;
    }
    
    public String getCode(){        
        return m_code;
    }
    public String getName(){
    	return m_name;
    }
    
}
