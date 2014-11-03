package com.gop.society.business.controllers;

import com.gop.society.models.Pageable;
import com.gop.society.models.User;
import com.gop.society.models.UserCreationRequest;
import com.gop.society.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * @author GhostOfPQ
 */
@Slf4j
@Controller
@RequestMapping("/users")
@Component("userController")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public User create(@RequestBody final UserCreationRequest userToCreate) {
        log.debug("In create with {}", userToCreate.getLogin());
        final User user = new User();
        user.setLogin(userToCreate.getLogin());
        user.setPassword(userService.encodePassword(userToCreate.getPassword()));
        final List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        roles.add("ROLE_ANONYMOUS");
        user.setUserRole(roles);
        user.setEmail(userToCreate.getEmail());
        return userService.add(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User get(@PathVariable("id") final String id) {
        return userService.get(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public User update(@PathVariable("id") final String id, @RequestBody User user) {
        return userService.update(id, user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable("id") final String id) {
        userService.delete(id);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Pageable<User> getByParameters(@RequestParam(value = "page", required = false, defaultValue = "0") final Integer pageNumber,
                                          @RequestParam(value = "size", required = false, defaultValue = "10") final Integer size) {
        return userService.getAll(pageNumber, size);
    }


}