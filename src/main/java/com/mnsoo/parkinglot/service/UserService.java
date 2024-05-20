package com.mnsoo.parkinglot.service;

import com.mnsoo.parkinglot.domain.dto.Auth;
import com.mnsoo.parkinglot.domain.persist.UserEntity;
import com.mnsoo.parkinglot.exception.impl.LoginIdNotFoundException;
import com.mnsoo.parkinglot.exception.impl.PasswordDoNotMatchException;
import com.mnsoo.parkinglot.exception.impl.UserAlreadyExistsException;
import com.mnsoo.parkinglot.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        logger.info("Loading user by username: {}", loginId);
        return this.userRepository.findByLoginId(loginId)
                .orElseThrow(LoginIdNotFoundException::new);
    }

    public UserEntity authenticate(Auth.SignIn request){
        logger.info("Authenticating user: {}", request.getLoginId());
        var user = this.userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(LoginIdNotFoundException::new);

        if(!this.passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new PasswordDoNotMatchException();
        }

        return user;
    }

    public UserEntity getCurrentUser(){
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.info("Getting current user: {}", user.getLoginId());
        return user;
    }

    public UserEntity register(Auth.SignUp user){
        logger.info("Registering user: {}", user.getLoginId());
        boolean exists = this.userRepository.existsByLoginId(user.getLoginId());
        if(exists){
            logger.warn("User already exists: {}", user.getLoginId());
            throw new UserAlreadyExistsException();
        }

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        var result = this.userRepository.save(user.toUserEntity());
        return result;
    }

    public UserEntity updateAccount(Auth.SignUp user){
        logger.info("Updating account for user: {}", user.getLoginId());
        UserEntity currentUser = getCurrentUser();

        if(user.getLoginId() != null){
            currentUser.setLoginId(user.getLoginId());
        }
        if(user.getPassword() != null){
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));
            currentUser.setPassword(user.getPassword());
        }
        if(user.getName() != null){
            currentUser.setName(user.getName());
        }
        if(user.getEmail() != null){
            currentUser.setEmail(user.getEmail());
        }

        userRepository.save(currentUser);
        return currentUser;
    }

    public String deleteAccount(){
        UserEntity currentUser = getCurrentUser();
        logger.info("Deleting account for user: {}", currentUser.getLoginId());
        userRepository.delete(currentUser);
        return "Account deleted successfully";
    }
}
