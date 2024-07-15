package raihanhori.spring_contact_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
	
	private Integer id;
	
	private String username;
	
	private String name;
	
}
