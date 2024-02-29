package com.camcyber.config;

import com.camcyber.entities.UserEntity;
import com.camcyber.repositories.UserRepository;
import com.camcyber.shares.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoUserDetailService implements UserDetailsService {

    @Autowired
    private  UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByPhone(phoneNumber);
        return user.map(UserInfoUserDetails::new).orElseThrow(()->new NotFoundException("User not found."));
    }
    public UserInfoUserDetailService(){}
}
