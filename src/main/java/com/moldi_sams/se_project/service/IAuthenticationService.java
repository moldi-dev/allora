package com.moldi_sams.se_project.service;

import com.moldi_sams.se_project.request.user.SignInRequest;
import com.moldi_sams.se_project.request.user.SignUpRequest;
import com.moldi_sams.se_project.response.UserResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public interface IAuthenticationService {
    UserResponse signUp(SignUpRequest request);
    void signIn(SignInRequest request, HttpServletResponse response);
    void signOut(HttpServletResponse response);
}
