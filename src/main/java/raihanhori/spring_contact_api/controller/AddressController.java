package raihanhori.spring_contact_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import raihanhori.spring_contact_api.entity.User;
import raihanhori.spring_contact_api.model.AddressResponse;
import raihanhori.spring_contact_api.model.ApiResponse;
import raihanhori.spring_contact_api.model.CreateAddressRequest;
import raihanhori.spring_contact_api.model.UpdateAddressRequest;
import raihanhori.spring_contact_api.service.AddressService;

@RestController
public class AddressController {

	@Autowired
	private AddressService addressService;
	
	@PostMapping(path = "/api/contacts/{contactId}/address", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResponse<AddressResponse> store(@RequestBody CreateAddressRequest request, @PathVariable(value = "contactId") Integer contactId, User user) {
		request.setContactId(contactId);
		
		AddressResponse addressResponse = addressService.create(user, request);
		
		return ApiResponse.<AddressResponse>builder().data(addressResponse).build();
	}
	
	@GetMapping(path = "/api/contacts/{contactId}/address/{addressId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResponse<AddressResponse> get(@PathVariable(value = "contactId") Integer contactId, @PathVariable(value = "addressId") Integer addressId, User user) {
		AddressResponse addressResponse = addressService.get(user, contactId, addressId);
		
		return ApiResponse.<AddressResponse>builder().data(addressResponse).build();
	}
	
	@PatchMapping(path = "/api/contacts/{contactId}/address/{addressId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResponse<AddressResponse> update(@RequestBody UpdateAddressRequest request, @PathVariable(value = "contactId") Integer contactId, @PathVariable(value = "addressId") Integer addressId, User user) {
		request.setId(addressId);
		request.setContactId(contactId);
		
		AddressResponse addressResponse = addressService.update(user, request);
		
		return ApiResponse.<AddressResponse>builder().data(addressResponse).build();
	}
	
	@DeleteMapping(path = "/api/contacts/{contactId}/address/{addressId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResponse<String> delete(@PathVariable(value = "contactId") Integer contactId, @PathVariable(value = "addressId") Integer addressId, User user) {
		addressService.delete(user, contactId, addressId);
		
		return ApiResponse.<String>builder().data("OK").build();
	}
	
	@GetMapping(path = "/api/contacts/{contactId}/address", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResponse<List<AddressResponse>> delete(@PathVariable(value = "contactId") Integer contactId, User user) {
		List<AddressResponse> addressResponse = addressService.getAll(user, contactId);
		
		return ApiResponse.<List<AddressResponse>>builder().data(addressResponse).build();
	}
	
}
