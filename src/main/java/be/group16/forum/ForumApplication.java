package be.group16.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
@EnableCaching
public class ForumApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForumApplication.class, args);
    }

    @SuppressWarnings("unused")
    public CommandLineRunner dataLoader() {
        return args -> {
            System.out.println("\n\n=================================");
            System.out.println("Initializing Forum Application...");
            System.out.println("=================================\n");
            
            System.out.println("\n=================================");
            System.out.println("Forum Application started!");
            System.out.println("Go to:\n\t http://localhost:8000/swagger-ui/index.html");
            System.out.println("=================================\n");
        };
    }
}
