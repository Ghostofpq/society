package com.gop.society.services;

import com.google.common.collect.Lists;
import com.gop.society.models.Pageable;
import com.gop.society.models.User;
import com.gop.society.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author GhostOfPQ
 */
@Slf4j
@Component("userService")
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private MessageDigest md;
    @PostConstruct
    private void setUpEncoder() throws NoSuchAlgorithmException {
        // Create MessageDigest instance for MD5
        md = MessageDigest.getInstance("MD5");
    }

    public String encodePassword(final String password) {
        //Add password bytes to digest
        md.update(password.getBytes());
        //Get the hash's bytes
        byte[] bytes = md.digest();
        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        md.reset();
        //Get complete hashed password in hex format
        return sb.toString();
    }

    public User add(final User user) {
        return userRepository.save(user);
    }

    public User get(final String id) {
        return userRepository.findOne(id);
    }

    public User getByLogin(final String login) {
        return userRepository.findByLogin(login);
    }

    public User update(final String id, final User user) {
        user.setId(id);
        return userRepository.save(user);
    }

    public void delete(final String id) {
        userRepository.delete(id);
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
