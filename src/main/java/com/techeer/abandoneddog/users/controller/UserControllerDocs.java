//package com.techeer.abandoneddog.users.controller;
//
//import com.techeer.abandoneddog.users.dto.LoginRequestDto;
//import com.techeer.abandoneddog.users.dto.RegisterRequestDto;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.Parameters;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.http.ResponseEntity;
//
//@Tag(name = "User Api", description = "회원가입 및 로그인")
//public interface UserControllerDocs {
//
//    @Parameters(value = {
//            @Parameter(name = "username", description = "유저 아이디"),
//            @Parameter(name = "email", description = "이메일"),
//            @Parameter(name = "password", description = "비밀번호"),
//            @Parameter(name = "phoneNum", description = "전화번호")
//    })
//    @Operation(summary = "회원가입", description = "유저이름, 이메일, 비밀번호, 전화번호를 입력받는다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "회원가입 성공")
//    })
//    public ResponseEntity<?> register(RegisterRequestDto registerRequestDto);
//
//    @Parameters(value = {
//            @Parameter(name = "email", description = "이메일"),
//            @Parameter(name = "password", description = "비밀번호")
//    })
//    @Operation(summary = "로그인", description = "이메일, 비밀번호를 통해 로그인")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "로그인 성공")
//    })
//    public ResponseEntity<?> login(LoginRequestDto loginRequestDto);
//
//}
