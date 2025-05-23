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

    @FunctionName("getRandomThreads")
    public HttpResponseMessage getRandomThreads(
            @HttpTrigger(name = "req", methods = { HttpMethod.GET,
                    HttpMethod.OPTIONS }, route = "threads/random", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        String allowedOrigin = System.getenv("ALLOWED_ORIGIN");

        if (request.getHttpMethod() == HttpMethod.OPTIONS) {
            return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }

        List<Thread> latestThreads = threadService.fetchRandomThreads(5);
        return request.createResponseBuilder(HttpStatus.OK)
                .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                .body(Map.of("threads", latestThreads))
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

    @FunctionName("threadById")
    public HttpResponseMessage threadById(
            @HttpTrigger(name = "req", methods = { HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE,
                    HttpMethod.OPTIONS }, route = "thread/{id}", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<ThreadInput>> request,
            @BindingName("id") String id,
            final ExecutionContext context) {

        String allowedOrigin = System.getenv("ALLOWED_ORIGIN");
        HttpMethod method = request.getHttpMethod();

        // Handle CORS preflight
        if (method == HttpMethod.OPTIONS) {
            return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }

        // GET /thread/{id}
        if (method == HttpMethod.GET) {
            Optional<Thread> thread = threadService.fetchThread(id);
            return thread.map(t -> request.createResponseBuilder(HttpStatus.OK)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .body(t).build())
                    .orElseGet(() -> request.createResponseBuilder(HttpStatus.NOT_ACCEPTABLE)
                            .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                            .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                            .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                            .body("Didn't find Thread: " + id)
                            .build());
        }

        // PUT /thread/{id}
        if (method == HttpMethod.PUT) {
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
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                        .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                        .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                        .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                        .body("Missing thread data").build();
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

        // DELETE /thread/{id}
        if (method == HttpMethod.DELETE) {
            String authHeader = request.getHeaders().get("authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return request.createResponseBuilder(HttpStatus.UNAUTHORIZED)
                        .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                        .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                        .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                        .body("Missing or invalid Authorization header").build();
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

        // Method not allowed
        return request.createResponseBuilder(HttpStatus.METHOD_NOT_ALLOWED)
                .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                .body("Method not allowed")
                .build();
    }
}
