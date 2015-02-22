package com.gop.society.business.controllers;

import com.gop.society.models.User;
import com.gop.society.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by GhostOfPQ on 22/02/2015.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"**/resources/testContext.xml"})
@WebAppConfiguration("/resources/testWeb.xml")
public class UserControllerTests {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @PostConstruct
    private void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void findOneUserShouldReturnOneUser() throws Exception {
        final User mockedUser = new User();
        mockedUser.setId("aze");
        mockedUser.setEmail("em@ai.l");
        mockedUser.setLogin("login");
        mockedUser.setPassword("password");

        when(userService.get("id")).thenReturn(mockedUser);

        log.debug(userController.get("id").toString());

        mockMvc.perform(get("/api/users/aze"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("aze")))
                .andExpect(jsonPath("$[0].email", is("em@ai.l")))
                .andExpect(jsonPath("$[0].login", is("login")))
                .andExpect(jsonPath("$[0].encodedPassword", not("password")));
    }

}
