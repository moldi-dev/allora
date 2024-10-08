package com.moldi_sams.se_project.mapper;

import com.moldi_sams.se_project.entity.UserPersonalInformation;
import com.moldi_sams.se_project.response.UserPersonalInformationResponse;
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
