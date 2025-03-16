package com.moldi.allora.service;

import com.moldi.allora.entity.User;
import com.moldi.allora.entity.UserPersonalInformation;
import com.moldi.allora.enumeration.Role;
import com.moldi.allora.exception.ResourceAlreadyExistsException;
import com.moldi.allora.exception.ResourceNotFoundException;
import com.moldi.allora.mapper.UserMapper;
import com.moldi.allora.repository.UserRepository;
import com.moldi.allora.request.user.SignInRequest;
import com.moldi.allora.request.user.SignUpRequest;
import com.moldi.allora.response.UserResponse;
import com.moldi.allora.service.implementation.AuthenticationService;
import com.moldi.allora.service.implementation.CookieService;
import com.moldi.allora.service.implementation.JwtService;
import com.moldi.allora.service.implementation.ReCaptchaService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private CookieService cookieService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ReCaptchaService reCaptchaService;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User user;
    private UserResponse userResponse;
    private SignUpRequest signUpRequest;
    private SignInRequest signInRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userId(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .role(Role.ROLE_CUSTOMER)
                .personalInformation(UserPersonalInformation.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .address("123 Main St")
                        .build())
                .build();

        userResponse = new UserResponse(1L, "testuser", "test@example.com", false, null, LocalDateTime.now());

        signUpRequest = new SignUpRequest("testuser", "test@example.com", "John", "Doe", "123 Main St", "password", "password", "recaptchaToken");
        signInRequest = new SignInRequest("testuser", "password", "recaptchaToken");
    }

    @Test
    void signUp_ShouldReturnUserResponse_WhenUserDoesNotExist() {
        when(userRepository.findByUsernameIgnoreCase("testuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmailIgnoreCase("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);
        doNothing().when(reCaptchaService).validateTokenV2("recaptchaToken");

        UserResponse result = authenticationService.signUp(signUpRequest);

        assertNotNull(result);
        assertEquals(userResponse.userId(), result.userId());
        assertEquals(userResponse.username(), result.username());
        assertEquals(userResponse.email(), result.email());

        verify(reCaptchaService, times(1)).validateTokenV2("recaptchaToken");
        verify(userRepository, times(1)).findByUsernameIgnoreCase("testuser");
        verify(userRepository, times(1)).findByEmailIgnoreCase("test@example.com");
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toUserResponse(user);
    }

    @Test
    void signUp_ShouldThrowResourceAlreadyExistsException_WhenUsernameIsTaken() {
        when(userRepository.findByUsernameIgnoreCase("testuser")).thenReturn(Optional.of(user));
        doNothing().when(reCaptchaService).validateTokenV2("recaptchaToken");

        assertThrows(ResourceAlreadyExistsException.class, () -> authenticationService.signUp(signUpRequest));

        verify(reCaptchaService, times(1)).validateTokenV2("recaptchaToken");
        verify(userRepository, times(1)).findByUsernameIgnoreCase("testuser");
        verify(userRepository, never()).findByEmailIgnoreCase(any());
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toUserResponse(any());
    }

    @Test
    void signUp_ShouldThrowResourceAlreadyExistsException_WhenEmailIsTaken() {
        when(userRepository.findByUsernameIgnoreCase("testuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmailIgnoreCase("test@example.com")).thenReturn(Optional.of(user));
        doNothing().when(reCaptchaService).validateTokenV2("recaptchaToken");

        assertThrows(ResourceAlreadyExistsException.class, () -> authenticationService.signUp(signUpRequest));

        verify(reCaptchaService, times(1)).validateTokenV2("recaptchaToken");
        verify(userRepository, times(1)).findByUsernameIgnoreCase("testuser");
        verify(userRepository, times(1)).findByEmailIgnoreCase("test@example.com");
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toUserResponse(any());
    }

    @Test
    void signIn_ShouldThrowResourceNotFoundException_WhenCredentialsAreInvalid() {
        when(userRepository.findByUsernameIgnoreCase("testuser")).thenReturn(Optional.empty());
        doNothing().when(reCaptchaService).validateTokenV2("recaptchaToken");

        assertThrows(ResourceNotFoundException.class, () -> authenticationService.signIn(signInRequest, response));

        verify(reCaptchaService, times(1)).validateTokenV2("recaptchaToken");
        verify(userRepository, times(1)).findByUsernameIgnoreCase("testuser");
        verify(authenticationManager, never()).authenticate(any());
        verify(jwtService, never()).generateToken(any(), anyLong());
        verify(cookieService, never()).createAccessTokenCookie(any(), anyLong());
        verify(response, never()).addHeader(any(), any());
        verify(userRepository, never()).save(any());
    }
}