package be.group16.forum.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import be.group16.forum.model.Thread;

public interface ThreadRepository extends MongoRepository<Thread, String> {
    
}
