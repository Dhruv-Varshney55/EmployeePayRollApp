package com.example.EmployeePayRollApp.interfaces;

import com.example.EmployeePayRollApp.dto.AuthUserDTO;
import com.example.EmployeePayRollApp.dto.LoginDTO;
import com.example.EmployeePayRollApp.dto.PassDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthInterface {
    public String register(AuthUserDTO user) throws Exception;
    public String login(LoginDTO user);
    public AuthUserDTO forgotPassword(PassDTO pass, String email) throws Exception;
    public String resetPassword(String email, String currentPass, String newPass) throws Exception;
}