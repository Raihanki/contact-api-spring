package raihanhori.spring_contact_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginationResponse {
	
	private Integer current_page;
	
	private Integer total_page;
	
	private Integer size;
	
}
