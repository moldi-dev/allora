package com.moldi_sams.se_project.service.implementation;

import com.moldi_sams.se_project.entity.User;
import com.moldi_sams.se_project.exception.ResourceNotFoundException;
import com.moldi_sams.se_project.mapper.UserMapper;
import com.moldi_sams.se_project.repository.UserRepository;
import com.moldi_sams.se_project.response.UserResponse;
import com.moldi_sams.se_project.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Page<UserResponse> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users could be found");
        }

        return users.map(userMapper::toUserResponse);
    }

    @Override
    public UserResponse findById(Long userId) {
        return userRepository
                .findById(userId)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided id could not be found"));
    }

    @Override
    public Page<UserResponse> findAllByUsernameLikeIgnoreCase(String username, Pageable pageable) {
        Page<User> users = userRepository.findAllByUsernameLikeIgnoreCase(username, pageable);

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users could be found");
        }

        return users.map(userMapper::toUserResponse);
    }

    @Override
    public Page<UserResponse> findAllByEmailLikeIgnoreCase(String email, Pageable pageable) {
        Page<User> users = userRepository.findAllByEmailLikeIgnoreCase(email, pageable);

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users could be found");
        }

        return users.map(userMapper::toUserResponse);
    }

    @Override
    public UserResponse findAuthenticatedUserData(Authentication authentication) {
        User authenticatedUser = ((User) authentication.getPrincipal());
        return userMapper.toUserResponse(authenticatedUser);
    }
}
