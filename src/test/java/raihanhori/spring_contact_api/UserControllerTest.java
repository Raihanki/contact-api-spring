package raihanhori.spring_contact_api;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import raihanhori.spring_contact_api.entity.User;
import raihanhori.spring_contact_api.model.ApiResponse;
import raihanhori.spring_contact_api.model.LoginUserRequest;
import raihanhori.spring_contact_api.model.RegisterUserRequest;
import raihanhori.spring_contact_api.model.UpdateUserRequest;
import raihanhori.spring_contact_api.model.UserResponse;
import raihanhori.spring_contact_api.repository.UserRepository;
import raihanhori.spring_contact_api.security.BCrypt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		userRepository.deleteAll();
	}

	@Test
	void testRegisterSuccess() throws Exception {
		RegisterUserRequest request = new RegisterUserRequest();
		request.setName("Raihanhori");
		request.setUsername("Raihanhori");
		request.setPassword("password");

		mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpectAll(status().isOk()).andDo(result -> {
					ApiResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
							new TypeReference<>() {
							});

					Assertions.assertEquals("OK", response.getData());
				});

	}

	@Test
	void testRegisterBadRequest() throws Exception {
		RegisterUserRequest request = new RegisterUserRequest();
		request.setName("");
		request.setUsername("");
		request.setPassword("");

		mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpectAll(status().isBadRequest())
				.andDo(result -> {
					ApiResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
							new TypeReference<>() {
							});

					Assertions.assertNotNull(response.getErrors());
				});

	}

	@Test
	void testLoginSuccess() throws JsonProcessingException, Exception {
		User user = new User();
		user.setName("test");
		user.setUsername("test");
		user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
		userRepository.save(user);

		LoginUserRequest request = new LoginUserRequest();
		request.setUsername("test");
		request.setPassword("password");

		mockMvc.perform(post("/api/users/login").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk());
	}

	@Test
	void testLoginFailed() throws JsonProcessingException, Exception {
		LoginUserRequest request = new LoginUserRequest();
		request.setUsername("test-fail");
		request.setPassword("password-fail");

		mockMvc.perform(post("/api/users/login").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void getUserUnautorized() throws Exception {
		mockMvc.perform(get("/api/users/current").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).header("X-API-TOKEN", "invalid-token"))
				.andExpect(status().isUnauthorized()).andDo(result -> {
					ApiResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
							new TypeReference<>() {
							});
					Assertions.assertNotNull(response.getErrors());
				});
	}

	@Test
	void getUserSuccess() throws Exception {
		User user = new User();
		user.setName("test");
		user.setUsername("test");
		user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
		user.setToken("test-token");
		user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);
		userRepository.save(user);

		mockMvc.perform(get("/api/users/current").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).header("X-API-TOKEN", "test-token")).andExpect(status().isOk())
				.andDo(result -> {
					ApiResponse<UserResponse> response = objectMapper
							.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
							});
					Assertions.assertNull(response.getErrors());
					Assertions.assertEquals("test", response.getData().getUsername());
				});
	}

	@Test
	void testUpdateUserUnauthorize() throws Exception {
		UpdateUserRequest updateUserRequest = new UpdateUserRequest();

		mockMvc.perform(patch("/api/users/update").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateUserRequest))
				.header("X-API-TOKEN", "invalid-token")).andExpect(status().isUnauthorized()).andDo(result -> {
					ApiResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
							new TypeReference<>() {
							});
					Assertions.assertNotNull(response.getErrors());
				});
	}

	@Test
	void testUpdateUserSuccess() throws Exception {
		User user = new User();
		user.setName("test");
		user.setUsername("test");
		user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
		user.setToken("test-token");
		user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);
		userRepository.save(user);
		
		UpdateUserRequest updateUserRequest = new UpdateUserRequest();
		updateUserRequest.setName("raihan");
		updateUserRequest.setPassword("12345678");
		
		mockMvc.perform(patch("/api/users/update").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateUserRequest))
				.header("X-API-TOKEN", "test-token")).andExpect(status().isOk()).andDo(result -> {
					ApiResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
							new TypeReference<>() {
							});
					
					Assertions.assertEquals("raihan", response.getData().getName());
					Assertions.assertNull(response.getErrors());
				});
	}
	
	@Test
	void testLogoutUnauthorized() throws Exception {
		mockMvc.perform(post("/api/users/logout").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("X-API-TOKEN", "invalid-token")).andExpect(status().isUnauthorized()).andDo(result -> {
					ApiResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
							new TypeReference<>() {
							});
					Assertions.assertNotNull(response.getErrors());
				});
	}
	
	@Test
	void testLogoutSuccess() throws Exception {
		User user = new User();
		user.setName("test");
		user.setUsername("test");
		user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
		user.setToken("test-token");
		user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);
		userRepository.save(user);
		
		mockMvc.perform(post("/api/users/logout").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("X-API-TOKEN", "test-token")).andExpect(status().isOk()).andDo(result -> {
					ApiResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
							new TypeReference<>() {
							});
					
					Assertions.assertEquals("OK", response.getData());
					Assertions.assertNull(response.getErrors());
				});
	}

}
