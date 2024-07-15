package raihanhori.spring_contact_api.service;

import org.springframework.data.domain.Page;

import raihanhori.spring_contact_api.entity.User;
import raihanhori.spring_contact_api.model.ContactResponse;
import raihanhori.spring_contact_api.model.CreateContactRequest;
import raihanhori.spring_contact_api.model.SearchContactRequest;
import raihanhori.spring_contact_api.model.UpdateContactRequest;

public interface ContactService {
	
	ContactResponse create(User user, CreateContactRequest request);
	
	ContactResponse get(User user, Integer contactId);
	
	ContactResponse update(User user, UpdateContactRequest request);
	
	void delete(User user, Integer contactId);
	
	Page<ContactResponse> search(User user, SearchContactRequest request);
	
}
