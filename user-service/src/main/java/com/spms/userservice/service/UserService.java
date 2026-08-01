package com.spms.userservice.service;

import com.spms.userservice.dto.UserRegistrationRequest;
import com.spms.userservice.dto.UserResponse;
import com.spms.userservice.dto.UserUpdateRequest;
import com.spms.userservice.entity.Role;
import com.spms.userservice.entity.User;
import com.spms.userservice.exception.DuplicateEmailException;
import com.spms.userservice.exception.ResourceNotFoundException;
import com.spms.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse register(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("A user with email '" + request.getEmail() + "' already exists");
        }

        Role role = request.getRole() != null ? request.getRole() : Role.USER;

        User user = new User(
                request.getName(),
                request.getEmail(),
                request.getPassword(), // NOTE: plain text for assignment scope; use a hash (BCrypt) in production
                request.getPhone(),
                role
        );

        User saved = userRepository.save(user);
        return new UserResponse(saved);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::new)
                .toList();
    }

    public UserResponse getUserById(Long id) {
        User user = findUserOrThrow(id);
        return new UserResponse(user);
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = findUserOrThrow(id);

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            user.setPhone(request.getPhone());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(request.getPassword());
        }

        User saved = userRepository.save(user);
        return new UserResponse(saved);
    }

    /** Package-private/internal helper reused by the booking feature. */
    User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
