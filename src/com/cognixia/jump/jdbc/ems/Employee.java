package com.cognixia.jump.jdbc.ems;


public class Employee {
	
	private int employeeId;
	private String employeeFirstName;
	private String employeeLastName;
	private String employeeEmail;
	private int addressId;
	private int departmentId;
	
	// TODO: static employee ID?
public Employee(int employeeId, String employeeFirstName, String employeeLastName, String employeeEmail,
			int addressId, int departmentId) {

	this.employeeId = employeeId;
	this.employeeFirstName = employeeFirstName;
	this.employeeLastName = employeeLastName;
	this.employeeEmail = employeeEmail;
	this.addressId = addressId;
	this.departmentId = departmentId;
}
	
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeFirstName() {
		return employeeFirstName;
	}
	public void setEmployeeFirstName(String employeeFirstName) {
		this.employeeFirstName = employeeFirstName;
	}
	public String getEmployeeLastName() {
		return employeeLastName;
	}
	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}
	public String getEmployeeEmail() {
		return employeeEmail;
	}
	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}
	public int getAddressId() {
		return addressId;
	}
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", employeeFirstName=" + employeeFirstName + ", employeeLastName="
				+ employeeLastName + ", employeeEmail=" + employeeEmail + ", addressId=" + addressId
				+ ", departmentId=" + departmentId + "]";
	}
}