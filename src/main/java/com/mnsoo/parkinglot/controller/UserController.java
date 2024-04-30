package com.mnsoo.parkinglot.controller;

import com.mnsoo.parkinglot.domain.dto.Auth;
import com.mnsoo.parkinglot.security.TokenProvider;
import com.mnsoo.parkinglot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> updateAccount(@RequestBody Auth.SignUp request){
        var result = this.userService.updateAccount(request);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> deleteAccount(){
        var result = this.userService.deleteAccount();
        return ResponseEntity.ok(result);
    }
}
