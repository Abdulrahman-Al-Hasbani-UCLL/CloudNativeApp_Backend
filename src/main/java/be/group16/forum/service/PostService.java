package be.group16.forum.service;

import be.group16.forum.dto.PostInput;
import be.group16.forum.model.Like;
import be.group16.forum.model.Post;
import be.group16.forum.model.User;
import be.group16.forum.repository.LikeRepository;
import be.group16.forum.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    UserService userService;

    @Autowired
    LikeRepository likeRepository;

    public List<Post> fetchThreadPosts(String threadId, int page, int size) {
        return postRepository.findByThreadId(threadId, PageRequest.of(page, size));
    }

    public List<Post> fetchPosts(int page, int size) {
        return postRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    public Post createPost(PostInput input) {
        Post post = new Post();
        post.setBody(input.getBody());
        post.setThreadId(input.getThreadId());
        post.setUserId(input.getUserId());
        post.setParentId(input.getParentId());
        post.setCreatedAt(Instant.now().toString());
        post.setUpdatedAt(Instant.now().toString());
        return postRepository.save(post);
    }

    public Optional<Post> fetchPost(String id) {
        return postRepository.findById(id);
    }

    public Optional<Post> updatePost(String id, PostInput input) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.setBody(input.getBody());
            post.setUpdatedAt(Instant.now().toString());
            return Optional.of(postRepository.save(post));
        }
        return Optional.empty();
    }

    public boolean deletePost(String id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean likePost(String postId, String userToken) {
        String userId = userService.getUserIdFromToken(userToken);
        if (userId == null)
            return false;

        if (likeRepository.existsByPostIdAndUserId(postId, userId))
            return false;

        Like like = new Like();
        like.setPostId(postId);
        like.setUserId(userId);
        like.setDislike(false);
        like.setCreatedAt(Instant.now().toString());
        like.setUpdatedAt(Instant.now().toString());
        likeRepository.save(like);

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isPresent()) {
            // Update Post's likes
            Post post = postOpt.get();
            post.getLikes().add(like);
            post.setUpdatedAt(Instant.now().toString());
            postRepository.save(post);

            // Update reputation of the post's author
            String authorId = post.getUserId();
            User author = userService.findUserById(authorId);
            author.setReputation(author.getReputation() + 1);
            userService.updateUser(authorId, author);
        }
        return true;
    }

    public boolean unlikePost(String postId, String userToken) {
        String userId = userService.getUserIdFromToken(userToken);
        if (userId == null)
            return false;

        Optional<Like> likeOpt = likeRepository.findByPostIdAndUserId(postId, userId);
        if (likeOpt.isEmpty())
            return false;

        Like like = likeOpt.get();
        likeRepository.delete(like);

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isPresent()) {
            // Remove post likes
            Post post = postOpt.get();
            post.getLikes().removeIf(l -> l.getId().equals(like.getId()));
            post.setUpdatedAt(Instant.now().toString());
            postRepository.save(post);

            // Decrement reputation of the post's author
            String authorId = post.getUserId();
            User author = userService.findUserById(authorId);
            author.setReputation(Math.max(0, author.getReputation() - 1));
            userService.updateUser(authorId, author);
        }

        return true;
    }
}
