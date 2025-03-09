package com.moldi.allora.service;

import com.moldi.allora.request.UserPersonalInformationRequest;
import com.moldi.allora.response.UserPersonalInformationResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface IUserPersonalInformationService {
    UserPersonalInformationResponse findById(Long userPersonalInformationId);
    UserPersonalInformationResponse findAuthenticatedUserData(Authentication authentication);
    UserPersonalInformationResponse updateAuthenticatedUserData(Authentication authentication, UserPersonalInformationRequest request);
    UserPersonalInformationResponse updateById(Long userPersonalInformationId, UserPersonalInformationRequest request);
}
