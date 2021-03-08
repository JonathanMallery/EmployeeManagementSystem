package com.cognixia.jump.jdbc.ems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAOClass implements DepartmentDAO {
	private Connection conn = ConnectionManager.getConnection();
	@Override
	public boolean addDepartment(Department dept) {
		try {
			PreparedStatement pstmt = conn.prepareStatement("insert into department (department_id, department_name) values (?,?)");
			//tests
			pstmt.setInt(1,0);
			pstmt.setString(2, dept.getName());
			int i = pstmt.executeUpdate();
			if (i > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public List<Department> getAllDepartments() {
		try {
			PreparedStatement pstmt = conn.prepareStatement("select * from department");
			ResultSet rs = pstmt.executeQuery();
			List<Department> deptList = new ArrayList<>();
			while(rs.next()) {
				int id = rs.getInt("department_id");
				String name = rs.getString("department_name");
				Department dept = new Department(id, name);
				deptList.add(dept);
			}
			return deptList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public Department getDepartmentById(int deptId) {
		try {
			PreparedStatement pstmt = conn.prepareStatement("select * from department where department_id = ? ");
			pstmt.setInt(1, deptId);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			//column object
			int id = rs.getInt("department_id");
		    String name = rs.getString("department_name");
		    //Department object
		    Department dept = new Department(id,name);
		    return dept;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public Department getDepartmentByName(String deptName) {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Department dept = null;
		try {
			pstmt = conn.prepareStatement("select * from department where department_name = ?");
			pstmt.setString(1, deptName);
			rs = pstmt.executeQuery();
			rs.next();
			
			int id = rs.getInt("department_id");
			String name = rs.getString("department_name");
			
			dept = new Department(id,name);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return dept;
	}
	@Override
	public boolean updateDepartment(Department dept) {
	 try {
		PreparedStatement pstmt = conn.prepareStatement("UPDATE department SET department_name = ? WHERE department_id = ?");
		pstmt.setString(1,dept.getName());
		pstmt.setInt(2, dept.getId());
		int i = pstmt.executeUpdate();
		if (i>0) {
			return true;
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
	 return false;
	}
	@Override
	public boolean deleteDepartment(int deptId) {
	  try {
		PreparedStatement pstmt = conn.prepareStatement("DELETE from department WHERE department_id = ?");
		pstmt.setInt(1, deptId);
		int i = pstmt.executeUpdate();
		if(i > 0) {
			return true;
		}
	} catch (SQLException e) {
		System.out.println("Department with id = " + deptId + " not found.");
	}
	return false;
}
	}
	