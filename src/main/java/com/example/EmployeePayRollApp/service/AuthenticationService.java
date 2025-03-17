package com.example.EmployeePayRollApp.service;

import com.example.EmployeePayRollApp.dto.AuthUserDTO;
import com.example.EmployeePayRollApp.dto.LoginDTO;
import com.example.EmployeePayRollApp.dto.PassDTO;
import com.example.EmployeePayRollApp.interfaces.AuthInterface;
import com.example.EmployeePayRollApp.entity.AuthUser;
import com.example.EmployeePayRollApp.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@Slf4j
public class AuthenticationService implements AuthInterface {

    ObjectMapper obj = new ObjectMapper();

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    JwtTokenService jwtTokenService;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public String register(AuthUserDTO user) throws Exception{
        List<AuthUser> l1 = userRepository.findAll().stream().filter(authuser -> user.getEmail().equals(authuser.getEmail())).collect(Collectors.toList());

        if(l1.size()>0){
            log.error("User already registered with email: {} ", user.getEmail());
            return "User already registered";
        }

        //creating hashed password using bcrypt
        String hashPass = bCryptPasswordEncoder.encode(user.getPassword());

        //creating new user
        AuthUser newUser = new AuthUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), hashPass);

        //setting the new hashed password
        newUser.setHashPass(hashPass);

        //saving the user in the database
        userRepository.save(newUser);

        log.info("User saved in database : {}", obj.writeValueAsString(newUser));

        //sending the confirmation mail to the user
        emailService.sendEmail(user.getEmail(), "Your Account is Ready!", "UserName : "+user.getFirstName()+" "+user.getLastName()+"\nEmail : "+user.getEmail()+"\nYou are registered!");

        return "User registered";
    }


    public String login(LoginDTO user){

        List<AuthUser> l1 = userRepository.findAll().stream().filter(authuser -> authuser.getEmail().equals(user.getEmail())).collect(Collectors.toList());
        if(l1.size() == 0) {
            log.error("User not registered with email: {} ", user.getEmail());
            return "User not registered";
        }
        AuthUser foundUser = l1.get(0);

        //matching the stored hashed password with the password provided by user

        if(!bCryptPasswordEncoder.matches(user.getPassword(), foundUser.getHashPass())) {
            log.error("Invalid password entered for email {} where entered password is {}", user.getEmail(), user.getPassword());
            return "Invalid password";
        }

        //creating Jwt Token
        String token = jwtTokenService.createToken(foundUser.getId());

        //setting token for user login
        foundUser.setToken(token);

        //saving the current status of user in database
        userRepository.save(foundUser);

        log.info("User logged in with email {}", user.getEmail());

        return "User logged in"+"\ntoken : "+token;
    }

    public AuthUserDTO forgotPassword(PassDTO pass, String email) throws Exception{

        AuthUser foundUser = userRepository.findByEmail(email);

        if(foundUser == null) {
            log.error("User not registered with email: {}", email);
            throw new RuntimeException("User not registered!");
        }
        String hashpass = bCryptPasswordEncoder.encode(pass.getPassword());

        foundUser.setPassword(pass.getPassword());
        foundUser.setHashPass(hashpass);

        log.info("Hashpassword : {} for password : {} saved for user: {}", hashpass, pass.getPassword(), obj.writeValueAsString(foundUser));

        userRepository.save(foundUser);

        emailService.sendEmail(email, "Password Forgot Status", "Your password has been changed!");

        AuthUserDTO resDto = new AuthUserDTO(foundUser.getFirstName(), foundUser.getLastName(), foundUser.getEmail(), foundUser.getPassword(), foundUser.getId() );

        return resDto;
    }

    public String resetPassword(String email, String currentPass, String newPass) throws  Exception{
        AuthUser foundUser = userRepository.findByEmail(email);

        if(foundUser == null) {
            return "User not registered!";
        }

        if(!bCryptPasswordEncoder.matches(currentPass, foundUser.getHashPass())) {
            return "Incorrect password!";
        }

        String hashpass = bCryptPasswordEncoder.encode(newPass);

        foundUser.setHashPass(hashpass);
        foundUser.setPassword(newPass);

        userRepository.save(foundUser);

        log.info("Hashpassword : {} for password : {} saved for user : {}", hashpass, newPass, obj.writeValueAsString(foundUser));

        emailService.sendEmail(email, "Password reset status", "Your password is reset successfully");

        return "Password reset successfull!";
    }
}