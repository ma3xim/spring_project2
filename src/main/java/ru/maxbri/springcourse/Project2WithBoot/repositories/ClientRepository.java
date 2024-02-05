package ru.maxbri.springcourse.Project2WithBoot.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maxbri.springcourse.Project2WithBoot.models.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByUsername(String username);
}
