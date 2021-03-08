package com.cognixia.jump.jdbc.ems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
public class AddressDAOClass implements AddressDAO{
	private static Connection conn = ConnectionManager.getConnection();
	@Override
	public List<Address> getAllAddress() {
		try {
			// find all the address...
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM address");
			List<Address> addressList = new ArrayList<Address>();
			while(rs.next()) {
				// ...iterate through to get column info...
				int id = rs.getInt("address_id");
				String streetName = rs.getString("address_streetname");
				String city = rs.getString("address_city");
    			String state = rs.getString("address_state");
    			int zip = rs.getInt("address_zipcode");
				// ...then add them to a list...
				Address address = new Address(id, streetName, city, state, zip);
				addressList.add(address);
			}
			// ...and return that list once finished
			rs.close();
			stmt.close();
			return addressList;
		} catch (SQLException e) {
			System.out.println("Could not retrieve list of address from database");
		}	
		// return null just in case exception is thrown
		return null;
	}
	@Override
	public Address getAddressById(int addId) {
		try {
			// set up prepared statement to get a Address using its id
			PreparedStatement pstmt = conn.prepareStatement("select * from address where address_id = ?");
			pstmt.setInt(1, addId);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			// retrieve all column info and save it to Address object and return that object
			int id = rs.getInt("address_id");
			String streetName = rs.getString("address_streetname");
			String city = rs.getString("address_city");
			String state = rs.getString("address_state");
			int zip = rs.getInt("address_zipcode");
			Address address = new Address(id, streetName, city, state, zip);
			rs.close();
			pstmt.close();
			return address;
		} catch (SQLException e) {
			System.out.println("Address with id = " + addId + " not found.");
		}
		// if address not found, will return null
		return null;
	}
	@Override
	public Address getAddressByName(String streetName) {
		try {
		PreparedStatement pstmt = conn.prepareStatement("select * from address where address_streetname = ?");
		pstmt.setString(1, streetName);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		int id = rs.getInt("address_id");
		String streetName1 = rs.getString("address_streetname");
		String city = rs.getString("address_city");
		String state = rs.getString("address_state");
		int zip = rs.getInt("address_zipcode");
		Address address = new Address(id, streetName1, city, state, zip);
		rs.close();
		pstmt.close();
		return address;
	} catch (SQLException e) {
		System.out.println("Address with name = " + streetName + " not found.");
	}
		return null;
	}
	@Override
	public boolean addAddress(Address address) {
		try {
		PreparedStatement pstmt = conn.prepareStatement("insert into address (address_streetname, address_city, address_state, address_zipcode) values (?, ?, ?, ?)"); 
		pstmt.setString(1, address.getStreetName());
		pstmt.setString(2, address.getCity());
		pstmt.setString(3, address.getState());
		pstmt.setInt(4, address.getZipCode());
		int i = pstmt.executeUpdate();
		if(i > 0) {
			return true;
		}
		pstmt.close();
	} catch (SQLException e) {
		e.printStackTrace();
	}
		return false;
	}
	@Override
	public boolean deleteAddress(int addId) {
		try {
		PreparedStatement pstmt = conn.prepareStatement("delete from address where address_id = ?");
		pstmt.setInt(1, addId);
		int i = pstmt.executeUpdate();
		if(i > 0) {
			System.out.println("Delete was Successful");
			return true;
		}
		pstmt.close();
	} catch (SQLException e) {
		System.out.println("Address with id = " + addId + " not found.");
	}
		return false;
	}
	@Override
	public boolean updateAddress(Address address) {
		try {
		PreparedStatement pstmt = conn.prepareStatement("update address set " + 
																			"address_streetname = ?," + 
																			"address_city = ?, " +
																			"address_state = ?," + 
																			"address_zipcode = ? WHERE address_id = ?"); 
		pstmt.setString(1, address.getStreetName());
		pstmt.setString(2, address.getCity());
		pstmt.setString(3, address.getState());
		pstmt.setInt(4, address.getZipCode());;
		pstmt.setInt(5, address.getAddId());
		int i = pstmt.executeUpdate();
		if(i > 0) {
			return true;
		}
		pstmt.close();
	} catch (SQLException e) {
		e.printStackTrace();
	}
		return false;
	}
}