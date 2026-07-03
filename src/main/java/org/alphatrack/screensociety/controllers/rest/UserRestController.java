package org.alphatrack.screensociety.controllers.rest;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.alphatrack.screensociety.dto.request.UserRequestDto;
import org.alphatrack.screensociety.dto.response.PostResponseDto;
import org.alphatrack.screensociety.dto.response.UserResponseDto;
import org.alphatrack.screensociety.models.User;
import org.alphatrack.screensociety.models.enums.Role;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    //TODO inject the dependencies


    public UserRestController() {
    }

    @Operation(summary = "Creates new user")
    @PostMapping
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        //call the service to create a new user
        //service.create(userDTO)
        return null;
    }

    @Operation(summary = "Returns a list with all users")
    @GetMapping
    public List<UserResponseDto> getAll(@AuthenticationPrincipal User currentUser) {
        //service.getAll(currentUser)
        return null; //TODO
    }

    @Operation(summary = "Returns a specific user via provided ID")
    @GetMapping("/{id}")
    public UserResponseDto getById(@PathVariable int id) {
        return null; //TODO
    }

    @Operation(summary = "Searches a user based on a filter : username, email or first name")
    @GetMapping("/search")
    public List<UserResponseDto> searchUser(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName) {
        return null; //service.searchUser(username,email,firstName)
    }

    @Operation(summary = "Returns a specific user's posts, which can be sorted or filtered by criteria")
    @GetMapping("/{targetId}/posts")
    public List<PostResponseDto> getUserPosts(
            @PathVariable int targetId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String filterBy) {

        return null; //service.getUserPosts(targetId,sortBy,filterBy)
    }

    @Operation(summary = "Deletes an existing user")
    @DeleteMapping("/{targetId}")
    public void deleteUser(@PathVariable int targetId, @AuthenticationPrincipal User currentUser) {
        //service.delete(targetId,currentUser)
    }

    @Operation(summary = "Editing a specific user")
    @PutMapping("/{targetId}")
    public UserResponseDto updateUser(@Valid @RequestBody UserRequestDto userRequestDto,
                                      @AuthenticationPrincipal User currentUser, @PathVariable int targetId) {
        //service.updateUser(userDTO,currentUser,id)
        return null;
    }
    @Operation(summary = "Changes a specific user's status, ADMIN only")
    @PutMapping("/{targetId}/status")
    public void changeUserStatus(@Valid @RequestBody boolean isBlocked, @AuthenticationPrincipal User currentUser,
                                 @PathVariable int targetId) {
        //service.changeStatus(isBlocked,currentUser,targetId)
    }

    @Operation(summary = "Changes a specific user's role, ADMIN only")
    @PutMapping("/{targetId}/role")
    public void changeUserRole(@Valid @RequestBody Role role, @AuthenticationPrincipal User currentUser,
                               @PathVariable int targetId) {
        //service.changeRole(role,currentUser,targetId)
    }
}
