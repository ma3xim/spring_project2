package ru.maxbri.springcourse.Project2WithBoot.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.maxbri.springcourse.Project2WithBoot.models.Client;
import ru.maxbri.springcourse.Project2WithBoot.services.RegistrationService;
import ru.maxbri.springcourse.Project2WithBoot.util.ClientValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final ClientValidator clientValidator;
    private final RegistrationService registrationService;

    @Autowired
    public AuthController(ClientValidator clientValidator, RegistrationService registrationService) {
        this.clientValidator = clientValidator;
        this.registrationService = registrationService;
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @GetMapping("/login")
    public String loginPage(){
        if (isAuthenticated()) {
            return "redirect:/books";
        }
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("client") Client client){
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid Client client, BindingResult bindingResult){

        clientValidator.validate(client, bindingResult);
        if(bindingResult.hasErrors()) {
            return "/auth/registration";
        }
        registrationService.register(client);
        return "redirect:/auth/login";
    }
}
