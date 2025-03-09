package com.moldi.allora.service;

import com.moldi.allora.request.user.SignInRequest;
import com.moldi.allora.request.user.SignUpRequest;
import com.moldi.allora.response.UserResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public interface IAuthenticationService {
    UserResponse signUp(SignUpRequest request);
    void signIn(SignInRequest request, HttpServletResponse response);
    void signOut(HttpServletResponse response);
}
