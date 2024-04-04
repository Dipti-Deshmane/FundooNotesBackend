package com.bridgelabz.fundoo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo.model.User;
import com.bridgelabz.fundoo.repository.UserRepository;
import com.bridgelabz.fundoo.response.ReqRes;
import com.bridgelabz.fundoo.services.AuthService;
import com.bridgelabz.fundoo.util.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<ReqRes> signUp(@RequestBody ReqRes signUpRequest){
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }
    
    @PostMapping("/signin")
    public ResponseEntity<ReqRes> signIn(@RequestBody ReqRes signInRequest){
        ReqRes response = authService.signIn(signInRequest);
        if (response.getStatusCode() == 400 && response.getError().equals("Email not verified")) {  // Email is not verified, return response with appropriate message
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } else {
            return ResponseEntity.ok(response);  // Email is verified or other error occurred, return response as it is
        }
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }
    
    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token){
        // Decode token and extract necessary information
        String userEmail = null;
        try {
            userEmail = jwtUtil.extractUsername(token); // Assuming JwtUtil has a method to extract email
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid token");
        }
                
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);   // Fetch user from database based on email
        if (!optionalUser.isPresent()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = optionalUser.get();

        // Update user's verification status
        user.setVerified(true);
        userRepository.save(user); // Save the updated user entity to the database
        
        return ResponseEntity.ok("Email verified successfully");
    }
}
