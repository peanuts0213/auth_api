package com.bict.auth_api.repositories;

import com.bict.auth_api.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, Long>{

    UserEntity getUserIdByUsername(String username);

    UserEntity findByUsername(String username);

    boolean existsByUsername(String username);
}
