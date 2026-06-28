package org.alphatrack.screensociety.controllers.rest;

import jakarta.validation.Valid;
import org.alphatrack.screensociety.models.User;
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


    @PostMapping
    public void createUser(@Valid @RequestBody /*TODO UserDTO*/) {
        //call the service to create a new user
        //service.create(userDTO)
    }

    @GetMapping
    public List<User> getAll(@AuthenticationPrincipal User currentUser) {
        //service.getAll(currentUser)
        return null; //TODO
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {
        return null; //TODO
    }

    @GetMapping("/search")
    public List<User> searchUser(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName) {
        //return service.searchUser(username,email,firstName)
    }

    @GetMapping("/{targetId}/posts")
    public List<Post> getUserPosts(
            @PathVariable int targetId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String filterBy) {

        //return service.getUserPosts(targetId,sortBy,filterBy)
    }

    @DeleteMapping("/{targetId}")
    public void deleteUser(@PathVariable int targetId, @AuthenticationPrincipal User currentUser) {
        //service.delete(targetId,currentUser)
    }

    @PutMapping("/{targetId}")
    public void updateUser(@Valid @RequestBody /*TODO userDTO*/, @AuthenticationPrincipal User currentUser, @PathVariable int targetId) {
        //service.updateUser(userDTO,currentUser,id)
    }

    @PutMapping("/{targetId}/status")
    public void changeUserStatus(@Valid @RequestBody boolean isBlocked, @AuthenticationPrincipal User currentUser, @PathVariable int targetId) {
        //service.changeStatus(isBlocked,currentUser,targetId)
    }

    @PutMapping("/{targetId}/role")
    public void changeUserRole(@Valid @RequestBody /*TODO ENUM ROLE CLASS*/ role, @AuthenticationPrincipal User currentUser, @PathVariable int targetId) {
        //service.changeRole(role,currentUser,targetId)
    }
}
