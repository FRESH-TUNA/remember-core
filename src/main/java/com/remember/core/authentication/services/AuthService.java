package com.remember.core.authentication.services;

import com.remember.core.authentication.dtos.UserIdentity;
import com.remember.core.authentication.exceptions.RememberAuthenticationException;
import com.remember.core.exceptions.ErrorCode;
import com.remember.core.authentication.dtos.RememberUserDetails;
import com.remember.core.authentication.repositories.UsersRepository;
import com.remember.core.authentication.domains.User;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final UsersRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RememberAuthenticationException(ErrorCode.BAD_EMAIL_PASSWORD));

        return RememberUserDetails.builder()
                .userIdentity(UserIdentity.of(user))
                .username(user.getEmail()).password(user.getPassword())
                .roles(Collections.singletonList(user.getRole().name()))
                .build();
    }
}
