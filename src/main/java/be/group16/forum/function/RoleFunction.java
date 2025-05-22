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
        @HttpTrigger(name = "req", methods = {HttpMethod.GET}, route = "role/{id}", authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<String>> request,
        @BindingName("id") String id,
        final ExecutionContext context) {

        Role role = roleService.getRoleById(id);
        return request.createResponseBuilder(HttpStatus.OK).body(role).build();
    }

    @FunctionName("updateRoleById")
    public HttpResponseMessage updateRoleById(
        @HttpTrigger(name = "req", methods = {HttpMethod.PUT}, route = "role/{id}", authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<Role>> request,
        @BindingName("id") String id,
        final ExecutionContext context) {

        Role updatedRole = request.getBody().orElse(null);
        if (updatedRole == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Missing role data").build();
        }
        Role role = roleService.updateRole(id, updatedRole);
        return request.createResponseBuilder(HttpStatus.OK).body(role).build();
    }

    @FunctionName("deleteRoleById")
    public HttpResponseMessage deleteRoleById(
        @HttpTrigger(name = "req", methods = {HttpMethod.DELETE}, route = "role/{id}", authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<String>> request,
        @BindingName("id") String id,
        final ExecutionContext context) {

        roleService.deleteRoleById(id);
        return request.createResponseBuilder(HttpStatus.NO_CONTENT).build();
    }

    @FunctionName("createRole")
    public HttpResponseMessage createRole(
        @HttpTrigger(name = "req", methods = {HttpMethod.POST}, route = "role", authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<Role>> request,
        final ExecutionContext context) {

        Role newRole = request.getBody().orElse(null);
        if (newRole == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Missing role data").build();
        }
        Role role = roleService.createRole(newRole);
        return request.createResponseBuilder(HttpStatus.CREATED).body(role).build();
    }
}
