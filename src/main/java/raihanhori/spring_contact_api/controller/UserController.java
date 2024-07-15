package raihanhori.spring_contact_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import raihanhori.spring_contact_api.entity.User;
import raihanhori.spring_contact_api.model.ApiResponse;
import raihanhori.spring_contact_api.model.LoginUserRequest;
import raihanhori.spring_contact_api.model.LoginUserResponse;
import raihanhori.spring_contact_api.model.RegisterUserRequest;
import raihanhori.spring_contact_api.model.UpdateUserRequest;
import raihanhori.spring_contact_api.model.UserResponse;
import raihanhori.spring_contact_api.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping(path = "/api/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResponse<String> register(@RequestBody RegisterUserRequest request) {
		userService.register(request);
		
		return ApiResponse.<String>builder().data("OK").build();
	}
	
	@PostMapping(path = "/api/users/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResponse<LoginUserResponse> login(@RequestBody LoginUserRequest request) {
		LoginUserResponse response = userService.login(request);
		
		return ApiResponse.<LoginUserResponse>builder().data(response).build();
	}
	
	@GetMapping(path = "/api/users/current", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResponse<UserResponse> show(User user) {
		UserResponse userResponse = userService.getAuthenticatedUser(user);
		
		return ApiResponse.<UserResponse>builder().data(userResponse).build();
	}
	
	@PatchMapping(path = "/api/users/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResponse<UserResponse> udpate(@RequestBody UpdateUserRequest request, User user) {
		UserResponse userResponse = userService.updateUser(user, request);
				
		return ApiResponse.<UserResponse>builder().data(userResponse).build();
	}
	
	@PostMapping(path = "/api/users/logout", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResponse<String> logout(User user) {
		userService.logout(user);
		
		return ApiResponse.<String>builder().data("OK").build();
	}
}
