package ru.maxbri.springcourse.Project2WithBoot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxbri.springcourse.Project2WithBoot.models.Book;
import ru.maxbri.springcourse.Project2WithBoot.models.Person;
import ru.maxbri.springcourse.Project2WithBoot.repositories.BooksRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BooksRepository booksRepository;

    @Autowired
    public BookService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(Sort.by("year"));
        } else {
            return booksRepository.findAll();
        }
    }

    public List<Book> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        } else {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
    }

    public Book findOne(int id) {
        Optional<Book> byId = booksRepository.findById(id);
        return byId.orElse(null);
    }

    public List<Book> searchByName(String query){
        return booksRepository.findByNameStartingWith(query);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    public List<Book> findByOwner(Person owner) {
        return booksRepository.findByOwner(owner);
    }


    @Transactional
    public void release(int id) {
//        Optional<Book> optionalBook = booksRepository.findById(id);
//        if (optionalBook.isPresent()){
//            Book book = optionalBook.get();
//            book.setOwner(null);
//            booksRepository.save(book);
//        }
        booksRepository.findById(id).ifPresent(book -> book.setOwner(null));
    }

//    @Transactional
//    public void release(int id){
//        booksRepository.release(id);
//    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        Optional<Book> optionalBook = booksRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setOwner(selectedPerson);
            booksRepository.save(book);
        }
    }

    public Person getBookOwner(int id) {
        return booksRepository.findById(id).map(Book::getOwner).orElse(null);
    }
}
