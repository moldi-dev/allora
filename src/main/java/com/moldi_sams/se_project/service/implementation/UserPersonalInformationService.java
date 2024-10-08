package com.moldi_sams.se_project.service.implementation;

import com.moldi_sams.se_project.entity.User;
import com.moldi_sams.se_project.entity.UserPersonalInformation;
import com.moldi_sams.se_project.exception.ResourceNotFoundException;
import com.moldi_sams.se_project.mapper.UserPersonalInformationMapper;
import com.moldi_sams.se_project.repository.UserPersonalInformationRepository;
import com.moldi_sams.se_project.request.UserPersonalInformationRequest;
import com.moldi_sams.se_project.response.UserPersonalInformationResponse;
import com.moldi_sams.se_project.service.IUserPersonalInformationService;
import jakarta.persistence.EntityManager;
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
    private final EntityManager entityManager;

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

        personalInformation = entityManager.merge(personalInformation);

        return userPersonalInformationMapper.toUserPersonalInformationResponse(personalInformation);
    }

    @Override
    public UserPersonalInformationResponse updateAuthenticatedUserData(Authentication authentication, UserPersonalInformationRequest request) {
        User authenticatedUser = ((User) authentication.getPrincipal());
        UserPersonalInformation personalInformation = authenticatedUser.getPersonalInformation();

        personalInformation = entityManager.merge(personalInformation);

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
