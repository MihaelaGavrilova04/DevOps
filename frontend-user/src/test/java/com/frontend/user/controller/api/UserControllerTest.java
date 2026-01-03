package com.frontend.user.controller.api;

import com.frontend.user.dto.UserDTO;
import com.frontend.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void getUser_shouldReturnUser_whenUserExists() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO(userId, "John Doe", 30);
        when(userService.findById(userId)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.getUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userId, response.getBody().id());
        assertEquals("John Doe", response.getBody().name());

        verify(userService, times(1)).findById(userId);
    }

    @Test
    void getUsers_shouldReturnAllUsers() {
        UserDTO user1 = new UserDTO(1L, "User 1", 25);
        UserDTO user2 = new UserDTO(2L, "User 2", 35);
        when(userService.getAll()).thenReturn(List.of(user1, user2));

        ResponseEntity<List<UserDTO>> response = userController.getUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(userService, times(1)).getAll();
    }

    @Test
    void getUsers_shouldReturnEmptyList_whenNoUsers() {
        when(userService.getAll()).thenReturn(List.of());

        ResponseEntity<List<UserDTO>> response = userController.getUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(userService, times(1)).getAll();
    }
}