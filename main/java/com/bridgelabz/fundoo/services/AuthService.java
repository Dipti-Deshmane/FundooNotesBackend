package com.bridgelabz.fundoo.services;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoo.exception.UserAlreadyExistsException;
import com.bridgelabz.fundoo.model.User;
import com.bridgelabz.fundoo.repository.UserRepository;
import com.bridgelabz.fundoo.response.ReqRes;
import com.bridgelabz.fundoo.util.JwtUtil;

@Service
public class AuthService {
	@Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private EmailService emailService;
    
    public ReqRes signUp(ReqRes registrationRequest) {
        ReqRes response = new ReqRes();
        if (!registrationRequest.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,}$")) {
            response.setStatusCode(400); // Bad Request
            response.setError("Password must contain at least one digit, one lowercase character, one uppercase character, one special character, no whitespace, and at least 8 characters");
            return response;
        }
        try {
            Optional<User> existingUser = userRepository.findByEmail(registrationRequest.getEmail());
            if (existingUser.isPresent()) {
                String errorMessage = "User with email " + registrationRequest.getEmail() + " already exists.";
                response.setStatusCode(400); // Bad Request
                response.setError(errorMessage);
                throw new UserAlreadyExistsException(errorMessage);
            }
            User user = new User();
            user.setFirstName(registrationRequest.getFirstName());
            user.setLastName(registrationRequest.getLastName());
            user.setEmail(registrationRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setDateOfBirth(registrationRequest.getDateOfBirth()); // Assuming dateOfBirth is provided in the request

            User savedUser = userRepository.save(user);
            response.setUser(savedUser);
            response.setMessage("User Saved Successfully");
                        
            // Generate verification token
            String verificationToken = jwtUtil.generateToken(savedUser);

            // Send verification email
            sendVerificationEmail(savedUser.getEmail(), verificationToken);

            response.setStatusCode(200);  //Request success
        } catch (UserAlreadyExistsException e) {
            // Handled the exception, response is already set
        }
        return response;
    }
    
    public void sendVerificationEmail(String email, String verificationToken) {
  	  String verificationLink = "http://localhost:8080/auth/verify?token=" + verificationToken;
  	  String loginPageLink = "http://localhost:3000";
      String subject = "Verify Your Email";
      String body =  "Please click on the following link to verify your email and then proceed to login:\n" +
              verificationLink + "\n\n" +
              "If you have already verified your email, you can directly proceed to login here:\n" +
              loginPageLink;
        emailService.sendEmail(email, subject, body);
  }


    public ReqRes signIn(ReqRes signinRequest) {
        ReqRes response = new ReqRes();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(),signinRequest.getPassword()));
            var user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow();

            if (!user.isVerified()) {
                response.setStatusCode(400); // Bad Request
                response.setError("Email not verified");
                return response;
            }

            var jwt = jwtUtil.generateToken(user);
            var refreshToken = jwtUtil.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("This token expires in 24 hours.");
            response.setMessage("Successfully Signed In");
        } catch (BadCredentialsException e) {
            String errorMessage = "Incorrect username or password.";
            response.setStatusCode(400); // Bad request
            response.setError(errorMessage);
            throw new BadCredentialsException(errorMessage);
        }
        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRequest){
        ReqRes response = new ReqRes();
        String email = jwtUtil.extractUsername(refreshTokenRequest.getToken());
        User users = userRepository.findByEmail(email).orElseThrow();
        if (jwtUtil.validateToken(refreshTokenRequest.getToken(), users)) {
            var jwt = jwtUtil.generateToken(users);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenRequest.getToken());
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Refreshed Token");
        }
        response.setStatusCode(500);
        return response;
    }
}
