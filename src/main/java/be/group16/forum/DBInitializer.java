package be.group16.forum;

import be.group16.forum.model.User;
import be.group16.forum.model.Thread;
import be.group16.forum.model.Post;
import be.group16.forum.repository.UserRepository;
import be.group16.forum.service.PostService;
import be.group16.forum.service.ThreadService;
import be.group16.forum.service.UserService;
import be.group16.forum.repository.ThreadRepository;
import be.group16.forum.repository.LikeRepository;
import be.group16.forum.repository.PostRepository;
import be.group16.forum.repository.RoleRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Component
public class DBInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ThreadRepository threadRepository;
    private final PostRepository postRepository;
    private final RoleRepository roleRepository;
    private final LikeRepository likeRepository;

    private final UserService userService;
    @SuppressWarnings("unused")
    private final ThreadService threadService; // Maybe needed later
    @SuppressWarnings("unused")
    private final PostService postService;  // Maybe needed later

    public DBInitializer(UserRepository userRepository, ThreadRepository threadRepository,
            PostRepository postRepository, UserService userService, ThreadService threadService,
            PostService postService, RoleRepository roleRepository, LikeRepository likeRepository) {
        this.userRepository = userRepository;
        this.threadRepository = threadRepository;
        this.postRepository = postRepository;
        this.userService = userService;
        this.threadService = threadService;
        this.postService = postService;
        this.roleRepository = roleRepository;
        this.likeRepository = likeRepository;
    }

    @Override
    public void run(String... args) {
        System.out.print("\n\n\n\n\n\nDo you want to clear and re-initialize the database? (yes/no): ");
        String input = null;
        System.out.flush();
        try {
            java.util.concurrent.Callable<String> inputTask = () -> {
                try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
                    return scanner.nextLine().trim().toLowerCase();
                }
            };
            java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();
            java.util.concurrent.Future<String> future = executor.submit(inputTask);
            try {
                input = future.get(10, java.util.concurrent.TimeUnit.SECONDS);
            } catch (java.util.concurrent.TimeoutException e) {
                System.out.println("\nNo input received in 10 seconds. Proceeding with database initialization.");
                input = "yes";
            } finally {
                executor.shutdownNow();
            }
        } catch (Exception e) {
            System.out.println("Could not read input. Skipping database initialization.");
            return;
        }
        if (!"yes".equals(input)) {
            System.out.println("Database initialization skipped.");
            return;
        } else {
            // Clear existing data
            System.out.println("Database Clearing started");
            postRepository.deleteAll();
            threadRepository.deleteAll();
            userRepository.deleteAll();
            roleRepository.deleteAll();
            System.out.println("Database cleared!");
            // Continue with the shit
        }

        // 1. Create 20 realistic users
        String[] names = {
                "Alice", "Bob", "Charlie", "Diana", "Eve", "Frank", "Grace", "Heidi", "Ivan", "Judy",
                "Karl", "Laura", "Mallory", "Niaj", "Olivia", "Peggy", "Quentin", "Rupert", "Sybil", "Trent"
        };
        List<User> users = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            String username = names[i].toLowerCase();
            String email = username + "@cloudforum.com";
            if (userRepository.findByUsername(username).isPresent() || userRepository.findByEmail(email).isPresent()) {
                users.add(userRepository.findByUsername(username).get());
                continue;
            }
            User user = new User();
            user.setUsername(username);
            user.setPassword("Password123!");
            user.setEmail(email);
            user.setEmailVerified(true);
            user.setDisplayName(names[i]);
            user.addRole("USER");
            users.add(userService.registerUser(user));
        }
        System.out.println("Users created");

        // 2. Create 15 threads with realistic titles and bodies
        String[] threadTitles = {
                "Best Practices for AWS VPC Design",
                "Kubernetes Networking Deep Dive",
                "Securing Cloud Storage Buckets",
                "Terraform vs Pulumi: IaC Showdown",
                "Serverless Architectures Explained",
                "Hybrid Cloud: Pros and Cons",
                "Disaster Recovery in the Cloud",
                "Cost Optimization Strategies for Azure",
                "Multi-Cloud Deployments: Pitfalls",
                "Monitoring Cloud Infrastructure",
                "CI/CD Pipelines for Cloud Apps",
                "Cloud Identity and Access Management",
                "Scaling Databases in the Cloud",
                "Cloud Migration War Stories",
                "Zero Trust Security in Cloud Environments"
        };
        String[] threadBodies = {
                "Let's discuss how to design secure and scalable VPCs on AWS. What are your go-to patterns?",
                "Share your experience with Kubernetes networking, CNI plugins, and troubleshooting tips.",
                "How do you secure S3 buckets or GCS buckets? Any automation tips?",
                "Which IaC tool do you prefer and why? Share your real-world use cases.",
                "What are the main benefits and challenges of going serverless?",
                "Anyone running hybrid cloud? What are the biggest challenges you've faced?",
                "How do you plan for disaster recovery in cloud-native environments?",
                "Share your best tips for reducing Azure cloud costs without sacrificing performance.",
                "What are the common pitfalls when deploying across multiple cloud providers?",
                "What tools and strategies do you use for monitoring cloud resources?",
                "How do you set up robust CI/CD pipelines for cloud-native applications?",
                "What are the best practices for managing IAM roles and permissions?",
                "How do you scale databases in AWS, Azure, or GCP? Share your architectures.",
                "Share your cloud migration stories—what went well, what didn't?",
                "How are you implementing Zero Trust in your cloud infrastructure?"
        };

        List<Thread> threads = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < threadTitles.length; i++) {
            String title = threadTitles[i];
            if (threadRepository.findByTitle(title).isPresent()) {
                threads.add(threadRepository.findByTitle(title).get());
                continue;
            }
            User author = users.get(rand.nextInt(users.size()));
            Thread thread = new Thread();
            thread.setTitle(title);
            thread.setBody(threadBodies[i]);
            thread.setUser(author);
            threadRepository.save(thread);
            threads.add(thread);
        }
        System.out.println("Threads created");

        // 3. Create posts for each thread (random amount, some threads with none)
        List<Post> allPosts = new ArrayList<>();
        for (Thread thread : threads) {
            int postCount = rand.nextInt(8);
            Set<Integer> usedUserIndexes = new HashSet<>();
            for (int j = 0; j < postCount; j++) {
                int userIdx;
                do {
                    userIdx = rand.nextInt(users.size());
                } while (usedUserIndexes.contains(userIdx) && usedUserIndexes.size() < users.size());
                usedUserIndexes.add(userIdx);

                User poster = users.get(userIdx);
                String postBody = "Reply " + (j + 1) + " to '" + thread.getTitle() + "' by " + poster.getDisplayName() +
                        ": " + "Here's my experience with this topic...";
                if (postRepository.findByBodyAndThreadId(postBody, thread.getId()).isPresent()) {
                    allPosts.add(postRepository.findByBodyAndThreadId(postBody, thread.getId()).get());
                    continue;
                }
                Post post = new Post();
                post.setBody(postBody);
                post.setThreadId(thread.getId());
                post.setUserId(poster.getId());
                post.setCreatedAt(java.time.Instant.now().toString());
                post.setUpdatedAt(java.time.Instant.now().toString());
                postRepository.save(post);
                allPosts.add(post);
            }
        }
        System.out.println("Posts created");

        // 4. Randomly like some posts by random users
        for (Post post : allPosts) {
    int likes = rand.nextInt(users.size() / 2);
    Set<Integer> likedUserIndexes = new HashSet<>();
    for (int l = 0; l < likes; l++) {
        int likerIdx;
        do {
            likerIdx = rand.nextInt(users.size());
        } while (likedUserIndexes.contains(likerIdx));
        likedUserIndexes.add(likerIdx);

        User liker = users.get(likerIdx);
        // Check if like already exists
        if (post.getLikes() != null && post.getLikes().stream().anyMatch(like -> like.getUserId().equals(liker.getId()))) {
            continue;
        }
        be.group16.forum.model.Like like = new be.group16.forum.model.Like();
        like.setUserId(liker.getId());
        like.setPostId(post.getId());
        like.setDislike(false);
        like.setCreatedAt(java.time.Instant.now().toString());
        like.setUpdatedAt(java.time.Instant.now().toString());
        // Save like and add to post
        like = likeRepository.save(like);
        if (post.getLikes() == null) post.setLikes(new HashSet<>());
        post.getLikes().add(like);
        post.setUpdatedAt(java.time.Instant.now().toString());
        postRepository.save(post);
    }
}
        System.out.println("Posts liked");

        System.out.println("Database initialized with realistic sample data!");
    }
}