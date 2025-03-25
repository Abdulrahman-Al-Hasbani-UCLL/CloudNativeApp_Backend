package be.group16.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;

import be.group16.forum.model.User;
import be.group16.forum.repository.UserRepository;

@SpringBootApplication
public class ForumApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForumApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader(UserRepository userRepository) {
        return args -> {
            System.out.println("\n\n=================================");
            System.out.println("Initializing Forum Application...");
            System.out.println("=================================\n");
            
            try {
                if (userRepository.findByUsername("admin").isEmpty()) {
                    User admin = new User();
                    admin.setUsername("admin");
                    admin.setPassword("admin123");
                    admin.setEmail("admin@example.com");
                    admin.setEmailVerified(true);
                    admin.addRole("ADMIN");
                    userRepository.save(admin);
                    System.out.println("Admin user created");
                }

                if (userRepository.findByUsername("testAbdul").isEmpty()) {
                    User testUser = new User();
                    testUser.setUsername("testAbdul");
                    testUser.setPassword("abdul123");
                    testUser.setEmail("testabdul@example.com");
                    testUser.setEmailVerified(true);
                    testUser.addRole("USER");
                    userRepository.save(testUser);
                    System.out.println("Test user created");
                }
            } catch (Exception e) {
                System.err.println("Error initializing data: " + e.getMessage());
                e.printStackTrace();
            }
            
            System.out.println("\n=================================");
            System.out.println("Forum Application started!");
            System.out.println("=================================\n");
        };
    }
}
