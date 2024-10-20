package com.moldi_sams.se_project.mapper;

import com.moldi_sams.se_project.entity.User;
import com.moldi_sams.se_project.enumeration.Role;
import com.moldi_sams.se_project.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final UserPersonalInformationMapper userPersonalInformationMapper;

    public UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().equals(Role.ROLE_ADMINISTRATOR),
                userPersonalInformationMapper.toUserPersonalInformationResponse(user.getPersonalInformation())
        );
    }
}
