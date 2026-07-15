package org.alphatrack.screensociety.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.alphatrack.screensociety.dto.request.UserRegistrationDto;
import org.alphatrack.screensociety.dto.request.UserUpdateDto;
import org.alphatrack.screensociety.dto.request.filters.PostFilterOptions;
import org.alphatrack.screensociety.dto.request.filters.UserFilterOptions;
import org.alphatrack.screensociety.exceptions.AuthorizationFailureException;
import org.alphatrack.screensociety.exceptions.DuplicateEntityException;
import org.alphatrack.screensociety.models.Post;
import org.alphatrack.screensociety.models.User;
import org.alphatrack.screensociety.models.enums.Role;
import org.alphatrack.screensociety.repositories.contracts.PostRepository;
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
    private final PostRepository postRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.postRepository = postRepository;
    }

    @Transactional
    @Override
    public User registerUser(UserRegistrationDto userRegistrationDto) {
        if (userRepository.findUserByUsername(userRegistrationDto.getUsername()).isPresent()) {
            throw new DuplicateEntityException("User", "username", userRegistrationDto.getUsername());
        }
        if (userRepository.findUserByEmail(userRegistrationDto.getEmail()).isPresent()) {
            throw new DuplicateEntityException("User", "email", userRegistrationDto.getEmail());
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
    public User updateProfile(UserUpdateDto userUpdateDto, User currentUser, Long userid) {

        boolean isOwner = currentUser.getId().equals(userid);


        if (!isOwner) {
            throw new AuthorizationFailureException("Only the owner can edit this account.");
        }

        if (userUpdateDto.getFirstName() != null) {
            currentUser.setFirstName(userUpdateDto.getFirstName());
        }

        if (userUpdateDto.getLastName() != null) {
            currentUser.setLastName(userUpdateDto.getLastName());
        }

        return userRepository.save(currentUser);
    }

    @Override
    public List<User> searchUsers(UserFilterOptions options) {
        return userRepository.findAll(options);
    }

    @Transactional
    @Override
    public void blockUser(Long userId) {
        User user = getUserById(userId);

        if (user.getRole() == Role.ADMIN) {
            throw new AuthorizationFailureException("You cannot block another admin");
        }

        user.setIsBlocked(true);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void unBlockUser(Long userId) {
        User user = getUserById(userId);
        user.setIsBlocked(false);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void promoteToAdmin(Long userId) {
        User user = getUserById(userId);
        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %d not found", id)));
    }

    @Override
    public List<Post> getUserPosts(Long userId, PostFilterOptions filterOptions) {

        return postRepository.findAll(userId, filterOptions);

    }

    @Override
    public void removeUser(Long id, User currentUser) {

        boolean isAdmin = currentUser.getRole().equals(Role.ADMIN);
        boolean isOwner = currentUser.getId().equals(id);


        if (!isOwner && !isAdmin) {
            throw new AuthorizationFailureException("Only the author or an admin can delete this account.");
        }

        userRepository.delete(getUserById(id));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with username %s not found", username)));
    }


}
