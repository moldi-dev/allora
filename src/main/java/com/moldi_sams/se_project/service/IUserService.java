package com.moldi_sams.se_project.service;

import com.moldi_sams.se_project.request.user.PasswordChangeRequest;
import com.moldi_sams.se_project.request.user.PasswordResetRequest;
import com.moldi_sams.se_project.request.user.PasswordResetTokenRequest;
import com.moldi_sams.se_project.response.UserResponse;
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
}
