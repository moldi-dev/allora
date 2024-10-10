package com.moldi_sams.se_project.response;

public record ReCaptchaV3Response(
        Boolean success,
        String challege_ts,
        String hostname,
        Double score,
        String action
) {
}
