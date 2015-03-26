package com.gop.society.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gop.society.models.User;
import com.gop.society.repositories.UserRepository;
import com.gop.society.security.CustomAuthenticationProvider;
import com.gop.society.test.config.MockedSecurityConfig;
import com.gop.society.utils.UserCreationRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by VMPX4526 on 25/03/2015.
 */

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockedSecurityConfig.class)
@WebAppConfiguration
public class GlobalIT {
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CharacterEncodingFilter characterEncodingFilter;

    private MockMvc mockMvc;

    @PostConstruct
    private void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(this.characterEncodingFilter).build();
    }

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    @Before
    public void cleanDataBase() {
        log.debug("X wipe userRepository X");
        userRepository.deleteAll();
    }

    @After
    public void printDatabase() {
        log.debug("O userRepository O");
        for (User u : userRepository.findAll()) {
            log.debug(u.toString());
        }
    }

    private final String login1 = "login1";
    private final String pwd1 = "pwd1";
    private final String email1 = "email1";

    private final UserCreationRequest userCreationRequest1 = new UserCreationRequest(login1, pwd1, email1);

    private final String login2 = "login2";
    private final String pwd2 = "pwd2";
    private final String email2 = "email2";

    private final UserCreationRequest userCreationRequest2 = new UserCreationRequest(login2, pwd2, email2);


    @Test
    public void createUserShouldGoFine() throws Exception {
        mockMvc.perform(post("/api/users/").content(objToJson(userCreationRequest1)).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.login", is(login1)))
                .andExpect(jsonPath("$.email", is(email1)));
        User user = userRepository.findByLogin(login1);
        assertTrue(user != null);
    }

    @Test
    public void createUserAlreadyCreatedShouldGoWrong() throws Exception {
        createUserShouldGoFine();

        mockMvc.perform(post("/api/users/").content(objToJson(userCreationRequest1)).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().is(400))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8));
    }


    private String objToJson(Object o) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        mapper.writeValue(os, o);
        final String asJSON = os.toString();
        os.close();
        return asJSON;
    }
}
