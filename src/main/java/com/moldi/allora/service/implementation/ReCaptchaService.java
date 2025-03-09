package com.moldi.allora.service.implementation;

import com.moldi.allora.exception.BadRequestException;
import com.moldi.allora.response.ReCaptchaV2Response;
import com.moldi.allora.response.ReCaptchaV3Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ReCaptchaService {
    private final RestTemplate restTemplate;

    @Value("${recaptcha.secret-key}")
    private String secretKey;

    @Value("${recaptcha.verify-url}")
    private String verifyUrl;

    public void validateTokenV2(String recaptchaToken) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("secret", secretKey);
        map.add("response", recaptchaToken);

        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(map,headers);

        ResponseEntity<ReCaptchaV2Response> response = restTemplate.exchange(
                verifyUrl,
                HttpMethod.POST,
                entity,
                ReCaptchaV2Response.class);

        ReCaptchaV2Response reCaptchaResponseV2 = response.getBody();

        if (reCaptchaResponseV2 == null || !reCaptchaResponseV2.success()) {
            throw new BadRequestException("The provided recaptcha is invalid. Please refresh the page and try solving it again");
        }
    }

    public void validateTokenV3(String recaptchaToken) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("secret", secretKey);
        map.add("response", recaptchaToken);

        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(map,headers);

        ResponseEntity<ReCaptchaV3Response> response = restTemplate.exchange(
                verifyUrl,
                HttpMethod.POST,
                entity,
                ReCaptchaV3Response.class);

        ReCaptchaV3Response reCaptchaResponseV3 = response.getBody();

        if (reCaptchaResponseV3 == null || !reCaptchaResponseV3.success() || reCaptchaResponseV3.score() < 0.7) {
            throw new BadRequestException("The provided recaptcha is invalid. Please refresh the page and try solving it again");
        }
    }
}
