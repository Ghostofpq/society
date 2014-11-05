package com.gop.society.services;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.security.SecureRandom;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/spring/service-tu-context.xml"})
@Slf4j
public class UserServiceTest {
    @InjectMocks
    @Autowired
    private UserService userService;

    @Test
    public void testEncodePassword() throws Exception {
        // Generate Salt
        final Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);
        final String saltEnStringCommeTaMere = new String(salt);
        final String password = "bob";
        final String encodedPassword1 = userService.encodePassword(password, saltEnStringCommeTaMere);
        final String encodedPassword2 = userService.encodePassword(password, saltEnStringCommeTaMere);
        final String encodedPassword3 = userService.encodePassword(password, saltEnStringCommeTaMere);

        log.debug("1:] " + encodedPassword1);
        log.debug("2:] " + encodedPassword2);
        log.debug("3:] " + encodedPassword3);
    }
}