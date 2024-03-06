package com.sparta.lecture.global.security;

import com.sparta.lecture.global.entity.User;
import com.sparta.lecture.global.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Not Found " + email);
        }
        return new UserDetailsImpl(user);
    }
//                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + email));
//
//        return new UserDetailsImpl(user);
//    }
}