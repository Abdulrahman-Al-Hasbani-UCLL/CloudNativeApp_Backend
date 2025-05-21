package be.group16.forum.controller;

import be.group16.forum.dto.PostInput;
import be.group16.forum.model.Post;
import be.group16.forum.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;

    // GET /thread/{threadId}/posts?page=1
    @GetMapping("/thread/{threadId}/posts")
    public ResponseEntity<?> getThreadPosts(
            @PathVariable String threadId,
            @RequestParam(defaultValue = "1") int page) {
        int pageSize = 10;
        List<Post> posts = postService.fetchThreadPosts(threadId, page - 1, pageSize);
        return ResponseEntity.ok().body(java.util.Map.of("posts", posts));
    }

    // GET /posts?page=1
    @GetMapping("/posts")
    public ResponseEntity<?> getPosts(@RequestParam(defaultValue = "1") int page) {
        int pageSize = 10;
        List<Post> posts = postService.fetchPosts(page - 1, pageSize);
        return ResponseEntity.ok().body(java.util.Map.of("posts", posts));
    }

    // POST /post
    @PostMapping("/post")
    public ResponseEntity<?> createPost(@RequestBody PostInput input) {
        Post post = postService.createPost(input);
        return ResponseEntity.ok(post);
    }

    // GET /post/{id}
    @GetMapping("/post/{id}")
    public ResponseEntity<?> getPost(@PathVariable String id) {
        Optional<Post> post = postService.fetchPost(id);
        return post.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /post/{id}
    @PutMapping("/post/{id}")
    public ResponseEntity<?> updatePost(@PathVariable String id, @RequestBody PostInput input) {
        Optional<Post> updated = postService.updatePost(id, input);
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /post/{id}
    @DeleteMapping("/post/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id) {
        boolean deleted = postService.deletePost(id);
        if (deleted) {
            return ResponseEntity.ok().body("Post deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /posts/{postId}/like
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable String postId, @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "Missing or invalid Authorization header"));
        }
        String token = authHeader.substring(7);
        boolean success = postService.likePost(postId, token);
        if (success) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Post liked"));
        } else {
            return ResponseEntity.status(400).body(Map.of("success", false, "message", "Could not like post"));
        }
    }

    // DELETE /posts/{postId}/like
    @DeleteMapping("/posts/{postId}/like")
    public ResponseEntity<?> unlikePost(@PathVariable String postId,
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "Missing or invalid Authorization header"));
        }
        String token = authHeader.substring(7);
        boolean success = postService.unlikePost(postId, token);
        if (success) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Like removed"));
        } else {
            return ResponseEntity.status(400).body(Map.of("success", false, "message", "Could not remove like"));
        }
    }
}
