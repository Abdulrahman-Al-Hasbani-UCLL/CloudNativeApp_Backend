package be.group16.forum.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import be.group16.forum.model.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByThreadId(String threadId, Pageable pageable);
}
