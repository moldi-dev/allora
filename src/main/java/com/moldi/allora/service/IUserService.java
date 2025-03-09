package com.moldi.allora.service;

import com.moldi.allora.request.user.PasswordChangeRequest;
import com.moldi.allora.request.user.PasswordResetRequest;
import com.moldi.allora.request.user.PasswordResetTokenRequest;
import com.moldi.allora.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {
    Page<UserResponse> findAll(Pageable pageable);
    UserResponse findById(Long userId);
    Page<UserResponse> findAllByUsernameLikeIgnoreCase(String username, Pageable pageable);
    Page<UserResponse> findAllByEmailLikeIgnoreCase(String email, Pageable pageable);
    UserResponse findAuthenticatedUserData(Authentication authentication);
    void requestPasswordResetToken(PasswordResetTokenRequest request);
    void resetPassword(PasswordResetRequest request);
    void changePassword(Authentication authentication, PasswordChangeRequest request);
    void deleteById(Long userId);
}
