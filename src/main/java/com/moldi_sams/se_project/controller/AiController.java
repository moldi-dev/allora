package com.moldi_sams.se_project.controller;

import com.moldi_sams.se_project.request.admin.AiPromptRequest;
import com.moldi_sams.se_project.response.HttpResponse;
import com.moldi_sams.se_project.service.implementation.AiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {
    private final AiService aiService;

    @PostMapping
    public ResponseEntity<HttpResponse> processRequest(@RequestBody @Valid AiPromptRequest request) {
        var result = aiService.processRequest(request);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("Prompt result successfully processed")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }
}
