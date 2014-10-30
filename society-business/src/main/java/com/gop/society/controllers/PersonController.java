package com.gop.society.controllers;

import com.gop.society.models.Pageable;
import com.gop.society.models.Person;
import com.gop.society.services.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * @author GhostOfPQ
 */
@Slf4j
@Controller
@RequestMapping("/persons")
@Component
public class PersonController {
    @Autowired
    private PersonService personService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public Person create(@RequestParam(value = "firstName", required = false) final String firstName,
                         @RequestParam(value = "lastName", required = false) final String lastName,
                         @RequestParam(value = "surname", required = false) final String surname,
                         @RequestParam(value = "email", required = true) final String email) {
        Person person = new Person();
        person.setEmail(email);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setLastName(surname);
        return personService.add(person);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Person get(@PathVariable("id") final String id) {
        return personService.get(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable("id") final String id) {
        personService.delete(id);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Pageable<Person> getByParameters(@RequestParam(value = "page", required = false, defaultValue = "0") final Integer pageNumber,
                                            @RequestParam(value = "size", required = false, defaultValue = "10") final Integer size) {
        return personService.getAll(pageNumber, size);
    }
}