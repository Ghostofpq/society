package config;

import com.gop.society.security.CustomAuthenticationProvider;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by VMPX4526 on 25/02/2015.
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.gop.society"})
public class MockedSecurityConfig {
    @Bean
    @Primary
    public CustomAuthenticationProvider customAuthenticationManager() {
        return Mockito.mock(CustomAuthenticationProvider.class);
    }
}
