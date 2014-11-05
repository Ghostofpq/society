package com.gop.society.business.controllers;

import com.gop.society.exceptions.CustomInvalidFieldException;
import com.gop.society.exceptions.CustomNotAuthorizedException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.models.User;
import com.gop.society.services.UserService;
import com.gop.society.utils.Pageable;
import com.gop.society.utils.UserAuthenticationToken;
import com.gop.society.utils.UserCreationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.security.Principal;
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
        user.setPassword(userService.encodePassword(userToCreate.getPassword(),user.getSalt()));
        // Set basic Role
        final List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        user.setUserRole(roles);

        // Save
        return userService.add(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User get(
            @PathVariable("id") final String id,
            final Principal principal)
            throws CustomNotFoundException, CustomNotAuthorizedException {
        final UserAuthenticationToken token = (UserAuthenticationToken) principal;
        final User loggedUser = (User) token.getPrincipal();
        if (loggedUser.getId().equals(id) || isAdmin(token)) {
            return userService.get(id);
        }
        throw new CustomNotAuthorizedException();
    }

    @RequestMapping(value = "/{id}/login", method = RequestMethod.PUT)
    @ResponseBody
    public User updateLogin(
            @PathVariable("id") final String id,
            @RequestBody final String login,
            final Principal principal)
            throws CustomNotFoundException,
            CustomNotAuthorizedException,
            CustomInvalidFieldException {
        final UserAuthenticationToken token = (UserAuthenticationToken) principal;
        final User loggedUser = (User) token.getPrincipal();
        if (loggedUser.getId().equals(id) || isAdmin(token)) {
            return userService.updateLogin(id, login);
        }
        throw new CustomNotAuthorizedException();
    }

    @RequestMapping(value = "/{id}/password", method = RequestMethod.PUT)
    @ResponseBody
    public User updatePassword(
            @PathVariable("id") final String id,
            @RequestBody final String newPassword,
            final Principal principal)
            throws CustomNotFoundException,
            CustomNotAuthorizedException,
            CustomInvalidFieldException {
        final UserAuthenticationToken token = (UserAuthenticationToken) principal;
        final User loggedUser = (User) token.getPrincipal();
        if (loggedUser.getId().equals(id) || isAdmin(token)) {
            return userService.updatePassword(id, newPassword);
        }
        throw new CustomNotAuthorizedException();
    }


    @RequestMapping(value = "/{id}/email", method = RequestMethod.PUT)
    @ResponseBody
    public User updateEmail(
            @PathVariable("id") final String id,
            @RequestBody final String email,
            final Principal principal)
            throws CustomNotFoundException,
            CustomNotAuthorizedException,
            CustomInvalidFieldException {
        final UserAuthenticationToken token = (UserAuthenticationToken) principal;
        final User loggedUser = (User) token.getPrincipal();
        if (loggedUser.getId().equals(id) || isAdmin(token)) {
            return userService.updateEmail(id, email);
        }
        throw new CustomNotAuthorizedException();
    }

    @RequestMapping(value = "/{id}/roles", method = RequestMethod.PUT)
    @ResponseBody
    public User updateRoles(
            @PathVariable("id") final String id,
            @RequestBody final List<String> roles,
            final Principal principal)
            throws CustomNotFoundException,
            CustomNotAuthorizedException,
            CustomInvalidFieldException {
        final UserAuthenticationToken token = (UserAuthenticationToken) principal;
        final User loggedUser = (User) token.getPrincipal();
        if (loggedUser.getId().equals(id) || isAdmin(token)) {
            return userService.updateRoles(id, roles);
        }
        throw new CustomNotAuthorizedException();
    }

    @RequestMapping(value = "/{id}/roles/add", method = RequestMethod.PUT)
    @ResponseBody
    public User addRole(
            @PathVariable("id") final String id,
            @RequestBody final String role,
            final Principal principal)
            throws CustomNotFoundException,
            CustomNotAuthorizedException,
            CustomInvalidFieldException {
        final UserAuthenticationToken token = (UserAuthenticationToken) principal;
        final User loggedUser = (User) token.getPrincipal();
        if (loggedUser.getId().equals(id) || isAdmin(token)) {
            return userService.addRole(id, role);
        }
        throw new CustomNotAuthorizedException();
    }

    @RequestMapping(value = "/{id}/roles/remove", method = RequestMethod.PUT)
    @ResponseBody
    public User removeRole(
            @PathVariable("id") final String id,
            @RequestBody final String role,
            final Principal principal)
            throws CustomNotFoundException,
            CustomNotAuthorizedException,
            CustomInvalidFieldException {
        final UserAuthenticationToken token = (UserAuthenticationToken) principal;
        final User loggedUser = (User) token.getPrincipal();
        if (loggedUser.getId().equals(id) || isAdmin(token)) {
            return userService.removeRole(id, role);
        }
        throw new CustomNotAuthorizedException();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(
            @PathVariable("id") final String id,
            final Principal principal)
            throws CustomNotFoundException, CustomNotAuthorizedException {
        final UserAuthenticationToken token = (UserAuthenticationToken) principal;
        final User loggedUser = (User) token.getPrincipal();
        if (loggedUser.getId().equals(id) || isAdmin(token)) {
            userService.delete(id);
        }
        throw new CustomNotAuthorizedException();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public Pageable<User> getByParameters(
            @RequestParam(value = "page", required = false, defaultValue = "0") final Integer pageNumber,
            @RequestParam(value = "size", required = false, defaultValue = "10") final Integer size,
            final Principal principal)
            throws CustomNotAuthorizedException {
        final UserAuthenticationToken token = (UserAuthenticationToken) principal;
        if (isAdmin(token)) {
            return userService.getAll(pageNumber, size);
        }
        throw new CustomNotAuthorizedException();
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

    private boolean isAdmin(final Principal principal) {
        final UserAuthenticationToken token = (UserAuthenticationToken) principal;
        for (GrantedAuthority ga : token.getAuthorities()) {
            if (ga.getAuthority().equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }
}