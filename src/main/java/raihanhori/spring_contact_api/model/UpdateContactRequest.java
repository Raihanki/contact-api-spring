package raihanhori.spring_contact_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateContactRequest {
	
	@JsonIgnore
	private Integer id;

	@Size(max = 100)
	private String firstName;
	
	@Size(max = 100)
	private String lastName;
	
	@Size(max = 100)
	private String phone;
	
	@Size(max = 100)
	@Email
	private String email;
	
}
