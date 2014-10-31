package com.gop.society.services;

import com.google.common.collect.Lists;
import com.gop.society.models.Pageable;
import com.gop.society.models.Person;
import com.gop.society.repositories.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

/**
 * @author GhostOfPQ
 */
@Slf4j
@Component("personService")
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public Person add(final Person person) {
        return personRepository.save(person);
    }

    public Person get(final String id) {
        return personRepository.findOne(id);
    }

    public Person update(final String id, final Person person) {
        person.setId(id);
        return personRepository.save(person);
    }

    public void delete(final String id) {
        personRepository.delete(id);
    }

    public Pageable<Person> getAll(final int pageNumber, final int size) {
        final Page<Person> persons = personRepository.findAll(new PageRequest(pageNumber, size));
        return new Pageable<>(
                persons.getNumber(),
                persons.getSize(),
                persons.getTotalElements(),
                Lists.newArrayList(persons.iterator()));
    }
}
