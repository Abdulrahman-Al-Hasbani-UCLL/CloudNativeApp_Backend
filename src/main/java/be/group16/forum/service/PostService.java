package be.group16.forum.service;

import be.group16.forum.dto.PostInput;
import be.group16.forum.model.Post;
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
}
