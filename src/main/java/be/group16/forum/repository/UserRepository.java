package be.group16.forum.repository;

import be.group16.forum.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    List<User> findByEmailVerifiedTrue();
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}