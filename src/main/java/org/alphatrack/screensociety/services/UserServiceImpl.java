package org.alphatrack.screensociety.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.alphatrack.screensociety.dto.request.UserRegistrationDto;
import org.alphatrack.screensociety.dto.request.UserUpdateDto;
import org.alphatrack.screensociety.dto.request.filters.UserFilterOptions;
import org.alphatrack.screensociety.models.User;
import org.alphatrack.screensociety.models.enums.Role;
import org.alphatrack.screensociety.repositories.contracts.UserRepository;
import org.alphatrack.screensociety.services.contracts.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public User registerUser(UserRegistrationDto userRegistrationDto) {
        if (userRepository.findUserByUsername(userRegistrationDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.findUserByEmail(userRegistrationDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User newUser = User.builder()
                .username(userRegistrationDto.getUsername())
                .firstName(userRegistrationDto.getFirstName())
                .lastName(userRegistrationDto.getLastName())
                .password(passwordEncoder.encode(userRegistrationDto.getPassword()))
                .email(userRegistrationDto.getEmail())
                .role(Role.USER)
                .isBlocked(false)
                .build();

        return userRepository.save(newUser);
    }

    @Transactional
    @Override
    public User updateProfile(UserUpdateDto userUpdateDto, User currentUser) {
        if (userUpdateDto.getFirstName() != null) {
            currentUser.setFirstName(userUpdateDto.getFirstName());
        }

        if (userUpdateDto.getLastName() != null) {
            currentUser.setLastName(userUpdateDto.getLastName());
        }

        if (userUpdateDto.getEmail() != null && !userUpdateDto.getEmail().equals(currentUser.getEmail())) {
            if (userRepository.findUserByEmail(userUpdateDto.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email is already taken.");
            }
            currentUser.setEmail(userUpdateDto.getEmail());
        }
        return userRepository.save(currentUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<User> searchUsers(UserFilterOptions options) {
        return userRepository.findAll(options);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void blockUser(Integer userId) {
        User user = getUserById(userId);

        if (user.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("You cannot block another admin");
        }

        user.setBlocked(true);
        userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void unBlockUser(Integer userId) {
        User user = getUserById(userId);
        user.setBlocked(false);
        userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void promoteToAdmin(Integer userId) {
        User user = getUserById(userId);
        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }

    @Override
    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %d not found", id)));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with username %s not found", username)));
    }

}
