package raihanhori.spring_contact_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import raihanhori.spring_contact_api.entity.Address;
import raihanhori.spring_contact_api.entity.Contact;

public interface AddressRepository extends JpaRepository<Address, Integer> {
	
	Optional<Address> findFirstByContactIdAndId(Integer contactId, Integer addressId);
	
	List<Address> findAllByContact(Contact contact);
	
}
