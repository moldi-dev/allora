package com.moldi.allora.mapper;

import com.moldi.allora.entity.User;
import com.moldi.allora.enumeration.Role;
import com.moldi.allora.response.UserResponse;
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
