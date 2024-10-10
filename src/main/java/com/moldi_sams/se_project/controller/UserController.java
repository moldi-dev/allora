package com.moldi_sams.se_project.controller;

import com.moldi_sams.se_project.request.user.PasswordChangeRequest;
import com.moldi_sams.se_project.request.user.PasswordResetRequest;
import com.moldi_sams.se_project.request.user.PasswordResetTokenRequest;
import com.moldi_sams.se_project.response.HttpResponse;
import com.moldi_sams.se_project.service.implementation.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<HttpResponse> findAll(Pageable pageable) {
        var result = userService.findAll(pageable);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The users have been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @GetMapping("/id={userId}")
    public ResponseEntity<HttpResponse> findById(@PathVariable("userId") Long userId) {
        var result = userService.findById(userId);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The user has been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @GetMapping("/all/username-like={username}")
    public ResponseEntity<HttpResponse> findAllByUsernameLikeIgnoreCase(@PathVariable("username") String username, Pageable pageable) {
        var result = userService.findAllByUsernameLikeIgnoreCase(username, pageable);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The users have been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @GetMapping("/all/email-like={email}")
    public ResponseEntity<HttpResponse> findAllByEmailLikeIgnoreCase(@PathVariable("email") String email, Pageable pageable) {
        var result = userService.findAllByEmailLikeIgnoreCase(email, pageable);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The users have been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @GetMapping("/authenticated")
    public ResponseEntity<HttpResponse> findAuthenticatedUserData(Authentication authentication) {
        var result = userService.findAuthenticatedUserData(authentication);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The user has been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @GetMapping("/authenticated/is-admin")
    public ResponseEntity<HttpResponse> checkIfAuthenticatedUserIsAdmin() {
        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The user is an administrator")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/request-password-reset-code")
    public ResponseEntity<HttpResponse> requestPasswordResetCode(@RequestBody @Valid PasswordResetTokenRequest request) {
        userService.requestPasswordResetToken(request);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The password reset code has been sent on the provided email successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<HttpResponse> resetPassword(@RequestBody @Valid PasswordResetRequest request) {
        userService.resetPassword(request);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The password has been resetted successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/change-password")
    public ResponseEntity<HttpResponse> changePassword(Authentication authentication, @RequestBody @Valid PasswordChangeRequest request) {
        userService.changePassword(authentication, request);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The password has been changed successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @DeleteMapping("/id={userId}")
    public ResponseEntity<HttpResponse> deleteById(@PathVariable("userId") Long userId) {
        userService.deleteById(userId);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The user has been deleted successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}
