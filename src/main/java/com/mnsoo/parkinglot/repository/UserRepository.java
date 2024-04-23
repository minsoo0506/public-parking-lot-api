package com.mnsoo.parkinglot.repository;

import com.mnsoo.parkinglot.domain.persist.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByLoginId(String loginId);

    Optional<UserEntity> findByLoginId(String loginId);
}
