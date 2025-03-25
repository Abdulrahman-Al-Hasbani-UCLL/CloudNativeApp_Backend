package be.group16.forum.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import be.group16.forum.model.Role;

public interface RoleRepository extends CrudRepository<Role, String> {

    Optional<Role> findByName(String roleName);

}
