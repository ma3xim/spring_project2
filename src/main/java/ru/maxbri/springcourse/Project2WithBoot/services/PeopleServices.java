package ru.maxbri.springcourse.Project2WithBoot.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxbri.springcourse.Project2WithBoot.models.Book;
import ru.maxbri.springcourse.Project2WithBoot.models.Person;
import ru.maxbri.springcourse.Project2WithBoot.repositories.PeopleRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleServices {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleServices(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll(){
        return peopleRepository.findAll();
    }

    public Person findOne(int id){
        Optional<Person> byId = peopleRepository.findById(id);
        return byId.orElse(null);
    }

    @Transactional
    public void save(Person person){
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson){
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id){
        peopleRepository.deleteById(id);
    }

    public Person findByFullName(String fullName){
        Optional<Person> person = peopleRepository.findByFullName(fullName);
        return person.orElse(null);
    }

    public List<Book> getBooksByPersonId(int id){
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isPresent()){
            Hibernate.initialize(person.get().getBooks());//на всякий случай, если что-то поменяется

            //проверка просрочки книг
            person.get().getBooks().forEach(book -> {
                long milSeconds = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                if (milSeconds > 864000000) //если больше 10 суток
                    book.setExpired(true);
            });
            return person.get().getBooks();
        }
        else {
            return Collections.emptyList();
        }
    }
}
