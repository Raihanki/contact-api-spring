package raihanhori.spring_contact_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactResponse {
	
	private Integer id;
	
	private String firstName;
	
	private String lastName;
	
	private String phone;
	
	private String email;
}
