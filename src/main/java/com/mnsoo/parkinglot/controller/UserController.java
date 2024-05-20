package com.mnsoo.parkinglot.controller;

import com.mnsoo.parkinglot.domain.dto.Auth;
import com.mnsoo.parkinglot.security.TokenProvider;
import com.mnsoo.parkinglot.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"회원 정보를 다루는 Controller"})
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @ApiOperation(value = "회원 정보를 등록합니다.(회원가입)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "request", value = "회원 가입 정보", required = true, dataType = "Auth.SignUp", paramType = "body")
    })
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Auth.SignUp request){
        var result = this.userService.register(request);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "로그인을 진행합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "request", value = "로그인 정보", required = true, dataType = "Auth.SignIn", paramType = "body")
    })
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody Auth.SignIn request){
        var user = this.userService.authenticate(request);
        var token = this.tokenProvider.generateToken(user.getLoginId(), user.getRoles());

        return ResponseEntity.ok(token);
    }

    @ApiOperation(value = "회원 정보를 업데이트 합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "request", value = "업데이트할 회원 정보", required = true, dataType = "Auth.SignUp", paramType = "body")
    })
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> updateAccount(@RequestBody Auth.SignUp request){
        var result = this.userService.updateAccount(request);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "회원 정보를 삭제합니다.(탈퇴)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> deleteAccount(){
        var result = this.userService.deleteAccount();
        return ResponseEntity.ok(result);
    }
}
