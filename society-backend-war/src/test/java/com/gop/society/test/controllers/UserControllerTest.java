package com.gop.society.test.controllers;

import com.gop.society.controllers.UserController;
import com.gop.society.models.User;
import com.gop.society.repositories.UserRepository;
import com.gop.society.security.AuthenticationManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by GhostOfPQ on 22/02/2015.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:META-INF/spring/userTestContext.xml")
@WebAppConfiguration
public class UserControllerTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserController userController;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @PostConstruct
    private void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    @Test
    public void findOneUserShouldReturnOneUser() throws Exception {
        final User mockedUser = new User();
        mockedUser.setId("aze");
        mockedUser.setEmail("em@ai.l");
        mockedUser.setLogin("login");
        mockedUser.setPassword("password");

        when(userRepository.findOne("aze")).thenReturn(mockedUser);
        when(authenticationManager.getAuthenticatedUserId()).thenReturn("aze");

        mockMvc.perform(get("/users/aze"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is("aze")))
                .andExpect(jsonPath("$.login", is("login")))
                .andExpect(jsonPath("$.email", is("em@ai.l")));
    }

    @Test
    public void findOneUserShouldReturn404WhenUserIsNotFound() throws Exception {
        when(userRepository.findOne("aze")).thenReturn(null);
        when(authenticationManager.getAuthenticatedUserId()).thenReturn("aze");


        mockMvc.perform(get("/users/aze"))
                .andExpect(status().is(404));
    }

}
