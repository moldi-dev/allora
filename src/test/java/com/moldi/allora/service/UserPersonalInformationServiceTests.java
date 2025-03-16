package com.moldi.allora.service;

import com.moldi.allora.entity.User;
import com.moldi.allora.entity.UserPersonalInformation;
import com.moldi.allora.exception.ResourceNotFoundException;
import com.moldi.allora.mapper.UserPersonalInformationMapper;
import com.moldi.allora.repository.UserPersonalInformationRepository;
import com.moldi.allora.request.UserPersonalInformationRequest;
import com.moldi.allora.response.UserPersonalInformationResponse;
import com.moldi.allora.service.implementation.UserPersonalInformationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserPersonalInformationServiceTests {

    @Mock
    private UserPersonalInformationRepository userPersonalInformationRepository;

    @Mock
    private UserPersonalInformationMapper userPersonalInformationMapper;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserPersonalInformationService userPersonalInformationService;

    private User user;
    private UserPersonalInformation personalInformation;
    private UserPersonalInformationResponse personalInformationResponse;
    private UserPersonalInformationRequest personalInformationRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        personalInformation = UserPersonalInformation.builder()
                .userPersonalInformationId(1L)
                .firstName("John")
                .lastName("Doe")
                .address("123 Main St")
                .build();
        user.setPersonalInformation(personalInformation);

        personalInformationResponse = new UserPersonalInformationResponse(1L, "John", "Doe", "123 Main St");
        personalInformationRequest = new UserPersonalInformationRequest("John", "Doe", "123 Main St");
    }

    @Test
    void findById_ShouldReturnUserPersonalInformationResponse_WhenUserPersonalInformationExists() {
        when(userPersonalInformationRepository.findById(1L)).thenReturn(Optional.of(personalInformation));
        when(userPersonalInformationMapper.toUserPersonalInformationResponse(personalInformation)).thenReturn(personalInformationResponse);

        UserPersonalInformationResponse result = userPersonalInformationService.findById(1L);

        assertNotNull(result);
        assertEquals(personalInformationResponse.userPersonalInformationId(), result.userPersonalInformationId());
        assertEquals(personalInformationResponse.firstName(), result.firstName());
        assertEquals(personalInformationResponse.lastName(), result.lastName());
        assertEquals(personalInformationResponse.address(), result.address());

        verify(userPersonalInformationRepository, times(1)).findById(1L);
        verify(userPersonalInformationMapper, times(1)).toUserPersonalInformationResponse(personalInformation);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenUserPersonalInformationDoesNotExist() {
        when(userPersonalInformationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userPersonalInformationService.findById(1L));

        verify(userPersonalInformationRepository, times(1)).findById(1L);
        verify(userPersonalInformationMapper, never()).toUserPersonalInformationResponse(any());
    }

    @Test
    void findAuthenticatedUserData_ShouldReturnUserPersonalInformationResponse_WhenUserIsAuthenticated() {
        when(authentication.getPrincipal()).thenReturn(user);
        when(userPersonalInformationMapper.toUserPersonalInformationResponse(personalInformation)).thenReturn(personalInformationResponse);

        UserPersonalInformationResponse result = userPersonalInformationService.findAuthenticatedUserData(authentication);

        assertNotNull(result);
        assertEquals(personalInformationResponse.userPersonalInformationId(), result.userPersonalInformationId());
        assertEquals(personalInformationResponse.firstName(), result.firstName());
        assertEquals(personalInformationResponse.lastName(), result.lastName());
        assertEquals(personalInformationResponse.address(), result.address());

        verify(authentication, times(1)).getPrincipal();
        verify(userPersonalInformationMapper, times(1)).toUserPersonalInformationResponse(personalInformation);
    }

    @Test
    void updateAuthenticatedUserData_ShouldReturnUpdatedUserPersonalInformationResponse_WhenUserIsAuthenticated() {
        when(authentication.getPrincipal()).thenReturn(user);
        when(userPersonalInformationRepository.save(personalInformation)).thenReturn(personalInformation);
        when(userPersonalInformationMapper.toUserPersonalInformationResponse(personalInformation)).thenReturn(personalInformationResponse);

        UserPersonalInformationResponse result = userPersonalInformationService.updateAuthenticatedUserData(authentication, personalInformationRequest);

        assertNotNull(result);
        assertEquals(personalInformationResponse.userPersonalInformationId(), result.userPersonalInformationId());
        assertEquals(personalInformationResponse.firstName(), result.firstName());
        assertEquals(personalInformationResponse.lastName(), result.lastName());
        assertEquals(personalInformationResponse.address(), result.address());

        verify(authentication, times(1)).getPrincipal();
        verify(userPersonalInformationRepository, times(1)).save(personalInformation);
        verify(userPersonalInformationMapper, times(1)).toUserPersonalInformationResponse(personalInformation);
    }

    @Test
    void updateById_ShouldReturnUpdatedUserPersonalInformationResponse_WhenUserPersonalInformationExists() {
        when(userPersonalInformationRepository.findById(1L)).thenReturn(Optional.of(personalInformation));
        when(userPersonalInformationRepository.save(personalInformation)).thenReturn(personalInformation);
        when(userPersonalInformationMapper.toUserPersonalInformationResponse(personalInformation)).thenReturn(personalInformationResponse);

        UserPersonalInformationResponse result = userPersonalInformationService.updateById(1L, personalInformationRequest);

        assertNotNull(result);
        assertEquals(personalInformationResponse.userPersonalInformationId(), result.userPersonalInformationId());
        assertEquals(personalInformationResponse.firstName(), result.firstName());
        assertEquals(personalInformationResponse.lastName(), result.lastName());
        assertEquals(personalInformationResponse.address(), result.address());

        verify(userPersonalInformationRepository, times(1)).findById(1L);
        verify(userPersonalInformationRepository, times(1)).save(personalInformation);
        verify(userPersonalInformationMapper, times(1)).toUserPersonalInformationResponse(personalInformation);
    }

    @Test
    void updateById_ShouldThrowResourceNotFoundException_WhenUserPersonalInformationDoesNotExist() {
        when(userPersonalInformationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userPersonalInformationService.updateById(1L, personalInformationRequest));

        verify(userPersonalInformationRepository, times(1)).findById(1L);
        verify(userPersonalInformationRepository, never()).save(any());
        verify(userPersonalInformationMapper, never()).toUserPersonalInformationResponse(any());
    }
}