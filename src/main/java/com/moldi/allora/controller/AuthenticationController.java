package com.moldi.allora.controller;

import com.moldi.allora.request.user.SignInRequest;
import com.moldi.allora.request.user.SignUpRequest;
import com.moldi.allora.response.HttpResponse;
import com.moldi.allora.service.implementation.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public ResponseEntity<HttpResponse> signUp(@RequestBody @Valid SignUpRequest request) {
        var result = authenticationService.signUp(request);

        return ResponseEntity.created(URI.create("")).body(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("Account successfully created")
                        .responseStatus(HttpStatus.CREATED)
                        .responseStatusCode(HttpStatus.CREATED.value())
                        .body(result)
                        .build()
        );
    }

    @PostMapping("/sign-in")
    public ResponseEntity<HttpResponse> signIn(@RequestBody @Valid SignInRequest request, HttpServletResponse response) {
        authenticationService.signIn(request, response);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("Sign in successful. Welcome " + request.username())
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/sign-out")
    public ResponseEntity<HttpResponse> signOut(HttpServletResponse response) {
        authenticationService.signOut(response);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("Sign out successful")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}
