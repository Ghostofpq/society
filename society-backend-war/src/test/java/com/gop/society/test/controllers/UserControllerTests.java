package com.gop.society.test.controllers;

import com.gop.society.controllers.UserController;
import com.gop.society.models.User;
import com.gop.society.repositories.UserRepository;
import com.gop.society.security.AuthenticationManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by GhostOfPQ on 22/02/2015.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:META-INF/spring/userTestContext.xml")
public class UserControllerTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserController userController;

    @Test
    public void findOneUserShouldReturnOneUser() throws Exception {

        final User mockedUser = new User();
        mockedUser.setId("aze");
        mockedUser.setEmail("em@ai.l");
        mockedUser.setLogin("login");
        mockedUser.setPassword("password");

        Mockito.when(userRepository.findOne("id")).thenReturn(mockedUser);
        Mockito.when(authenticationManager.getAuthenticatedUserId()).thenReturn("id");

        log.debug(userController.get("id").toString());
    }

}
