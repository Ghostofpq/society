package com.gop.society.test.config;

import com.gop.society.repositories.UserRepository;
import com.gop.society.security.CustomAuthenticationProvider;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by VMPX4526 on 25/02/2015.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.gop.society.exceptions","com.gop.society.models","com.gop.society.services", "com.gop.society.controllers","com.gop.society.security"})
public class MockedRepositoriesConfig {
    @Bean
    @Primary
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
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
