package raihanhori.spring_contact_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import raihanhori.spring_contact_api.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	boolean existsByUsername(String username);
	
	Optional<User> findByUsername(String username);
	
	Optional<User> findFirstByToken(String token);
	
}
