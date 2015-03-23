package com.gop.society.services;

import com.google.common.collect.Lists;
import com.gop.society.exceptions.CustomBadRequestException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.exceptions.CustomUserNotFoundForIdException;
import com.gop.society.exceptions.CustomUserNotFoundForLoginException;
import com.gop.society.models.User;
import com.gop.society.repositories.UserRepository;
import com.gop.society.utils.Pageable;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.NoSuchAlgorithmException;

/**
 * @author GhostOfPQ
 */
@Slf4j
@Component("userService")
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    private void init() throws NoSuchAlgorithmException {
        log.info("UserService started !");
    }

    public User add(User user) throws CustomBadRequestException {
        try {
            user.setCreationTs(DateTime.now().getMillis());
            user.setUpdateTs(DateTime.now().getMillis());
            return userRepository.save(user);
        } catch (Exception e) {
            throw new CustomBadRequestException("Could not save new user.");
        }
    }

    public User get(final String id) throws CustomNotFoundException {
        final User user = userRepository.findOne(id);
        if (user != null) {
            return user;
        }
        throw new CustomUserNotFoundForIdException(id);
    }

    public User getByLogin(final String login) throws CustomNotFoundException {
        final User user = userRepository.findByLogin(login);
        if (user != null) {
            return user;
        }
        throw new CustomUserNotFoundForLoginException(login);
    }

    public User update(User user) throws CustomBadRequestException {
        try {
            user.setUpdateTs(DateTime.now().getMillis());
            return userRepository.save(user);
        } catch (Exception e) {
            throw new CustomBadRequestException("Could not save new user.");
        }
    }

    public void delete(final String id) throws CustomNotFoundException {
        final User user = get(id);
        userRepository.delete(user);
    }

    public Pageable<User> getAll(final int pageNumber, final int size) {
        final Page<User> users = userRepository.findAll(new PageRequest(pageNumber, size));
        return new Pageable<User>(
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                Lists.newArrayList(users.iterator()));
    }
}
