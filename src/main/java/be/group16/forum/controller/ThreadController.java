package be.group16.forum.controller;

import be.group16.forum.dto.ThreadInput;
import be.group16.forum.model.Thread;
import be.group16.forum.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ThreadController {

    @Autowired
    private ThreadService threadService;

    // GET /threads?page=1
    @GetMapping("/threads")
    public ResponseEntity<?> getThreads(@RequestParam(defaultValue = "1") int page) {
        int pageSize = 10; // or any default page size you want
        List<Thread> threads = threadService.fetchThreads(page - 1, pageSize);
        return ResponseEntity.ok().body(
                java.util.Map.of("threads", threads));
    }

    // POST /thread
    @PostMapping("/thread")
    public ResponseEntity<?> createThread(
            @RequestBody ThreadInput input,
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        Thread thread = threadService.createThread(input, token);
        return ResponseEntity.ok(thread);
    }

    // GET /thread/{id}
    @GetMapping("/thread/{id}")
    public ResponseEntity<?> getThread(@PathVariable String id) {
        Optional<Thread> thread = threadService.fetchThread(id);
        return thread.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /thread/{id}
    @PutMapping("/thread/{id}")
    public ResponseEntity<?> updateThread(
            @PathVariable String id,
            @RequestBody ThreadInput input,
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        Optional<Thread> updated = threadService.updateThread(id, input);
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /thread/{id}
    @DeleteMapping("/thread/{id}")
    public ResponseEntity<?> deleteThread(
            @PathVariable String id,
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        boolean deleted = threadService.deleteThread(id);
        if (deleted) {
            return ResponseEntity.ok().body("Thread deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}