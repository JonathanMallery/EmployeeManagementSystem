package com.cognixia.jump.jdbc.ems;

import java.util.List;
public interface DepartmentDAO {
	
	public boolean addDepartment(Department dept);
	List<Department>getAllDepartments();
	public Department getDepartmentById(int deptId);
	public Department getDepartmentByName(String deptName);
	public boolean updateDepartment(Department dept);
	public boolean deleteDepartment(int deptId);
}