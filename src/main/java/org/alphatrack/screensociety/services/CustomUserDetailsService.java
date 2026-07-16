package org.alphatrack.screensociety.services;

import org.alphatrack.screensociety.exceptions.EntityNotFoundException;
import org.alphatrack.screensociety.models.User;
import org.alphatrack.screensociety.repositories.contracts.UserRepository;
import org.alphatrack.screensociety.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

import static org.springframework.security.core.userdetails.User.withUsername;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User","username",username));

        Set<GrantedAuthority> authorities = Set.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        return new CustomUserDetails(user);

//        return org.springframework.security.core.userdetails.User
//                .withUsername(user.getUsername())
//                .password(user.getPassword())
//                .authorities(authorities)
//                .build();


    }
}
