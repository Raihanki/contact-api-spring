package raihanhori.spring_contact_api.service;

import raihanhori.spring_contact_api.model.LoginUserResponse;
import raihanhori.spring_contact_api.entity.User;
import raihanhori.spring_contact_api.model.LoginUserRequest;
import raihanhori.spring_contact_api.model.RegisterUserRequest;
import raihanhori.spring_contact_api.model.UpdateUserRequest;
import raihanhori.spring_contact_api.model.UserResponse;

public interface UserService {
	
	void register(RegisterUserRequest request);
	
	LoginUserResponse login(LoginUserRequest request);
	
	UserResponse getAuthenticatedUser(User user);
	
	UserResponse updateUser(User user, UpdateUserRequest request);
	
	void logout(User user);
	
}
