package com.moldi_sams.se_project.mapper;

import com.moldi_sams.se_project.entity.User;
import com.moldi_sams.se_project.enumeration.Role;
import com.moldi_sams.se_project.response.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().equals(Role.ROLE_ADMINISTRATOR)
        );
    }
}
