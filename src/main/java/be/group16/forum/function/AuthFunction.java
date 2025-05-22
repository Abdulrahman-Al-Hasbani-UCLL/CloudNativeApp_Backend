package be.group16.forum.function;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import be.group16.forum.dto.LoginInput;
import be.group16.forum.dto.RegisterInput;
import be.group16.forum.model.User;
import be.group16.forum.repository.UserRepository;
import be.group16.forum.service.UserService;
import be.group16.forum.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuthFunction {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @FunctionName("login")
    public HttpResponseMessage login(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.POST,
                    HttpMethod.OPTIONS }, route = "auth/login", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<LoginInput> request,
            final ExecutionContext context) {
        String allowedOrigin = System.getenv("ALLOWED_ORIGIN");

        if (request.getHttpMethod() == HttpMethod.OPTIONS) {
            return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }

        LoginInput loginInput = request.getBody();
        String login = loginInput.getLogin();
        String password = loginInput.getPassword();

        User user = userRepository.findByUsername(login)
                .or(() -> userRepository.findByEmail(login))
                .orElse(null);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return request.createResponseBuilder(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid login credentials"))
                    .build();
        }

        String token = jwtUtil.generateToken(user.getUsername());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return request.createResponseBuilder(HttpStatus.OK)
                .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                .body(response)
                .build();
    }

    @FunctionName("register")
    public HttpResponseMessage register(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.POST,
                    HttpMethod.OPTIONS }, route = "auth/register", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<RegisterInput> request,
            final ExecutionContext context) {
        String allowedOrigin = System.getenv("ALLOWED_ORIGIN");
        RegisterInput newUser = request.getBody();

        if (request.getHttpMethod() == HttpMethod.OPTIONS) {
            return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }

        if (userRepository.existsByUsername(newUser.getUsername())) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .body(Map.of("error", "Username is already taken"))
                    .build();
        }
        if (userRepository.existsByEmail(newUser.getEmail())) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .body(Map.of("error", "Email is already registered"))
                    .build();
        }

        if (newUser.getDisplayName() == null || newUser.getDisplayName().isEmpty()) {
            newUser.setDisplayName(newUser.getUsername());
        }
        if (newUser.getRoles() == null || newUser.getRoles().length == 0) {
            newUser.addRole("user");
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        User user = new User();
        user.setUsername(newUser.getUsername());
        user.setEmail(newUser.getEmail());
        user.setPassword(newUser.getPassword());
        user.setDisplayName(newUser.getDisplayName());
        user.setEmailVerified(false);
        user.setRoles(newUser.getRoles());
        user.setExtendedData(newUser.getExtendedData());

        User savedUser = userService.registerUser(user);

        return request.createResponseBuilder(HttpStatus.OK)
                .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                .body(savedUser).build();
    }
}
