package pohaci.gumunda.titis.project.dbapi;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Vector;

import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.hrm.dbapi.HRMSQLSAP;
import pohaci.gumunda.titis.project.cgui.CompanyGroup;
import pohaci.gumunda.titis.project.cgui.ContractPayment;
import pohaci.gumunda.titis.project.cgui.Customer;
import pohaci.gumunda.titis.project.cgui.EmployeeQualification;
import pohaci.gumunda.titis.project.cgui.EmployeeTimeSheetByCriteria;
import pohaci.gumunda.titis.project.cgui.EmployeeTimesheet;
import pohaci.gumunda.titis.project.cgui.FileAttachment;
import pohaci.gumunda.titis.project.cgui.Partner;
import pohaci.gumunda.titis.project.cgui.Personal;
import pohaci.gumunda.titis.project.cgui.PersonalBusiness;
import pohaci.gumunda.titis.project.cgui.PersonalHome;
import pohaci.gumunda.titis.project.cgui.ProjectClientContact;
import pohaci.gumunda.titis.project.cgui.ProjectContract;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import pohaci.gumunda.titis.project.cgui.ProjectLocation;
import pohaci.gumunda.titis.project.cgui.ProjectNotes;
import pohaci.gumunda.titis.project.cgui.ProjectPartner;
import pohaci.gumunda.titis.project.cgui.ProjectPersonal;
import pohaci.gumunda.titis.project.cgui.ProjectProgress;
import pohaci.gumunda.titis.project.cgui.SpecificAddress;
import pohaci.gumunda.titis.project.cgui.TimeSheet;
import pohaci.gumunda.titis.project.cgui.TimeSheetDetail;
import pohaci.gumunda.util.OtherSQLException;

public class ProjectSQLSAP implements IProjectSQL {
	
	public long getMaxIndex(String table, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT MAX(" + IDBConstants.ATTR_AUTOINDEX + 
					") as maxindex FROM " + table);
			rs.next();
			return rs.getLong("maxindex");
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void createCustomerCompanyGroup(CompanyGroup group, Connection conn) throws SQLException {
		PreparedStatement stm = null;		
		try {
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_CUSTOMER_COMPANY_GROUP + "(" +
					IDBConstants.ATTR_NAME + "," +
					IDBConstants.ATTR_DESCRIPTION + ")" +
			" values (?, ?)");
			
			stm.setString(1, group.getName());
			stm.setString(2, group.getDescription());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public CompanyGroup[] getAllCustomerCompanyGroup(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_CUSTOMER_COMPANY_GROUP);			
			while(rs.next()) {
				vresult.addElement(new CompanyGroup(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_NAME), rs.getString(IDBConstants.ATTR_DESCRIPTION)));
			}			
			CompanyGroup[] group = new CompanyGroup[vresult.size()];
			vresult.copyInto(group);
			return group;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public CompanyGroup getCustomerCompanyGroup(long index, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_CUSTOMER_COMPANY_GROUP +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);			
			if(rs.next()) {
				return new CompanyGroup(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_NAME), rs.getString(IDBConstants.ATTR_DESCRIPTION));
			}
			
			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void updateCustomerCompanyGroup(long index, CompanyGroup group, Connection conn) throws SQLException {
		PreparedStatement stm = null;		
		try {
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_CUSTOMER_COMPANY_GROUP + " SET " +
					IDBConstants.ATTR_NAME + "=?, " +
					IDBConstants.ATTR_DESCRIPTION + "=? " +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
			
			stm.setString(1, group.getName());
			stm.setString(2, group.getDescription());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deleteCustomerCompanyGroup(long index, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_CUSTOMER_COMPANY_GROUP +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);			
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void createPartnerCompanyGroup(CompanyGroup group, Connection conn) throws SQLException {
		PreparedStatement stm = null;		
		try {
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_PARTNER_COMPANY_GROUP + "(" +
					IDBConstants.ATTR_NAME + "," +
					IDBConstants.ATTR_DESCRIPTION + ")" +
			" values (?, ?)");			
			stm.setString(1, group.getName());
			stm.setString(2, group.getDescription());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public CompanyGroup[] getAllPartnerCompanyGroup(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_PARTNER_COMPANY_GROUP);			
			while(rs.next()) {
				vresult.addElement(new CompanyGroup(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_NAME), rs.getString(IDBConstants.ATTR_DESCRIPTION)));
			}			
			CompanyGroup[] group = new CompanyGroup[vresult.size()];
			vresult.copyInto(group);
			return group;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public CompanyGroup getPartnerCompanyGroup(long index, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_PARTNER_COMPANY_GROUP +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);			
			if(rs.next()) {
				return new CompanyGroup(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_NAME), rs.getString(IDBConstants.ATTR_DESCRIPTION));
			}			
			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void updatePartnerCompanyGroup(long index, CompanyGroup group, Connection conn) throws SQLException {
		PreparedStatement stm = null;		
		try {
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_PARTNER_COMPANY_GROUP + " SET " +
					IDBConstants.ATTR_NAME + "=?, " +
					IDBConstants.ATTR_DESCRIPTION + "=? " +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);			
			stm.setString(1, group.getName());
			stm.setString(2, group.getDescription());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deletePartnerCompanyGroup(long index, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_PARTNER_COMPANY_GROUP +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);			
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	
	public void createCustomer(Customer customer, Connection conn) throws SQLException {
		PreparedStatement stm = null;		
		try{
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_CUSTOMER + "(" +
					IDBConstants.ATTR_CODE + ", " +
					IDBConstants.ATTR_NAME + ", " +
					IDBConstants.ATTR_ADDRESS + ", " +
					IDBConstants.ATTR_CITY + ", " +
					IDBConstants.ATTR_POSTALCODE + "," +
					IDBConstants.ATTR_PROVINCE + ", " +
					IDBConstants.ATTR_COUNTRY + ", " +
					IDBConstants.ATTR_PHONE1 + ", " +
					IDBConstants.ATTR_PHONE2 + ", " +
					IDBConstants.ATTR_FAX1 + ", " +
					IDBConstants.ATTR_FAX2 + ", " +
					IDBConstants.ATTR_EMAIL + ", " +
					IDBConstants.ATTR_WEBSITE + ")"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");			
			stm.setString(1, customer.getCode());
			stm.setString(2, customer.getName());
			stm.setString(3, customer.getAddress());
			stm.setString(4, customer.getCity());
			stm.setInt(5, customer.getPostCode());
			stm.setString(6, customer.getProvince());
			stm.setString(7, customer.getCountry());
			stm.setString(8, customer.getPhone1());
			stm.setString(9, customer.getPhone2());
			stm.setString(10, customer.getFax1());
			stm.setString(11, customer.getFax2());
			stm.setString(12, customer.getEmail());
			stm.setString(13, customer.getWebsite());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public Customer[] getAllCustomer(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_CUSTOMER);			
			while(rs.next()) {
				vresult.addElement(new Customer(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_CITY),
						rs.getInt(IDBConstants.ATTR_POSTALCODE),
						rs.getString(IDBConstants.ATTR_PROVINCE),
						rs.getString(IDBConstants.ATTR_COUNTRY),
						rs.getString(IDBConstants.ATTR_PHONE1),
						rs.getString(IDBConstants.ATTR_PHONE2),
						rs.getString(IDBConstants.ATTR_FAX1),
						rs.getString(IDBConstants.ATTR_FAX2),
						rs.getString(IDBConstants.ATTR_EMAIL),
						rs.getString(IDBConstants.ATTR_WEBSITE)
				));
			}			
			Customer[] customer = new Customer[vresult.size()];
			vresult.copyInto(customer);
			return customer;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public Customer getCustomerByIndex(long index, Connection conn) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();      
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_CUSTOMER +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);			
			if(rs.next()) {
				return  new Customer(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_CITY),
						rs.getInt(IDBConstants.ATTR_POSTALCODE),
						rs.getString(IDBConstants.ATTR_PROVINCE),
						rs.getString(IDBConstants.ATTR_COUNTRY),
						rs.getString(IDBConstants.ATTR_PHONE1),
						rs.getString(IDBConstants.ATTR_PHONE2),
						rs.getString(IDBConstants.ATTR_FAX1),
						rs.getString(IDBConstants.ATTR_FAX2),
						rs.getString(IDBConstants.ATTR_EMAIL),
						rs.getString(IDBConstants.ATTR_WEBSITE)
				);
			}			
			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void updateCustomer(long index, Customer customer, Connection conn) throws SQLException {
		PreparedStatement stm = null;		
		try{
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_CUSTOMER + " SET " +
					IDBConstants.ATTR_CODE + "=?, " +
					IDBConstants.ATTR_NAME + "=?, " +
					IDBConstants.ATTR_ADDRESS + "=?, " +
					IDBConstants.ATTR_CITY + "=?, " +
					IDBConstants.ATTR_POSTALCODE + "=?," +
					IDBConstants.ATTR_PROVINCE + "=?, " +
					IDBConstants.ATTR_COUNTRY + "=?, " +
					IDBConstants.ATTR_PHONE1 + "=?, " +
					IDBConstants.ATTR_PHONE2 + "=?, " +
					IDBConstants.ATTR_FAX1 + "=?, " +
					IDBConstants.ATTR_FAX2 + "=?, " +
					IDBConstants.ATTR_EMAIL + "=?, " +
					IDBConstants.ATTR_WEBSITE + "=? "
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);			
			stm.setString(1, customer.getCode());
			stm.setString(2, customer.getName());
			stm.setString(3, customer.getAddress());
			stm.setString(4, customer.getCity());
			stm.setInt(5, customer.getPostCode());
			stm.setString(6, customer.getProvince());
			stm.setString(7, customer.getCountry());
			stm.setString(8, customer.getPhone1());
			stm.setString(9, customer.getPhone2());
			stm.setString(10, customer.getFax1());
			stm.setString(11, customer.getFax2());
			stm.setString(12, customer.getEmail());
			stm.setString(13, customer.getWebsite());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deleteCustomer(long index, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_CUSTOMER +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void createCustomerGroup(long customerindex, long groupindex, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("INSERT INTO " + IDBConstants.TABLE_CUSTOMER_GROUP +
					" values (" + customerindex + "," + groupindex + ")");
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public CompanyGroup[] getCustomerGroup(long customerindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT " + IDBConstants.TABLE_CUSTOMER_COMPANY_GROUP + ".* FROM " +
					IDBConstants.TABLE_CUSTOMER_COMPANY_GROUP + "," +
					IDBConstants.TABLE_CUSTOMER_GROUP +
					" WHERE " + IDBConstants.ATTR_CUSTOMER_INDEX + "=" + customerindex +
					" AND " + IDBConstants.ATTR_GROUP_INDEX +
					"=" + IDBConstants.TABLE_CUSTOMER_COMPANY_GROUP + "." + IDBConstants.ATTR_AUTOINDEX);			
			while(rs.next()) {
				vresult.addElement(new CompanyGroup(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_NAME),
						rs.getString(IDBConstants.ATTR_DESCRIPTION)
				));
			}			
			CompanyGroup[] result = new CompanyGroup[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deleteCustomerGroup(long customerindex, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_CUSTOMER_GROUP +
					" WHERE " + IDBConstants.ATTR_CUSTOMER_INDEX + "=" + customerindex);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	
	public void createCustomerAddress(long customerindex, SpecificAddress address, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try{
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_CUSTOMER_ADDRESS + "(" +
					IDBConstants.ATTR_CUSTOMER_INDEX + ", " +
					IDBConstants.ATTR_ADDRESS_NAME + ", " +
					IDBConstants.ATTR_ADDRESS + ", " +
					IDBConstants.ATTR_CITY + ", " +
					IDBConstants.ATTR_POSTALCODE + "," +
					IDBConstants.ATTR_PROVINCE + ", " +
					IDBConstants.ATTR_COUNTRY + "," +
					IDBConstants.ATTR_PHONE1 + ", " +
					IDBConstants.ATTR_PHONE2 + ", " +
					IDBConstants.ATTR_FAX1 + ", " +
					IDBConstants.ATTR_FAX2 + ", " +
					IDBConstants.ATTR_EMAIL + ") " +
			" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");			
			stm.setLong(1, customerindex);
			stm.setString(2, address.getType());
			stm.setString(3, address.getAddress());
			stm.setString(4, address.getCity());
			stm.setInt(5, address.getPostCode());
			stm.setString(6, address.getProvince());
			stm.setString(7, address.getCountry());
			stm.setString(8, address.getPhone1());
			stm.setString(9, address.getPhone2());
			stm.setString(10, address.getFax1());
			stm.setString(11, address.getFax2());
			stm.setString(12, address.getEmail());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public SpecificAddress[] getCustomerAddress(long customerindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_CUSTOMER_ADDRESS +
					" WHERE " + IDBConstants.ATTR_CUSTOMER_INDEX + "=" + customerindex);			
			while(rs.next()) {
				vresult.addElement(new SpecificAddress(rs.getString(IDBConstants.ATTR_ADDRESS_NAME),
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_CITY),
						rs.getInt(IDBConstants.ATTR_POSTALCODE),
						rs.getString(IDBConstants.ATTR_PROVINCE),
						rs.getString(IDBConstants.ATTR_COUNTRY),
						rs.getString(IDBConstants.ATTR_PHONE1),
						rs.getString(IDBConstants.ATTR_PHONE2),
						rs.getString(IDBConstants.ATTR_FAX1),
						rs.getString(IDBConstants.ATTR_FAX2),
						rs.getString(IDBConstants.ATTR_EMAIL)
				));
			}			
			SpecificAddress[] address = new SpecificAddress[vresult.size()];
			vresult.copyInto(address);
			return address;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deleteCustomerAddress(long customerindex, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_CUSTOMER_ADDRESS +
					" WHERE " + IDBConstants.ATTR_CUSTOMER_INDEX + "=" + customerindex);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public Customer[] getCustomerByCriteria(String query, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);			
			while(rs.next()) {
				Customer customer = new Customer(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_CITY),
						rs.getInt(IDBConstants.ATTR_POSTALCODE),
						rs.getString(IDBConstants.ATTR_PROVINCE),
						rs.getString(IDBConstants.ATTR_COUNTRY),
						rs.getString(IDBConstants.ATTR_PHONE1),
						rs.getString(IDBConstants.ATTR_PHONE2),
						rs.getString(IDBConstants.ATTR_FAX1),
						rs.getString(IDBConstants.ATTR_FAX2),
						rs.getString(IDBConstants.ATTR_EMAIL),
						rs.getString(IDBConstants.ATTR_WEBSITE)
				);
				customer.setCompanyGroup(this.getCustomerGroup(customer.getIndex(), conn));
				vresult.addElement(customer);
			}			
			Customer[] customer = new Customer[vresult.size()];
			vresult.copyInto(customer);
			return customer;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void createCustomerContact(long customerindex, Personal personal, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try{
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_CUSTOMER_CONTACT + "(" +
					IDBConstants.ATTR_CUSTOMER_INDEX + ", " +
					IDBConstants.ATTR_PERSONAL_INDEX + ") " +
			" values (?,?)");			
			stm.setLong(1, customerindex);
			stm.setLong(2, personal.getIndex());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public Personal[] getCustomerContact(long customerindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT " + IDBConstants.TABLE_PERSONAL + ".* FROM " +
					IDBConstants.TABLE_PERSONAL + "," + IDBConstants.TABLE_CUSTOMER_CONTACT +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_PERSONAL_INDEX +
					" AND " + IDBConstants.ATTR_CUSTOMER_INDEX + "=" + customerindex);			
			while(rs.next()) {
				vresult.addElement(new Personal(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_TITLE),
						rs.getString(IDBConstants.ATTR_FIRST_NAME),
						rs.getString(IDBConstants.ATTR_LAST_NAME),
						rs.getString(IDBConstants.ATTR_NICK_NAME),
						rs.getString(IDBConstants.ATTR_BIRTH_PLACE),
						rs.getDate(IDBConstants.ATTR_BIRTH_DATE),
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_CITY),
						rs.getInt(IDBConstants.ATTR_POSTALCODE),
						rs.getString(IDBConstants.ATTR_PROVINCE),
						rs.getString(IDBConstants.ATTR_COUNTRY),
						rs.getString(IDBConstants.ATTR_PHONE1),
						rs.getString(IDBConstants.ATTR_PHONE2),
						rs.getString(IDBConstants.ATTR_FAX1),
						rs.getString(IDBConstants.ATTR_FAX2),
						rs.getString(IDBConstants.ATTR_EMAIL),
						rs.getString(IDBConstants.ATTR_WEBSITE)
				));
			}			
			Personal[] result = new Personal[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deleteCustomerContact(long customerindex, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_CUSTOMER_CONTACT +
					" WHERE " + IDBConstants.ATTR_CUSTOMER_INDEX + "=" + customerindex);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void createPartner(Partner partner, Connection conn) throws SQLException {
		PreparedStatement stm = null;		
		try{
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_PARTNER + "(" +
					IDBConstants.ATTR_CODE + ", " +
					IDBConstants.ATTR_NAME + ", " +
					IDBConstants.ATTR_ADDRESS + ", " +
					IDBConstants.ATTR_CITY + ", " +
					IDBConstants.ATTR_POSTALCODE + "," +
					IDBConstants.ATTR_PROVINCE + ", " +
					IDBConstants.ATTR_COUNTRY + ", " +
					IDBConstants.ATTR_PHONE1 + ", " +
					IDBConstants.ATTR_PHONE2 + ", " +
					IDBConstants.ATTR_FAX1 + ", " +
					IDBConstants.ATTR_FAX2 + ", " +
					IDBConstants.ATTR_EMAIL + ", " +
					IDBConstants.ATTR_WEBSITE + ")"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");			
			stm.setString(1, partner.getCode());
			stm.setString(2, partner.getName());
			stm.setString(3, partner.getAddress());
			stm.setString(4, partner.getCity());
			stm.setInt(5, partner.getPostCode());
			stm.setString(6, partner.getProvince());
			stm.setString(7, partner.getCountry());
			stm.setString(8, partner.getPhone1());
			stm.setString(9, partner.getPhone2());
			stm.setString(10, partner.getFax1());
			stm.setString(11, partner.getFax2());
			stm.setString(12, partner.getEmail());
			stm.setString(13, partner.getWebsite());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public Partner[] getAllPartner(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_PARTNER);			
			while(rs.next()) {
				vresult.addElement(new Partner(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_CITY),
						rs.getInt(IDBConstants.ATTR_POSTALCODE),
						rs.getString(IDBConstants.ATTR_PROVINCE),
						rs.getString(IDBConstants.ATTR_COUNTRY),
						rs.getString(IDBConstants.ATTR_PHONE1),
						rs.getString(IDBConstants.ATTR_PHONE2),
						rs.getString(IDBConstants.ATTR_FAX1),
						rs.getString(IDBConstants.ATTR_FAX2),
						rs.getString(IDBConstants.ATTR_EMAIL),
						rs.getString(IDBConstants.ATTR_WEBSITE)
				));
			}			
			Partner[] partner = new Partner[vresult.size()];
			vresult.copyInto(partner);
			return partner;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public Partner getPartnerByIndex(long index, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_PARTNER +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);			
			if(rs.next()) {
				return  new Partner(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_CITY),
						rs.getInt(IDBConstants.ATTR_POSTALCODE),
						rs.getString(IDBConstants.ATTR_PROVINCE),
						rs.getString(IDBConstants.ATTR_COUNTRY),
						rs.getString(IDBConstants.ATTR_PHONE1),
						rs.getString(IDBConstants.ATTR_PHONE2),
						rs.getString(IDBConstants.ATTR_FAX1),
						rs.getString(IDBConstants.ATTR_FAX2),
						rs.getString(IDBConstants.ATTR_EMAIL),
						rs.getString(IDBConstants.ATTR_WEBSITE)
				);
			}			
			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void updatePartner(long index, Partner partner, Connection conn) throws SQLException {
		PreparedStatement stm = null;		
		try{
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_PARTNER + " SET " +
					IDBConstants.ATTR_CODE + "=?, " +
					IDBConstants.ATTR_NAME + "=?, " +
					IDBConstants.ATTR_ADDRESS + "=?, " +
					IDBConstants.ATTR_CITY + "=?, " +
					IDBConstants.ATTR_POSTALCODE + "=?," +
					IDBConstants.ATTR_PROVINCE + "=?, " +
					IDBConstants.ATTR_COUNTRY + "=?, " +
					IDBConstants.ATTR_PHONE1 + "=?, " +
					IDBConstants.ATTR_PHONE2 + "=?, " +
					IDBConstants.ATTR_FAX1 + "=?, " +
					IDBConstants.ATTR_FAX2 + "=?, " +
					IDBConstants.ATTR_EMAIL + "=?, " +
					IDBConstants.ATTR_WEBSITE + "=? "
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);			
			stm.setString(1, partner.getCode());
			stm.setString(2, partner.getName());
			stm.setString(3, partner.getAddress());
			stm.setString(4, partner.getCity());
			stm.setInt(5, partner.getPostCode());
			stm.setString(6, partner.getProvince());
			stm.setString(7, partner.getCountry());
			stm.setString(8, partner.getPhone1());
			stm.setString(9, partner.getPhone2());
			stm.setString(10, partner.getFax1());
			stm.setString(11, partner.getFax2());
			stm.setString(12, partner.getEmail());
			stm.setString(13, partner.getWebsite());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deletePartner(long index, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_PARTNER +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void createPartnerGroup(long partnerindex, long groupindex, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("INSERT INTO " + IDBConstants.TABLE_PARTNER_GROUP +
					" values (" + partnerindex + "," + groupindex + ")");
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public CompanyGroup[] getPartnerGroup(long partnerindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT " + IDBConstants.TABLE_PARTNER_COMPANY_GROUP + ".* FROM " +
					IDBConstants.TABLE_PARTNER_COMPANY_GROUP + "," +
					IDBConstants.TABLE_PARTNER_GROUP +
					" WHERE " + IDBConstants.ATTR_PARTNER_INDEX + "=" + partnerindex +
					" AND " + IDBConstants.ATTR_GROUP_INDEX +
					"=" + IDBConstants.TABLE_PARTNER_COMPANY_GROUP + "." + IDBConstants.ATTR_AUTOINDEX);			
			while(rs.next()) {
				vresult.addElement(new CompanyGroup(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_NAME),
						rs.getString(IDBConstants.ATTR_DESCRIPTION)
				));
			}			
			CompanyGroup[] result = new CompanyGroup[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deletePartnerGroup(long partnerindex, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_PARTNER_GROUP +
					" WHERE " + IDBConstants.ATTR_PARTNER_INDEX + "=" + partnerindex);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void createPartnerAddress(long partnerindex, SpecificAddress address, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try{
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_PARTNER_ADDRESS + "(" +
					IDBConstants.ATTR_PARTNER_INDEX + ", " +
					IDBConstants.ATTR_ADDRESS_NAME + ", " +
					IDBConstants.ATTR_ADDRESS + ", " +
					IDBConstants.ATTR_CITY + ", " +
					IDBConstants.ATTR_POSTALCODE + "," +
					IDBConstants.ATTR_PROVINCE + ", " +
					IDBConstants.ATTR_COUNTRY + "," +
					IDBConstants.ATTR_PHONE1 + ", " +
					IDBConstants.ATTR_PHONE2 + ", " +
					IDBConstants.ATTR_FAX1 + ", " +
					IDBConstants.ATTR_FAX2 + ", " +
					IDBConstants.ATTR_EMAIL + ") " +
			" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");			
			stm.setLong(1, partnerindex);
			stm.setString(2, address.getType());
			stm.setString(3, address.getAddress());
			stm.setString(4, address.getCity());
			stm.setInt(5, address.getPostCode());
			stm.setString(6, address.getProvince());
			stm.setString(7, address.getCountry());
			stm.setString(8, address.getPhone1());
			stm.setString(9, address.getPhone2());
			stm.setString(10, address.getFax1());
			stm.setString(11, address.getFax2());
			stm.setString(12, address.getEmail());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public SpecificAddress[] getPartnerAddress(long partnerindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_PARTNER_ADDRESS +
					" WHERE " + IDBConstants.ATTR_PARTNER_INDEX + "=" + partnerindex);			
			while(rs.next()) {
				vresult.addElement(new SpecificAddress(rs.getString(IDBConstants.ATTR_ADDRESS_NAME),
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_CITY),
						rs.getInt(IDBConstants.ATTR_POSTALCODE),
						rs.getString(IDBConstants.ATTR_PROVINCE),
						rs.getString(IDBConstants.ATTR_COUNTRY),
						rs.getString(IDBConstants.ATTR_PHONE1),
						rs.getString(IDBConstants.ATTR_PHONE2),
						rs.getString(IDBConstants.ATTR_FAX1),
						rs.getString(IDBConstants.ATTR_FAX2),
						rs.getString(IDBConstants.ATTR_EMAIL)
				));
			}			
			SpecificAddress[] address = new SpecificAddress[vresult.size()];
			vresult.copyInto(address);
			return address;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deletePartnerAddress(long customerindex, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_PARTNER_ADDRESS +
					" WHERE " + IDBConstants.ATTR_PARTNER_INDEX + "=" + customerindex);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public Partner[] getPartnerByCriteria(String query, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);			
			while(rs.next()) {
				Partner partner = new Partner(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_CITY),
						rs.getInt(IDBConstants.ATTR_POSTALCODE),
						rs.getString(IDBConstants.ATTR_PROVINCE),
						rs.getString(IDBConstants.ATTR_COUNTRY),
						rs.getString(IDBConstants.ATTR_PHONE1),
						rs.getString(IDBConstants.ATTR_PHONE2),
						rs.getString(IDBConstants.ATTR_FAX1),
						rs.getString(IDBConstants.ATTR_FAX2),
						rs.getString(IDBConstants.ATTR_EMAIL),
						rs.getString(IDBConstants.ATTR_WEBSITE)
				);
				partner.setCompanyGroup(getPartnerGroup(rs.getLong(IDBConstants.ATTR_AUTOINDEX), conn));
				vresult.addElement(partner);
			}			
			Partner[] partner = new Partner[vresult.size()];
			vresult.copyInto(partner);
			return partner;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void createPersonal(Personal personal, Connection conn) throws SQLException {
		PreparedStatement stm = null;		
		try{
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_PERSONAL + "(" +
					IDBConstants.ATTR_CODE + ", " +
					IDBConstants.ATTR_TITLE + ", " +
					IDBConstants.ATTR_FIRST_NAME + ", " +
					IDBConstants.ATTR_LAST_NAME + ", " +
					IDBConstants.ATTR_NICK_NAME + ", " +
					IDBConstants.ATTR_BIRTH_PLACE + ", " +
					IDBConstants.ATTR_BIRTH_DATE + ", " +
					IDBConstants.ATTR_ADDRESS + ", " +
					IDBConstants.ATTR_CITY + ", " +
					IDBConstants.ATTR_POSTALCODE + "," +
					IDBConstants.ATTR_PROVINCE + ", " +
					IDBConstants.ATTR_COUNTRY + ", " +
					IDBConstants.ATTR_PHONE1 + ", " +
					IDBConstants.ATTR_PHONE2 + ", " +
					IDBConstants.ATTR_FAX1 + ", " +
					IDBConstants.ATTR_FAX2 + ", " +
					IDBConstants.ATTR_EMAIL + ", " +
					IDBConstants.ATTR_WEBSITE + ")"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			int col = 1;
			stm.setString(col++, personal.getCode());
			stm.setString(col++, personal.getTitle());
			stm.setString(col++, personal.getFirstName());
			stm.setString(col++, personal.getLastName());
			stm.setString(col++, personal.getNickName());
			stm.setString(col++, personal.getBirthPlace());			
			if(personal.getBirthDate()!= null)
				stm.setDate(col++, new java.sql.Date(personal.getBirthDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);			
			stm.setString(col++, personal.getAddress());
			stm.setString(col++, personal.getCity());
			stm.setInt(col++, personal.getPostCode());
			stm.setString(col++, personal.getProvince());
			stm.setString(col++, personal.getCountry());
			stm.setString(col++, personal.getPhone1());
			stm.setString(col++, personal.getPhone2());
			stm.setString(col++, personal.getFax1());
			stm.setString(col++, personal.getFax2());
			stm.setString(col++, personal.getEmail());
			stm.setString(col++, personal.getWebSite());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public Personal[] getAllPersonal(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_PERSONAL);			
			while(rs.next()) {
				vresult.addElement(new Personal(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_TITLE),
						rs.getString(IDBConstants.ATTR_FIRST_NAME),
						rs.getString(IDBConstants.ATTR_LAST_NAME),
						rs.getString(IDBConstants.ATTR_NICK_NAME),
						rs.getString(IDBConstants.ATTR_BIRTH_PLACE),
						rs.getDate(IDBConstants.ATTR_BIRTH_DATE),
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_CITY),
						rs.getInt(IDBConstants.ATTR_POSTALCODE),
						rs.getString(IDBConstants.ATTR_PROVINCE),
						rs.getString(IDBConstants.ATTR_COUNTRY),
						rs.getString(IDBConstants.ATTR_PHONE1),
						rs.getString(IDBConstants.ATTR_PHONE2),
						rs.getString(IDBConstants.ATTR_FAX1),
						rs.getString(IDBConstants.ATTR_FAX2),
						rs.getString(IDBConstants.ATTR_EMAIL),
						rs.getString(IDBConstants.ATTR_WEBSITE)
				));
			}			
			Personal[] personal = new Personal[vresult.size()];
			vresult.copyInto(personal);
			return personal;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public Personal getPersonalByIndex(long index, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_PERSONAL +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);			
			if(rs.next()) {
				return  new Personal(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_TITLE),
						rs.getString(IDBConstants.ATTR_FIRST_NAME),
						rs.getString(IDBConstants.ATTR_LAST_NAME),
						rs.getString(IDBConstants.ATTR_NICK_NAME),
						rs.getString(IDBConstants.ATTR_BIRTH_PLACE),
						rs.getDate(IDBConstants.ATTR_BIRTH_DATE),
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_CITY),
						rs.getInt(IDBConstants.ATTR_POSTALCODE),
						rs.getString(IDBConstants.ATTR_PROVINCE),
						rs.getString(IDBConstants.ATTR_COUNTRY),
						rs.getString(IDBConstants.ATTR_PHONE1),
						rs.getString(IDBConstants.ATTR_PHONE2),
						rs.getString(IDBConstants.ATTR_FAX1),
						rs.getString(IDBConstants.ATTR_FAX2),
						rs.getString(IDBConstants.ATTR_EMAIL),
						rs.getString(IDBConstants.ATTR_WEBSITE)
				);
			}			
			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void updatePersonal(long index, Personal personal, Connection conn) throws SQLException {
		PreparedStatement stm = null;		
		try{
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_PERSONAL + " SET " +
					IDBConstants.ATTR_CODE + "=?, " +
					IDBConstants.ATTR_TITLE + "=?, " +
					IDBConstants.ATTR_FIRST_NAME + "=?, " +
					IDBConstants.ATTR_LAST_NAME + "=?, " +
					IDBConstants.ATTR_NICK_NAME + "=?, " +
					IDBConstants.ATTR_BIRTH_PLACE + "=?, " +
					IDBConstants.ATTR_BIRTH_DATE + "=?, " +
					IDBConstants.ATTR_ADDRESS + "=?, " +
					IDBConstants.ATTR_CITY + "=?, " +
					IDBConstants.ATTR_POSTALCODE + "=?," +
					IDBConstants.ATTR_PROVINCE + "=?, " +
					IDBConstants.ATTR_COUNTRY + "=?, " +
					IDBConstants.ATTR_PHONE1 + "=?, " +
					IDBConstants.ATTR_PHONE2 + "=?, " +
					IDBConstants.ATTR_FAX1 + "=?, " +
					IDBConstants.ATTR_FAX2 + "=?, " +
					IDBConstants.ATTR_EMAIL + "=?, " +
					IDBConstants.ATTR_WEBSITE + "=? "
					+ " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);			
			int col = 1;
			stm.setString(col++, personal.getCode());
			stm.setString(col++, personal.getTitle());
			stm.setString(col++, personal.getFirstName());
			stm.setString(col++, personal.getLastName());
			stm.setString(col++, personal.getNickName());
			stm.setString(col++, personal.getBirthPlace());			
			if(personal.getBirthDate()!= null)
				stm.setDate(col++, new java.sql.Date(personal.getBirthDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);			
			stm.setString(col++, personal.getAddress());
			stm.setString(col++, personal.getCity());
			stm.setInt(col++, personal.getPostCode());
			stm.setString(col++, personal.getProvince());
			stm.setString(col++, personal.getCountry());
			stm.setString(col++, personal.getPhone1());
			stm.setString(col++, personal.getPhone2());
			stm.setString(col++, personal.getFax1());
			stm.setString(col++, personal.getFax2());
			stm.setString(col++, personal.getEmail());
			stm.setString(col++, personal.getWebSite());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deletePersonal(long index, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_PERSONAL +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void createPersonalHome(long personalindex, PersonalHome home, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try{
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_PERSONAL_HOME + "(" +
					IDBConstants.ATTR_PERSONAL_INDEX + ", " +
					IDBConstants.ATTR_ADDRESS + ", " +
					IDBConstants.ATTR_CITY + ", " +
					IDBConstants.ATTR_POSTALCODE + "," +
					IDBConstants.ATTR_PROVINCE + ", " +
					IDBConstants.ATTR_COUNTRY + "," +
					IDBConstants.ATTR_WEBSITE + ", " +
					IDBConstants.ATTR_PHONE1 + ", " +
					IDBConstants.ATTR_PHONE2 + ", " +
					IDBConstants.ATTR_MOBILE1 + ", " +
					IDBConstants.ATTR_MOBILE2 + ", " +
					IDBConstants.ATTR_FAX + ") " +
			" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");			
			stm.setLong(1, personalindex);
			stm.setString(2, home.getAddress());
			stm.setString(3, home.getCity());
			stm.setInt(4, home.getPostCode());
			stm.setString(5, home.getProvince());
			stm.setString(6, home.getCountry());
			stm.setString(7, home.getWebsie());
			stm.setString(8, home.getPhone1());
			stm.setString(9, home.getPhone2());
			stm.setString(10, home.getMobile1());
			stm.setString(11, home.getMobile2());
			stm.setString(12, home.getFax());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public PersonalHome[] getPersonalHome(long personalindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_PERSONAL_HOME +
					" WHERE " + IDBConstants.ATTR_PERSONAL_INDEX + "=" + personalindex);			
			while(rs.next()) {
				vresult.addElement(new PersonalHome(
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_CITY),
						rs.getInt(IDBConstants.ATTR_POSTALCODE),
						rs.getString(IDBConstants.ATTR_PROVINCE),
						rs.getString(IDBConstants.ATTR_COUNTRY),
						rs.getString(IDBConstants.ATTR_WEBSITE),
						rs.getString(IDBConstants.ATTR_PHONE1),
						rs.getString(IDBConstants.ATTR_PHONE2),
						rs.getString(IDBConstants.ATTR_MOBILE1),
						rs.getString(IDBConstants.ATTR_MOBILE2),
						rs.getString(IDBConstants.ATTR_FAX)
				));
			}			
			PersonalHome[] home = new PersonalHome[vresult.size()];
			vresult.copyInto(home);
			return home;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deletePersonalHome(long personalindex, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_PERSONAL_HOME +
					" WHERE " + IDBConstants.ATTR_PERSONAL_INDEX + "=" + personalindex);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void createPersonalBusiness(long personalindex, PersonalBusiness business, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try{
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_PERSONAL_BUSINESS + "(" +
					IDBConstants.ATTR_PERSONAL_INDEX + ", " +
					IDBConstants.ATTR_COMPANY + "," +
					IDBConstants.ATTR_ADDRESS + ", " +
					IDBConstants.ATTR_CITY + ", " +
					IDBConstants.ATTR_POSTALCODE + "," +
					IDBConstants.ATTR_PROVINCE + ", " +
					IDBConstants.ATTR_COUNTRY + "," +
					IDBConstants.ATTR_WEBSITE + ", " +
					IDBConstants.ATTR_PHONE1 + ", " +
					IDBConstants.ATTR_PHONE2 + ", " +
					IDBConstants.ATTR_FAX + ", " +
					IDBConstants.ATTR_JOB_TITLE + ", " +
					IDBConstants.ATTR_DEPARTEMENT + ", " +
					IDBConstants.ATTR_OFFICE + ") " +
			" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");			
			int col = 1;
			stm.setLong(col++, personalindex);
			stm.setString(col++, business.getCompany());
			stm.setString(col++, business.getAddress());
			stm.setString(col++, business.getCity());
			stm.setInt(col++, business.getPostCode());
			stm.setString(col++, business.getProvince());
			stm.setString(col++, business.getCountry());
			stm.setString(col++, business.getWebsie());
			stm.setString(col++, business.getPhone1());
			stm.setString(col++, business.getPhone2());
			stm.setString(col++, business.getFax());
			stm.setString(col++, business.getJobTitle());
			stm.setString(col++, business.getDepartement());
			stm.setString(col++, business.getOffice());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public PersonalBusiness[] getPersonalBusiness(long personalindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_PERSONAL_BUSINESS +
					" WHERE " + IDBConstants.ATTR_PERSONAL_INDEX + "=" + personalindex);			
			while(rs.next()) {
				vresult.addElement(new PersonalBusiness(
						rs.getString(IDBConstants.ATTR_COMPANY),
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_CITY),
						rs.getInt(IDBConstants.ATTR_POSTALCODE),
						rs.getString(IDBConstants.ATTR_PROVINCE),
						rs.getString(IDBConstants.ATTR_COUNTRY),
						rs.getString(IDBConstants.ATTR_WEBSITE),
						rs.getString(IDBConstants.ATTR_PHONE1),
						rs.getString(IDBConstants.ATTR_PHONE2),
						rs.getString(IDBConstants.ATTR_FAX),
						rs.getString(IDBConstants.ATTR_JOB_TITLE),
						rs.getString(IDBConstants.ATTR_DEPARTEMENT),
						rs.getString(IDBConstants.ATTR_OFFICE)
				));
			}			
			PersonalBusiness[] business = new PersonalBusiness[vresult.size()];
			vresult.copyInto(business);
			return business;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deletePersonalBusiness(long personalindex, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_PERSONAL_BUSINESS +
					" WHERE " + IDBConstants.ATTR_PERSONAL_INDEX + "=" + personalindex);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public Personal[] getPersonalByCriteria(String query, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);			
			while(rs.next()) {
				Personal personal = new Personal(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_TITLE),
						rs.getString(IDBConstants.ATTR_FIRST_NAME),
						rs.getString(IDBConstants.ATTR_LAST_NAME),
						rs.getString(IDBConstants.ATTR_NICK_NAME),
						rs.getString(IDBConstants.ATTR_BIRTH_PLACE),
						rs.getDate(IDBConstants.ATTR_BIRTH_DATE),
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_CITY),
						rs.getInt(IDBConstants.ATTR_POSTALCODE),
						rs.getString(IDBConstants.ATTR_PROVINCE),
						rs.getString(IDBConstants.ATTR_COUNTRY),
						rs.getString(IDBConstants.ATTR_PHONE1),
						rs.getString(IDBConstants.ATTR_PHONE2),
						rs.getString(IDBConstants.ATTR_FAX1),
						rs.getString(IDBConstants.ATTR_FAX2),
						rs.getString(IDBConstants.ATTR_EMAIL),
						rs.getString(IDBConstants.ATTR_WEBSITE));				
				personal.setPersonalBusiness(this.getPersonalBusiness(personal.getIndex(), conn));
				vresult.addElement(personal);
			}			
			Personal[] personal = new Personal[vresult.size()];
			vresult.copyInto(personal);
			return personal;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void createPartnerContact(long partnerindex, Personal personal, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try{
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_PARTNER_CONTACT + "(" +
					IDBConstants.ATTR_PARTNER_INDEX + ", " +
					IDBConstants.ATTR_PERSONAL_INDEX + ")" +
			" values (?,?)");			
			stm.setLong(1, partnerindex);
			stm.setLong(2, personal.getIndex());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public Personal[] getPartnerContact(long partnerindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT " + IDBConstants.TABLE_PERSONAL + ".* FROM " +
					IDBConstants.TABLE_PERSONAL + "," + IDBConstants.TABLE_PARTNER_CONTACT +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_PERSONAL_INDEX +
					" AND " + IDBConstants.ATTR_PARTNER_INDEX + "=" + partnerindex);			
			while(rs.next()) {
				vresult.addElement(new Personal(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_TITLE),
						rs.getString(IDBConstants.ATTR_FIRST_NAME),
						rs.getString(IDBConstants.ATTR_LAST_NAME),
						rs.getString(IDBConstants.ATTR_NICK_NAME),
						rs.getString(IDBConstants.ATTR_BIRTH_PLACE),
						rs.getDate(IDBConstants.ATTR_BIRTH_DATE),
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_CITY),
						rs.getInt(IDBConstants.ATTR_POSTALCODE),
						rs.getString(IDBConstants.ATTR_PROVINCE),
						rs.getString(IDBConstants.ATTR_COUNTRY),
						rs.getString(IDBConstants.ATTR_PHONE1),
						rs.getString(IDBConstants.ATTR_PHONE2),
						rs.getString(IDBConstants.ATTR_FAX1),
						rs.getString(IDBConstants.ATTR_FAX2),
						rs.getString(IDBConstants.ATTR_EMAIL),
						rs.getString(IDBConstants.ATTR_WEBSITE)
				));
			}			
			Personal[] result = new Personal[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deletePartnerContact(long customerindex, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_PARTNER_CONTACT +
					" WHERE " + IDBConstants.ATTR_PARTNER_INDEX + "=" + customerindex);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void createProjectData(ProjectData project, Connection conn) throws SQLException {
		PreparedStatement stm = null;		
		try {
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_PROJECT_DATA + "(" +
					IDBConstants.ATTR_CODE + ", " +
					IDBConstants.ATTR_CUSTOMER + ", " +
					IDBConstants.ATTR_UNIT + ", " +
					IDBConstants.ATTR_ACTIVITY + ", " +
					IDBConstants.ATTR_DEPARTMENT + ", " +
					IDBConstants.ATTR_WORK_DESCRIPTION + ", " +
					IDBConstants.ATTR_REGDATE + ", " +
					IDBConstants.ATTR_ORNO + ", " +
					IDBConstants.ATTR_PONO + ", " +
					IDBConstants.ATTR_IPCNO + ", " +
					IDBConstants.ATTR_ORDATE + ", " +
					IDBConstants.ATTR_PODATE + ", " +
					IDBConstants.ATTR_IPCDATE + ", " +
					IDBConstants.ATTR_FILE + ", " +
					IDBConstants.ATTR_SHEET + ") " +
			" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");			
			int col = 1;
			stm.setString(col++, project.getCode());
			stm.setLong(col++, project.getCustomer().getIndex());
			stm.setLong(col++, project.getUnit().getIndex());
			stm.setLong(col++, project.getActivity().getIndex());
			stm.setLong(col++, project.getDepartment().getIndex());
			stm.setString(col++, project.getWorkDescription());			
			if(project.getRegDate() != null)
				stm.setDate(col++, new java.sql.Date(project.getRegDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);			
			stm.setString(col++, project.getORNo());
			stm.setString(col++, project.getPONo());
			stm.setString(col++, project.getIPCNo());
			if(project.getORDate() != null)
				stm.setDate(col++, new java.sql.Date(project.getORDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if(project.getPODate() != null)
				stm.setDate(col++, new java.sql.Date(project.getPODate().getTime()));
			else
				stm.setNull(col++, Types.DATE);			
			if(project.getIPCDate() != null)
				stm.setDate(col++, new java.sql.Date(project.getIPCDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);			
			stm.setString(col++, project.getFile());			
			if(project.getSheet() == null || project.getSheet().length == 0)
				stm.setNull(col++, Types.LONGVARBINARY);
			else
				stm.setBytes(col++, project.getSheet());			
			stm.executeUpdate();
		}
		catch(Exception exc) {
			exc.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(exc));
		}
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public ProjectData[] getAllProjectData(Connection conn) throws SQLException {
		java.util.Date date = new java.util.GregorianCalendar().getTime();         
		java.text.DateFormat formatDate = new java.text.SimpleDateFormat( "yyyy-MM-dd" );
		String tgl = formatDate.format(date); 
		Statement stm = null;
		Vector vresult = new Vector();    
		try{
			stm = conn.createStatement();
			String qry = "SELECT a." + IDBConstants.ATTR_AUTOINDEX + 
			",a." + IDBConstants.ATTR_CODE + 
			",b." + IDBConstants.ATTR_NAME + 
			",a." + IDBConstants.ATTR_WORK_DESCRIPTION + 
			",c." + IDBConstants.ATTR_NAME + " as activity" +
			",a." + IDBConstants.ATTR_ORNO +
			",a." + IDBConstants.ATTR_ORDATE + 
			",a." + IDBConstants.ATTR_PONO +
			",a." + IDBConstants.ATTR_PODATE + 
			",a." + IDBConstants.ATTR_IPCNO +
			",a." + IDBConstants.ATTR_IPCDATE + 
			",d." + IDBConstants.ATTR_ACTUAL_START_DATE +
			",d." + IDBConstants.ATTR_ACTUAL_END_DATE +  
			",d." + IDBConstants.ATTR_VALIDATION + 
			",a." + IDBConstants.ATTR_REGDATE + 
			",a." + IDBConstants.ATTR_FILE +
			",a." + IDBConstants.ATTR_SHEET + // baru
			",a." + IDBConstants.ATTR_CUSTOMER + " as indexcust " +
			",a." + IDBConstants.ATTR_UNIT + " as indexunit" +             
			",a." + IDBConstants.ATTR_ACTIVITY + " as indexact" +
			",a." + IDBConstants.ATTR_DEPARTEMENT + " as indexdept " +
			" from " + IDBConstants.TABLE_PROJECT_DATA + " a " +
			" left join " + IDBConstants.TABLE_CUSTOMER + " b on a." + IDBConstants.ATTR_CUSTOMER + "=b." + IDBConstants.ATTR_AUTOINDEX +
			" left join " + IDBConstants.TABLE_ACTIVITY + " c on a." + IDBConstants.ATTR_ACTIVITY + "=c." + IDBConstants.ATTR_AUTOINDEX +
			" left join " + IDBConstants.TABLE_PROJECT_CONTRACT + " as d on a." + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_PROJECT +
			" left join " + IDBConstants.TABLE_UNIT + " e on a." + IDBConstants.ATTR_UNIT + "=e." + IDBConstants.ATTR_AUTOINDEX +
			" left join " + IDBConstants.TABLE_ORGANIZATION + " f on a." + IDBConstants.ATTR_DEPARTMENT + "=f." + IDBConstants.ATTR_AUTOINDEX + 
			" WHERE d." + IDBConstants.ATTR_ACTUAL_END_DATE + " >='" + tgl + "' or  d." + IDBConstants.ATTR_ACTUAL_END_DATE + " is null " +
			"order by a." + IDBConstants.ATTR_CODE;
			ResultSet rs = stm.executeQuery(qry);
			while(rs.next()) {
				ProjectData project = new ProjectData(rs.getLong("autoindex"),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						rs.getString(IDBConstants.ATTR_WORK_DESCRIPTION),
						rs.getString(IDBConstants.ATTR_ACTIVITY),
						rs.getString(IDBConstants.ATTR_ORNO),
						rs.getDate(IDBConstants.ATTR_ORDATE),
						rs.getString(IDBConstants.ATTR_PONO),
						rs.getDate(IDBConstants.ATTR_PODATE),
						rs.getString(IDBConstants.ATTR_IPCNO),
						rs.getDate(IDBConstants.ATTR_IPCDATE),
						rs.getDate(IDBConstants.ATTR_ACTUAL_START_DATE),
						rs.getDate(IDBConstants.ATTR_ACTUAL_END_DATE),
						rs.getDate(IDBConstants.ATTR_VALIDATION),      
						rs.getDate(IDBConstants.ATTR_REGDATE),
						rs.getString(IDBConstants.ATTR_FILE),  
						rs.getBytes(IDBConstants.ATTR_SHEET),
						rs.getLong("indexcust"),
						rs.getLong("indexunit"),
						rs.getLong("indexact"),
						rs.getLong("indexdept")
				);               
				vresult.addElement(project);
				
			}
			/*ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_PROJECT_DATA); 
			 while(rs.next()) {
			 ProjectData project = new ProjectData(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
			 rs.getString(IDBConstants.ATTR_CODE),
			 getCustomerByIndex(rs.getLong(IDBConstants.ATTR_CUSTOMER), conn),
			 new AccountingSQLSAP().getUnitByIndex(rs.getLong(IDBConstants.ATTR_UNIT), conn),
			 new AccountingSQLSAP().getActivityByIndex(rs.getLong(IDBConstants.ATTR_ACTIVITY), conn),
			 new HRMSQLSAP().getOrganizationByIndex(rs.getLong(IDBConstants.ATTR_DEPARTMENT), conn),
			 rs.getString(IDBConstants.ATTR_WORK_DESCRIPTION),
			 rs.getDate(IDBConstants.ATTR_REGDATE),            
			 rs.getString(IDBConstants.ATTR_ORNO),
			 rs.getString(IDBConstants.ATTR_PONO),
			 rs.getString(IDBConstants.ATTR_IPCNO),
			 rs.getDate(IDBConstants.ATTR_ORDATE),
			 rs.getDate(IDBConstants.ATTR_PODATE),
			 rs.getDate(IDBConstants.ATTR_IPCDATE),
			 rs.getString(IDBConstants.ATTR_FILE),
			 rs.getBytes(IDBConstants.ATTR_SHEET)
			 );        
			 project.setProjectContract(this.getProjectContract(project.getIndex(), conn));
			 vresult.addElement(project);    
			 }*/			
			ProjectData[] project = new ProjectData[vresult.size()];
			vresult.copyInto(project);
			return project;
		}
		catch(Exception exc) {
			throw new SQLException(OtherSQLException.getMessage(exc));
		}		
		finally{
			if(stm != null)
				stm.close();
		}
	}
	
	public ProjectData getProjectDataByIndex(long index, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_PROJECT_DATA +  
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);			
			if(rs.next()) {
				return new ProjectData(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						getCustomerByIndex(rs.getLong(IDBConstants.ATTR_CUSTOMER), conn),
						new AccountingSQLSAP().getUnitByIndex(rs.getLong(IDBConstants.ATTR_UNIT), conn),
						new AccountingSQLSAP().getActivityByIndex(rs.getLong(IDBConstants.ATTR_ACTIVITY), conn),
						new HRMSQLSAP().getOrganizationByIndex(rs.getLong(IDBConstants.ATTR_DEPARTMENT), conn),
						rs.getString(IDBConstants.ATTR_WORK_DESCRIPTION),
						rs.getDate(IDBConstants.ATTR_REGDATE),
						rs.getString(IDBConstants.ATTR_ORNO),
						rs.getString(IDBConstants.ATTR_PONO),
						rs.getString(IDBConstants.ATTR_IPCNO),
						rs.getDate(IDBConstants.ATTR_ORDATE),
						rs.getDate(IDBConstants.ATTR_PODATE),
						rs.getDate(IDBConstants.ATTR_IPCDATE),
						rs.getString(IDBConstants.ATTR_FILE),
						rs.getBytes(IDBConstants.ATTR_SHEET)
				);
			}			
			return null;
		}
		catch(Exception exc) {
			throw new SQLException(OtherSQLException.getMessage(exc));
		}		
		finally{
			if(stm != null)
				stm.close();
		}
	}
	
	public void updateProjectData(long index, ProjectData project, Connection conn) throws SQLException {
		PreparedStatement stm = null;		
		try{
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_PROJECT_DATA + " SET " +
					IDBConstants.ATTR_CODE + "=?, " +
					IDBConstants.ATTR_CUSTOMER + "=?, " +
					IDBConstants.ATTR_UNIT + "=?, " +
					IDBConstants.ATTR_ACTIVITY + "=?, " +
					IDBConstants.ATTR_DEPARTMENT + "=?, " +
					IDBConstants.ATTR_WORK_DESCRIPTION + "=?, " +
					IDBConstants.ATTR_REGDATE + "=?, " +
					IDBConstants.ATTR_ORNO + "=?, " +
					IDBConstants.ATTR_PONO + "=?, " +
					IDBConstants.ATTR_IPCNO + "=?, " +
					IDBConstants.ATTR_ORDATE + "=?, " +
					IDBConstants.ATTR_PODATE + "=?, " +
					IDBConstants.ATTR_IPCDATE + "=?, " +
					IDBConstants.ATTR_FILE + "=?, " +
					IDBConstants.ATTR_SHEET + "=? " +
					"WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);			
			int col = 1;
			stm.setString(col++, project.getCode());
			stm.setLong(col++, project.getCustomer().getIndex());      
			stm.setLong(col++, project.getUnit().getIndex());
			stm.setLong(col++, project.getActivity().getIndex());
			stm.setLong(col++, project.getDepartment().getIndex());
			stm.setString(col++, project.getWorkDescription());			
			if(project.getRegDate() != null)
				stm.setDate(col++, new java.sql.Date(project.getRegDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);			
			stm.setString(col++, project.getORNo());
			stm.setString(col++, project.getPONo());
			stm.setString(col++, project.getIPCNo());
			if(project.getORDate() != null)
				stm.setDate(col++, new java.sql.Date(project.getORDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if(project.getPODate() != null)
				stm.setDate(col++, new java.sql.Date(project.getPODate().getTime()));
			else
				stm.setNull(col++, Types.DATE);			
			if(project.getIPCDate() != null)
				stm.setDate(col++, new java.sql.Date(project.getIPCDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);			
			stm.setString(col++, project.getFile());			
			if(project.getSheet() == null || project.getSheet().length == 0)
				stm.setNull(col++, Types.LONGVARBINARY);
			else
				stm.setBytes(col++, project.getSheet());
			stm.executeUpdate();
		}
		catch(Exception exc) {
			throw new SQLException(OtherSQLException.getMessage(exc));
		}		
		finally{
			if(stm != null)
				stm.close();
		}
	}
	
	public void deleteProjectData(long index, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM FROM " + IDBConstants.TABLE_PROJECT_DATA +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		}
		catch(Exception exc) {
			throw new SQLException(OtherSQLException.getMessage(exc));
		}		
		finally{
			if(stm != null)
				stm.close();
		}
	}
	
	public ProjectData[] getProjectDataByCriteria(String query, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();    		
		try{
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);            
			while(rs.next()) {      
				ProjectData project = new ProjectData(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString("CUSTOMERNAME"), //customer name
						rs.getString(IDBConstants.ATTR_WORK_DESCRIPTION),
						rs.getString("ACTIVITYNAME"),
						rs.getString(IDBConstants.ATTR_ORNO),
						rs.getDate(IDBConstants.ATTR_ORDATE),
						rs.getString(IDBConstants.ATTR_PONO),
						rs.getDate(IDBConstants.ATTR_PODATE),
						rs.getString(IDBConstants.ATTR_IPCNO),
						rs.getDate(IDBConstants.ATTR_IPCDATE),
						rs.getDate(IDBConstants.ATTR_ACTUAL_START_DATE),
						rs.getDate(IDBConstants.ATTR_ACTUAL_END_DATE),
						rs.getDate(IDBConstants.ATTR_VALIDATION),      
						rs.getDate(IDBConstants.ATTR_REGDATE),     
						rs.getString(IDBConstants.ATTR_FILE),
						rs.getBytes(IDBConstants.ATTR_SHEET),
						rs.getLong(IDBConstants.ATTR_CUSTOMER),
						rs.getLong(IDBConstants.ATTR_UNIT),
						rs.getLong(IDBConstants.ATTR_ACTIVITY),
						rs.getLong(IDBConstants.ATTR_DEPARTMENT)                     
				);        
				vresult.addElement(project);				
			}
			ProjectData[] project = new ProjectData[vresult.size()];
			vresult.copyInto(project);
			return project;
		}		
		catch(Exception exc) {
			throw new SQLException(OtherSQLException.getMessage(exc));
		}		
		finally{
			if(stm != null)
				stm.close();
		}
	}
	
	
	
	
	public void createProjectPersonal(long projectindex, ProjectPersonal person, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		String query = "INSERT INTO " + IDBConstants.TABLE_PROJECT_PERSONAL + "(" +
		IDBConstants.ATTR_PROJECT + "," +
		IDBConstants.ATTR_EMPLOYEE + "," +
		IDBConstants.ATTR_POSITION + "," +
		IDBConstants.ATTR_TASK + "," +
		IDBConstants.ATTR_WORK_DESCRIPTION + "," +
		IDBConstants.ATTR_STATUS + ") " +
		" values (?, ?, ?, ?, ?, ?)";    
		try {
			stm = conn.prepareStatement(query);   			
			int col = 1;
			stm.setLong(col++, projectindex);
			stm.setLong(col++, person.getEmployee().getIndex());
			stm.setString(col++, person.getPosition());
			stm.setString(col++, person.getTask());
			stm.setString(col++, person.getWorkDescription());
			stm.setString(col++, person.getStatus());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public ProjectPersonal[] getProjectPersonal(long projectindex, Connection conn) throws SQLException {
		Statement stm = null;
		HRMSQLSAP hrmsql = new HRMSQLSAP();
		Vector vresult = new Vector();		
		try{
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_PROJECT_PERSONAL +
					" WHERE " + IDBConstants.ATTR_PROJECT + "=" + projectindex);	
			while(rs.next()) {
				vresult.addElement(new ProjectPersonal(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						hrmsql.getEmployeeByIndex(rs.getLong(IDBConstants.ATTR_EMPLOYEE), conn),
						rs.getString(IDBConstants.ATTR_POSITION),
						rs.getString(IDBConstants.ATTR_TASK),
						rs.getString(IDBConstants.ATTR_WORK_DESCRIPTION),
						rs.getString(IDBConstants.ATTR_STATUS)
				));
			}		
			ProjectPersonal[] project = new ProjectPersonal[vresult.size()];
			vresult.copyInto(project);
			return project;
		}
		catch(Exception exc) {
			throw new SQLException(OtherSQLException.getMessage(exc));
		}
		finally{
			if(stm != null)
				stm.close();
		}
	}
	
	public void updateProjectPersonal(long index, ProjectPersonal person, Connection conn) throws SQLException {
		PreparedStatement stm = null;		
		try {
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_PROJECT_PERSONAL + " SET " +
					IDBConstants.ATTR_EMPLOYEE + "=?, " +
					IDBConstants.ATTR_POSITION + "=?, " +
					IDBConstants.ATTR_TASK + "=?, " +
					IDBConstants.ATTR_WORK_DESCRIPTION + "=?, " +
					IDBConstants.ATTR_STATUS + "=? " +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);			
			int col = 1;
			stm.setLong(col++, person.getEmployee().getIndex());
			stm.setString(col++, person.getPosition());
			stm.setString(col++, person.getTask());
			stm.setString(col++, person.getWorkDescription());
			stm.setString(col++, person.getStatus());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deleteProjectPersonal(long index, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_PROJECT_PERSONAL +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		}
		catch(Exception exc) {
			throw new SQLException(OtherSQLException.getMessage(exc));
		}		
		finally{
			if(stm != null)
				stm.close();
		}
	}
	
	public void createProjectOrganizationAttachment(long index, FileAttachment file, Connection conn) throws SQLException {
		PreparedStatement stm = null;		
		try {
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_PROJECT_ORGANIZATION_ATTACH + "(" +
					IDBConstants.ATTR_PROJECT + "," +
					IDBConstants.ATTR_FILE + "," +
					IDBConstants.ATTR_SHEET + ")" +
			" values (?, ?, ?)");			
			stm.setLong(1, index);
			stm.setString(2, file.getName());
			stm.setBytes(3, file.getBytes());
			stm.executeUpdate();
		}
		catch(Exception exc) {
			exc.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(exc));
		}		
		finally{
			if(stm != null)
				stm.close();
		}
	}
	
	public FileAttachment getProjectOrganizationAttachment(long index, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_PROJECT_ORGANIZATION_ATTACH +
					" WHERE " + IDBConstants.ATTR_PROJECT + "=" + index);
			if(rs.next()) {
				return new FileAttachment(rs.getString(IDBConstants.ATTR_FILE), rs.getBytes(IDBConstants.ATTR_SHEET));
			}			
			return null;
		}
		catch(Exception exc) {
			throw new SQLException(OtherSQLException.getMessage(exc));
		}		
		finally{
			if(stm != null)
				stm.close();
		}
	}
	
	public void deleteProjectOrganizationAttachment(long index, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_PROJECT_ORGANIZATION_ATTACH +
					" WHERE " + IDBConstants.ATTR_PROJECT + "=" + index);
		}
		catch(Exception exc) {
			throw new SQLException(OtherSQLException.getMessage(exc));
		}		
		finally{
			if(stm != null)
				stm.close();
		}
	}
	
	public void createProjectContract(long projectindex, ProjectContract contract, Connection conn) throws SQLException {
		PreparedStatement stm = null;		
		try {
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_PROJECT_CONTRACT + "(" +
					IDBConstants.ATTR_PROJECT + ", " +
					IDBConstants.ATTR_ESTIMATE_START_DATE + ", " +
					IDBConstants.ATTR_ESTIMATE_END_DATE + ", " +
					IDBConstants.ATTR_ACTUAL_START_DATE + ", " +
					IDBConstants.ATTR_ACTUAL_END_DATE + ", " +
					IDBConstants.ATTR_VALUE + ", " +
					IDBConstants.ATTR_CURRENCY + ", " +
					IDBConstants.ATTR_PPN + ", " +
					IDBConstants.ATTR_VALIDATION + ", " +
					IDBConstants.ATTR_DESCRIPTION + ", " +
					IDBConstants.ATTR_FILE + ", " +
					IDBConstants.ATTR_SHEET + ") " +
			" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");			
			int col = 1;
			stm.setLong(col++, projectindex);
			if(contract.getEstimateStartDate() != null)
				stm.setDate(col++, new java.sql.Date(contract.getEstimateStartDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if(contract.getEstimateEndDate() != null)
				stm.setDate(col++, new java.sql.Date(contract.getEstimateEndDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if(contract.getActualStartDate() != null)
				stm.setDate(col++, new java.sql.Date(contract.getActualStartDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if(contract.getActualEndDate() != null)
				stm.setDate(col++, new java.sql.Date(contract.getActualEndDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);			
			stm.setDouble(col++, contract.getValue());			
			if(contract.getCurrency() != null)
				stm.setLong(col++, contract.getCurrency().getIndex());
			else
				stm.setNull(col++, Types.LONGVARCHAR);			
			stm.setBoolean(col++, contract.getPPN());
			if(contract.getValidation() != null)
				stm.setDate(col++, new java.sql.Date(contract.getValidation().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			stm.setString(col++, contract.getDescription());
			stm.setString(col++, contract.getFile());
			if(contract.getSheet() == null || contract.getSheet().length == 0)
				stm.setNull(col++, Types.LONGVARBINARY);
			else
				stm.setBytes(col++, contract.getSheet());			
			stm.executeUpdate();
		}
		catch(Exception exc) {
			throw new SQLException(OtherSQLException.getMessage(exc));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void updateProjectContract(long index, ProjectContract contract, Connection conn) throws SQLException {
		PreparedStatement stm = null;    
		try {
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_PROJECT_CONTRACT + " SET " +
					IDBConstants.ATTR_ESTIMATE_START_DATE + "=?, " +
					IDBConstants.ATTR_ESTIMATE_END_DATE + "=?, " +
					IDBConstants.ATTR_ACTUAL_START_DATE + "=?, " +
					IDBConstants.ATTR_ACTUAL_END_DATE + "=?, " +
					IDBConstants.ATTR_VALUE + "=?, " +
					IDBConstants.ATTR_CURRENCY + "=?, " +
					IDBConstants.ATTR_PPN + "=?, " +
					IDBConstants.ATTR_VALIDATION + "=?, " +
					IDBConstants.ATTR_DESCRIPTION + "=?, " +
					IDBConstants.ATTR_FILE + "=?, " +
					IDBConstants.ATTR_SHEET + "=? " +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);			
			int col = 1;
			if(contract.getEstimateStartDate() != null)
				stm.setDate(col++, new java.sql.Date(contract.getEstimateStartDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if(contract.getEstimateEndDate() != null)
				stm.setDate(col++, new java.sql.Date(contract.getEstimateEndDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if(contract.getActualStartDate() != null)
				stm.setDate(col++, new java.sql.Date(contract.getActualStartDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			if(contract.getActualEndDate() != null)
				stm.setDate(col++, new java.sql.Date(contract.getActualEndDate().getTime()));
			else
				stm.setNull(col++, Types.DATE);			
			stm.setDouble(col++, contract.getValue());			
			if(contract.getCurrency() != null)
				stm.setLong(col++, contract.getCurrency().getIndex());
			else
				stm.setNull(col++, Types.LONGVARCHAR);
			stm.setBoolean(col++, contract.getPPN());
			if(contract.getValidation() != null)
				stm.setDate(col++, new java.sql.Date(contract.getValidation().getTime()));
			else
				stm.setNull(col++, Types.DATE);
			stm.setString(col++, contract.getDescription());
			stm.setString(col++, contract.getFile());
			if(contract.getSheet() == null || contract.getSheet().length == 0)
				stm.setNull(col++, Types.LONGVARBINARY);
			else
				stm.setBytes(col++, contract.getSheet());			
			stm.executeUpdate();
		}
		catch(Exception exc) {
			throw new SQLException(OtherSQLException.getMessage(exc));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public ProjectContract getProjectContract(long projectindex, Connection conn) throws SQLException {
		Statement stm = null;
		String query = "SELECT * FROM " + IDBConstants.TABLE_PROJECT_CONTRACT +
		" WHERE " + IDBConstants.ATTR_PROJECT + "=" + projectindex;                                            
		try{
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);			
			if(rs.next()) {
				ProjectContract contract =  new ProjectContract(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getDate(IDBConstants.ATTR_ESTIMATE_START_DATE),
						rs.getDate(IDBConstants.ATTR_ESTIMATE_END_DATE),
						rs.getDate(IDBConstants.ATTR_ACTUAL_START_DATE),
						rs.getDate(IDBConstants.ATTR_ACTUAL_END_DATE),
						rs.getDouble(IDBConstants.ATTR_VALUE),
						rs.getLong(IDBConstants.ATTR_CURRENCY),
						rs.getBoolean(IDBConstants.ATTR_PPN),
						rs.getDate(IDBConstants.ATTR_VALIDATION),
						rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getString(IDBConstants.ATTR_FILE),
						rs.getBytes(IDBConstants.ATTR_SHEET));
				//contract.setContractPayment(this.getContractPayment(contract.getIndex(), conn));        
				return contract;
			}			
			return null;
		}
		catch(Exception exc) {
			throw new SQLException(OtherSQLException.getMessage(exc));
		}		
		finally{
			if(stm != null)
				stm.close();
		}
	}
	
	public void createContractPayment(long contractindex, ContractPayment payment, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		String query = "INSERT INTO " + IDBConstants.TABLE_CONTRACT_PAYMENT + "(" +
		IDBConstants.ATTR_CONTRACT + ", " +
		IDBConstants.ATTR_DESCRIPTION + ", " +
		IDBConstants.ATTR_VALUE + ", " +
		IDBConstants.ATTR_COMPLETION + ") " +
		" values (?, ?, ?, ?)";    
		try {
			stm = conn.prepareStatement(query);			
			int col = 1;
			stm.setLong(col++, contractindex);
			stm.setString(col++, payment.getDescription());
			stm.setDouble(col++, payment.getValue());
			stm.setFloat(col++, payment.getCompletion());
			stm.executeUpdate();
		}
		catch(Exception exc) {
			throw new SQLException(OtherSQLException.getMessage(exc));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public ContractPayment[] getContractPayment(long contractindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_CONTRACT_PAYMENT +
					" WHERE " + IDBConstants.ATTR_CONTRACT + "=" + contractindex);			
			while(rs.next()) {
				vresult.addElement(new ContractPayment(rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getDouble(IDBConstants.ATTR_VALUE), rs.getFloat(IDBConstants.ATTR_COMPLETION)));
			}			
			ContractPayment[] payment = new ContractPayment[vresult.size()];
			vresult.copyInto(payment);
			return payment;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deleteContractPayment(long contractindex, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_CONTRACT_PAYMENT +
					" WHERE " + IDBConstants.ATTR_CONTRACT + "=" + contractindex);
		}
		catch(Exception exc) {
			throw new SQLException(OtherSQLException.getMessage(exc));
		}		
		finally{
			if(stm != null)
				stm.close();
		}
	}
	
	public void createProjectPartner(long projectindex, Partner partner, Connection conn) throws SQLException {
		PreparedStatement stm = null;		
		try{
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_PROJECT_PARTNER + "(" +
					IDBConstants.ATTR_PROJECT + "," +
					IDBConstants.ATTR_PARTNER + ")" +
			" values (?, ?)");			
			stm.setLong(1, projectindex);
			stm.setLong(2, partner.getIndex());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public ProjectPartner[] getProjectPartner(long projectindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT " + IDBConstants.TABLE_PROJECT_PARTNER + ".* FROM " +
					IDBConstants.TABLE_PROJECT_PARTNER + "," +IDBConstants.TABLE_PARTNER +
					" WHERE " + IDBConstants.TABLE_PARTNER + "." + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_PARTNER +
					" AND " + IDBConstants.ATTR_PROJECT + "=" + projectindex);			
			while(rs.next()) {
				Partner partner = getPartnerByIndex(rs.getLong(IDBConstants.ATTR_PARTNER), conn);
				partner.setCompanyGroup(getPartnerGroup(rs.getLong(IDBConstants.ATTR_PARTNER), conn));
				vresult.addElement(new ProjectPartner(rs.getLong(IDBConstants.ATTR_AUTOINDEX), partner));
			}		
			ProjectPartner[] result = new ProjectPartner[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void updateProjectPartner(long index, Partner partner, Connection conn) throws SQLException {
		PreparedStatement stm = null;		
		try{
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_PROJECT_PARTNER + " SET " +
					IDBConstants.ATTR_PARTNER + "=? " +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);			
			stm.setLong(1, partner.getIndex());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deleteProjectPartner(long index, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_PROJECT_PARTNER +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void createProjectPartnerContact(long index, Personal personal, Connection conn) throws SQLException {
		PreparedStatement stm = null;		
		try{
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_PROJECT_PARTNER_CONTACT + "(" +
					IDBConstants.ATTR_PARTNER + ", " +
					IDBConstants.ATTR_PERSONAL + ") " +
			" values (?, ?)");			
			stm.setLong(1, index);
			stm.setLong(2, personal.getIndex());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public Personal[] getProjectPartnerContact(long index, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT " + IDBConstants.TABLE_PERSONAL + ".* FROM " +
					IDBConstants.TABLE_PERSONAL + "," + IDBConstants.TABLE_PROJECT_PARTNER_CONTACT +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_PERSONAL +
					" AND " + IDBConstants.ATTR_PARTNER + "=" + index);			
			while(rs.next()) {
				vresult.addElement(new Personal(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_TITLE),
						rs.getString(IDBConstants.ATTR_FIRST_NAME),
						rs.getString(IDBConstants.ATTR_LAST_NAME),
						rs.getString(IDBConstants.ATTR_NICK_NAME),
						rs.getString(IDBConstants.ATTR_BIRTH_PLACE),
						rs.getDate(IDBConstants.ATTR_BIRTH_DATE),
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_CITY),
						rs.getInt(IDBConstants.ATTR_POSTALCODE),
						rs.getString(IDBConstants.ATTR_PROVINCE),
						rs.getString(IDBConstants.ATTR_COUNTRY),
						rs.getString(IDBConstants.ATTR_PHONE1),
						rs.getString(IDBConstants.ATTR_PHONE2),
						rs.getString(IDBConstants.ATTR_FAX1),
						rs.getString(IDBConstants.ATTR_FAX2),
						rs.getString(IDBConstants.ATTR_EMAIL),
						rs.getString(IDBConstants.ATTR_WEBSITE)
				));
			}			
			Personal[] result = new Personal[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deleteProjectPartnerContact(long index, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_PROJECT_PARTNER_CONTACT +
					" WHERE " + IDBConstants.ATTR_PARTNER + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void createProjectClientContact(long projectindex, Personal personal, Connection conn) throws SQLException {
		PreparedStatement stm = null;		
		try{
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_PROJECT_CLIENT_CONTACT + "(" +
					IDBConstants.ATTR_PROJECT + "," +
					IDBConstants.ATTR_PERSONAL + ") " +
			" values (?, ?)");			
			stm.setLong(1, projectindex);
			stm.setLong(2, personal.getIndex());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public ProjectClientContact[] getProjectClientContact(long projectindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_PROJECT_CLIENT_CONTACT +
					" WHERE " + IDBConstants.ATTR_PROJECT + "=" + projectindex);			
			while(rs.next()) {
				vresult.addElement(new ProjectClientContact(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						this.getPersonalByIndex(rs.getLong(IDBConstants.ATTR_PERSONAL), conn)));
			}			
			ProjectClientContact[] result = new ProjectClientContact[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deleteProjectClientContact(long index, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_PROJECT_CLIENT_CONTACT +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void createProjectLocation(long projectindex, ProjectLocation location, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		String query = "INSERT INTO " + IDBConstants.TABLE_PROJECT_LOCATION + "(" +
		IDBConstants.ATTR_PROJECT + ", " +
		IDBConstants.ATTR_LOCATION + ", " +
		IDBConstants.ATTR_DESCRIPTION + ") " +
		" values (?, ?, ?)";    
		try{
			stm = conn.prepareStatement(query);
			stm.setLong(1, projectindex);
			stm.setString(2, location.getLocation());
			stm.setString(3, location.getDescription());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public ProjectLocation[] getProjectLocation(long projectindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_PROJECT_LOCATION +
					" WHERE " + IDBConstants.ATTR_PROJECT + "=" + projectindex);			
			while(rs.next()) {
				vresult.addElement(new ProjectLocation(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_LOCATION),
						rs.getString(IDBConstants.ATTR_DESCRIPTION)));
			}			
			ProjectLocation[] location = new ProjectLocation[vresult.size()];
			vresult.copyInto(location);
			return location;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void updateProjectLocation(long index, ProjectLocation location, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		String query = "UPDATE " + IDBConstants.TABLE_PROJECT_LOCATION + " SET " +
		IDBConstants.ATTR_LOCATION + "=?, " +
		IDBConstants.ATTR_DESCRIPTION + "=? " +
		" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index;    
		try {
			stm = conn.prepareStatement(query);
			stm.setString(1, location.getLocation());
			stm.setString(2, location.getDescription());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deleteProjectLocation(long index, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_PROJECT_LOCATION +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void createProjectNotes(long projectindex, ProjectNotes notes, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try{
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_PROJECT_NOTES + "(" +
					IDBConstants.ATTR_PROJECT + ", " +
					IDBConstants.ATTR_NOTES_DATE + ", " +
					IDBConstants.ATTR_DESCRIPTION + "," +
					IDBConstants.ATTR_ACTION + ", " +
					IDBConstants.ATTR_RESPONSIBILITY + ", " +
					IDBConstants.ATTR_PREPARED_BY + ", " +
					IDBConstants.ATTR_APPROVER + ", " +
					IDBConstants.ATTR_REMARK + ", " +
					IDBConstants.ATTR_FILE + ", " +
					IDBConstants.ATTR_SHEET + ") " +
			" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");			
			int col = 1;
			stm.setLong(col++, projectindex);
			stm.setDate(col++, new java.sql.Date(notes.getDate().getTime()));
			stm.setString(col++, notes.getDescription());
			stm.setString(col++, notes.getAction());
			stm.setLong(col++, notes.getResponsibility().getIndex());
			stm.setLong(col++, notes.getPreparedBy().getIndex());
			stm.setLong(col++, notes.getApprover().getIndex());
			stm.setString(col++, notes.getRemark());
			stm.setString(col++, notes.getFile());
			if(notes.getSheet() == null || notes.getSheet().length == 0)
				stm.setNull(col++, Types.LONGVARBINARY);
			else
				stm.setBytes(col++, notes.getSheet());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public ProjectNotes[] getProjectNotes(long projectindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_PROJECT_NOTES +
					" WHERE " + IDBConstants.ATTR_PROJECT + "=" + projectindex);			
			while(rs.next()) {
				vresult.addElement(new ProjectNotes(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getDate(IDBConstants.ATTR_NOTES_DATE),
						rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getString(IDBConstants.ATTR_ACTION),
						new HRMSQLSAP().getEmployeeByIndex(rs.getLong(IDBConstants.ATTR_RESPONSIBILITY), conn),
						new HRMSQLSAP().getEmployeeByIndex(rs.getLong(IDBConstants.ATTR_PREPARED_BY), conn),
						new HRMSQLSAP().getEmployeeByIndex(rs.getLong(IDBConstants.ATTR_APPROVER), conn),
						rs.getString(IDBConstants.ATTR_REMARK),
						rs.getString(IDBConstants.ATTR_FILE),
						rs.getBytes(IDBConstants.ATTR_SHEET)
				));
			}			
			ProjectNotes[] result = new ProjectNotes[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void updateProjectNotes(long index, ProjectNotes notes, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try{
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_PROJECT_NOTES + " SET " +
					IDBConstants.ATTR_NOTES_DATE + "=?, " +
					IDBConstants.ATTR_DESCRIPTION + "=?," +
					IDBConstants.ATTR_ACTION + "=?, " +
					IDBConstants.ATTR_RESPONSIBILITY + "=?, " +
					IDBConstants.ATTR_PREPARED_BY + "=?, " +
					IDBConstants.ATTR_APPROVER + "=?, " +
					IDBConstants.ATTR_REMARK + "=?, " +
					IDBConstants.ATTR_FILE + "=?, " +
					IDBConstants.ATTR_SHEET + "=? " +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);			
			int col = 1;
			stm.setDate(col++, new java.sql.Date(notes.getDate().getTime()));
			stm.setString(col++, notes.getDescription());
			stm.setString(col++, notes.getAction());
			stm.setLong(col++, notes.getResponsibility().getIndex());
			stm.setLong(col++, notes.getPreparedBy().getIndex());
			stm.setLong(col++, notes.getApprover().getIndex());
			stm.setString(col++, notes.getRemark());
			stm.setString(col++, notes.getFile());
			if(notes.getSheet() == null || notes.getSheet().length == 0)
				stm.setNull(col++, Types.LONGVARBINARY);
			else
				stm.setBytes(col++, notes.getSheet());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deleteProjectNotes(long index, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_PROJECT_NOTES +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public ProjectNotes[] getProjectNotesByCriteria(String query, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);			
			while(rs.next()) {
				vresult.addElement(new ProjectNotes(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getDate(IDBConstants.ATTR_NOTES_DATE),
						rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getString(IDBConstants.ATTR_ACTION),
						new HRMSQLSAP().getEmployeeByIndex(rs.getLong(IDBConstants.ATTR_RESPONSIBILITY), conn),
						new HRMSQLSAP().getEmployeeByIndex(rs.getLong(IDBConstants.ATTR_PREPARED_BY), conn),
						new HRMSQLSAP().getEmployeeByIndex(rs.getLong(IDBConstants.ATTR_APPROVER), conn),
						rs.getString(IDBConstants.ATTR_REMARK),
						rs.getString(IDBConstants.ATTR_FILE),
						rs.getBytes(IDBConstants.ATTR_SHEET)
				));
			}			
			ProjectNotes[] result = new ProjectNotes[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void createProjectProgress(long projectindex, ProjectProgress progress, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try{
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_PROJECT_PROGRESS + "(" +
					IDBConstants.ATTR_PROJECT + ", " +
					IDBConstants.ATTR_PROGRESS_DATE + ", " +
					IDBConstants.ATTR_DESCRIPTION + "," +
					IDBConstants.ATTR_COMPLETION + ", " +
					IDBConstants.ATTR_PREPARED_BY + ", " +
					IDBConstants.ATTR_APPROVER + ", " +
					IDBConstants.ATTR_REMARK + ") " +
			" values (?, ?, ?, ?, ?, ?, ?)");			
			int col = 1;
			stm.setLong(col++, projectindex);
			stm.setDate(col++, new java.sql.Date(progress.getDate().getTime()));
			stm.setString(col++, progress.getDescription());
			stm.setFloat(col++, progress.getCompletion());
			stm.setLong(col++, progress.getPreparedBy().getIndex());
			stm.setLong(col++, progress.getApprover().getIndex());
			stm.setString(col++, progress.getRemark());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public ProjectProgress[] getProjectProgress(long projectindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String qry = "SELECT * FROM " + IDBConstants.TABLE_PROJECT_PROGRESS +
		" WHERE " + IDBConstants.ATTR_PROJECT + "=" + projectindex;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(qry);	
			while(rs.next()) {
				vresult.addElement(new ProjectProgress(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getDate(IDBConstants.ATTR_PROGRESS_DATE),
						rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getFloat(IDBConstants.ATTR_COMPLETION),
						new HRMSQLSAP().getEmployeeByIndex(rs.getLong(IDBConstants.ATTR_PREPARED_BY), conn),
						new HRMSQLSAP().getEmployeeByIndex(rs.getLong(IDBConstants.ATTR_APPROVER), conn),
						rs.getString(IDBConstants.ATTR_REMARK))
				);
			}			
			ProjectProgress[] result = new ProjectProgress[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void updateProjectProgress(long index, ProjectProgress progress, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try{
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_PROJECT_PROGRESS + " SET " +
					IDBConstants.ATTR_PROGRESS_DATE + "=?, " +
					IDBConstants.ATTR_DESCRIPTION + "=?," +
					IDBConstants.ATTR_COMPLETION + "=?, " +
					IDBConstants.ATTR_PREPARED_BY + "=?, " +
					IDBConstants.ATTR_APPROVER + "=?, " +
					IDBConstants.ATTR_REMARK + "=? " +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);			
			int col = 1;
			stm.setDate(col++, new java.sql.Date(progress.getDate().getTime()));
			stm.setString(col++, progress.getDescription());
			stm.setFloat(col++, progress.getCompletion());
			stm.setLong(col++, progress.getPreparedBy().getIndex());
			stm.setLong(col++, progress.getApprover().getIndex());
			stm.setString(col++, progress.getRemark());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deleteProjectProgress(long index, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_PROJECT_PROGRESS +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public ProjectProgress[] getProjectProgressByCriteria(String qry, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(qry);
			while(rs.next()) {
				vresult.addElement(new ProjectProgress(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getDate(IDBConstants.ATTR_PROGRESS_DATE),
						rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getFloat(IDBConstants.ATTR_COMPLETION),
						new HRMSQLSAP().getEmployeeByIndex(rs.getLong(IDBConstants.ATTR_PREPARED_BY), conn),
						new HRMSQLSAP().getEmployeeByIndex(rs.getLong(IDBConstants.ATTR_APPROVER), conn),
						rs.getString(IDBConstants.ATTR_REMARK))
				);
			}			
			ProjectProgress[] result = new ProjectProgress[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	
	public void createTimeSheet(long projectindex, TimeSheet sheet, Connection conn) throws SQLException {
		PreparedStatement stm = null;		
		try {
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_TIME_SHEET + "(" +
					IDBConstants.ATTR_PROJECT + "," +
					IDBConstants.ATTR_ENTRY_DATE + "," +
					IDBConstants.ATTR_PREPARED_BY + "," +
					IDBConstants.ATTR_PREPARED_DATE + "," +
					IDBConstants.ATTR_CHEKED_BY + "," +
					IDBConstants.ATTR_CHEKED_DATE + "," +
					IDBConstants.ATTR_APPROVAL + "," +
					IDBConstants.ATTR_APPROVAL_DATE + "," +
					IDBConstants.ATTR_WORK_DESCRIPTION + ")" +
			" values(?, ?, ?, ?, ?, ?, ?, ?, ?)");			
			int col = 1;
			stm.setLong(col++, projectindex);
			stm.setDate(col++, new java.sql.Date(sheet.getEntryDate().getTime()));
			stm.setLong(col++, sheet.getPreparedBy().getIndex());
			stm.setDate(col++, new java.sql.Date(sheet.getPreparedDate().getTime()));
			stm.setLong(col++, sheet.getCheckedBy().getIndex());
			stm.setDate(col++, new java.sql.Date(sheet.getCheckedDate().getTime()));
			stm.setString(col++, sheet.getApproved());
			stm.setDate(col++, new java.sql.Date(sheet.getApprovedDate().getTime()));
			stm.setString(col++, sheet.getDescription());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public TimeSheet[] getTimeSheet(long projectindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "SELECT * FROM " + IDBConstants.TABLE_TIME_SHEET +
		" WHERE " + IDBConstants.ATTR_PROJECT + "=" + projectindex;    
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);			
			while(rs.next()) {
				vresult.addElement(new TimeSheet(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getDate(IDBConstants.ATTR_ENTRY_DATE),
						new HRMSQLSAP().getEmployeeByIndex(rs.getLong(IDBConstants.ATTR_PREPARED_BY), conn),
						rs.getDate(IDBConstants.ATTR_PREPARED_DATE),
						new HRMSQLSAP().getEmployeeByIndex(rs.getLong(IDBConstants.ATTR_CHEKED_BY), conn),
						rs.getDate(IDBConstants.ATTR_CHEKED_DATE),
						rs.getString(IDBConstants.ATTR_APPROVAL),
						rs.getDate(IDBConstants.ATTR_APPROVAL_DATE),
						rs.getString(IDBConstants.ATTR_WORK_DESCRIPTION)));
			}			
			TimeSheet[] result = new TimeSheet[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void updateTimeSheet(long index, TimeSheet sheet, Connection conn) throws SQLException {
		PreparedStatement stm = null;		
		try {
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_TIME_SHEET + " SET " +
					IDBConstants.ATTR_ENTRY_DATE + "=?, " +
					IDBConstants.ATTR_PREPARED_BY + "=?, " +
					IDBConstants.ATTR_PREPARED_DATE + "=?, " +
					IDBConstants.ATTR_CHEKED_BY + "=?, " +
					IDBConstants.ATTR_CHEKED_DATE + "=?, " +
					IDBConstants.ATTR_APPROVAL + "=?, " +
					IDBConstants.ATTR_APPROVAL_DATE + "=?, " +
					IDBConstants.ATTR_WORK_DESCRIPTION + "=? " +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);			
			int col = 1;
			stm.setDate(col++, new java.sql.Date(sheet.getEntryDate().getTime()));
			stm.setLong(col++, sheet.getPreparedBy().getIndex());
			stm.setDate(col++, new java.sql.Date(sheet.getPreparedDate().getTime()));
			stm.setLong(col++, sheet.getCheckedBy().getIndex());
			stm.setDate(col++, new java.sql.Date(sheet.getCheckedDate().getTime()));
			stm.setString(col++, sheet.getApproved());
			stm.setDate(col++, new java.sql.Date(sheet.getApprovedDate().getTime()));
			stm.setString(col++, sheet.getDescription());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deleteTimeSheet(long index, Connection conn) throws SQLException {
		Statement stm = null;		
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_TIME_SHEET +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public TimeSheet[] getTimeSheetByCriteria(String query, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();    
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);			
			while(rs.next()) {
				vresult.addElement(new TimeSheet(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getDate(IDBConstants.ATTR_ENTRY_DATE),
						new HRMSQLSAP().getEmployeeByIndex(rs.getLong(IDBConstants.ATTR_PREPARED_BY), conn),
						rs.getDate(IDBConstants.ATTR_PREPARED_DATE),
						new HRMSQLSAP().getEmployeeByIndex(rs.getLong(IDBConstants.ATTR_CHEKED_BY), conn),
						rs.getDate(IDBConstants.ATTR_CHEKED_DATE),
						rs.getString(IDBConstants.ATTR_APPROVAL),
						rs.getDate(IDBConstants.ATTR_APPROVAL_DATE),
						rs.getString(IDBConstants.ATTR_WORK_DESCRIPTION)));
			}			
			TimeSheet[] result = new TimeSheet[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void createTimeSheetDetail(long sheetindex, TimeSheetDetail detail,
			Connection conn) throws SQLException {
		PreparedStatement stm = null;
		String query ="INSERT INTO " + IDBConstants.TABLE_TIME_SHEET_DETAIL + "(" +
		IDBConstants.ATTR_TIME_SHEET + "," +
		IDBConstants.ATTR_PERSONAL + "," +
		IDBConstants.ATTR_AREA_CODE + "," +
		IDBConstants.ATTR_QUALIFICATION + "," +
		IDBConstants.ATTR_START_DATE + "," +
		IDBConstants.ATTR_FINISH_DATE + "," +
		IDBConstants.ATTR_DAYS + "," +
		IDBConstants.ATTR_REGULER + "," +
		IDBConstants.ATTR_HOLIDAY + ")" +
		" values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			stm = conn.prepareStatement(query);
			int col = 1;
			stm.setLong(col++, sheetindex);
			stm.setLong(col++, detail.getPersonel().getIndex());
			stm.setString(col++, detail.getAreaCode());
			stm.setLong(col++, detail.getQualification().getIndex());
			stm.setDate(col++, new java.sql.Date(detail.getStartDate().getTime()));
			stm.setDate(col++, new java.sql.Date(detail.getFinishDate().getTime()));
			stm.setInt(col++, detail.getDays());
			stm.setInt(col++, detail.getReguler());
			stm.setInt(col++, detail.getHoliday());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public TimeSheetDetail[] getTimeSheetDetail(long sheetindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();		
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_TIME_SHEET_DETAIL +
					" WHERE " + IDBConstants.ATTR_TIME_SHEET + "=" + sheetindex);			
			while(rs.next()) {
				vresult.addElement(new TimeSheetDetail(
						new HRMSQLSAP().getEmployeeByIndex(rs.getLong(IDBConstants.ATTR_PERSONAL), conn),
						rs.getString(IDBConstants.ATTR_AREA_CODE),
						new HRMSQLSAP().getQualification(rs.getLong(IDBConstants.ATTR_QUALIFICATION), conn),
						rs.getDate(IDBConstants.ATTR_START_DATE),
						rs.getDate(IDBConstants.ATTR_FINISH_DATE),
						rs.getInt(IDBConstants.ATTR_DAYS),
						rs.getInt(IDBConstants.ATTR_REGULER),
						rs.getInt(IDBConstants.ATTR_HOLIDAY)
				));
			}			
			TimeSheetDetail[] result = new TimeSheetDetail[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public void deleteTimeSheetDetail(long sheetindex, Connection conn) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_TIME_SHEET_DETAIL +
					" WHERE " + IDBConstants.ATTR_TIME_SHEET + "=" + sheetindex);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}		
		finally {
			if(stm != null)
				stm.close();
		}
	}
	
	public EmployeeQualification[] getEmployeeQualification(long index, Connection conn) throws SQLException{
		Statement stm = null;       
		Vector vresult = new Vector();
		String query ="select a." + IDBConstants.ATTR_AUTOINDEX + 
		",b." + IDBConstants.ATTR_QUALIFICATION + 
		",c." + IDBConstants.ATTR_CODE +
		" from " + IDBConstants.TABLE_EMPLOYEE + " a " +
		" left join " + IDBConstants.TABLE_EMPLOYEE_QUALIFICATION + " b on a." + IDBConstants.ATTR_AUTOINDEX + "=b." + IDBConstants.ATTR_EMPLOYEE + 
		" left join " + IDBConstants.TABLE_QUALIFICATION + " c on b." + IDBConstants.ATTR_QUALIFICATION + "=c." + IDBConstants.ATTR_AUTOINDEX +
		" where a." + IDBConstants.ATTR_AUTOINDEX + "=" + index;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			
			while(rs.next()) {
				vresult.addElement(new EmployeeQualification(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getLong(IDBConstants.ATTR_QUALIFICATION),rs.getString(IDBConstants.ATTR_CODE)));    
			}
			EmployeeQualification[] result = new EmployeeQualification[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
	}  
	
	public EmployeeQualification[] getEmployeeQualificationUsingName(long index, Connection conn) throws SQLException{
		Statement stm = null;       
		Vector vresult = new Vector();
		String query ="select a." + IDBConstants.ATTR_AUTOINDEX + 
		",b." + IDBConstants.ATTR_QUALIFICATION + 
		",c." + IDBConstants.ATTR_CODE + 
		",c.name " + 
		" from " + IDBConstants.TABLE_EMPLOYEE + " a " +
		" left join " + IDBConstants.TABLE_EMPLOYEE_QUALIFICATION + " b on a." + IDBConstants.ATTR_AUTOINDEX + "=b." + IDBConstants.ATTR_EMPLOYEE + 
		" left join " + IDBConstants.TABLE_QUALIFICATION + " c on b." + IDBConstants.ATTR_QUALIFICATION + "=c." + IDBConstants.ATTR_AUTOINDEX +
		" where a." + IDBConstants.ATTR_AUTOINDEX + "=" + index;
		System.err.println("qry : " + query);
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);			
			while(rs.next()) {
				vresult.addElement(new EmployeeQualification(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getLong(IDBConstants.ATTR_QUALIFICATION),
						rs.getString(IDBConstants.ATTR_CODE),rs.getString("name")
				));          
				
			}
			EmployeeQualification[] result = new EmployeeQualification[vresult.size()];
			vresult.copyInto(result);
			return result;		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
	}  
	
	public EmployeeTimeSheetByCriteria[] getEmployeeTimeSheetByCriteria(long index,Connection conn) throws SQLException{
		/*String query = "select a.workdescription,b.areacode,b.days,b.qualification," +
		 "d.name as customer,c.ipcno " +
		 "from timesheet a " +
		 "inner join timesheetdetail b on a.autoindex=b.timesheet " +
		 "inner join projectdata c on a.project=c.autoindex " +
		 "inner join customer d on c.customer=d.autoindex";*/
		return null;
	}
	
	public EmployeeTimesheet[] getAllEmployeeTimesheet(String query,Connection conn) throws SQLException{
		Statement stm = null;       
		Vector vresult = new Vector();	
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while(rs.next()) {
				vresult.addElement(new EmployeeTimesheet(rs.getLong("personal"),
						rs.getString("employeeno"),
						rs.getString("firstname"),
						rs.getString("midlename"),
						rs.getString("lastname"),
						rs.getString("jobtitle"),
						rs.getString("qualification"),
						rs.getString("workdescription"),
						rs.getString("client"),
						rs.getString("ipcno"),
						rs.getInt("days"),
						rs.getString("areacode"),
						rs.getDate("entrydate")));
			}
			EmployeeTimesheet[] result = new EmployeeTimesheet[vresult.size()];
			vresult.copyInto(result);
			return result;
		}catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
	}  
	
	public EmployeeTimesheet[] getAllEmployeeTimesheetUtilization(String query,Connection conn) throws SQLException{
		Statement stm = null;       
		Vector vresult = new Vector();	
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while(rs.next()) {
				vresult.addElement(new EmployeeTimesheet(rs.getLong("personal"),
						rs.getString("employeeno"),
						rs.getString("firstname"),
						rs.getString("midlename"),
						rs.getString("lastname"),
						rs.getString("jobtitle"),
						rs.getString("qualification"),
						rs.getString("workdescription"),
						rs.getString("client"),
						rs.getString("ipcno"),
						rs.getInt("days"),
						rs.getString("areacode"),
						rs.getDate("entrydate"),
						rs.getInt("reguler"),
						rs.getInt("holiday")));
			}
			EmployeeTimesheet[] result = new EmployeeTimesheet[vresult.size()];
			vresult.copyInto(result);
			return result;
		}catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
	}

	public ProjectData[] getProjectDataByAccount(Connection conn, long account) throws SQLException {
		
		Statement stm = null;
		Vector vresult = new Vector();    
		try{
			stm = conn.createStatement();
			String qry = "SELECT distinct a." + IDBConstants.ATTR_AUTOINDEX + 
			",a." + IDBConstants.ATTR_CODE + 
			",b." + IDBConstants.ATTR_NAME + 
			",a." + IDBConstants.ATTR_WORK_DESCRIPTION + 
			",c." + IDBConstants.ATTR_NAME + " as activity" +
			",a." + IDBConstants.ATTR_ORNO +
			",a." + IDBConstants.ATTR_ORDATE + 
			",a." + IDBConstants.ATTR_PONO +
			",a." + IDBConstants.ATTR_PODATE + 
			",a." + IDBConstants.ATTR_IPCNO +
			",a." + IDBConstants.ATTR_IPCDATE + 
			",d." + IDBConstants.ATTR_ACTUAL_START_DATE +
			",d." + IDBConstants.ATTR_ACTUAL_END_DATE +  
			",d." + IDBConstants.ATTR_VALIDATION + 
			",a." + IDBConstants.ATTR_REGDATE + 
			",a." + IDBConstants.ATTR_FILE +
			",a." + IDBConstants.ATTR_SHEET + // baru
			",a." + IDBConstants.ATTR_CUSTOMER + " as indexcust " +
			",a." + IDBConstants.ATTR_UNIT + " as indexunit" +             
			",a." + IDBConstants.ATTR_ACTIVITY + " as indexact" +
			",a." + IDBConstants.ATTR_DEPARTEMENT + " as indexdept " +
			" from " + IDBConstants.TABLE_PROJECT_DATA + " a " +
			" left join " + IDBConstants.TABLE_CUSTOMER + " b on a." + IDBConstants.ATTR_CUSTOMER + "=b." + IDBConstants.ATTR_AUTOINDEX +
			" left join " + IDBConstants.TABLE_ACTIVITY + " c on a." + IDBConstants.ATTR_ACTIVITY + "=c." + IDBConstants.ATTR_AUTOINDEX +
			" left join " + IDBConstants.TABLE_PROJECT_CONTRACT + " as d on a." + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_PROJECT +
			" left join " + IDBConstants.TABLE_UNIT + " e on a." + IDBConstants.ATTR_UNIT + "=e." + IDBConstants.ATTR_AUTOINDEX +
			" left join " + IDBConstants.TABLE_ORGANIZATION + " f on a." + IDBConstants.ATTR_DEPARTMENT + "=f." + IDBConstants.ATTR_AUTOINDEX + 
			" right join (select distinct subsidiaryaccount from transvalueposted where account=" + account + ") tv on a.autoindex=tv.subsidiaryaccount " +
			"order by a." + IDBConstants.ATTR_CODE;
			ResultSet rs = stm.executeQuery(qry);
			while(rs.next()) {
				ProjectData project = new ProjectData(rs.getLong("autoindex"),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						rs.getString(IDBConstants.ATTR_WORK_DESCRIPTION),
						rs.getString(IDBConstants.ATTR_ACTIVITY),
						rs.getString(IDBConstants.ATTR_ORNO),
						rs.getDate(IDBConstants.ATTR_ORDATE),
						rs.getString(IDBConstants.ATTR_PONO),
						rs.getDate(IDBConstants.ATTR_PODATE),
						rs.getString(IDBConstants.ATTR_IPCNO),
						rs.getDate(IDBConstants.ATTR_IPCDATE),
						rs.getDate(IDBConstants.ATTR_ACTUAL_START_DATE),
						rs.getDate(IDBConstants.ATTR_ACTUAL_END_DATE),
						rs.getDate(IDBConstants.ATTR_VALIDATION),      
						rs.getDate(IDBConstants.ATTR_REGDATE),
						rs.getString(IDBConstants.ATTR_FILE),  
						rs.getBytes(IDBConstants.ATTR_SHEET),
						rs.getLong("indexcust"),
						rs.getLong("indexunit"),
						rs.getLong("indexact"),
						rs.getLong("indexdept")
				);               
				vresult.addElement(project);
				
			}
			
			ProjectData[] project = new ProjectData[vresult.size()];
			vresult.copyInto(project);
			return project;
		}
		catch(Exception exc) {
			throw new SQLException(OtherSQLException.getMessage(exc));
		}		
		finally{
			if(stm != null)
				stm.close();
		}
	}
	
}
