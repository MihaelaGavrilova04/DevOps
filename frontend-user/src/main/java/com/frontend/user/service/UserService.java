package com.frontend.user.service;

import com.frontend.user.dto.UserDTO;
import com.frontend.user.model.User;
import com.frontend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserDTO findById(Long id) {
        User found = repository.findById(id).orElseThrow(() -> new RuntimeException("User not found!"));
        return toDTO(found);
    }

    public List<UserDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getAge());
    }
}
