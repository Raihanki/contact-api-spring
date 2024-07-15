package raihanhori.spring_contact_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import raihanhori.spring_contact_api.entity.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer>, JpaSpecificationExecutor<Contact> {
	
	Optional<Contact> findFirstByUserIdAndId(Integer userId, Integer id); 
	
}
