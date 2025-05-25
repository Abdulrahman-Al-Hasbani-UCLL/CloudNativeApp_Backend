package be.group16.forum.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import be.group16.forum.model.Thread;
import be.group16.forum.model.User;
import be.group16.forum.dto.ThreadInput;
import be.group16.forum.repository.ThreadRepository;
import be.group16.forum.util.JwtUtil;

@Service
public class ThreadService {
    @Autowired
    private ThreadRepository threadRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    // Fetch paginated threads
    @Cacheable(value = "threads", key = "#page")
    public List<Thread> fetchThreads(int page, int size) {
        return threadRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    // Create a new thread
    public Thread createThread(ThreadInput input, String token) {
        String username = jwtUtil.validateToken(token);
        User user = userService.findUserByUsername(username);

        Thread thread = new Thread();
        thread.setTitle(input.getTitle());
        thread.setBody(input.getBody());

        thread.setUser(user);
        return threadRepository.save(thread);
    }

    // Fetch a single thread by ID
    public Optional<Thread> fetchThread(String id) {
        return threadRepository.findById(id);
    }

    // Update a thread
    public Optional<Thread> updateThread(String id, ThreadInput input) {
        Optional<Thread> optionalThread = threadRepository.findById(id);
        if (optionalThread.isPresent()) {
            Thread thread = optionalThread.get();
            thread.setTitle(input.getTitle());
            thread.setBody(input.getBody());

            return Optional.of(threadRepository.save(thread));
        }
        return Optional.empty();
    }

    // Delete a thread
    public boolean deleteThread(String id) {
        if (threadRepository.existsById(id)) {
            threadRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Search for a Thread
    public List<Thread> searchThreads(String query, int page, int pageSize) {
        try {
            if (page < 0)
                page = 0;
            if (pageSize <= 0)
                pageSize = 10;

            if (query == null || query.trim().isEmpty()) {
                return threadRepository.findAll(PageRequest.of(page, pageSize)).getContent();
            }
            List<Thread> result = threadRepository.findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(
                    query, query, PageRequest.of(page, pageSize));
            return result != null ? result : List.of();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return List.of();
        }
    }

    public List<Thread> fetchRandomThreads(int i) {
        return threadRepository.findRandomThreads(i);
    }
}
