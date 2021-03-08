package com.cognixia.jump.jdbc.ems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOClass implements EmployeeDAO {
	private Connection connection = ConnectionManager.getConnection();
	private static final String TABLE_NAME = "employee";
	@Override
	public boolean addEmployee(Employee employee) {
		// TODO Auto-generated method stub
		
		String query = "INSERT INTO " + TABLE_NAME + 
				" (employee_id, employee_firstname, employee_lastname, employee_email, addr_id, dept_id) " + 
				" values (null, ?, ?, ?, ?, ?)";
		
		boolean isSuccessful = false;
		
		try (PreparedStatement stmt = connection.prepareStatement(query);){
			
			stmt.setString(1, employee.getEmployeeFirstName());
			stmt.setString(2, employee.getEmployeeLastName());
			stmt.setString(3, employee.getEmployeeEmail());
			stmt.setInt(4, employee.getAddressId());
			stmt.setInt(5, employee.getDepartmentId());
			
			int rowCount = stmt.executeUpdate();
			
			if (rowCount > 0 ) {
				isSuccessful = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return isSuccessful;
	}
	
	@Override
	public List<Employee> getAllEmployees() {
		Statement stmt;
		List<Employee> employeeList = new ArrayList<>();
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("select * from employee");
			
			while(rs.next()) {
				Employee empl = new Employee(
						        rs.getInt("employee_id"), 
						        rs.getString("employee_firstname"), 
						        rs.getString("employee_lastname"),
						        rs.getString("employee_email"),
						        rs.getInt("addr_id"),
						        rs.getInt("dept_id"));
				
				employeeList.add(empl);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeeList;
	}
	
	@Override
	public Employee getEmployeeById(int employeeId) {
		
		Employee employee = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = connection.prepareStatement("select * from employee where employee_id = ?");
			pst.setInt(1, employeeId);
			
			rs = pst.executeQuery();
			rs.next();
			employee = new Employee(
			        rs.getInt("employee_id"), 
			        rs.getString("employee_firstname"), 
			        rs.getString("employee_lastname"),
			        rs.getString("employee_email"),
			        rs.getInt("addr_id"),
			        rs.getInt("dept_id"));
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Employee with id = " + employeeId + " not found.");
		}
		
		
		try {
			rs.close();
			pst.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
		
		return employee;
	}
	
	@Override
	public List<Employee> getEmployeesByLastName(String employeeLastName) {
			
		List<Employee> employees = new ArrayList<>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = connection.prepareStatement("select * from employee where employee_lastname = ?");
			pst.setString(1, employeeLastName);
			
			rs = pst.executeQuery();
			
			while (rs.next()) {
				Employee empl = new Employee(
				        rs.getInt("employee_id"), 
				        rs.getString("employee_firstname"), 
				        rs.getString("employee_lastname"),
				        rs.getString("employee_email"),
				        rs.getInt("addr_id"),
				        rs.getInt("dept_id"));
				employees.add(empl);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Employee with last name = " + employeeLastName + " not found.");
		}
		
		try {
			rs.close();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return employees;
	}	
		
		
	@Override
	public boolean updateEmployee(Employee employee) {
		try {
			PreparedStatement pst = 
			connection.prepareStatement("UPDATE employee SET employee_firstname = ?, employee_lastname = ?, employee_email = ?, addr_id = ?, dept_id = ? WHERE employee_id = ?");
			pst.setString(1, employee.getEmployeeFirstName());
			pst.setString(2, employee.getEmployeeLastName());
			pst.setString(3, employee.getEmployeeEmail());
			pst.setInt(4, employee.getAddressId());
			pst.setInt(5,  employee.getDepartmentId());
			pst.setInt(6, employee.getEmployeeId());
			int i = pst.executeUpdate();
			if(i > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Employee with id = " + employee.getEmployeeId() + " not found.");
		}
		return false;
	}

	
	@Override
	public boolean deleteEmployee(int employeeId) {
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement("delete from employee WHERE employee_id = ?");
			pst.setInt(1, employeeId);
			
			int i = pst.executeUpdate();
			
			if(i > 0) {
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Employee with id = " + employeeId + " not found.");
		}
		return false;
	}

	@Override
	public List<Employee> getEmployeesByDepartmentName(String departmentName) {
		
		DepartmentDAOClass dept = new DepartmentDAOClass();
		int deptId = dept.getDepartmentByName(departmentName).getId();
		
		List<Employee> employees = new ArrayList<>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = connection.prepareStatement("select * from employee where dept_id = ?");
		
			pst.setInt(1, deptId);
			
			rs = pst.executeQuery();
			while (rs.next()) {
				Employee empl = new Employee(
				        rs.getInt("employee_id"), 
				        rs.getString("employee_firstname"), 
				        rs.getString("employee_lastname"),
				        rs.getString("employee_email"),
				        rs.getInt("addr_id"),
				        rs.getInt("dept_id"));
				
				employees.add(empl);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Department not found.");
		}
		
		try {
			rs.close();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return employees;
	}

	@Override
	public List<Employee> getEmployeeByAddressId(int AddressId) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			List<Employee> employees = new ArrayList<>();
			pst = connection.prepareStatement("select * from employee where addr_id = ?");
			pst.setInt(1, AddressId);
			rs = pst.executeQuery();
			while (rs.next()) {
				Employee empl = new Employee(
				        rs.getInt("employee_id"), 
				        rs.getString("employee_firstname"), 
				        rs.getString("employee_lastname"),
				        rs.getString("employee_email"),
				        rs.getInt("addr_id"),
				        rs.getInt("dept_id"));
				employees.add(empl);
			}
			return employees;
		} catch (SQLException e) {
//			e.printStackTrace();
			System.out.println("Address Id" + "\"" + AddressId + "\"" + " not found.");
		}
		return null;
	}
	
}
