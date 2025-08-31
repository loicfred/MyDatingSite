package my.dating.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final VerifyUserDetails userDetailsService;
    public SecurityConfig(VerifyUserDetails userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home"
                                , "/accounts/signup", "/accounts/login"
                                , "/post/accounts/register", "/post/accounts/login"
                                , "/accounts/verify", "/css/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/accounts/login")
                        .loginProcessingUrl("/post/accounts/login")
                        .defaultSuccessUrl("/home")  // redirect after login
                        .failureUrl("/accounts/login?error")  // login error
                        .permitAll()
                )
                .userDetailsService(userDetailsService)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/accounts/login?logout")
                        .permitAll()
                );
        return http.build();
    }
}