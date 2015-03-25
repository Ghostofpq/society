package com.gop.society.test.config;

import com.gop.society.repositories.AccountRepository;
import com.gop.society.repositories.CurrencyRepository;
import com.gop.society.repositories.OrganisationRepository;
import com.gop.society.repositories.UserRepository;
import com.gop.society.security.CustomAuthenticationProvider;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by VMPX4526 on 25/02/2015.
 */
public class MockedRepositoriesConfig {
    @Bean
    @Primary
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    @Primary
    public CurrencyRepository currencyRepository() {
        return Mockito.mock(CurrencyRepository.class);
    }

    @Bean
    @Primary
    public AccountRepository accountRepository() {
        return Mockito.mock(AccountRepository.class);
    }

    @Bean
    @Primary
    public OrganisationRepository organisationRepository() {
        return Mockito.mock(OrganisationRepository.class);
    }

    @Bean
    @Primary
    public CustomAuthenticationProvider customAuthenticationManager() {
        return Mockito.mock(CustomAuthenticationProvider.class);
    }

    @Bean
    @Primary
    public MongoTemplate mongoTemplate() {
        return Mockito.mock(MongoTemplate.class);
    }
}
