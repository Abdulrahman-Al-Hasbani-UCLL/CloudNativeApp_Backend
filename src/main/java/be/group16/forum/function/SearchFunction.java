package be.group16.forum.function;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import be.group16.forum.model.Thread;
import be.group16.forum.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class SearchFunction {
    @Autowired
    private ThreadService threadService;

    @FunctionName("search")
    public HttpResponseMessage search(
            @HttpTrigger(name = "req", methods = { HttpMethod.POST,
                    HttpMethod.OPTIONS }, route = "search", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<Map<String, String>>> request,
            final ExecutionContext context) {
        String allowedOrigin = System.getenv("ALLOWED_ORIGIN");

        if (request.getHttpMethod() == HttpMethod.OPTIONS) {
            return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }

        Map<String, String> payload = request.getBody().orElse(Map.of());
        String pageParam = request.getQueryParameters().get("page");
        int page = 1;
        try {
            if (pageParam != null)
                page = Integer.parseInt(pageParam);
        } catch (NumberFormatException e) {
            page = 1;
        }

        String query = payload.getOrDefault("query", "");
        String type = payload.getOrDefault("type", "");
        int pageSize = 10;

        if ("threads".equalsIgnoreCase(type)) {
            List<Thread> threads = threadService.searchThreads(query, page - 1, pageSize);
            if (threads == null)
                threads = List.of();
            return request.createResponseBuilder(HttpStatus.OK)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .body(Map.of(
                            "threads", threads,
                            "type", type,
                            "query", query,
                            "nextCursor", threads.size() == pageSize ? page + 1 : null))
                    .build();
        }

        return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                .body(Map.of("error", "Unsupported search type: " + type))
                .build();
    }
}
