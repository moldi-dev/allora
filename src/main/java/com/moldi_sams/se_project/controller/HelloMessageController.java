package com.moldi_sams.se_project.controller;

import com.moldi_sams.se_project.entity.HelloMessage;
import com.moldi_sams.se_project.repository.HelloMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloMessageController {

    private final HelloMessageRepository repository;

    @GetMapping("/hello-world")
    public String getHelloMessage() {
        return repository.findById(1L)
                .orElse(HelloMessage
                        .builder()
                        .content("Hello World!")
                        .build())
                .getContent();
    }
}
