package com.mnsoo.parkinglot.service;

import com.mnsoo.parkinglot.domain.Auth;
import com.mnsoo.parkinglot.domain.persist.UserEntity;
import com.mnsoo.parkinglot.exception.impl.LoginIdNotFoundException;
import com.mnsoo.parkinglot.exception.impl.PasswordDoNotMatchException;
import com.mnsoo.parkinglot.exception.impl.UserAlreadyExistsException;
import com.mnsoo.parkinglot.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return this.userRepository.findByLoginId(loginId)
                .orElseThrow(LoginIdNotFoundException::new);
    }

    public UserEntity authenticate(Auth.SignIn request){
        var user = this.userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(LoginIdNotFoundException::new);

        if(!this.passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new PasswordDoNotMatchException();
        }

        return user;
    }

    public UserEntity register(Auth.SignUp user){
        boolean exists = this.userRepository.existsByLoginId(user.getLoginId());
        if(exists){
            throw new UserAlreadyExistsException();
        }

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        var result = this.userRepository.save(user.toUserEntity());
        return result;
    }
}
