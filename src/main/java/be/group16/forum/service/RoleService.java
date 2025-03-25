package be.group16.forum.service;

import be.group16.forum.model.Role;
import be.group16.forum.model.User;
import be.group16.forum.repository.RoleRepository;
import be.group16.forum.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    // Create a new role
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    // Update an existing role
    public Role updateRole(String roleId, Role updatedRole) {
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with ID: " + roleId));
        existingRole.setName(updatedRole.getName());
        existingRole.setDescription(updatedRole.getDescription());
        existingRole.setColor(updatedRole.getColor());
        existingRole.setExtendedData(updatedRole.getExtendedData());
        return roleRepository.save(existingRole);
    }

    // Assign a role to a user
    public User assignRoleToUser(String userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with name: " + roleName));
        user.addRole(role.getName());
        return userRepository.save(user);
    }

    // Remove a role from a user
    public User removeRoleFromUser(String userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        if (!java.util.Arrays.asList(user.getRoles()).contains(roleName)) {
            throw new IllegalArgumentException("User does not have the role: " + roleName);
        }
        user.removeRole(roleName);
        return userRepository.save(user);
    }

    // Change a user's role
    public User changeUserRole(String userId, String oldRoleName, String newRoleName) {
        @SuppressWarnings("unused")
        User user = removeRoleFromUser(userId, oldRoleName);
        return assignRoleToUser(userId, newRoleName);
    }

    public Role getRoleById(String id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with ID: " + id));
    }

    public void deleteRoleById(String roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with ID: " + roleId));
        roleRepository.delete(role);
    }
}
