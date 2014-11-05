package com.gop.society.services;

import com.google.common.collect.Lists;
import com.gop.society.exceptions.CustomInvalidFieldException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.exceptions.CustomUserNotFoundForIdException;
import com.gop.society.exceptions.CustomUserNotFoundForLoginException;
import com.gop.society.models.User;
import com.gop.society.repositories.UserRepository;
import com.gop.society.utils.EmailValidator;
import com.gop.society.utils.Pageable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

/**
 * @author GhostOfPQ
 */
@Slf4j
@Component("userService")
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private ShaPasswordEncoder passwordEncoder;
    private EmailValidator emailValidator;

    @PostConstruct
    private void init() throws NoSuchAlgorithmException {
        // Create MessageDigest instance for MD5
        passwordEncoder = new ShaPasswordEncoder(256);
        emailValidator = new EmailValidator();
    }

    public String encodePassword(final String password, final String salt) {
        return passwordEncoder.encodePassword(password, salt);
    }

    public boolean authenticate(final String encPassword, final String password, final String salt) {
        return passwordEncoder.isPasswordValid(encPassword, password, salt);
    }

    public User add(final User user) {
        return userRepository.save(user);
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

    public User updateLogin(final String id, final String newLogin)
            throws CustomNotFoundException, CustomInvalidFieldException {
        final User user = get(id);
        if (loginIsValid(newLogin)) {
            user.setLogin(newLogin);
            return userRepository.save(user);
        }
        throw new CustomInvalidFieldException("Login");
    }

    public User updatePassword(final String id, final String newPassword)
            throws CustomNotFoundException, CustomInvalidFieldException {
        final User user = get(id);
        if (passwordIsValid(newPassword)) {
            // Generate Salt
            final Random r = new SecureRandom();
            byte[] salt = new byte[32];
            r.nextBytes(salt);
            // Encode Password
            user.setSalt(new String(salt));
            user.setPassword(encodePassword(newPassword, user.getSalt()));
            return userRepository.save(user);
        }
        throw new CustomInvalidFieldException("Password");
    }

    public User updateEmail(final String id, final String newEmail)
            throws CustomNotFoundException, CustomInvalidFieldException {
        final User user = get(id);
        if (emailIsValid(newEmail)) {
            user.setEmail(newEmail);
            return userRepository.save(user);
        }
        throw new CustomInvalidFieldException("Email");
    }

    public User addRole(final String id, final String role)
            throws CustomNotFoundException, CustomInvalidFieldException {
        final User user = get(id);
        if (!user.getUserRole().contains(role)) {
            user.getUserRole().add(role);
            return userRepository.save(user);
        }
        throw new CustomInvalidFieldException("Role");
    }

    public User removeRole(final String id, final String role)
            throws CustomNotFoundException, CustomInvalidFieldException {
        final User user = get(id);
        if (user.getUserRole().contains(role)) {
            user.getUserRole().remove(role);
            return userRepository.save(user);
        }
        throw new CustomInvalidFieldException("Role");
    }

    public User updateRoles(final String id, final List<String> roles)
            throws CustomNotFoundException {
        final User user = get(id);
        user.setUserRole(roles);
        return userRepository.save(user);
    }

    public void delete(final String id) throws CustomNotFoundException {
        final User user = get(id);
        throw new CustomUserNotFoundForIdException(id);
    }

    public Pageable<User> getAll(final int pageNumber, final int size) {
        final Page<User> users = userRepository.findAll(new PageRequest(pageNumber, size));
        return new Pageable<User>(
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                Lists.newArrayList(users.iterator()));
    }

    private boolean loginIsValid(final String login) {
        log.debug("check {}", login);
        return (login != null && login.length() >= 3 && login.length() <= 18);
    }

    private boolean emailIsValid(final String email) {
        log.debug("check {}", email);
        return (email != null && emailValidator.validate(email));
    }

    private boolean passwordIsValid(final String password) {
        log.debug("check {}", password);
        return (password != null && password.length() >= 3 && password.length() <= 18);
    }
}
