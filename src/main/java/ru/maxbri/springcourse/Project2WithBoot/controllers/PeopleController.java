package ru.maxbri.springcourse.Project2WithBoot.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxbri.springcourse.Project2WithBoot.models.Person;
import ru.maxbri.springcourse.Project2WithBoot.services.BookService;
import ru.maxbri.springcourse.Project2WithBoot.services.PeopleServices;
import ru.maxbri.springcourse.Project2WithBoot.util.PersonValidator;


@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleServices peopleServices;
    private final PersonValidator personValidator;
    private final BookService bookService;

    @Autowired
    public PeopleController(PeopleServices peopleServices, PersonValidator personValidator, BookService bookService) {
        this.peopleServices = peopleServices;
        this.personValidator = personValidator;
        this.bookService = bookService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleServices.findAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleServices.findOne(id));
        //Person owner = peopleServices.findOne(id);
        //model.addAttribute("books", bookService.findByOwner(owner));
        model.addAttribute("books", peopleServices.getBooksByPersonId(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "people/new";

        peopleServices.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", peopleServices.findOne(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "people/edit";

        peopleServices.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleServices.delete(id);
        return "redirect:/people";
    }
}