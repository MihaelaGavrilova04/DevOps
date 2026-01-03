package com.frontend.user.service;

import com.frontend.user.dto.UserDTO;
import com.frontend.user.model.User;
import com.frontend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceLazyTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    @Test
    void toDTO_shouldMapUserCorrectly() {
        User user = new User();
        user.setName("Alice");
        user.setAge(25);

        UserDTO dto = new UserDTO(user.getId(), user.getName(), user.getAge());

        assertEquals(user.getName(), dto.name());
        assertEquals(user.getAge(), dto.age());
        assertEquals(user.getId(), dto.id());
    }
}
