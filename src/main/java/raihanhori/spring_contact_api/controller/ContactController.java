package raihanhori.spring_contact_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import raihanhori.spring_contact_api.entity.User;
import raihanhori.spring_contact_api.model.ApiResponse;
import raihanhori.spring_contact_api.model.ContactResponse;
import raihanhori.spring_contact_api.model.CreateContactRequest;
import raihanhori.spring_contact_api.model.PaginationResponse;
import raihanhori.spring_contact_api.model.SearchContactRequest;
import raihanhori.spring_contact_api.model.UpdateContactRequest;
import raihanhori.spring_contact_api.service.ContactService;

@RestController
public class ContactController {
	
	@Autowired
	private ContactService contactService;
	
	@PostMapping(path = "/api/contacts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResponse<ContactResponse> store(@RequestBody CreateContactRequest request, User user) {
		ContactResponse contactResponse = contactService.create(user, request);
		
		return ApiResponse.<ContactResponse>builder().data(contactResponse).build();
	}
	
	@GetMapping(path = "/api/contacts/{contactId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResponse<ContactResponse> show(@PathVariable Integer contactId, User user) {
		ContactResponse contactResponse = contactService.get(user, contactId);
		
		return ApiResponse.<ContactResponse>builder().data(contactResponse).build();
	}
	
	@PatchMapping(path = "/api/contacts/{contactId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResponse<ContactResponse> update(@PathVariable Integer contactId, @RequestBody UpdateContactRequest request, User user) {
		request.setId(contactId);
		ContactResponse contactResponse = contactService.update(user, request);
		
		return ApiResponse.<ContactResponse>builder().data(contactResponse).build();
	}
	
	@DeleteMapping(path = "/api/contacts/{contactId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResponse<String> update(@PathVariable Integer contactId, User user) {
		contactService.delete(user, contactId);
		
		return ApiResponse.<String>builder().data("OK").build();
	}
	
	@GetMapping(path = "/api/contacts", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResponse<List<ContactResponse>> searchContacts(
				@RequestParam(value = "name", required = false) String name,
				@RequestParam(value = "email", required = false) String email,
				@RequestParam(value = "phone", required = false) String phone,
				@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
				@RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
				User user
			) {
		SearchContactRequest reqeust = SearchContactRequest.builder()
				.name(name).email(email).phone(phone).page(page).size(size)
				.build();
		
		Page<ContactResponse> contacts = contactService.search(user, reqeust);
		
		return ApiResponse.<List<ContactResponse>>builder()
				.data(contacts.getContent())
				.pagination(
						PaginationResponse.builder()
							.current_page(contacts.getNumber())
							.total_page(contacts.getTotalPages())
							.size(contacts.getSize())
							.build()
				).build();
	}
	
}
