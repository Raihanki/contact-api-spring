package raihanhori.spring_contact_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginUserResponse {

	private String token;
	
	private Long expiredAt;
	
}
