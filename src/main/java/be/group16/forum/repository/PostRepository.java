package be.group16.forum.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import be.group16.forum.model.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByThreadId(String threadId, Pageable pageable);

    Optional<Post> findByBodyAndThreadId(String postBody, String id);

    @Aggregation(pipeline = { "{ $sample: { size: ?0 } }" })
    List<Post> findRandomPosts(int size);
}
