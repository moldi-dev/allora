package com.moldi.allora.service.implementation;

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
import com.moldi.allora.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final ReCaptchaService reCaptchaService;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public Page<UserResponse> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users could be found");
        }

        return users.map(userMapper::toUserResponse);
    }

    @Override
    public UserResponse findById(Long userId) {
        return userRepository
                .findById(userId)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided id could not be found"));
    }

    @Override
    public Page<UserResponse> findAllByUsernameLikeIgnoreCase(String username, Pageable pageable) {
        Page<User> users = userRepository.findAllByUsernameLikeIgnoreCase(username, pageable);

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users could be found");
        }

        return users.map(userMapper::toUserResponse);
    }

    @Override
    public Page<UserResponse> findAllByEmailLikeIgnoreCase(String email, Pageable pageable) {
        Page<User> users = userRepository.findAllByEmailLikeIgnoreCase(email, pageable);

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users could be found");
        }

        return users.map(userMapper::toUserResponse);
    }

    @Override
    public UserResponse findAuthenticatedUserData(Authentication authentication) {
        User authenticatedUser = ((User) authentication.getPrincipal());
        return userMapper.toUserResponse(authenticatedUser);
    }

    @Override
    public void requestPasswordResetToken(PasswordResetTokenRequest request) {
        reCaptchaService.validateTokenV2(request.recaptchaToken());

        User searchedUserByEmail = userRepository
                .findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email provided"));

        String token = generateToken(searchedUserByEmail, 20);

        searchedUserByEmail.setResetPasswordToken(token);

        userRepository.save(searchedUserByEmail);

        mailService.sendResetPasswordTokenEmail(searchedUserByEmail.getEmail(), token);
    }

    @Override
    public void resetPassword(PasswordResetRequest request) {
        reCaptchaService.validateTokenV2(request.recaptchaToken());

        User searchedUser = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid provided email"));

        if (!searchedUser.getResetPasswordToken().equals(request.resetPasswordCode())) {
            throw new ResourceNotFoundException("Invalid provided password reset code");
        }

        searchedUser.setPassword(passwordEncoder.encode(request.newPassword()));

        userRepository.save(searchedUser);
    }

    @Override
    public void changePassword(Authentication authentication, PasswordChangeRequest request) {
        User authenticatedUser = ((User) authentication.getPrincipal());

        if (!passwordEncoder.matches(request.currentPassword(), authenticatedUser.getPassword())) {
            throw new ResourceNotFoundException("The provided current password is invalid");
        }

        if (request.currentPassword().equals(request.newPassword())) {
            throw new ResourceAlreadyExistsException("The new password can't be the same as the current password");
        }

        authenticatedUser.setPassword(passwordEncoder.encode(request.newPassword()));

        userRepository.save(authenticatedUser);
    }

    @Override
    public void deleteById(Long userId) {
        User searchedUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided id couldn't be found"));

        UserPersonalInformation personalInformation = searchedUser.getPersonalInformation();

        Page<Order> userOrders = orderRepository.findAllByUserPersonalInformationUserPersonalInformationId(personalInformation.getUserPersonalInformationId(), null);
        Page<Review> userReviews = reviewRepository.findAllByUserPersonalInformationUserPersonalInformationId(personalInformation.getUserPersonalInformationId(), null);

        orderRepository.deleteAll(userOrders);
        reviewRepository.deleteAll(userReviews);

        userRepository.delete(searchedUser);
    }

    private String generateToken(User user, int length) {
        UUID uuid = UUID.randomUUID();
        String username = user.getUsername();
        String resetPasswordCode = uuid + username;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(resetPasswordCode.getBytes());

            String base64Encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);

            return base64Encoded.substring(0, length - 1);
        }

        catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
