package com.application.sharkpark.service;

import com.application.sharkpark.domain.ParkingAllocation;
import com.application.sharkpark.domain.ParkingLot;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.dto.request.CreateParkingAllocationRequest;
import com.application.sharkpark.dto.request.StopParkingAllocationRequest;
import com.application.sharkpark.dto.response.ParkingAllocationResponse;
import com.application.sharkpark.enums.ParkingAllocationStatus;
import com.application.sharkpark.exceptions.ActiveParkingAllocationExistException;
import com.application.sharkpark.exceptions.InvalidParkingAllocationOwnerException;
import com.application.sharkpark.exceptions.RequestException;
import com.application.sharkpark.exceptions.UserNotFoundException;
import com.application.sharkpark.repository.ParkingAllocationRepository;
import com.application.sharkpark.repository.ParkingLotRepository;
import com.application.sharkpark.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingAllocationServiceTest {

    @Mock
    private ParkingAllocationRepository parkingAllocationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ParkingLotRepository parkingLotRepository;

    @InjectMocks
    private ParkingAllocationService parkingAllocationService;

    @Nested
    class CreateParkingAllocation {

        @Test
        void createParkingAllocation_Success() {
            int memberId = 1;
            int parkingLotId = 10;
            String licensePlate = "1-XYZ-123";

            CreateParkingAllocationRequest request = new CreateParkingAllocationRequest(memberId, licensePlate, parkingLotId);

            User mockUser = mock(User.class);
            when(mockUser.isValidLicensePlate(licensePlate)).thenReturn(true);
            when(userRepository.findById(memberId)).thenReturn(Optional.of(mockUser));

            ParkingLot mockParkingLot = mock(ParkingLot.class);
            ParkingAllocation mockAllocation = mock(ParkingAllocation.class);
            when(mockAllocation.getParkingAllocationId()).thenReturn(100);
            when(mockAllocation.getParkingLot()).thenReturn(mockParkingLot);
            when(mockAllocation.getMember()).thenReturn(mockUser);

            when(parkingLotRepository.findByParkingLotIdForUpdate(parkingLotId)).thenReturn(Optional.of(mockParkingLot));
            when(parkingAllocationRepository.findAllByLicensePlateAndStatus(licensePlate, ParkingAllocationStatus.ACTIVE))
                    .thenReturn(Collections.emptyList());
            when(mockParkingLot.allocateSpot(mockUser, licensePlate)).thenReturn(mockAllocation);
            when(parkingAllocationRepository.save(any(ParkingAllocation.class))).thenReturn(mockAllocation);

            ParkingAllocationResponse response = parkingAllocationService.createParkingAllocation(request);

            assertNotNull(response);
            verify(parkingLotRepository).save(mockParkingLot);
            verify(parkingAllocationRepository).save(mockAllocation);
        }

        @Test
        void createParkingAllocation_UserNotFound_ThrowsException() {
            CreateParkingAllocationRequest request = new CreateParkingAllocationRequest(1, "1-XYZ-123", 10);
            when(userRepository.findById(1)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () ->
                    parkingAllocationService.createParkingAllocation(request)
            );
            verifyNoInteractions(parkingLotRepository, parkingAllocationRepository);
        }

        @Test
        void createParkingAllocation_InvalidLicensePlate_ThrowsException() {
            CreateParkingAllocationRequest request = new CreateParkingAllocationRequest(1, "INVALID", 10);
            User mockUser = mock(User.class);
            when(mockUser.isValidLicensePlate("INVALID")).thenReturn(false);
            when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

            assertThrows(RequestException.class, () ->
                    parkingAllocationService.createParkingAllocation(request)
            );
            verifyNoInteractions(parkingLotRepository, parkingAllocationRepository);
        }

        @Test
        void createParkingAllocation_ActiveAllocationExists_ThrowsException() {
            CreateParkingAllocationRequest request = new CreateParkingAllocationRequest(1, "1-XYZ-123", 10);
            User mockUser = mock(User.class);
            ParkingLot mockParkingLot = mock(ParkingLot.class);
            ParkingAllocation existingAllocation = mock(ParkingAllocation.class);

            when(mockUser.isValidLicensePlate("1-XYZ-123")).thenReturn(true);
            when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
            when(parkingLotRepository.findByParkingLotIdForUpdate(10)).thenReturn(Optional.of(mockParkingLot));

            when(parkingAllocationRepository.findAllByLicensePlateAndStatus("1-XYZ-123", ParkingAllocationStatus.ACTIVE))
                    .thenReturn(List.of(existingAllocation));

            assertThrows(ActiveParkingAllocationExistException.class, () ->
                    parkingAllocationService.createParkingAllocation(request)
            );
            verify(parkingLotRepository, never()).save(any());
            verify(parkingAllocationRepository, never()).save(any());
        }
    }

    @Nested
    class StopParkingAllocation {

        @Test
        void stopParkingAllocation_Success() {
            StopParkingAllocationRequest request = new StopParkingAllocationRequest(1, 50);

            User mockUser = mock(User.class);
            when(mockUser.getUserId()).thenReturn(50);
            when(userRepository.findById(50)).thenReturn(Optional.of(mockUser));

            ParkingAllocation mockAllocation = mock(ParkingAllocation.class);
            User allocationOwner = mock(User.class);
            when(allocationOwner.getUserId()).thenReturn(50);
            when(mockAllocation.getMember()).thenReturn(allocationOwner);
            when(parkingAllocationRepository.findById(1)).thenReturn(Optional.of(mockAllocation));

            parkingAllocationService.stopParkingAllocation(request);

            verify(mockAllocation).stopParkingAllocation();
            verify(parkingAllocationRepository).save(mockAllocation);
        }

        @Test
        void stopParkingAllocation_NotOwner_ThrowsException() {
            StopParkingAllocationRequest request = new StopParkingAllocationRequest(1, 50);

            User mockUser = mock(User.class);
            when(mockUser.getUserId()).thenReturn(50);
            when(userRepository.findById(50)).thenReturn(Optional.of(mockUser));

            ParkingAllocation mockAllocation = mock(ParkingAllocation.class);
            User allocationOwner = mock(User.class);
            when(allocationOwner.getUserId()).thenReturn(999);
            when(mockAllocation.getMember()).thenReturn(allocationOwner);
            when(parkingAllocationRepository.findById(1)).thenReturn(Optional.of(mockAllocation));

            assertThrows(InvalidParkingAllocationOwnerException.class, () ->
                    parkingAllocationService.stopParkingAllocation(request)
            );
            verify(mockAllocation, never()).stopParkingAllocation();
            verify(parkingAllocationRepository, never()).save(mockAllocation);
        }
    }
}