// package com.app.countrydata.Controller;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.app.countrydata.Security.JwtHelper;
// import com.app.countrydata.Security.JwtRequest;
// import com.app.countrydata.Security.JwtResponse;

// // // Controller logic  for handling JWT requests and generating token
// // @RestController
// // @RequestMapping("/auth")
// // public class AuthController {

// //     @Autowired
// //     private UserDetailsService userDetailsService;

// //     @Autowired
// //     private AuthenticationManager manager;


// //     @Autowired
// //     private JwtHelper helper;

// //     // Handles login request from the client, generates a token if authentication is successful
// //     @PostMapping("/login")
// //     public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {

// //         this.doAuthenticate(request.getUsername(), request.getPassword());


// //         UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
// //         String token = this.helper.generateToken(userDetails);

// //         JwtResponse response = JwtResponse.builder()
// //                 .jwtToken(token)
// //                 .userName(userDetails.getUsername()).url("http://localhost:8080/api/country/India").build();
// //         return new ResponseEntity<>(response, HttpStatus.OK);
// //     }
// //     /*Validates username / password against our database. If it's valid, we generate a token and send it back in**/

// //     private void doAuthenticate(String username, String password) {

// //         UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
// //         try {
// //             manager.authenticate(authentication);


// //         } catch (BadCredentialsException e) {
// //             throw new BadCredentialsException(" Invalid Username or Password  !!");
// //         }

// //     }

// //     @ExceptionHandler(BadCredentialsException.class)
// //     public String exceptionHandler() {
// //         return "Credentials Invalid !!";
// //     }

// // }

