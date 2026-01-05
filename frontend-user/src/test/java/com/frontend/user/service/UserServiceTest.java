package com.frontend.user.service;

import com.frontend.user.dto.UserDTO;
import com.frontend.user.model.User;
import com.frontend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    @Test
    void testUserServiceCreation() {
        assertNotNull(userService);
        assertNotNull(repository);
    }

    @Test
    void testToDTOMethod() {
        User user = new User();
        user.setName("Test");
        user.setAge(25);

        UserDTO dto = userService.toDTO(user);

        assertEquals("Test", dto.name());
        assertEquals(25, dto.age());
    }

    @Test
    void testUserModel() {
        User user = new User();
        user.setName("John");
        user.setAge(30);
        user.setPrivatePersonalInfo("secret");

        assertEquals("John", user.getName());
        assertEquals(30, user.getAge());
    }

    @Test
    void testUserDTO() {
        UserDTO dto = new UserDTO(1L, "Alice", 35);

        assertEquals(1L, dto.id());
        assertEquals("Alice", dto.name());
        assertEquals(35, dto.age());
    }

    @Test
    void testDTOListConversion() {
        User user1 = new User();
        user1.setName("User1");
        user1.setAge(20);

        User user2 = new User();
        user2.setName("User2");
        user2.setAge(30);

        List<User> users = List.of(user1, user2);
        List<UserDTO> dtos = users.stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getAge()))
                .toList();

        assertEquals(2, dtos.size());
        assertEquals("User1", dtos.get(0).name());
        assertEquals("User2", dtos.get(1).name());
    }
}