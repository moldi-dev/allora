package com.moldi.allora.service;

import com.moldi.allora.entity.Order;
import com.moldi.allora.entity.Review;
import com.moldi.allora.entity.User;
import com.moldi.allora.entity.UserPersonalInformation;
import com.moldi.allora.exception.ResourceAlreadyExistsException;
import com.moldi.allora.exception.ResourceNotFoundException;
import com.moldi.allora.mapper.UserMapper;
import com.moldi.allora.repository.OrderRepository;
import com.moldi.allora.repository.ReviewRepository;
import com.moldi.allora.repository.UserRepository;
import com.moldi.allora.request.user.PasswordChangeRequest;
import com.moldi.allora.request.user.PasswordResetRequest;
import com.moldi.allora.request.user.PasswordResetTokenRequest;
import com.moldi.allora.response.UserResponse;
import com.moldi.allora.service.implementation.MailService;
import com.moldi.allora.service.implementation.ReCaptchaService;
import com.moldi.allora.service.implementation.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private MailService mailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ReCaptchaService reCaptchaService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserResponse userResponse;
    private UserPersonalInformation personalInformation;

    @BeforeEach
    void setUp() {
        personalInformation = new UserPersonalInformation();
        personalInformation.setUserPersonalInformationId(1L);

        user = User.builder()
                .userId(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .personalInformation(personalInformation)
                .build();

        userResponse = new UserResponse(1L, "testuser", "test@example.com", false, null, null);
    }

    @Test
    void findAll_ShouldReturnPageOfUserResponses_WhenUsersExist() {
        Page<User> userPage = new PageImpl<>(Collections.singletonList(user));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        Page<UserResponse> result = userService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(userResponse, result.getContent().get(0));

        verify(userRepository, times(1)).findAll(any(Pageable.class));
        verify(userMapper, times(1)).toUserResponse(user);
    }

    @Test
    void findAll_ShouldThrowResourceNotFoundException_WhenNoUsersExist() {
        Page<User> emptyPage = new PageImpl<>(Collections.emptyList());
        when(userRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        assertThrows(ResourceNotFoundException.class, () -> userService.findAll(Pageable.unpaged()));

        verify(userRepository, times(1)).findAll(any(Pageable.class));
        verify(userMapper, never()).toUserResponse(any());
    }

    @Test
    void findById_ShouldReturnUserResponse_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(userResponse.userId(), result.userId());
        assertEquals(userResponse.username(), result.username());
        assertEquals(userResponse.email(), result.email());

        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).toUserResponse(user);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findById(1L));

        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, never()).toUserResponse(any());
    }

    @Test
    void findAllByUsernameLikeIgnoreCase_ShouldReturnPageOfUserResponses_WhenUsersExist() {
        Page<User> userPage = new PageImpl<>(Collections.singletonList(user));
        when(userRepository.findAllByUsernameLikeIgnoreCase("test", Pageable.unpaged())).thenReturn(userPage);
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        Page<UserResponse> result = userService.findAllByUsernameLikeIgnoreCase("test", Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(userResponse, result.getContent().get(0));

        verify(userRepository, times(1)).findAllByUsernameLikeIgnoreCase("test", Pageable.unpaged());
        verify(userMapper, times(1)).toUserResponse(user);
    }

    @Test
    void findAllByUsernameLikeIgnoreCase_ShouldThrowResourceNotFoundException_WhenNoUsersExist() {
        Page<User> emptyPage = new PageImpl<>(Collections.emptyList());
        when(userRepository.findAllByUsernameLikeIgnoreCase("test", Pageable.unpaged())).thenReturn(emptyPage);

        assertThrows(ResourceNotFoundException.class, () -> userService.findAllByUsernameLikeIgnoreCase("test", Pageable.unpaged()));

        verify(userRepository, times(1)).findAllByUsernameLikeIgnoreCase("test", Pageable.unpaged());
        verify(userMapper, never()).toUserResponse(any());
    }

    @Test
    void findAllByEmailLikeIgnoreCase_ShouldReturnPageOfUserResponses_WhenUsersExist() {
        Page<User> userPage = new PageImpl<>(Collections.singletonList(user));
        when(userRepository.findAllByEmailLikeIgnoreCase("test@example.com", Pageable.unpaged())).thenReturn(userPage);
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        Page<UserResponse> result = userService.findAllByEmailLikeIgnoreCase("test@example.com", Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(userResponse, result.getContent().get(0));

        verify(userRepository, times(1)).findAllByEmailLikeIgnoreCase("test@example.com", Pageable.unpaged());
        verify(userMapper, times(1)).toUserResponse(user);
    }

    @Test
    void findAllByEmailLikeIgnoreCase_ShouldThrowResourceNotFoundException_WhenNoUsersExist() {
        Page<User> emptyPage = new PageImpl<>(Collections.emptyList());
        when(userRepository.findAllByEmailLikeIgnoreCase("test@example.com", Pageable.unpaged())).thenReturn(emptyPage);

        assertThrows(ResourceNotFoundException.class, () -> userService.findAllByEmailLikeIgnoreCase("test@example.com", Pageable.unpaged()));

        verify(userRepository, times(1)).findAllByEmailLikeIgnoreCase("test@example.com", Pageable.unpaged());
        verify(userMapper, never()).toUserResponse(any());
    }

    @Test
    void findAuthenticatedUserData_ShouldReturnUserResponse_WhenUserIsAuthenticated() {
        when(authentication.getPrincipal()).thenReturn(user);
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.findAuthenticatedUserData(authentication);

        assertNotNull(result);
        assertEquals(userResponse.userId(), result.userId());
        assertEquals(userResponse.username(), result.username());
        assertEquals(userResponse.email(), result.email());

        verify(authentication, times(1)).getPrincipal();
        verify(userMapper, times(1)).toUserResponse(user);
    }

    @Test
    void requestPasswordResetToken_ShouldSendResetPasswordTokenEmail_WhenUserExists() {
        PasswordResetTokenRequest request = new PasswordResetTokenRequest("test@example.com", "recaptchaToken");
        when(userRepository.findByEmailIgnoreCase("test@example.com")).thenReturn(Optional.of(user));
        doNothing().when(reCaptchaService).validateTokenV2("recaptchaToken");

        userService.requestPasswordResetToken(request);

        verify(reCaptchaService, times(1)).validateTokenV2("recaptchaToken");
        verify(userRepository, times(1)).findByEmailIgnoreCase("test@example.com");
        verify(mailService, times(1)).sendResetPasswordTokenEmail(eq("test@example.com"), any());
    }

    @Test
    void requestPasswordResetToken_ShouldThrowResourceNotFoundException_WhenUserDoesNotExist() {
        PasswordResetTokenRequest request = new PasswordResetTokenRequest("test@example.com", "recaptchaToken");
        when(userRepository.findByEmailIgnoreCase("test@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.requestPasswordResetToken(request));

        verify(reCaptchaService, times(1)).validateTokenV2("recaptchaToken");
        verify(userRepository, times(1)).findByEmailIgnoreCase("test@example.com");
        verify(mailService, never()).sendResetPasswordTokenEmail(any(), any());
    }

    @Test
    void resetPassword_ShouldUpdatePassword_WhenTokenIsValid() {
        PasswordResetRequest request = new PasswordResetRequest("test@example.com", "resetCode", "newPassword", "newPassword", "recaptchaToken");
        when(userRepository.findByEmailIgnoreCase("test@example.com")).thenReturn(Optional.of(user));
        doNothing().when(reCaptchaService).validateTokenV2("recaptchaToken");
        user.setResetPasswordToken("resetCode");

        userService.resetPassword(request);

        verify(reCaptchaService, times(1)).validateTokenV2("recaptchaToken");
        verify(userRepository, times(1)).findByEmailIgnoreCase("test@example.com");
        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void resetPassword_ShouldThrowResourceNotFoundException_WhenTokenIsInvalid() {
        PasswordResetRequest request = new PasswordResetRequest("test@example.com", "invalidCode", "newPassword", "newPassword", "recaptchaToken");
        when(userRepository.findByEmailIgnoreCase("test@example.com")).thenReturn(Optional.of(user));
        doNothing().when(reCaptchaService).validateTokenV2("recaptchaToken");
        user.setResetPasswordToken("resetCode");

        assertThrows(ResourceNotFoundException.class, () -> userService.resetPassword(request));

        verify(reCaptchaService, times(1)).validateTokenV2("recaptchaToken");
        verify(userRepository, times(1)).findByEmailIgnoreCase("test@example.com");
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_ShouldUpdatePassword_WhenCurrentPasswordIsValid() {
        PasswordChangeRequest request = new PasswordChangeRequest("currentPassword", "newPassword", "newPassword");
        when(authentication.getPrincipal()).thenReturn(user);
        when(passwordEncoder.matches("currentPassword", "password")).thenReturn(true);

        userService.changePassword(authentication, request);

        verify(authentication, times(1)).getPrincipal();
        verify(passwordEncoder, times(1)).matches("currentPassword", "password");
        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void changePassword_ShouldThrowResourceNotFoundException_WhenCurrentPasswordIsInvalid() {
        PasswordChangeRequest request = new PasswordChangeRequest("invalidPassword", "newPassword", "newPassword");
        when(authentication.getPrincipal()).thenReturn(user);
        when(passwordEncoder.matches("invalidPassword", "password")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.changePassword(authentication, request));

        verify(authentication, times(1)).getPrincipal();
        verify(passwordEncoder, times(1)).matches("invalidPassword", "password");
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_ShouldThrowResourceAlreadyExistsException_WhenNewPasswordIsSameAsCurrent() {
        PasswordChangeRequest request = new PasswordChangeRequest("currentPassword", "currentPassword", "currentPassword");
        when(authentication.getPrincipal()).thenReturn(user);
        when(passwordEncoder.matches("currentPassword", "password")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> userService.changePassword(authentication, request));

        verify(authentication, times(1)).getPrincipal();
        verify(passwordEncoder, times(1)).matches("currentPassword", "password");
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteById_ShouldDeleteUserAndRelatedData_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Page<Order> orders = new PageImpl<>(Collections.emptyList());
        Page<Review> reviews = new PageImpl<>(Collections.emptyList());
        when(orderRepository.findAllByUserPersonalInformationUserPersonalInformationId(1L, null)).thenReturn(orders);
        when(reviewRepository.findAllByUserPersonalInformationUserPersonalInformationId(1L, null)).thenReturn(reviews);

        userService.deleteById(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).findAllByUserPersonalInformationUserPersonalInformationId(1L, null);
        verify(reviewRepository, times(1)).findAllByUserPersonalInformationUserPersonalInformationId(1L, null);
        verify(orderRepository, times(1)).deleteAll(orders);
        verify(reviewRepository, times(1)).deleteAll(reviews);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteById_ShouldThrowResourceNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteById(1L));

        verify(userRepository, times(1)).findById(1L);
        verify(orderRepository, never()).findAllByUserPersonalInformationUserPersonalInformationId(any(), any());
        verify(reviewRepository, never()).findAllByUserPersonalInformationUserPersonalInformationId(any(), any());
        verify(orderRepository, never()).deleteAll(any());
        verify(reviewRepository, never()).deleteAll(any());
        verify(userRepository, never()).delete(any());
    }
}