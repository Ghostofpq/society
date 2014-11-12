package com.gop.society.business.controllers;

import com.gop.society.exceptions.CustomInvalidFieldException;
import com.gop.society.exceptions.CustomNotAuthorizedException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.models.User;
import com.gop.society.services.UserService;
import com.gop.society.utils.Pageable;
import com.gop.society.utils.UserCreationRequest;
import com.gop.society.utils.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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

    @PostConstruct
    private void init() {
        log.info("UserController started !");
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public User create(@RequestBody final UserCreationRequest userToCreate) {
        log.debug("In create with {}", userToCreate.getLogin());

        final User user = new User();

        // Set Login
        user.setLogin(userToCreate.getLogin());
        // Set Email
        user.setEmail(userToCreate.getEmail());
        // Generate Salt
        final Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);
        // Encode Password
        user.setSalt(new String(salt));
        user.setPassword(userService.encodePassword(userToCreate.getPassword(), user.getSalt()));
        // Set basic Role
        final List<UserRole> roles = new ArrayList<>();
        roles.add(UserRole.USER);
        user.setUserRole(roles);

        // Save
        return userService.add(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User get(
            @PathVariable("id") final String id)
            throws CustomNotFoundException, CustomNotAuthorizedException {
        return userService.get(id);
    }

    @RequestMapping(value = "/{id}/login", method = RequestMethod.PUT)
    @ResponseBody
    public User updateLogin(
            @PathVariable("id") final String id,
            @RequestBody final String login)
            throws CustomNotFoundException,
            CustomNotAuthorizedException,
            CustomInvalidFieldException {
        return userService.updateLogin(id, login);
    }

    @RequestMapping(value = "/{id}/password", method = RequestMethod.PUT)
    @ResponseBody
    public User updatePassword(
            @PathVariable("id") final String id,
            @RequestBody final String newPassword)
            throws CustomNotFoundException,
            CustomNotAuthorizedException,
            CustomInvalidFieldException {
        return userService.updatePassword(id, newPassword);
    }


    @RequestMapping(value = "/{id}/email", method = RequestMethod.PUT)
    @ResponseBody
    public User updateEmail(
            @PathVariable("id") final String id,
            @RequestBody final String email)
            throws CustomNotFoundException,
            CustomNotAuthorizedException,
            CustomInvalidFieldException {
        return userService.updateEmail(id, email);
    }

    @RequestMapping(value = "/{id}/roles", method = RequestMethod.PUT)
    @ResponseBody
    public User updateRoles(
            @PathVariable("id") final String id,
            @RequestBody final List<String> roles)
            throws CustomNotFoundException,
            CustomNotAuthorizedException,
            CustomInvalidFieldException {
        final List<UserRole> userRoles = new ArrayList<>();
        for (String role : roles) {
            if (UserRole.fromValue(role) != null) {
                userRoles.add(UserRole.fromValue(role));
            }
        }
        return userService.updateRoles(id, userRoles);
    }

    @RequestMapping(value = "/{id}/roles/add", method = RequestMethod.PUT)
    @ResponseBody
    public User addRole(
            @PathVariable("id") final String id,
            @RequestBody final String role)
            throws CustomNotFoundException,
            CustomNotAuthorizedException,
            CustomInvalidFieldException {
        final UserRole userRole = UserRole.fromValue(role);
        if (UserRole.fromValue(role) != null) {
            return userService.addRole(id, userRole);
        }
        throw new CustomInvalidFieldException("role");
    }

    @RequestMapping(value = "/{id}/roles/remove", method = RequestMethod.PUT)
    @ResponseBody
    public User removeRole(
            @PathVariable("id") final String id,
            @RequestBody final String role)
            throws CustomNotFoundException,
            CustomNotAuthorizedException,
            CustomInvalidFieldException {
        final UserRole userRole = UserRole.fromValue(role);
        if (UserRole.fromValue(role) != null) {
            return userService.removeRole(id, userRole);
        }
        throw new CustomInvalidFieldException("role");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(
            @PathVariable("id") final String id)
            throws CustomNotFoundException, CustomNotAuthorizedException {
        userService.delete(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public Pageable<User> getByParameters(
            @RequestParam(value = "page", required = false, defaultValue = "0") final Integer pageNumber,
            @RequestParam(value = "size", required = false, defaultValue = "10") final Integer size)
            throws CustomNotAuthorizedException {
        return userService.getAll(pageNumber, size);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomNotFoundException.class)
    @ResponseBody
    private String handleNotFoundException(CustomNotFoundException e) {
        log.error(e.getMessage());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(CustomNotAuthorizedException.class)
    @ResponseBody
    private String handleNotAuthorizedException(CustomNotAuthorizedException e) {
        log.error(e.getMessage());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomInvalidFieldException.class)
    @ResponseBody
    private String handleInvalidFieldException(CustomInvalidFieldException e) {
        log.error(e.getMessage());
        return e.getMessage();
    }

}