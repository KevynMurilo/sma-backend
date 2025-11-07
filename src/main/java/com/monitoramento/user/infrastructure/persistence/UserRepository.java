package com.monitoramento.user.infraestructure.persistence;

import com.monitoramento.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByCpf(String cpf);

    boolean existsByUsername(String username);
    boolean existsByCpf(String cpf);

    Optional<User> findByUsernameOrCpf(String username, String cpf);
}