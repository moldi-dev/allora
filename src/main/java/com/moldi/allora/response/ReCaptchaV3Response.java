package com.moldi.allora.response;

public record ReCaptchaV3Response(
        Boolean success,
        String challege_ts,
        String hostname,
        Double score,
        String action
) {
}
