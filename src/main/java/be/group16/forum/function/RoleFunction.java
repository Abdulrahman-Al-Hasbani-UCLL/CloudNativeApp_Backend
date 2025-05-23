package be.group16.forum.function;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import be.group16.forum.model.Role;
import be.group16.forum.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoleFunction {
    @Autowired
    private RoleService roleService;

    @FunctionName("getRoleById")
    public HttpResponseMessage getRoleById(
            @HttpTrigger(name = "req", methods = { HttpMethod.GET,
                    HttpMethod.OPTIONS }, route = "role/{id}", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            @BindingName("id") String id,
            final ExecutionContext context) {
        String allowedOrigin = System.getenv("ALLOWED_ORIGIN");

        if (request.getHttpMethod() == HttpMethod.OPTIONS) {
            return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }

        Role role = roleService.getRoleById(id);
        return request.createResponseBuilder(HttpStatus.OK)
                .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type,Authorization").body(role).build();
    }

    @FunctionName("updateRoleById")
    public HttpResponseMessage updateRoleById(
            @HttpTrigger(name = "req", methods = { HttpMethod.PUT,
                    HttpMethod.OPTIONS }, route = "role/{id}", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<Role>> request,
            @BindingName("id") String id,
            final ExecutionContext context) {
        String allowedOrigin = System.getenv("ALLOWED_ORIGIN");

        if (request.getHttpMethod() == HttpMethod.OPTIONS) {
            return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }
        Role updatedRole = request.getBody().orElse(null);
        if (updatedRole == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization").body("Missing role data")
                    .build();
        }

        Role role = roleService.updateRole(id, updatedRole);
        return request.createResponseBuilder(HttpStatus.OK)
                .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                .body(role).build();
    }

    @FunctionName("deleteRoleById")
    public HttpResponseMessage deleteRoleById(
            @HttpTrigger(name = "req", methods = { HttpMethod.DELETE,
                    HttpMethod.OPTIONS }, route = "role/{id}", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            @BindingName("id") String id,
            final ExecutionContext context) {
        String allowedOrigin = System.getenv("ALLOWED_ORIGIN");

        if (request.getHttpMethod() == HttpMethod.OPTIONS) {
            return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }

        roleService.deleteRoleById(id);
        return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                .build();
    }

    @FunctionName("createRole")
    public HttpResponseMessage createRole(
            @HttpTrigger(name = "req", methods = { HttpMethod.POST,
                    HttpMethod.OPTIONS }, route = "role", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<Role>> request,
            final ExecutionContext context) {
        String allowedOrigin = System.getenv("ALLOWED_ORIGIN");

        if (request.getHttpMethod() == HttpMethod.OPTIONS) {
            return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }

        Role newRole = request.getBody().orElse(null);
        if (newRole == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .body("Missing role data").build();
        }
        Role role = roleService.createRole(newRole);
        return request.createResponseBuilder(HttpStatus.CREATED)
                .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                .body(role).build();
    }
}
