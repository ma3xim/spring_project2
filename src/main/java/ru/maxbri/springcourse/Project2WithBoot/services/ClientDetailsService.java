package ru.maxbri.springcourse.Project2WithBoot.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.maxbri.springcourse.Project2WithBoot.models.Client;
import ru.maxbri.springcourse.Project2WithBoot.repositories.ClientRepository;
import ru.maxbri.springcourse.Project2WithBoot.security.ClientDetails;

import java.util.Optional;

@Service
public class ClientDetailsService implements UserDetailsService {
    private final ClientRepository clientRepository;

    public ClientDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    @Override
    public ClientDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Client> client = clientRepository.findByUsername(username);

        if (client.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new ClientDetails(client.get());
    }
}
