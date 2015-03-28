package com.gop.society.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gop.society.models.Account;
import com.gop.society.models.Currency;
import com.gop.society.models.Organisation;
import com.gop.society.models.User;
import com.gop.society.repositories.AccountRepository;
import com.gop.society.repositories.CurrencyRepository;
import com.gop.society.repositories.OrganisationRepository;
import com.gop.society.repositories.UserRepository;
import com.gop.society.security.CustomAuthenticationProvider;
import com.gop.society.test.config.MockedSecurityConfig;
import com.gop.society.utils.AccountType;
import com.gop.society.utils.CurrencyCreationRequest;
import com.gop.society.utils.OrganisationCreationRequest;
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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
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
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );
    private final String login1 = "login1";
    private final String pwd1 = "pwd1";
    private final String email1 = "email1";
    private final UserCreationRequest userCreationRequest1 = new UserCreationRequest(login1, pwd1, email1);
    private final String login2 = "login2";
    private final String pwd2 = "pwd2";
    private final String email2 = "email2";
    private final UserCreationRequest userCreationRequest2 = new UserCreationRequest(login2, pwd2, email2);
    private final String organisationName = "organisationName";
    private final String organisationDesc = "organisationDesc";
    private final String currencyName = "currencyName";
    private final long currencyInitialAmount = 123l;
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganisationRepository organisationRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CharacterEncodingFilter characterEncodingFilter;
    private MockMvc mockMvc;

    @PostConstruct
    private void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(this.characterEncodingFilter).build();
    }

    @Before
    public void cleanDataBase() {
        log.debug("=========================> cleanDataBase");
        userRepository.deleteAll();
        organisationRepository.deleteAll();
        currencyRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @After
    public void printDatabase() {
        log.debug("database content <=========================");
        log.debug("userRepository :");
        for (User u : userRepository.findAll()) {
            log.debug(u.toString());
        }
        log.debug("organisationRepository :");
        for (Organisation o : organisationRepository.findAll()) {
            log.debug(o.toString());
        }
        log.debug("currencyRepository :");
        for (Currency c : currencyRepository.findAll()) {
            log.debug(c.toString());
        }
        log.debug("accountRepository :");
        for (Account a : accountRepository.findAll()) {
            log.debug(a.toString());
        }
    }

    @Test
    public void createUserShouldGoFine() throws Exception {
        mockMvc.perform(post("/api/users/").content(objToJson(userCreationRequest1)).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.login", is(login1)))
                .andExpect(jsonPath("$.email", is(email1)));
        final User user = userRepository.findByLogin(login1);
        assertTrue(user != null);
    }

    @Test
    public void createUserAlreadyCreatedShouldGoWrong() throws Exception {
        createUserShouldGoFine();

        mockMvc.perform(post("/api/users/").content(objToJson(userCreationRequest1)).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().is(400))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8));
    }

    @Test
    public void createOrganisationShouldGoFine() throws Exception {
        createUserShouldGoFine();
        final User user = userRepository.findByLogin(login1);
        when(customAuthenticationProvider.getAuthenticatedUserId()).thenReturn(user.getId());

        final OrganisationCreationRequest organisationCreationRequest = new OrganisationCreationRequest(organisationName, organisationDesc);

        mockMvc.perform(post("/api/organisations/").content(objToJson(organisationCreationRequest)).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is(organisationName)))
                .andExpect(jsonPath("$.description", is(organisationDesc)))
                .andExpect(jsonPath("$.managers", contains(user.getId())))
                .andExpect(jsonPath("$.managers", hasSize(1)))
                .andExpect(jsonPath("$.members", contains(user.getId())))
                .andExpect(jsonPath("$.members", hasSize(1)))
                .andExpect(jsonPath("$.accounts", hasSize(0)));

        final Organisation organisation = organisationRepository.findByName(organisationName);
        assertTrue(organisation != null);
    }

    @Test
    public void createCurrencyShouldGoFine() throws Exception {
        createOrganisationShouldGoFine();
        final User user = userRepository.findByLogin(login1);
        final Organisation organisation = organisationRepository.findByName(organisationName);

        final CurrencyCreationRequest currencyCreationRequest = new CurrencyCreationRequest(currencyName, currencyInitialAmount);

        mockMvc.perform(post("/api/organisations/" + organisation.getId() + "/currency").content(objToJson(currencyCreationRequest)).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is(organisationName)))
                .andExpect(jsonPath("$.description", is(organisationDesc)))
                .andExpect(jsonPath("$.managers", contains(user.getId())))
                .andExpect(jsonPath("$.managers", hasSize(1)))
                .andExpect(jsonPath("$.members", contains(user.getId())))
                .andExpect(jsonPath("$.members", hasSize(1)))
                .andExpect(jsonPath("$.accounts", hasSize(1)));

        final Currency currency = currencyRepository.findByName(currencyName);
        assertTrue(currency != null);

        final Account account = accountRepository.findByOwnerIdAndCurrencyIdAndAccountType(organisation.getId(), currency.getId(), AccountType.ORGANISATION);
        assertTrue(account != null);
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
