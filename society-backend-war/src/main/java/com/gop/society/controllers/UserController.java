package com.gop.society.controllers;

import com.gop.society.exceptions.CustomBadRequestException;
import com.gop.society.exceptions.CustomNotAuthorizedException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.models.User;
import com.gop.society.services.UserService;
import com.gop.society.utils.Pageable;
import com.gop.society.utils.UserCreationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Map;


/**
 * @author GhostOfPQ
 */
@Slf4j
@Controller
@RequestMapping("/api/users")
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
    public User create(@RequestBody final UserCreationRequest userToCreate) throws CustomBadRequestException {
        log.debug("In create with {}", userToCreate.toString());
        final User user = new User();
        // Set Login
        user.setLogin(userToCreate.getLogin());
        // Set Email
        user.setEmail(userToCreate.getEmail());
        // Set Password
        user.setPassword(userToCreate.getPassword());
        // Save
        return userService.add(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User get(
            @PathVariable("id") final String id)
            throws CustomNotFoundException,
            CustomNotAuthorizedException {
        log.debug("get({})", id);
        return userService.get(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public User update(
            @PathVariable("id") final String id,
            @RequestBody final Map<String, Object> fields)
            throws CustomNotFoundException,
            CustomBadRequestException,
            CustomNotAuthorizedException {
        final User user = userService.get(id);
        for (final String key : fields.keySet()) {
            updateFieldForUser(user, key, fields.get(key));
        }
        return userService.update(user);
    }

    @RequestMapping(value = "/{id}/{field}", method = RequestMethod.PUT)
    @ResponseBody
    public User updateField(
            @PathVariable("id") final String id,
            @PathVariable("field") final String field,
            @RequestBody final Object value)
            throws CustomNotFoundException,
            CustomBadRequestException,
            CustomNotAuthorizedException {
        final User user = userService.get(id);
        updateFieldForUser(user, field, value);
        return userService.update(user);
    }

    private void updateFieldForUser(final User user, final String field, final Object value) {
        switch (field) {
            case User.FIELD_LOGIN:
                user.setLogin((String) value);
                break;
            case User.FIELD_PASSWORD:
                user.setPassword((String) value);
                break;
            case User.FIELD_EMAIL:
                user.setEmail((String) value);
                break;
            default:
                log.error("Unknown field {}", field);
                break;
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(
            @PathVariable("id") final String id)
            throws CustomNotFoundException,
            CustomNotAuthorizedException {
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
        log.error(HttpStatus.NOT_FOUND + ":" + e.getMessage());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(CustomNotAuthorizedException.class)
    @ResponseBody
    private String handleNotAuthorizedException(CustomNotAuthorizedException e) {
        log.error(HttpStatus.FORBIDDEN + ":" + e.getMessage());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomBadRequestException.class)
    @ResponseBody
    private String handleBadRequestException(CustomBadRequestException e) {
        log.error(HttpStatus.BAD_REQUEST + ":" + e.getMessage());
        return e.getMessage();
    }
}