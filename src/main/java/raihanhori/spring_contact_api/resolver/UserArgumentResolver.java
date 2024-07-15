package raihanhori.spring_contact_api.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import raihanhori.spring_contact_api.entity.User;
import raihanhori.spring_contact_api.repository.UserRepository;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return User.class.equals(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		String token = request.getHeader("X-API-TOKEN");
		if (token == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");
		}
		
		User user = userRepository.findFirstByToken(token).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized"));
		if (user.getTokenExpiredAt() < System.currentTimeMillis()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");
		}
		
		return user;
	}

}
