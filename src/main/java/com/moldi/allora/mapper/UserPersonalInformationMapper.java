package com.moldi.allora.mapper;

import com.moldi.allora.entity.UserPersonalInformation;
import com.moldi.allora.response.UserPersonalInformationResponse;
import org.springframework.stereotype.Service;

@Service
public class UserPersonalInformationMapper {
    public UserPersonalInformationResponse toUserPersonalInformationResponse(UserPersonalInformation userPersonalInformation) {
        return new UserPersonalInformationResponse(
                userPersonalInformation.getUserPersonalInformationId(),
                userPersonalInformation.getFirstName(),
                userPersonalInformation.getLastName(),
                userPersonalInformation.getAddress()
        );
    }
}
