package ru.maxbri.springcourse.Project2WithBoot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxbri.springcourse.Project2WithBoot.models.Client;
import ru.maxbri.springcourse.Project2WithBoot.repositories.ClientRepository;

@Service
public class RegistrationService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Client client){
        String encodedPassword = passwordEncoder.encode(client.getPassword());//шифруем пароль
        client.setPassword(encodedPassword);
        client.setRole("ROLE_USER");
        clientRepository.save(client);
    }
}
