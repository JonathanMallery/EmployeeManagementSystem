package com.cognixia.jump.jdbc.ems;

import java.util.List;
public interface AddressDAO {
	public List<Address> getAllAddress();
    public Address getAddressById(int addId);
    public Address getAddressByName(String streetName);
    public boolean addAddress(Address address);
    public boolean deleteAddress(int addId);
    public boolean updateAddress(Address address);
}