package raihanhori.spring_contact_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import raihanhori.spring_contact_api.entity.Address;
import raihanhori.spring_contact_api.entity.Contact;
import raihanhori.spring_contact_api.entity.User;
import raihanhori.spring_contact_api.helper.ValidationHelper;
import raihanhori.spring_contact_api.model.AddressResponse;
import raihanhori.spring_contact_api.model.CreateAddressRequest;
import raihanhori.spring_contact_api.model.UpdateAddressRequest;
import raihanhori.spring_contact_api.repository.AddressRepository;
import raihanhori.spring_contact_api.repository.ContactRepository;

@Service
public class AddressServiceImpl implements AddressService {
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private ValidationHelper validationHelper;
	
	private AddressResponse toAddressResponse(Address address) {
		return AddressResponse.builder()
				.street(address.getStreet())
				.city(address.getCity())
				.country(address.getCountry())
				.postalCode(address.getPostalCode())
				.id(address.getId())
				.build();
	}

	@Transactional
	@Override
	public AddressResponse create(User user, CreateAddressRequest request) {
		validationHelper.validate(request);
		
		Contact contact = contactRepository.findFirstByUserIdAndId(user.getId(), request.getContactId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found"));
		
		Address address = new Address();
		address.setStreet(request.getStreet());
		address.setCity(request.getCity());
		address.setCountry(request.getCountry());
		address.setPostalCode(request.getPostalCode());
		address.setContact(contact);
		addressRepository.save(address);
		
		return toAddressResponse(address);
	}

	@Override
	public AddressResponse get(User user, Integer contactId, Integer addressId) {
		Contact contact = contactRepository.findFirstByUserIdAndId(user.getId(), contactId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found"));
		
		Address address = addressRepository.findFirstByContactIdAndId(contact.getId(), addressId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "address not found"));
		
		return toAddressResponse(address);
	}

	@Transactional
	@Override
	public AddressResponse update(User user, UpdateAddressRequest request) {
		validationHelper.validate(request);
		
		Contact contact = contactRepository.findFirstByUserIdAndId(user.getId(), request.getContactId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found"));
		
		Address address = addressRepository.findFirstByContactIdAndId(contact.getId(), request.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "address not found"));
		
		address.setStreet(request.getStreet());
		address.setCity(request.getCity());
		address.setCountry(request.getCountry());
		address.setPostalCode(request.getPostalCode());
		address.setContact(contact);
		addressRepository.save(address);
		
		return toAddressResponse(address);
	}

	@Transactional
	@Override
	public void delete(User user, Integer contactId, Integer addressId) {
		Contact contact = contactRepository.findFirstByUserIdAndId(user.getId(), contactId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found"));
		
		Address address = addressRepository.findFirstByContactIdAndId(contact.getId(), addressId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "address not found"));
		
		addressRepository.delete(address);
	}

	@Override
	public List<AddressResponse> getAll(User user, Integer contactId) {
		Contact contact = contactRepository.findFirstByUserIdAndId(user.getId(), contactId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found"));
		
		List<Address> addresses = addressRepository.findAllByContact(contact);
		List<AddressResponse> addressResponse = addresses.stream().map(this::toAddressResponse).toList();
		
		return addressResponse;
	}

}
