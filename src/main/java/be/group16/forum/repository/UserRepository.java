package be.group16.forum.repository;

import be.group16.forum.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    List<User> findByEmailVerifiedTrue();
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}