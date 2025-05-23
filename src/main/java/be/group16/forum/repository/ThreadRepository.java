package be.group16.forum.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import be.group16.forum.model.Thread;
import org.springframework.data.mongodb.repository.Aggregation;

@Repository
public interface ThreadRepository extends MongoRepository<Thread, String> {
    List<Thread> findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(String title, String body,
            Pageable pageable);

    Optional<Thread> findByTitle(String title);

    @Aggregation(pipeline = { "{ $sample: { size: ?0 } }" })
    List<Thread> findRandomThreads(int size);
}
