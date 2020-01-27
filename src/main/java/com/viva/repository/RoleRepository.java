package com.viva.repository;

import com.viva.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository <Role, Long>{
    Set <Role> findAllByRole(String role);
}
