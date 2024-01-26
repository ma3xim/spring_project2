package ru.maxbri.springcourse.Project2WithBoot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maxbri.springcourse.Project2WithBoot.models.Book;
import ru.maxbri.springcourse.Project2WithBoot.models.Person;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByOwner(Person owner);

    List<Book> findByNameStartingWith(String query);


}
