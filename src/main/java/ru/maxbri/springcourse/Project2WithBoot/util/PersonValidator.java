package ru.maxbri.springcourse.Project2WithBoot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.maxbri.springcourse.Project2WithBoot.models.Person;
import ru.maxbri.springcourse.Project2WithBoot.services.PeopleServices;

@Component
public class PersonValidator implements Validator {

    private final PeopleServices peopleServices;

    @Autowired
    public PersonValidator( PeopleServices peopleServices) {
        this.peopleServices = peopleServices;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (peopleServices.findByFullName(person.getFullName())!=null){
            errors.rejectValue("fullName", "", "Человек с таким ФИО уже зарегистрирован в библиотеке");
        }

    }
}
