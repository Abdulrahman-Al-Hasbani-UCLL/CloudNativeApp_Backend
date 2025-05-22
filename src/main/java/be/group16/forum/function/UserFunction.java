package be.group16.forum.function;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import be.group16.forum.model.User;
import be.group16.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UserFunction {
    @Autowired
    private UserService userService;

    @FunctionName("getAllUsers")
    public HttpResponseMessage getAllUsers(
        @HttpTrigger(name = "req", methods = {HttpMethod.GET}, route = "users", authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<String>> request,
        final ExecutionContext context) {

        List<User> users = userService.getAllUsers();
        return request.createResponseBuilder(HttpStatus.OK).body(users).build();
    }

    @FunctionName("updateUser")
    public HttpResponseMessage updateUser(
        @HttpTrigger(name = "req", methods = {HttpMethod.PUT}, route = "users/{id}", authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<User>> request,
        @BindingName("id") String id,
        final ExecutionContext context) {

        String authHeader = request.getHeaders().get("authorization");
        String token = (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
        if (token == null) {
            return request.createResponseBuilder(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Missing or invalid Authorization header."))
                    .build();
        }

        String userId = userService.getUserIdFromToken(token);
        if (!id.equals(userId)) {
            return request.createResponseBuilder(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "You are not allowed to update this user."))
                    .build();
        }

        User updatedUser = request.getBody().orElse(null);
        if (updatedUser == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Missing user data."))
                    .build();
        }

        User user = userService.findUserById(id);
        if (user == null) {
            return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found."))
                    .build();
        }

        user.setDisplayName(updatedUser.getDisplayName());
        user.setBio(updatedUser.getBio());
        user.setUrl(updatedUser.getUrl());
        user.setImage(updatedUser.getImage());
        user.setEmail(updatedUser.getEmail());

        userService.updateUser(id, user);
        return request.createResponseBuilder(HttpStatus.OK).body(user).build();
    }
}
