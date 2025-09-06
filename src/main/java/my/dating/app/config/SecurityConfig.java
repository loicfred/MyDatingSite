package my.dating.app.config;

import my.dating.app.object.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;

import java.util.Collections;

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

    @Service
    private static class VerifyUserDetails implements UserDetailsService {
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            User user = User.getByUsername(username);
            if (user == null) user = User.getByEmail(username);
            if (user == null) throw new UsernameNotFoundException("User not found");
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .disabled(!user.isEnabled()) // prevent login if not verified
                    .authorities(Collections.emptyList())
                    .build();
        }
    }
}