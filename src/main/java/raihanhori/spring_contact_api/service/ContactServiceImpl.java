package raihanhori.spring_contact_api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import raihanhori.spring_contact_api.entity.Contact;
import raihanhori.spring_contact_api.entity.User;
import raihanhori.spring_contact_api.helper.ValidationHelper;
import raihanhori.spring_contact_api.model.ContactResponse;
import raihanhori.spring_contact_api.model.CreateContactRequest;
import raihanhori.spring_contact_api.model.SearchContactRequest;
import raihanhori.spring_contact_api.model.UpdateContactRequest;
import raihanhori.spring_contact_api.repository.ContactRepository;

@Service
public class ContactServiceImpl implements ContactService {

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private ValidationHelper validationHelper;

	private ContactResponse toContactResponse(Contact contact) {
		return ContactResponse.builder().id(contact.getId()).firstName(contact.getFirstName())
				.lastName(contact.getLastName()).email(contact.getEmail()).phone(contact.getPhone()).build();
	}

	@Transactional
	@Override
	public ContactResponse create(User user, CreateContactRequest request) {
		validationHelper.validate(request);

		Contact contact = new Contact();
		contact.setFirstName(request.getFirstName());
		contact.setLastName(request.getLastName());
		contact.setPhone(request.getPhone());
		contact.setEmail(request.getEmail());
		contact.setUser(user);

		contactRepository.save(contact);

		return toContactResponse(contact);
	}

	@Override
	public ContactResponse get(User user, Integer contactId) {
		Contact contactResult = contactRepository.findFirstByUserIdAndId(user.getId(), contactId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found"));

		return toContactResponse(contactResult);
	}

	@Transactional
	@Override
	public ContactResponse update(User user, UpdateContactRequest request) {
		validationHelper.validate(request);

		contactRepository.findFirstByUserIdAndId(user.getId(), request.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found"));

		Contact contact = new Contact();
		contact.setFirstName(request.getFirstName());
		contact.setLastName(request.getLastName());
		contact.setPhone(request.getPhone());
		contact.setEmail(request.getEmail());
		contact.setId(request.getId());
		contact.setUser(user);

		contactRepository.save(contact);

		return toContactResponse(contact);
	}

	@Transactional
	@Override
	public void delete(User user, Integer contactId) {
		Contact contact = contactRepository.findFirstByUserIdAndId(user.getId(), contactId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found"));

		contactRepository.delete(contact);
	}

	@Override
	public Page<ContactResponse> search(User user, SearchContactRequest request) {
		Specification<Contact> specification = (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(builder.equal(root.get("user"), user));
			if (Objects.nonNull(request.getName())) {
				predicates.add(builder.or(
						builder.like(root.get("firstName"), "%"+request.getName()+"%"),
						builder.like(root.get("lastName"), "%"+request.getName()+"%")
				));
			}
			
			if (Objects.nonNull(request.getEmail())) {
				predicates.add(builder.like(root.get("email"), "%"+request.getEmail()+"%"));
			}
			
			if (Objects.nonNull(request.getPhone())) {
				predicates.add(builder.like(root.get("phone"), "%"+request.getPhone()+"%"));
			}
			
			return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
		};
		
		Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
		Page<Contact> contacts = contactRepository.findAll(specification, pageable);
		
		List<ContactResponse> contactResponse = contacts.getContent().stream()
				.map(contact -> toContactResponse(contact)).toList();
		
		return new PageImpl<>(contactResponse, pageable, contacts.getTotalElements());
	}

}
