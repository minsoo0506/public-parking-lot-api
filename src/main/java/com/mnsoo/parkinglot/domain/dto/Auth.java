package com.mnsoo.parkinglot.domain.dto;

import com.mnsoo.parkinglot.domain.persist.UserEntity;
import lombok.Data;

import java.util.List;

public class Auth {

    @Data
    public static class SignIn{
        private String loginId;
        private String password;
    }

    @Data
    public static class SignUp{
        private String loginId;
        private String password;
        private String name;
        private String email;
        private List<String> roles;

        public UserEntity toUserEntity(){
            return UserEntity.builder()
                    .loginId(this.loginId)
                    .password(this.password)
                    .name(this.name)
                    .email(this.email)
                    .roles(this.roles)
                    .build();
        }
    }
}
