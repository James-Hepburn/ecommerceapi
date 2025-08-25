package com.example.ecommerceapi.service;

import com.example.ecommerceapi.model.User;
import com.example.ecommerceapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername (username).orElseThrow (() -> new UsernameNotFoundException ("User not found"));
        return org.springframework.security.core.userdetails.User.builder ()
                .username (user.getUsername ())
                .password (user.getPassword ())
                .authorities (user.getRoles ().stream ().toArray (String []::new))
                .build ();
    }
}
