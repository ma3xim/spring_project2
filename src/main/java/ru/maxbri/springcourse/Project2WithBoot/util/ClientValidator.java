package ru.maxbri.springcourse.Project2WithBoot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.maxbri.springcourse.Project2WithBoot.models.Client;
import ru.maxbri.springcourse.Project2WithBoot.services.ClientDetailsService;

@Component
public class ClientValidator implements Validator {
    private final ClientDetailsService clientDetailsService;

    @Autowired
    public ClientValidator(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Client.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Client client = (Client) target;

        try {
            clientDetailsService.loadUserByUsername(client.getUsername());
        }catch (UsernameNotFoundException e){
            return; //все ок, пользователь с такими именем не найден
            //вообще это плохой код, loadUserByUsername надо переписать чтобы он возвращал Optional
            //и вместо ловли исключения обрабатывать Optional
        }
        errors.rejectValue("username", "", "Человек с таким username уже существует");
    }
}
