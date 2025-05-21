package be.group16.forum.controller;

import be.group16.forum.model.Thread;
import be.group16.forum.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private ThreadService threadService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> search(
            @RequestBody Map<String, String> payload,
            @RequestParam(defaultValue = "1") int page) {

        String query = payload.get("query");
        String type = payload.get("type");
        int pageSize = 10;

        if (type == null)
            type = "";
        if (query == null)
            query = "";

        if ("threads".equalsIgnoreCase(type)) {
            List<Thread> threads = threadService.searchThreads(query, page - 1, pageSize);
            if (threads == null)
                threads = List.of();
            return ResponseEntity.ok(Map.of(
                    "threads", threads,
                    "type", type,
                    "query", query,
                    "nextCursor", threads.size() == pageSize ? page + 1 : null));
        }

        // Add this to handle unsupported types and ensure a return value
        return ResponseEntity.badRequest().body(Map.of(
                "error", "Unsupported search type: " + type));
    }
}