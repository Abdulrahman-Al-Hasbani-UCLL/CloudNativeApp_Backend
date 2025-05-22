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
        @HttpTrigger(name = "req", methods = {HttpMethod.GET}, route = "threads", authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<String>> request,
        final ExecutionContext context) {

        String pageParam = request.getQueryParameters().get("page");
        int page = 1;
        try {
            if (pageParam != null) page = Integer.parseInt(pageParam);
        } catch (NumberFormatException e) {
            page = 1;
        }
        int pageSize = 10;
        List<Thread> threads = threadService.fetchThreads(page - 1, pageSize);
        return request.createResponseBuilder(HttpStatus.OK)
                .body(Map.of("threads", threads))
                .build();
    }

    @FunctionName("createThread")
    public HttpResponseMessage createThread(
        @HttpTrigger(name = "req", methods = {HttpMethod.POST}, route = "thread", authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<ThreadInput>> request,
        final ExecutionContext context) {

        String authHeader = request.getHeaders().get("authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return request.createResponseBuilder(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header").build();
        }
        String token = authHeader.substring(7);
        ThreadInput input = request.getBody().orElse(null);
        if (input == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Missing thread data").build();
        }
        Thread thread = threadService.createThread(input, token);
        return request.createResponseBuilder(HttpStatus.OK).body(thread).build();
    }

    @FunctionName("getThread")
    public HttpResponseMessage getThread(
        @HttpTrigger(name = "req", methods = {HttpMethod.GET}, route = "thread/{id}", authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<String>> request,
        @BindingName("id") String id,
        final ExecutionContext context) {

        Optional<Thread> thread = threadService.fetchThread(id);
        return thread.map(t -> request.createResponseBuilder(HttpStatus.OK).body(t).build())
                .orElseGet(() -> request.createResponseBuilder(HttpStatus.NOT_FOUND).build());
    }

    @FunctionName("updateThread")
    public HttpResponseMessage updateThread(
        @HttpTrigger(name = "req", methods = {HttpMethod.PUT}, route = "thread/{id}", authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<ThreadInput>> request,
        @BindingName("id") String id,
        final ExecutionContext context) {

        String authHeader = request.getHeaders().get("authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return request.createResponseBuilder(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header").build();
        }
        ThreadInput input = request.getBody().orElse(null);
        if (input == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Missing thread data").build();
        }
        Optional<Thread> updated = threadService.updateThread(id, input);
        return updated.map(t -> request.createResponseBuilder(HttpStatus.OK).body(t).build())
                .orElseGet(() -> request.createResponseBuilder(HttpStatus.NOT_FOUND).build());
    }

    @FunctionName("deleteThread")
    public HttpResponseMessage deleteThread(
        @HttpTrigger(name = "req", methods = {HttpMethod.DELETE}, route = "thread/{id}", authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<String>> request,
        @BindingName("id") String id,
        final ExecutionContext context) {

        String authHeader = request.getHeaders().get("authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return request.createResponseBuilder(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header").build();
        }
        boolean deleted = threadService.deleteThread(id);
        if (deleted) {
            return request.createResponseBuilder(HttpStatus.OK).body("Thread deleted").build();
        } else {
            return request.createResponseBuilder(HttpStatus.NOT_FOUND).build();
        }
    }
}
