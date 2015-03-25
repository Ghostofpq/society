package com.gop.society.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gop.society.models.User;
import com.gop.society.repositories.UserRepository;
import com.gop.society.security.CustomAuthenticationProvider;
import com.gop.society.test.config.MockedSecurityConfig;
import com.gop.society.utils.UserCreationRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

import static org.hamcrest.Matchers.is;
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
    public void createUserShouldGoFine() throws Exception {
        final String testEmail = "em@ai.l";
        final String testLogin = "login";
        final String testPwd = "password";

        final UserCreationRequest userCreationRequest = new UserCreationRequest();
        userCreationRequest.setEmail(testEmail);
        userCreationRequest.setLogin(testLogin);
        userCreationRequest.setPassword(testPwd);
        final ObjectMapper mapper = new ObjectMapper();
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        mapper.writeValue(os, userCreationRequest);
        final String asJSON = os.toString();
        os.close();
        mockMvc.perform(post("/api/users/").content(asJSON).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.login", is(testLogin)))
                .andExpect(jsonPath("$.email", is(testEmail)));
        User user = userRepository.findByLogin(testLogin);
        Assert.assertTrue(user != null);


    }

}
