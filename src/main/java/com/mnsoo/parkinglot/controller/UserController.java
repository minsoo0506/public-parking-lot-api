package com.mnsoo.parkinglot.controller;

import com.mnsoo.parkinglot.domain.Auth;
import com.mnsoo.parkinglot.security.TokenProvider;
import com.mnsoo.parkinglot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Auth.SignUp request){
        var result = this.userService.register(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody Auth.SignIn request){
        var user = this.userService.authenticate(request);
        var token = this.tokenProvider.generateToken(user.getLoginId(), user.getRoles());

        return ResponseEntity.ok(token);
    }
}
