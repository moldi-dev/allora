package com.moldi_sams.se_project.service.implementation;

import com.moldi_sams.se_project.entity.User;
import com.moldi_sams.se_project.entity.UserPersonalInformation;
import com.moldi_sams.se_project.enumeration.Role;
import com.moldi_sams.se_project.exception.ResourceAlreadyExistsException;
import com.moldi_sams.se_project.exception.ResourceNotFoundException;
import com.moldi_sams.se_project.mapper.UserMapper;
import com.moldi_sams.se_project.repository.UserRepository;
import com.moldi_sams.se_project.request.user.SignInRequest;
import com.moldi_sams.se_project.request.user.SignUpRequest;
import com.moldi_sams.se_project.response.UserResponse;
import com.moldi_sams.se_project.service.IAuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ReCaptchaService reCaptchaService;

    @Value("${jwt.duration}")
    private String jwtDuration;

    @Override
    public UserResponse signUp(SignUpRequest request) {
        reCaptchaService.validateTokenV2(request.recaptchaToken());

        userRepository.findByUsernameIgnoreCase(request.username())
                .ifPresent(_ -> {
                    throw new ResourceAlreadyExistsException("This username is already taken");
                });

        userRepository.findByEmailIgnoreCase(request.email())
                .ifPresent(_ -> {
                    throw new ResourceAlreadyExistsException("This email is already taken");
                });

        User newUser = User
                .builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_CUSTOMER)
                .personalInformation(UserPersonalInformation
                        .builder()
                        .firstName(request.firstName())
                        .lastName(request.lastName())
                        .address(request.address())
                        .build())
                .build();

        User savedUser = userRepository.save(newUser);

        return userMapper.toUserResponse(savedUser);
    }

    @Override
    public void signIn(SignInRequest request, HttpServletResponse response) {
        reCaptchaService.validateTokenV2(request.recaptchaToken());

        User authenticatedUser = authenticate(request.username(), request.password());

        String jwt = jwtService.generateToken(authenticatedUser, Long.parseLong(jwtDuration));
        HttpCookie cookie = cookieService.createAccessTokenCookie(jwt, Long.parseLong(jwtDuration));

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @Override
    public void signOut(HttpServletResponse response) {
        HttpCookie cookie = cookieService.deleteAccessTokenCookie();
        HttpCookie cookie2 = cookieService.removeXsrfCookie();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE2, cookie2.toString());
    }

    private User authenticate(String username, String password) {
        User searchedUser = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid credentials provided"));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        return searchedUser;
    }
}
