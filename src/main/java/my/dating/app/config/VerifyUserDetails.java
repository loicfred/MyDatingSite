package my.dating.app.config;

import my.dating.app.object.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class VerifyUserDetails implements UserDetailsService {

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