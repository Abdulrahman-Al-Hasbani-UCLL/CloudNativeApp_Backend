package be.group16.forum.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {

    @PostMapping("/{query}")
    public ResponseEntity<Map<String, Object>> search(
            @PathVariable String query,
            @RequestParam String type,
            @RequestBody(required = false) Map<String, Object> body) {

        // Mock response for demonstration purposes the other things have to be finished so that this can work
        Map<String, Object> response = Map.of(
                "type", type,
                "query", query,
                "tags", Map.of(),
                "posts", Map.of(),
                "threads", Map.of(),
                "users", Map.of(),
                "nextCursor", "string"
        );

        return ResponseEntity.ok(response);
    }
}