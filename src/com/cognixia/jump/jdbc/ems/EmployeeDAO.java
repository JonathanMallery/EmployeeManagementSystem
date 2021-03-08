package com.cognixia.jump.jdbc.ems;

import java.util.List;

public interface EmployeeDAO {
	public boolean addEmployee(Employee employee);
	public List <Employee> getAllEmployees();
    public Employee getEmployeeById(int employeeId);
    public List<Employee> getEmployeesByLastName(String employeeLastName);
    public List<Employee> getEmployeesByDepartmentName(String deptName);
    public boolean updateEmployee(Employee employee);
	public boolean deleteEmployee(int employeeId);
	public List<Employee> getEmployeeByAddressId(int AddressId);
}