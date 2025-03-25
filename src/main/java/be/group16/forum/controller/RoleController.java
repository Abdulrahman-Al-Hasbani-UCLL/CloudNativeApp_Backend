package be.group16.forum.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.group16.forum.model.Role;
import be.group16.forum.service.RoleService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("role")
public class RoleController {
    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable String id) {
        Role role = roleService.getRoleById(id);
        return ResponseEntity.ok(role);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRoleById(@PathVariable String id, @RequestBody Role updatedRole) {
        Role role = roleService.updateRole(id, updatedRole);
        return ResponseEntity.ok(role);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoleById(@PathVariable String id) {
        roleService.deleteRoleById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role newRole) {
        Role role = roleService.createRole(newRole);
        return ResponseEntity.status(201).body(role);
    }
}
