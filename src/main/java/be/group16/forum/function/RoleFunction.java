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

    @FunctionName("roleById")
    public HttpResponseMessage roleById(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.OPTIONS
            }, route = "role/{id}", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<Role>> request,
            @BindingName("id") String id,
            final ExecutionContext context) {

        String allowedOrigin = System.getenv("ALLOWED_ORIGIN");
        HttpMethod method = request.getHttpMethod();

        // Handle CORS preflight
        if (method == HttpMethod.OPTIONS) {
            return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }

        // GET /role/{id}
        if (method == HttpMethod.GET) {
            Role role = roleService.getRoleById(id);
            return request.createResponseBuilder(HttpStatus.OK)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .body(role).build();
        }

        // PUT /role/{id}
        if (method == HttpMethod.PUT) {
            Role updatedRole = request.getBody().orElse(null);
            if (updatedRole == null) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                        .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                        .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                        .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                        .body("Missing role data").build();
            }
            Role role = roleService.updateRole(id, updatedRole);
            return request.createResponseBuilder(HttpStatus.OK)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .body(role).build();
        }

        // DELETE /role/{id}
        if (method == HttpMethod.DELETE) {
            roleService.deleteRoleById(id);
            return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                    .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                    .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                    .build();
        }

        // Method not allowed
        return request.createResponseBuilder(HttpStatus.METHOD_NOT_ALLOWED)
                .header("Access-Control-Allow-Origin", allowedOrigin != null ? allowedOrigin : "*")
                .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type,Authorization")
                .body("Method not allowed")
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
