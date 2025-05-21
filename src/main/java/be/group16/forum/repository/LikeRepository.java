package be.group16.forum.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import be.group16.forum.model.Like;

public interface LikeRepository extends MongoRepository<Like,String>{
    boolean existsByPostIdAndUserId(String postId, String userId);
    Optional<Like> findByPostIdAndUserId(String postId, String userId);
}
