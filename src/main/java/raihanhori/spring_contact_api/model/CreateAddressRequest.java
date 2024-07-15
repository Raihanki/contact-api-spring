package raihanhori.spring_contact_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAddressRequest { 
	
	@JsonIgnore
	private Integer contactId;

	@Size(max = 200)
	private String street;

	@Size(max = 100)
	private String city;

	@NotBlank
	@Size(max = 100)
	private String country;

	@Size(max = 100)
	private String postalCode;

}
