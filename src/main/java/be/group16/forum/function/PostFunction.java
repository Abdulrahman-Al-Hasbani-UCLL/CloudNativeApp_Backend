package be.group16.forum.function;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import be.group16.forum.dto.PostInput;
import be.group16.forum.model.Post;
import be.group16.forum.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class PostFunction {
    @Autowired
    private PostService postService;

    @FunctionName("getThreadPosts")
    public HttpResponseMessage getThreadPosts(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.GET,
                    HttpMethod.OPTIONS }, route = "thread/{threadId}/posts", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            @BindingName("threadId") String threadId,
            final ExecutionContext context) {
        String allowedOrigin = System.getenv("ALLOWED_ORIGIN");

        if (request.getHttpMethod() == HttpMethod.OPTIONS) {
            return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }

        String pageParam = request.getQueryParameters().get("page");
        int pageNum = 1;
        try {
            if (pageParam != null)
                pageNum = Integer.parseInt(pageParam);
        } catch (NumberFormatException e) {
            pageNum = 1;
        }
        int pageSize = 10;
        List<Post> posts = postService.fetchThreadPosts(threadId, pageNum - 1, pageSize);
        return request.createResponseBuilder(HttpStatus.OK)
                .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                .body(Map.of("posts", posts))
                .build();
    }

    @FunctionName("getPosts")
    public HttpResponseMessage getPosts(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.GET,
                    HttpMethod.OPTIONS }, route = "posts", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,

            final ExecutionContext context) {
        String pageParam = request.getQueryParameters().get("page");
        int pageNum = 1;
        String allowedOrigin = System.getenv("ALLOWED_ORIGIN");

        if (request.getHttpMethod() == HttpMethod.OPTIONS) {
            return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }

        try {
            if (pageParam != null)
                pageNum = Integer.parseInt(pageParam);
        } catch (NumberFormatException e) {
            pageNum = 1;
        }
        int pageSize = 10;
        List<Post> posts = postService.fetchPosts(pageNum - 1, pageSize);
        return request.createResponseBuilder(HttpStatus.OK)
                .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                .body(Map.of("posts", posts))
                .build();
    }

    @FunctionName("createPost")
    public HttpResponseMessage createPost(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.POST,
                    HttpMethod.OPTIONS }, route = "post", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<PostInput>> request,
            final ExecutionContext context) {
        String allowedOrigin = System.getenv("ALLOWED_ORIGIN");

        if (request.getHttpMethod() == HttpMethod.OPTIONS) {
            return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }

        PostInput input = request.getBody().orElse(null);
        if (input == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .body("Missing post data").build();
        }
        Post post = postService.createPost(input);
        return request.createResponseBuilder(HttpStatus.OK)
                .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                .body(post).build();
    }

    @FunctionName("getPost")
    public HttpResponseMessage getPost(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.GET,
                    HttpMethod.OPTIONS }, route = "post/{id}", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            @BindingName("id") String id,
            final ExecutionContext context) {
        String allowedOrigin = System.getenv("ALLOWED_ORIGIN");

        if (request.getHttpMethod() == HttpMethod.OPTIONS) {
            return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }
        Optional<Post> post = postService.fetchPost(id);
        return post.map(p -> request.createResponseBuilder(HttpStatus.OK)
                .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                .body(p).build())
                .orElseGet(() -> request.createResponseBuilder(HttpStatus.NOT_FOUND)
                        .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                        .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                        .header("Access-Control-Allow-Headers", "Content-Type,Authorization").build());
    }

    @FunctionName("updatePost")
    public HttpResponseMessage updatePost(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.PUT,
                    HttpMethod.OPTIONS }, route = "post/{id}", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<PostInput>> request,
            @BindingName("id") String id,
            final ExecutionContext context) {
        String allowedOrigin = System.getenv("ALLOWED_ORIGIN");

        if (request.getHttpMethod() == HttpMethod.OPTIONS) {
            return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }

        PostInput input = request.getBody().orElse(null);
        if (input == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .body("Missing post data").build();
        }
        Optional<Post> updated = postService.updatePost(id, input);
        return updated.map(p -> request.createResponseBuilder(HttpStatus.OK)
                .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                .body(p).build())
                .orElseGet(() -> request.createResponseBuilder(HttpStatus.NOT_FOUND)
                        .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                        .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                        .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                        .build());
    }

    @FunctionName("deletePost")
    public HttpResponseMessage deletePost(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.DELETE,
                    HttpMethod.OPTIONS }, route = "post/{id}", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            @BindingName("id") String id,
            final ExecutionContext context) {
        String allowedOrigin = System.getenv("ALLOWED_ORIGIN");

        if (request.getHttpMethod() == HttpMethod.OPTIONS) {
            return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }

        boolean deleted = postService.deletePost(id);
        if (deleted) {
            return request.createResponseBuilder(HttpStatus.OK)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .body("Post deleted").build();
        } else {
            return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }
    }

    @FunctionName("likePost")
    public HttpResponseMessage likePost(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.POST,
                    HttpMethod.OPTIONS }, route = "posts/{postId}/like", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            @BindingName("postId") String postId,
            final ExecutionContext context) {
        String allowedOrigin = System.getenv("ALLOWED_ORIGIN");

        if (request.getHttpMethod() == HttpMethod.OPTIONS) {
            return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }
        String authHeader = request.getHeaders().get("authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return request.createResponseBuilder(HttpStatus.UNAUTHORIZED)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .body(Map.of("success", false, "message", "Missing or invalid Authorization header"))
                    .build();
        }
        String token = authHeader.substring(7);
        boolean success = postService.likePost(postId, token);
        if (success) {
            return request.createResponseBuilder(HttpStatus.OK)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .body(Map.of("success", true, "message", "Post liked"))
                    .build();
        } else {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .body(Map.of("success", false, "message", "Could not like post")).build();
        }
    }

    @FunctionName("unlikePost")
    public HttpResponseMessage unlikePost(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.DELETE,
                    HttpMethod.OPTIONS }, route = "posts/{postId}/like", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            @BindingName("postId") String postId,
            final ExecutionContext context) {
        String allowedOrigin = System.getenv("ALLOWED_ORIGIN");

        if (request.getHttpMethod() == HttpMethod.OPTIONS) {
            return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }

        String authHeader = request.getHeaders().get("authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return request.createResponseBuilder(HttpStatus.UNAUTHORIZED)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .body(Map.of("success", false, "message", "Missing or invalid Authorization header"))
                    .build();
        }
        String token = authHeader.substring(7);
        boolean success = postService.unlikePost(postId, token);
        if (success) {
            return request.createResponseBuilder(HttpStatus.OK)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .body(Map.of("success", true, "message", "Like removed"))
                    .build();
        } else {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .body(Map.of("success", false, "message", "Could not remove like")).build();
        }
    }
}
