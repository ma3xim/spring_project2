package ru.maxbri.springcourse.Project2WithBoot.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxbri.springcourse.Project2WithBoot.models.Book;
import ru.maxbri.springcourse.Project2WithBoot.models.Person;
import ru.maxbri.springcourse.Project2WithBoot.services.BookService;
import ru.maxbri.springcourse.Project2WithBoot.services.PeopleServices;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final PeopleServices peopleServices;

    @Autowired
    public BookController(BookService bookService, PeopleServices peopleServices) {
        this.bookService = bookService;
        this.peopleServices = peopleServices;
    }


    @GetMapping()
    public String index(Model model, @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear) {
        if ((page == null) || (booksPerPage == null)){
            model.addAttribute("books", bookService.findAll(sortByYear));
        }else{
            model.addAttribute("books", bookService.findWithPagination(page,booksPerPage,sortByYear));
        }

        return "book/index";
    }



    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person")Person person) {
        model.addAttribute("book", bookService.findOne(id));
        Person owner = bookService.getBookOwner(id);

        if (owner!=null)
            model.addAttribute("owner", owner);
        else
            model.addAttribute("people", peopleServices.findAll());
        return "book/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "book/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "book/new";

        bookService.save(book);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.findOne(id));
        return "book/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "book/edit";

        bookService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id){
        bookService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson){
        bookService.assign(id, selectedPerson);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String searchPage(){
        return "book/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam("query") String query){ //в строку query будет помещен запрос из формы
        model.addAttribute("books", bookService.searchByName(query));
        return "book/search";
    }
}