package raihanhori.spring_contact_api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import raihanhori.spring_contact_api.entity.Address;
import raihanhori.spring_contact_api.entity.Contact;
import raihanhori.spring_contact_api.entity.User;
import raihanhori.spring_contact_api.model.AddressResponse;
import raihanhori.spring_contact_api.model.ApiResponse;
import raihanhori.spring_contact_api.model.CreateAddressRequest;
import raihanhori.spring_contact_api.model.UpdateAddressRequest;
import raihanhori.spring_contact_api.repository.AddressRepository;
import raihanhori.spring_contact_api.repository.ContactRepository;
import raihanhori.spring_contact_api.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerTest {

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private User user;

	private Contact contact;

	@BeforeEach
	void setUp() {
		addressRepository.deleteAll();
		contactRepository.deleteAll();
		userRepository.deleteAll();

		user = new User();
		user.setName("test");
		user.setUsername("test");
		user.setPassword("password");
		user.setToken("test-token");
		user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);
		userRepository.save(user);

		contact = new Contact();
		contact.setFirstName("shiina");
		contact.setLastName("mahiru");
		contact.setEmail("shiina@gmail.com");
		contact.setPhone("123");
		contact.setUser(user);
		contactRepository.save(contact);
	}

	@Test
	void testCreateAddressValidaationFailed() throws Exception {
		CreateAddressRequest request = new CreateAddressRequest();

		mockMvc.perform(post("/api/contacts/" + contact.getId() + "/address").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).header("X-API-TOKEN", "test-token")
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest()).andDo(result -> {
					ApiResponse<AddressResponse> response = objectMapper
							.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
							});

					Assertions.assertNotNull(response.getErrors());
				});
	}

	@Test
	void testCreateAddressSuccess() throws Exception {
		CreateAddressRequest request = new CreateAddressRequest();
		request.setStreet("soreang");
		request.setCity("bandung");
		request.setCountry("indonesia");
		request.setPostalCode("40914");

		mockMvc.perform(post("/api/contacts/" + contact.getId() + "/address").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).header("X-API-TOKEN", "test-token")
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk()).andDo(result -> {
					ApiResponse<AddressResponse> response = objectMapper
							.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
							});

					Assertions.assertNull(response.getErrors());
					Assertions.assertEquals("soreang", response.getData().getStreet());
					Assertions.assertEquals("bandung", response.getData().getCity());
					Assertions.assertEquals("indonesia", response.getData().getCountry());
					Assertions.assertEquals("40914", response.getData().getPostalCode());
				});
	}

	@Test
	void testGetAddressByIdSuccess() throws Exception {
		Address address = new Address();
		address.setStreet("soreang");
		address.setCity("bandung");
		address.setCountry("indonesia");
		address.setPostalCode("40914");
		address.setContact(contact);
		addressRepository.save(address);

		mockMvc.perform(get("/api/contacts/" + contact.getId() + "/address/" + address.getId())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("X-API-TOKEN", "test-token")).andExpect(status().isOk()).andDo(result -> {
					ApiResponse<AddressResponse> response = objectMapper
							.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
							});

					Assertions.assertNull(response.getErrors());
					Assertions.assertEquals("soreang", response.getData().getStreet());
					Assertions.assertEquals("bandung", response.getData().getCity());
					Assertions.assertEquals("indonesia", response.getData().getCountry());
					Assertions.assertEquals("40914", response.getData().getPostalCode());
				});
	}
	
	@Test
	void testGetAddressByIdNotfound() throws Exception {
		mockMvc.perform(get("/api/contacts/1/address/1")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("X-API-TOKEN", "test-token")).andExpect(status().isNotFound()).andDo(result -> {
					ApiResponse<String> response = objectMapper
							.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
							});

					Assertions.assertNotNull(response.getErrors());
				});
	}
	
	@Test
	void testUpdateAddressSuccess() throws Exception {
		Address address = new Address();
		address.setStreet("soreang");
		address.setCity("bandung");
		address.setCountry("indonesia");
		address.setPostalCode("40914");
		address.setContact(contact);
		addressRepository.save(address);
		
		UpdateAddressRequest request = new UpdateAddressRequest();
		request.setStreet("soreang-edited");
		request.setCity("bandung-edited");
		request.setCountry("indonesia-edited");
		request.setPostalCode("40915");

		mockMvc.perform(patch("/api/contacts/" + contact.getId() + "/address/" + address.getId())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.header("X-API-TOKEN", "test-token")).andExpect(status().isOk()).andDo(result -> {
					ApiResponse<AddressResponse> response = objectMapper
							.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
							});

					Assertions.assertNull(response.getErrors());
					Assertions.assertEquals("soreang-edited", response.getData().getStreet());
					Assertions.assertEquals("bandung-edited", response.getData().getCity());
					Assertions.assertEquals("indonesia-edited", response.getData().getCountry());
					Assertions.assertEquals("40915", response.getData().getPostalCode());
				});
	}
	
	@Test
	void testUpdateFailedValidation() throws Exception {
		Address address = new Address();
		address.setStreet("soreang");
		address.setCity("bandung");
		address.setCountry("indonesia");
		address.setPostalCode("40914");
		address.setContact(contact);
		addressRepository.save(address);
		
		UpdateAddressRequest request = new UpdateAddressRequest();
		request.setStreet("soreang-edited");
		request.setCity("bandung-edited");
		request.setCountry(null);
		request.setPostalCode("40915");

		mockMvc.perform(patch("/api/contacts/" + contact.getId() + "/address/" + address.getId())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.header("X-API-TOKEN", "test-token")).andExpect(status().isBadRequest()).andDo(result -> {
					ApiResponse<AddressResponse> response = objectMapper
							.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
							});

					Assertions.assertNotNull(response.getErrors());
				});
	}
	
	@Test
	void testDeleteAddressSuccess() throws Exception {
		Address address = new Address();
		address.setStreet("soreang");
		address.setCity("bandung");
		address.setCountry("indonesia");
		address.setPostalCode("40914");
		address.setContact(contact);
		addressRepository.save(address);

		mockMvc.perform(delete("/api/contacts/" + contact.getId() + "/address/" + address.getId())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("X-API-TOKEN", "test-token")).andExpect(status().isOk()).andDo(result -> {
					ApiResponse<String> response = objectMapper
							.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
							});

					Assertions.assertNull(response.getErrors());
					Assertions.assertEquals("OK", response.getData());
				});
	}
	
	@Test
	void testDeleteAddressNotFound() throws Exception {
		mockMvc.perform(delete("/api/contacts/1/address/1")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("X-API-TOKEN", "test-token")).andExpect(status().isNotFound()).andDo(result -> {
					ApiResponse<String> response = objectMapper
							.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
							});

					Assertions.assertNotNull(response.getErrors());
				});
	}
	
	@Test
	void testGetAllAddressByContact() throws Exception {
		Address address = new Address();
		address.setStreet("soreang");
		address.setCity("bandung");
		address.setCountry("indonesia");
		address.setPostalCode("40914");
		address.setContact(contact);
		addressRepository.save(address);

		mockMvc.perform(get("/api/contacts/" + contact.getId() + "/address")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("X-API-TOKEN", "test-token")).andExpect(status().isOk()).andDo(result -> {
					ApiResponse<List<AddressResponse>> response = objectMapper
							.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
							});

					Assertions.assertNull(response.getErrors());
					Assertions.assertEquals(1, response.getData().size());
				});
	}
 
}
