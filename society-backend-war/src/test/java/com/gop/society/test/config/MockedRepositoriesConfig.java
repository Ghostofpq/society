package com.gop.society.test.config;

import com.gop.society.repositories.AccountRepository;
import com.gop.society.repositories.CurrencyRepository;
import com.gop.society.repositories.OrganizationRepository;
import com.gop.society.repositories.UserRepository;
import com.gop.society.security.CustomAuthenticationManager;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created by VMPX4526 on 25/02/2015.
 */
@Configuration
@ComponentScan(basePackages = {"com.gop.society.exceptions","com.gop.society.models","com.gop.society.services", "com.gop.society.controllers", "com.gop.society.security"})
public class MockedRepositoriesConfig {
    @Bean
    @Primary
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    @Primary
    public AccountRepository accountRepository() {
        return Mockito.mock(AccountRepository.class);
    }

    @Bean
    @Primary
    public CurrencyRepository currencyRepository() {
        return Mockito.mock(CurrencyRepository.class);
    }

    @Bean
    @Primary
    public OrganizationRepository organizationRepository() {
        return Mockito.mock(OrganizationRepository.class);
    }

    @Bean
    @Primary
    public CustomAuthenticationManager customAuthenticationManager() {
        return Mockito.mock(CustomAuthenticationManager.class);
    }

    @Bean
    @Primary
    public MongoTemplate mongoTemplate() {
        return Mockito.mock(MongoTemplate.class);
    }
}
