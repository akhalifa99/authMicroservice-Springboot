package sumerge.com.example.springBootProject.authMicroservice.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sumerge.com.example.springBootProject.authMicroservice.Models.UserEntity;
import sumerge.com.example.springBootProject.authMicroservice.Security.JwtUtil;
import sumerge.com.example.springBootProject.authMicroservice.Services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil){
        this.userService=userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public void registerUser(@RequestBody UserEntity userEntity){
        userService.registerUser(userEntity.getEmail(), userEntity.getPassword());
    }
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserEntity userEntity){
        if (userService.authenticateUser(userEntity.getEmail(), userEntity.getPassword())){
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userEntity.getEmail(),
                            userEntity.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtil.generateToken(authentication);
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }

}
