package raihanhori.spring_contact_api;

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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import raihanhori.spring_contact_api.entity.Contact;
import raihanhori.spring_contact_api.entity.User;
import raihanhori.spring_contact_api.model.ApiResponse;
import raihanhori.spring_contact_api.model.ContactResponse;
import raihanhori.spring_contact_api.model.CreateContactRequest;
import raihanhori.spring_contact_api.model.UpdateContactRequest;
import raihanhori.spring_contact_api.repository.ContactRepository;
import raihanhori.spring_contact_api.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerTest {

	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	private User user;
	
	@BeforeEach
	void setUp() {
		contactRepository.deleteAll();
		userRepository.deleteAll();
		
		user = new User();
		user.setName("test");
		user.setUsername("test");
		user.setPassword("password");
		user.setToken("test-token");
		user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);
		userRepository.save(user);
	}

	@Test
	void testCreateValidationFailed() throws Exception {
		CreateContactRequest request = new CreateContactRequest();

		mockMvc.perform(post("/api/contacts").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("X-API-TOKEN", "test-token").content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest()).andDo(result -> {
					ApiResponse<ContactResponse> response = objectMapper
							.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
							});

					Assertions.assertNotNull(response.getErrors());
				});
	}
	
	@Test
	void testCreateSuccess() throws Exception {
		CreateContactRequest request = new CreateContactRequest();
		request.setFirstName("shiina");
		request.setLastName("mahiru");
		request.setEmail("shiina@gmail.com");
		request.setPhone("123");

		mockMvc.perform(post("/api/contacts").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("X-API-TOKEN", "test-token").content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk()).andDo(result -> {
					ApiResponse<ContactResponse> response = objectMapper
							.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
							});

					Assertions.assertNull(response.getErrors());
					Assertions.assertEquals("shiina", response.getData().getFirstName());
					Assertions.assertEquals("mahiru", response.getData().getLastName());
					Assertions.assertEquals("shiina@gmail.com", response.getData().getEmail());
					Assertions.assertEquals("123", response.getData().getPhone());
				});
	}
	
	@Test
	void testGetByIdSuccess() throws Exception {
		Contact contact = new Contact();
		contact.setFirstName("shiina");
		contact.setLastName("mahiru");
		contact.setEmail("shiina@gmail.com");
		contact.setPhone("123");
		contact.setUser(user);
		contactRepository.save(contact);

		mockMvc.perform(get("/api/contacts/" + contact.getId()).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("X-API-TOKEN", "test-token"))
				.andExpect(status().isOk()).andDo(result -> {
					ApiResponse<ContactResponse> response = objectMapper
							.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
							});

					Assertions.assertNull(response.getErrors());
					Assertions.assertEquals("shiina", response.getData().getFirstName());
					Assertions.assertEquals("mahiru", response.getData().getLastName());
					Assertions.assertEquals("shiina@gmail.com", response.getData().getEmail());
					Assertions.assertEquals("123", response.getData().getPhone());
				});
	}
	
	@Test
	void testGetByIdNotFound() throws Exception {
		Contact contact = new Contact();
		contact.setFirstName("shiina");
		contact.setLastName("mahiru");
		contact.setEmail("shiina@gmail.com");
		contact.setPhone("123");
		contact.setUser(user);
		contactRepository.save(contact);

		mockMvc.perform(get("/api/contacts/10").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("X-API-TOKEN", "test-token"))
				.andExpect(status().isNotFound()).andDo(result -> {
					ApiResponse<String> response = objectMapper
							.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
							});

					Assertions.assertNotNull(response.getErrors());
				});
	}
	
	@Test
	void testUpdateSuccess() throws Exception {
		Contact contact = new Contact();
		contact.setFirstName("shiina");
		contact.setLastName("mahiru");
		contact.setEmail("shiina@gmail.com");
		contact.setPhone("123");
		contact.setUser(user);
		contactRepository.save(contact);
		
		UpdateContactRequest request = new UpdateContactRequest();
		request.setFirstName("shiina-edited");
		request.setLastName("mahiru-edited");
		request.setEmail("shiina-edited@gmail.com");
		request.setPhone("456");

		mockMvc.perform(patch("/api/contacts/" + contact.getId()).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("X-API-TOKEN", "test-token").content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk()).andDo(result -> {
					ApiResponse<ContactResponse> response = objectMapper
							.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
							});

					Assertions.assertNull(response.getErrors());
					Assertions.assertEquals("shiina-edited", response.getData().getFirstName());
					Assertions.assertEquals("mahiru-edited", response.getData().getLastName());
					Assertions.assertEquals("shiina-edited@gmail.com", response.getData().getEmail());
					Assertions.assertEquals("456", response.getData().getPhone());
				});
	}
	
	@Test
	void testUpdateFailedNotFound() throws Exception {
		Contact contact = new Contact();
		contact.setFirstName("shiina");
		contact.setLastName("mahiru");
		contact.setEmail("shiina@gmail.com");
		contact.setPhone("123");
		contact.setUser(user);
		contactRepository.save(contact);
		
		UpdateContactRequest request = new UpdateContactRequest();
		request.setFirstName("shiina-edited");
		request.setLastName("mahiru-edited");
		request.setEmail("shiina-edited@gmail.com");
		request.setPhone("456");

		mockMvc.perform(patch("/api/contacts/1").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("X-API-TOKEN", "test-token").content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isNotFound()).andDo(result -> {
					ApiResponse<String> response = objectMapper
							.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
							});

					Assertions.assertNotNull(response.getErrors());
				});
	}
	
	@Test
	void testDeleteNotFound() throws Exception {
		mockMvc.perform(delete("/api/contacts/1").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("X-API-TOKEN", "test-token"))
				.andExpect(status().isNotFound()).andDo(result -> {
					ApiResponse<String> response = objectMapper
							.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
							});

					Assertions.assertNotNull(response.getErrors());
				});
	}
	
	@Test
	void testDeleteSuccess() throws Exception {
		Contact contact = new Contact();
		contact.setFirstName("shiina");
		contact.setLastName("mahiru");
		contact.setEmail("shiina@gmail.com");
		contact.setPhone("123");
		contact.setUser(user);
		contactRepository.save(contact);

		mockMvc.perform(delete("/api/contacts/" + contact.getId()).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("X-API-TOKEN", "test-token"))
				.andExpect(status().isOk()).andDo(result -> {
					ApiResponse<String> response = objectMapper
							.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
							});

					Assertions.assertNull(response.getErrors());
					Assertions.assertEquals("OK", response.getData());
				});
	}
	
	@Test
	void searchNotFound() throws Exception {
		mockMvc.perform(get("/api/contacts").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("X-API-TOKEN", "test-token"))
				.andExpect(status().isOk()).andDo(result -> {
					ApiResponse<List<ContactResponse>> response = objectMapper
							.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
							});

					Assertions.assertNull(response.getErrors());
					Assertions.assertEquals(0, response.getData().size());
					Assertions.assertEquals(0, response.getPagination().getTotal_page());
					Assertions.assertEquals(0, response.getPagination().getCurrent_page());
					Assertions.assertEquals(10, response.getPagination().getSize());
				});
	}

}
