package org.alphatrack.screensociety.controllers.rest;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.alphatrack.screensociety.dto.request.UserRegistrationDto;

import org.alphatrack.screensociety.dto.request.UserUpdateDto;
import org.alphatrack.screensociety.dto.request.filters.PostFilterOptions;
import org.alphatrack.screensociety.dto.request.filters.UserFilterOptions;
import org.alphatrack.screensociety.dto.response.PostResponseDto;
import org.alphatrack.screensociety.dto.response.UserResponseDto;
import org.alphatrack.screensociety.models.User;

import org.alphatrack.screensociety.services.contracts.UserService;
import org.alphatrack.screensociety.utils.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserRestController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Creates new user")
    @PostMapping
    public UserResponseDto createUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {

        return modelMapper.userToUserDto(userService.registerUser(userRegistrationDto));
    }

    @Operation(summary = "Returns a list with all users")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDto> getAll() {
        return modelMapper.usersListToResponseDtoList(userService.searchUsers(UserFilterOptions.builder().build()));
    }

    @Operation(summary = "Returns a specific user via provided ID")
    @GetMapping("/{id}")
    public UserResponseDto getById(@PathVariable Long id) {

        return modelMapper.userToUserDto(userService.getUserById(id));

    }

    @Operation(summary = "Searches a user based on a filter : username, email or first name")
    @GetMapping("/search")
    public List<UserResponseDto> searchUser(

            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName) {

        UserFilterOptions filterOptions = UserFilterOptions.builder()
                .username(username)
                .email(email)
                .firstName(firstName)
                .build();

        return modelMapper.usersListToResponseDtoList(userService.searchUsers(filterOptions));
    }

    @Operation(summary = "Returns a specific user's posts, which can be sorted or filtered by criteria")
    @GetMapping("/{targetId}/posts")
    public List<PostResponseDto> getUserPosts(

            @PathVariable Long targetId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String authorUsername,
            @RequestParam(required = false) String tagName,
            @RequestParam(required = false) LocalDate createdBefore,
            @RequestParam(required = false) LocalDate createdAfter,
            @RequestParam(required = false) String sortOrder) {

        PostFilterOptions postFilterOptions = PostFilterOptions.builder()
                .sortBy(sortBy)
                .title(title)
                .authorUsername(authorUsername)
                .tagName(tagName)
                .createdBefore(createdBefore)
                .createdAfter(createdAfter)
                .sortOrder(sortOrder)
                .build();

        return modelMapper.postsToPostsDto(userService.getUserPosts(targetId, postFilterOptions));

    }

    @Operation(summary = "Deletes an existing user")
    @PreAuthorize("hasRole('ADMIN') or #currentUser.id == #targetId ")
    @DeleteMapping("/{targetId}")
    public void deleteUser(@PathVariable Long targetId, @AuthenticationPrincipal User currentUser) {

        userService.removeUser(targetId, currentUser);
    }

    @Operation(summary = "Editing a specific user")
    @PreAuthorize("hasRole('ADMIN') or #currentUser.id == #targetId ")
    @PutMapping("/{targetId}")
    public UserResponseDto updateUser(@Valid @RequestBody UserUpdateDto userUpdateDto,
                                      @AuthenticationPrincipal User currentUser, @PathVariable Long targetId) {

        return modelMapper.userToUserDto(userService.updateProfile(userUpdateDto, currentUser, targetId));

    }

    @Operation(summary = "Changes a specific user's status, ADMIN only")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{targetId}/status")
    public void changeStatus(@PathVariable Long targetId) {

        User targetUser = userService.getUserById(targetId);

        if (targetUser.getIsBlocked()) {
            userService.unBlockUser(targetId);
        } else {
            userService.blockUser(targetId);
        }
    }

    @Operation(summary = "Changes a specific user's role, ADMIN only")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{targetId}/role")
    public void changeUserRole(@PathVariable Long targetId) {
        userService.promoteToAdmin(targetId);
    }
}
