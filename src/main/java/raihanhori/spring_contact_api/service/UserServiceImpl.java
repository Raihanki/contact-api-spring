package raihanhori.spring_contact_api.service;

import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import raihanhori.spring_contact_api.entity.User;
import raihanhori.spring_contact_api.helper.ValidationHelper;
import raihanhori.spring_contact_api.model.LoginUserRequest;
import raihanhori.spring_contact_api.model.LoginUserResponse;
import raihanhori.spring_contact_api.model.RegisterUserRequest;
import raihanhori.spring_contact_api.model.UpdateUserRequest;
import raihanhori.spring_contact_api.model.UserResponse;
import raihanhori.spring_contact_api.repository.UserRepository;
import raihanhori.spring_contact_api.security.BCrypt;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ValidationHelper validationHelper;

	@Override
	@Transactional
	public void register(RegisterUserRequest request) {
		validationHelper.validate(request);
		
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username already registered");
		}
		
		User user = new User();
		user.setName(request.getName());
		user.setUsername(request.getUsername());
		user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
		userRepository.save(user);
	}

	@Transactional
	@Override
	public LoginUserResponse login(LoginUserRequest request) {
		validationHelper.validate(request);
		
		User user = userRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "wrong username or password"));
		
		if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
			user.setToken(UUID.randomUUID().toString());
			user.setTokenExpiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 30));
			userRepository.save(user);
			
			return LoginUserResponse.builder()
					.token(user.getToken())
					.expiredAt(user.getTokenExpiredAt())
					.build();
		} else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "wrong username or password");
		}
	}

	@Override
	public UserResponse getAuthenticatedUser(User user) {
		return UserResponse.builder().username(user.getUsername()).name(user.getName()).id(user.getId()).build();
	}

	@Transactional
	@Override
	public UserResponse updateUser(User user, UpdateUserRequest request) {
		validationHelper.validate(request);
		
		if (Objects.nonNull(request.getName())) {
			user.setName(request.getName());
		}
		
		if (Objects.nonNull(request.getPassword())) {
			user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
		}
		
		userRepository.save(user);
		
		return UserResponse.builder().username(user.getUsername()).name(user.getName()).id(user.getId()).build();
	}

	@Override
	public void logout(User user) {
		user.setToken(null);
		user.setTokenExpiredAt(null);
		
		userRepository.save(user);
	}

}
