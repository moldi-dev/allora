package com.moldi.allora.service.implementation;

import com.moldi.allora.entity.User;
import com.moldi.allora.entity.UserPersonalInformation;
import com.moldi.allora.exception.ResourceNotFoundException;
import com.moldi.allora.mapper.UserPersonalInformationMapper;
import com.moldi.allora.repository.UserPersonalInformationRepository;
import com.moldi.allora.request.UserPersonalInformationRequest;
import com.moldi.allora.response.UserPersonalInformationResponse;
import com.moldi.allora.service.IUserPersonalInformationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserPersonalInformationService implements IUserPersonalInformationService {
    private final UserPersonalInformationRepository userPersonalInformationRepository;
    private final UserPersonalInformationMapper userPersonalInformationMapper;

    @Override
    public UserPersonalInformationResponse findById(Long userPersonalInformationId) {
        return userPersonalInformationRepository
                .findById(userPersonalInformationId)
                .map(userPersonalInformationMapper::toUserPersonalInformationResponse)
                .orElseThrow(() -> new ResourceNotFoundException("The user personal information by the provided id couldn't be found"));
    }

    @Override
    public UserPersonalInformationResponse findAuthenticatedUserData(Authentication authentication) {
        User authenticatedUser = ((User) authentication.getPrincipal());
        UserPersonalInformation personalInformation = authenticatedUser.getPersonalInformation();

        return userPersonalInformationMapper.toUserPersonalInformationResponse(personalInformation);
    }

    @Override
    public UserPersonalInformationResponse updateAuthenticatedUserData(Authentication authentication, UserPersonalInformationRequest request) {
        User authenticatedUser = ((User) authentication.getPrincipal());
        UserPersonalInformation personalInformation = authenticatedUser.getPersonalInformation();

        personalInformation.setAddress(request.address());

        return userPersonalInformationMapper.toUserPersonalInformationResponse(userPersonalInformationRepository.save(personalInformation));
    }

    @Override
    public UserPersonalInformationResponse updateById(Long userPersonalInformationId, UserPersonalInformationRequest request) {
        UserPersonalInformation searchedById = userPersonalInformationRepository
                .findById(userPersonalInformationId)
                .orElseThrow(() -> new ResourceNotFoundException("The user personal information by the provided id couldn't be found"));

        searchedById.setAddress(request.address());
        searchedById.setFirstName(request.firstName());
        searchedById.setLastName(request.lastName());

        return userPersonalInformationMapper.toUserPersonalInformationResponse(userPersonalInformationRepository.save(searchedById));
    }
}
