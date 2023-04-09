package com.loginsocial.persistence.repository;

import com.loginsocial.persistence.entity.UserPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<UserPrincipal, Long> {

    Optional<UserPrincipal> findByUsername(String username);
}
