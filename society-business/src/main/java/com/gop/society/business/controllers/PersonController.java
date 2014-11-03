package com.gop.society.business.controllers;

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
@Component("personController")
public class PersonController {
    @Autowired
    private PersonService personService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public Person create(@RequestBody Person person) {
        return personService.add(person);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Person get(@PathVariable("id") final String id) {
        return personService.get(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Person update(@PathVariable("id") final String id, @RequestBody Person person) {
        return personService.update(id, person);
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