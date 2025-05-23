package be.group16.forum.function;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import be.group16.forum.dto.ThreadInput;
import be.group16.forum.model.Thread;
import be.group16.forum.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ThreadFunction {
    @Autowired
    private ThreadService threadService;

    @FunctionName("getThreads")
    public HttpResponseMessage getThreads(
            @HttpTrigger(name = "req", methods = { HttpMethod.GET,
                    HttpMethod.OPTIONS }, route = "threads", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
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
        int page = 1;
        try {
            if (pageParam != null)
                page = Integer.parseInt(pageParam);
        } catch (NumberFormatException e) {
            page = 1;
        }
        int pageSize = 10;
        List<Thread> threads = threadService.fetchThreads(page - 1, pageSize);
        return request.createResponseBuilder(HttpStatus.OK)
                .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                .body(Map.of("threads", threads))
                .build();
    }

    @FunctionName("createThread")
    public HttpResponseMessage createThread(
            @HttpTrigger(name = "req", methods = { HttpMethod.POST,
                    HttpMethod.OPTIONS }, route = "thread", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<ThreadInput>> request,
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
                    .body("Missing or invalid Authorization header").build();
        }
        String token = authHeader.substring(7);
        ThreadInput input = request.getBody().orElse(null);
        if (input == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Missing thread data").build();
        }
        Thread thread = threadService.createThread(input, token);
        return request.createResponseBuilder(HttpStatus.OK)
                .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type,Authorization").body(thread).build();
    }

    @FunctionName("getThread")
    public HttpResponseMessage getThread(
            @HttpTrigger(name = "req", methods = { HttpMethod.GET,
                    HttpMethod.OPTIONS }, route = "thread/{id}", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
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

        Optional<Thread> thread = threadService.fetchThread(id);
        return thread.map(t -> request.createResponseBuilder(HttpStatus.OK)
                .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                .body(t).build())
                .orElseGet(() -> request.createResponseBuilder(HttpStatus.NOT_FOUND)
                        .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                        .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                        .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                        .build());
    }

    @FunctionName("updateThread")
    public HttpResponseMessage updateThread(
            @HttpTrigger(name = "req", methods = { HttpMethod.PUT,
                    HttpMethod.OPTIONS }, route = "thread/{id}", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<ThreadInput>> request,
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

        String authHeader = request.getHeaders().get("authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return request.createResponseBuilder(HttpStatus.UNAUTHORIZED)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .body("Missing or invalid Authorization header").build();
        }
        ThreadInput input = request.getBody().orElse(null);
        if (input == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Missing thread data").build();
        }
        Optional<Thread> updated = threadService.updateThread(id, input);
        return updated.map(t -> request.createResponseBuilder(HttpStatus.OK)
                .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                .body(t).build())
                .orElseGet(() -> request.createResponseBuilder(HttpStatus.NOT_FOUND)
                        .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                        .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                        .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                        .build());
    }

    @FunctionName("deleteThread")
    public HttpResponseMessage deleteThread(
            @HttpTrigger(name = "req", methods = { HttpMethod.DELETE,
                    HttpMethod.OPTIONS }, route = "thread/{id}", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
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

        String authHeader = request.getHeaders().get("authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return request.createResponseBuilder(HttpStatus.UNAUTHORIZED)
                    .body("Missing or invalid Authorization header")
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }
        boolean deleted = threadService.deleteThread(id);
        if (deleted) {
            return request.createResponseBuilder(HttpStatus.OK)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .body("Thread deleted").build();
        } else {
            return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }
    }
}
