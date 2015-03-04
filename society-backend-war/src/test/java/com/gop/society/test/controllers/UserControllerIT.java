package com.gop.society.test.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gop.society.Application;
import com.gop.society.controllers.UserController;
import com.gop.society.models.User;
import com.gop.society.repositories.UserRepository;
import com.gop.society.security.CustomAuthenticationManager;
import com.gop.society.test.config.MockedRepositoriesConfig;
import com.gop.society.utils.UserCreationRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by GhostOfPQ on 22/02/2015.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MockedRepositoriesConfig.class)
@WebAppConfiguration
//@ContextConfiguration(classes = MockedRepositoriesConfig.class)
//@ContextConfiguration(locations = "classpath*:META-INF/spring/userTestContext.xml")
public class UserControllerIT {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomAuthenticationManager customAuthenticationManager;

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
    public void createUserShouldGoFine() throws Exception {
        final String testEmail = "em@ai.l";
        final String testLogin = "login";
        final String testPwd = "password";

        final UserCreationRequest userCreationRequest = new UserCreationRequest();
        userCreationRequest.setEmail(testEmail);
        userCreationRequest.setLogin(testLogin);
        userCreationRequest.setPassword(testPwd);

        when(userRepository.save(any(User.class))).thenAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return (User) args[0];
            }
        });

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
    }

    @Test
    public void createUserShouldGoWrong() throws Exception {
        final String testEmail = "em@ai.l";
        final String testLogin = "login";
        final String testPwd = "password";

        final UserCreationRequest userCreationRequest = new UserCreationRequest();
        userCreationRequest.setEmail(testEmail);
        userCreationRequest.setLogin(testLogin);
        userCreationRequest.setPassword(testPwd);

        when(userRepository.save(any(User.class))).thenAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocation) throws Throwable {
                throw new Exception();
            }
        });

        final ObjectMapper mapper = new ObjectMapper();
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        mapper.writeValue(os, userCreationRequest);
        final String asJSON = os.toString();
        os.close();
        mockMvc.perform(post("/api/users/").content(asJSON).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().is(400));
    }

    @Test
    public void findOneUserShouldReturnOneUser() throws Exception {
        final String testId = "id";
        final String testEmail = "em@ai.l";
        final String testLogin = "login";
        final String testPwd = "password";

        final User mockedUser = new User();
        mockedUser.setId(testId);
        mockedUser.setEmail(testEmail);
        mockedUser.setLogin(testLogin);
        mockedUser.setPassword(testPwd);

        when(userRepository.findOne(testId)).thenReturn(mockedUser);
        when(customAuthenticationManager.getAuthenticatedUserId()).thenReturn(testId);
        mockMvc.perform(get("/api/users/{id}",testId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(testId)))
                .andExpect(jsonPath("$.login", is(testLogin)))
                .andExpect(jsonPath("$.email", is(testEmail)));
    }

    @Test
    public void findOneUserShouldReturn404WhenUserIsNotFound() throws Exception {
        final String testId = "id";
        when(userRepository.findOne(testId)).thenReturn(null);
        when(customAuthenticationManager.getAuthenticatedUserId()).thenReturn(testId);

        mockMvc.perform(get("/api/users/{id}",testId))
                .andExpect(status().is(404));
    }
    @Test
    public void findOneUserShouldReturn403WhenUserIsNotAuthorized() throws Exception {
        final String testId = "id";
        final String testEmail = "em@ai.l";
        final String testLogin = "login";
        final String testPwd = "password";

        final User mockedUser = new User();
        mockedUser.setId(testId);
        mockedUser.setEmail(testEmail);
        mockedUser.setLogin(testLogin);
        mockedUser.setPassword(testPwd);

        when(userRepository.findOne(testId)).thenReturn(mockedUser);
        when(customAuthenticationManager.getAuthenticatedUserId()).thenReturn("other");

        mockMvc.perform(get("/api/users/{id}",testId))
                .andExpect(status().is(403));
    }


}
