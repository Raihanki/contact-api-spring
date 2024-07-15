package raihanhori.spring_contact_api.service;

import java.util.List;

import raihanhori.spring_contact_api.entity.User;
import raihanhori.spring_contact_api.model.AddressResponse;
import raihanhori.spring_contact_api.model.CreateAddressRequest;
import raihanhori.spring_contact_api.model.UpdateAddressRequest;

public interface AddressService {
	
	AddressResponse create(User user, CreateAddressRequest request);
	
	AddressResponse get(User user, Integer contactId, Integer addressId);
	
	AddressResponse update(User user, UpdateAddressRequest request);
	
	void delete(User user, Integer contactId, Integer addressId);
	
	List<AddressResponse> getAll(User user, Integer contactId);
	
}
