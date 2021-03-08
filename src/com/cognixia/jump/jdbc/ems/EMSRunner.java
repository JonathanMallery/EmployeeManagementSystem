package com.cognixia.jump.jdbc.ems;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EMSRunner {
	public static DepartmentDAOClass departmentDB = new DepartmentDAOClass();
	public static EmployeeDAOClass employeeDB = new EmployeeDAOClass();
	public static AddressDAOClass addressDB = new AddressDAOClass();
	public static void main(String[] args) {
		promptUser();
	}
	public static void promptUser() {
		Scanner scan = new Scanner(System.in);
		boolean running = true;
		while (running) {
			System.out.println("\n[Employee Management System]");
			System.out.println("Available commands are: \n(1) add \n(2) remove \n(3) update \n(4) list \n(5) exit");
			System.out.print("Please enter a command: ");
			// Read Command, run through exception
			String command = scan.nextLine();
			int id = 0;
			String lastName = "";
			try {
				switch (command) {
				case "add":
				case "1":
					runAddCommand(scan);
					break;
				case "remove":
				case "2":
					runRemoveCommand(scan);					
					break;
				case "update":
				case "3":
					runUpdateCommand(scan);
					break;
				case "list":
				case "4": 
					runListCommand(scan);
					break;
				case "exit":
				case "5":
					running = false;
					break;
				default:
					System.out.println("Incorrect command. Please enter the numeric value or string corresponding with the available commands.");
				}
			}catch (InputMismatchException e) {
					System.out.println("Incorrect input");
				}
			}		
		scan.close();	
	}
	public static void runAddCommand(Scanner scan) {
		System.out.println("\n[Employee Management System]\n");
		System.out.println("Adding new employee...");
		String firstName = "";
		String lastName = "";
		String email = "";
		String department = "";
		String streetAddress = "";
		String city = "";
		String state = "";
		int zipCode = 0;
		
		// First Name
		System.out.print("Enter first name: ");
		firstName = scan.nextLine();
		
		// Last Name
		System.out.print("Enter last name: ");
		lastName = scan.nextLine();
		
		// Email
		System.out.print("Enter employee email: ");
		while (true) {
			email = scan.nextLine();
			if (isValidEmail(email)) {
				break;
			} else {
				System.out.println("Invalid email. Try again.");
			}
		}
		
		// Department
		while(true) {
			System.out.println("Here are our departments:");
			printDepartments();
			System.out.print("Enter department name: ");
			department = scan.nextLine();
			if (isDepartment(department)) {
				break;
			} else {
				System.out.println("Invalid department. Try again.");
			}
		}
		int deptId = departmentDB.getDepartmentByName(department).getId();
		
		//Address
		while(true) {
			System.out.print("Enter street address: ");
			streetAddress = scan.nextLine();
			if (!isValidStreetAddress(streetAddress)) {
				System.out.println("Invalid address. Please try again.");
				continue;
			}
			System.out.print("Enter city: ");
			city = scan.nextLine();
			
			System.out.print("Enter state (abbreviated): ");
			state = scan.nextLine();
			if (state.length() != 2) {
				System.out.println("Invalid state. Please try again.");
				continue;
			}
			System.out.print("Enter zipcode: ");
			try {
				zipCode = scan.nextInt();
				scan.nextLine();
			} catch (InputMismatchException e) {
				System.out.println("Invalid zipcode. Please try again.");
				continue;
			}
			
			break;
		}
		
		Address newAddress = new Address(-1, streetAddress, city, state, zipCode);
		if (!addressDB.addAddress(newAddress)) {
			System.out.println("Unable to add employee with this address.");
			return;
		}
		
		int addId = addressDB.getAddressByName(streetAddress).getAddId();
		
		
		Employee newEmployee = new Employee(-1, firstName, lastName, email, addId, deptId); // edit
		if (employeeDB.addEmployee(newEmployee)) {
			System.out.println("Employee successfully added.");
		} else {
			System.out.println("Employee could not be added.");
		}
	}
	public static void runRemoveCommand(Scanner scan) {
		System.out.println("\n[Employee Management System]\n");
		System.out.println("Removing employee...");
		System.out.print("Enter Employee ID:");
		int id = 0;
		while (true) {
			try {
				id = scan.nextInt();
				scan.nextLine();
				break;
			} catch (InputMismatchException e) {
				scan.nextLine();
				System.out.println("Incorrect input. Try again.");
			}
		}
		if (employeeDB.deleteEmployee(id)) {
			System.out.println("Employee successfully removed from the system. \n");
		} else {
			System.out.println("Employee removal failed.\n");
		}
	}
	public static void runUpdateCommand(Scanner scan) {
		System.out.println("\n[Employee Management System]\n");
		System.out.println("Updating employee...");
		System.out.print("Enter Employee ID: ");
		int id = 0;
		while (true) {
			try {
				id = scan.nextInt();
				scan.nextLine();
				break;
			} catch (InputMismatchException e) {
				scan.nextLine();
				System.out.println("Incorrect input. Try again.");
			}
		}
		Employee employee = employeeDB.getEmployeeById(id);
		if (employee == null) {
			runUpdateCommand(scan);
		}
		String field = "";
		Employee updatedEmployee = null;	
		boolean prompting = true;
		while (prompting) {
			System.out.println("\nField options: " + 
					"\n(1) first name\n(2) last name \n(3) email \n(4) department \n(5) address");
			System.out.print("Enter field to update: ");
			field = scan.nextLine();			
			
			String value = "";
			switch(field) {
				case "first name":
				case "1":
					System.out.println("Please enter the new value:");
					value = scan.nextLine();
					updatedEmployee = new Employee(
								employee.getEmployeeId(),
								value,
								employee.getEmployeeLastName(),
								employee.getEmployeeEmail(),
								employee.getAddressId(),
								employee.getDepartmentId()
							);
					if (employeeDB.updateEmployee(updatedEmployee)) {
						System.out.println("Employee successfully updated.");
						prompting = false;
					} else {
						System.out.println("Employee update failed. Please try again.");
					}
					
					break;
				case "last name":
				case "2":
					System.out.println("Please enter the new value:");
					value = scan.nextLine();
					updatedEmployee = new Employee(
							employee.getEmployeeId(),
							employee.getEmployeeFirstName(),
							value,
							employee.getEmployeeEmail(),
							employee.getAddressId(),
							employee.getDepartmentId()
						);
					if (employeeDB.updateEmployee(updatedEmployee)) {
						System.out.println("Employee successfully updated.");
						prompting = false;
					} else {
						System.out.println("Employee update failed. Please try again.");
					}
					
					break;
				case "email":
				case "3":
					System.out.println("Please enter the new value:");
					value = scan.nextLine();
					if (!isValidEmail(value)) {
						System.out.println("Invalid email format. Please try again.");
						break;
					}
					updatedEmployee = new Employee(
							employee.getEmployeeId(),
							employee.getEmployeeFirstName(),
							employee.getEmployeeLastName(),
							value,
							employee.getAddressId(),
							employee.getDepartmentId()
						);
					if (employeeDB.updateEmployee(updatedEmployee)) {
						System.out.println("Employee successfully updated.");
					} else {
						System.out.println("Employee update failed.");
					}
					prompting = false;
					break;
				case "department":
				case "4":
					System.out.println("Please enter the new value:");
					value = scan.nextLine();
				
					if (!isDepartment(value)) {
						System.out.println("Invalid department. These are the available departments:");
						printDepartments();
						break;
					}
					int departmentId = departmentDB.getDepartmentByName(value).getId();
					
					updatedEmployee = new Employee(
							employee.getEmployeeId(),
							employee.getEmployeeFirstName(),
							employee.getEmployeeLastName(),
							employee.getEmployeeEmail(),
							employee.getAddressId(),
							departmentId
						);
					if (employeeDB.updateEmployee(updatedEmployee)) {
						System.out.println("Employee successfully updated.");
						prompting = false;
					} else {
						System.out.println("Employee update failed. Please try again.");
					}
					
					break;
				case "address":
				case "5":
					//Address
					String streetAddress = "", city = "", state ="";
					int zipCode = 0;
					while(true) {
						System.out.print("Enter street address: ");
						streetAddress = scan.nextLine();
						if (!isValidStreetAddress(streetAddress)) {
							System.out.println("Invalid address. Please try again.");
							continue;
						}
						System.out.print("Enter city: ");
						city = scan.nextLine();
						
						System.out.print("Enter state (abbreviated): ");
						state = scan.nextLine();
						if (state.length() != 2) {
							System.out.println("Invalid state. Please try again.");
							continue;
						}
						System.out.print("Enter zipcode: ");
						try {
							zipCode = scan.nextInt();
							scan.nextLine();
						} catch (InputMismatchException e) {
							System.out.println("Invalid zipcode. Please try again.");
							continue;
						}
						
						break;
					}
					Address addr = new Address(-1, streetAddress, city, state, zipCode);
					addressDB.addAddress(addr);
					int addId = addressDB.getAddressByName(streetAddress).getAddId();
					updatedEmployee = new Employee(
							employee.getEmployeeId(),
							employee.getEmployeeFirstName(),
							employee.getEmployeeLastName(),
							employee.getEmployeeEmail(),
							addId,
							employee.getDepartmentId()
						);
					if (employeeDB.updateEmployee(updatedEmployee)) {
						System.out.println("Employee successfully updated.");
						prompting = false;
					} else {
						System.out.println("Employee update failed. Please try again.");
					}
					
				
				default:
					break;
			}
		}
	}
	public static void runListCommand(Scanner scan) {
		System.out.println("\n[Employee Management System]\n");
		System.out.println("Listing employee...\n");
		System.out.println("available commands are: \n(1) list all \n(2) list employee by id " +
							"\n(3) list employees by name \n(4) list employees by department name" + 
							"\n(5) list employees by address");
		String command = scan.nextLine();
		switch (command) {
		case "list all":
		case "1":
			List<Employee> allEmployees = employeeDB.getAllEmployees();
			printEmployeeList(allEmployees);
			break;
		case "list employee by id":
		case "2":
			System.out.print("Enter Employee ID: ");
			int id = 0;
			while (true ) {
				try {
					id = scan.nextInt();
					scan.nextLine();
					break;
				} catch (InputMismatchException e) {
					scan.nextLine();
					System.out.println("Incorrect input. Try again.");
				}
			}
			Employee employeeById = employeeDB.getEmployeeById(id);
			if (employeeById == null) {
				System.out.println("Employee not found.");
			} else {
				printEmployee(employeeById);
			}
			break;
		case "list employees by name":
		case "3":
			System.out.print("Enter Employee Last Name: ");
			String lastName = scan.nextLine();
			List<Employee> employeesByLastName = employeeDB.getEmployeesByLastName(lastName);
			
			if (employeesByLastName == null) {
				System.out.println("Employee not found.");
			} else {
				printEmployeeList(employeesByLastName);
			}
			break;
		case "list employees by department name":
		case "4":
			String departmentName = "";
			while (true) {
				System.out.println("These are the available departments:");
				printDepartments();
				System.out.println("Enter Department Name: ");
				departmentName = scan.nextLine();
				if (!isDepartment(departmentName)) {
					System.out.println("Invalided department.");
				} else {
					break;
				}
			}

			List<Employee> employeesByDepartmentName= employeeDB.getEmployeesByDepartmentName(departmentName);
			// Added to catch NullPointerException if user enters a Department not listed
			
			if (employeesByDepartmentName == null) {
				System.out.println("Employees not found.");
			} else {
				printEmployeeList(employeesByDepartmentName);
			}
			break;
		case "list employees by address":
		case "5":
			System.out.println("Available address are:");
			List<Address> addressList = addressDB.getAllAddress();
			printAddressList(addressList);
			int addId = 0; 
			while (true) {
				System.out.println("Enter Address Id: ");

				try {
					addId = scan.nextInt();
					scan.nextLine();
					break;
				} catch(InputMismatchException e){
					System.out.println("Incorrect input. Please enter a numeric value.");
				}
			}

			
			List<Employee> employeesByAddressId = employeeDB.getEmployeeByAddressId(addId);
			printEmployeeList(employeesByAddressId);
			break;
		default:
			System.out.println("Command not recognized. Please try again.");
			runListCommand(scan);
		}
	}
	public static void printEmployeeList(List<Employee> employeeList) {
		@SuppressWarnings("unchecked")
		ArrayList<Employee> employees = (ArrayList) employeeList;
		if (employees.isEmpty()) {
			System.out.println("No employees in the database.");
		} else {
			for (int i = 0; i < employees.size(); i++) {
				System.out.println("-----------------");
				printEmployee(employees.get(i));
			}
			System.out.println("-----------------");
		}
	}
	public static void printEmployee(Employee employee) {
		System.out.printf("%-13s \t %-15s\n","Id: ",employee.getEmployeeId());
		System.out.printf("%-13s \t %-15s\n","First Name: ",employee.getEmployeeFirstName());
		System.out.printf("%-13s \t %-15s\n", "Last Name: ", employee.getEmployeeLastName());
		System.out.printf("%-13s \t %-15s\n", "Email: ", employee.getEmployeeEmail());
		
		Address addr = addressDB.getAddressById(employee.getAddressId());
		printAddress(addr);
		Department dept =  departmentDB.getDepartmentById(employee.getDepartmentId());
		System.out.printf("%-14s  %-15s\n", "Department ID: ", employee.getDepartmentId());
		System.out.printf("%-13s \t %-15s\n", "Department: ", dept.getName());
		
		
	}
	
	public static void printAddressList(List<Address> addressList) {
		@SuppressWarnings("unchecked")
		ArrayList<Address> address = (ArrayList) addressList;
		if (address.isEmpty()) {
			System.out.println("No address in the database.");
		} else {
			for (int i = 0; i < address.size(); i++) {
				System.out.println("-----------------");
				printAddress(address.get(i));
			}
			System.out.println("-----------------");
		}
	}
	public static void printAddress(Address address) {
		System.out.println("Address Id: " + address.getAddId());
		System.out.printf(address.getStreetName());
		System.out.printf("\n" + address.getCity() + ", " + address.getState() + " " + address.getZipCode() + "\n");
	}
	
	public static boolean isDepartment(String deptName) {
		// TODO: Get all departments from departmentDatabase, and check
		List<Department> departments = departmentDB.getAllDepartments();
		Optional<Department> department = departments.stream()
				.filter(e -> e.getName().equals(deptName))
				.findFirst();
		if (department.isPresent()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void printDepartments() {
		List<Department> departments = departmentDB.getAllDepartments();
		if (departments.isEmpty()) {
			System.out.println("No employees in the database.");
		} else {
			for (int i = 0; i < departments.size(); i++) {
				System.out.printf("Id: %d   Name: %-15s \n", departments.get(i).getId(), departments.get(i).getName());
			}
		}
	}
	
	
	
	public static boolean isValidEmail(String email) {
		String emailLowerCase = email.toLowerCase();
		String regex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(emailLowerCase);
		return matcher.matches();
	}
	
	public static boolean isValidStreetAddress(String address) {
		String regex = "^(\\d+) ?([A-Za-z](?= ))? (.*?) ([^ ]+?)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(address);
		return matcher.matches();
	}
}