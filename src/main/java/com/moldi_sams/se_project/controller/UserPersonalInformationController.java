package com.moldi_sams.se_project.controller;

import com.moldi_sams.se_project.request.UserPersonalInformationRequest;
import com.moldi_sams.se_project.response.HttpResponse;
import com.moldi_sams.se_project.service.implementation.UserPersonalInformationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users-personal-informations")
public class UserPersonalInformationController {
    private final UserPersonalInformationService userPersonalInformationService;

    @GetMapping("/id={userPersonalInformationId}")
    public ResponseEntity<HttpResponse> findById(@PathVariable("userPersonalInformationId") Long userPersonalInformationId) {
        var result = userPersonalInformationService.findById(userPersonalInformationId);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The personal user information has been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @GetMapping("/authenticated")
    public ResponseEntity<HttpResponse> findAuthenticatedUserData(Authentication authentication) {
        var result = userPersonalInformationService.findAuthenticatedUserData(authentication);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The personal user information has been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @PatchMapping("/authenticated")
    public ResponseEntity<HttpResponse> updateAuthenticatedUserData(Authentication authentication, @RequestBody @Valid UserPersonalInformationRequest request) {
        var result = userPersonalInformationService.updateAuthenticatedUserData(authentication, request);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The personal user information has been updated successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @PatchMapping("/id={userPersonalInformationId}")
    public ResponseEntity<HttpResponse> updateById(@PathVariable("userPersonalInformationId") Long userPersonalInformationId, @RequestBody @Valid UserPersonalInformationRequest request) {
        var result = userPersonalInformationService.updateById(userPersonalInformationId, request);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The personal user information has been updated successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }
}
