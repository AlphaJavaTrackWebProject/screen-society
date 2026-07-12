package org.alphatrack.screensociety.controllers.mvc;

import org.alphatrack.screensociety.dto.request.filters.PostFilterOptions;
import org.alphatrack.screensociety.dto.request.filters.UserFilterOptions;
import org.alphatrack.screensociety.models.User;
import org.alphatrack.screensociety.services.contracts.PostService;
import org.alphatrack.screensociety.services.contracts.UserService;
import org.alphatrack.screensociety.utils.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
public class AdminController {

    private final PostService postService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminController(PostService postService, UserService userService, ModelMapper modelMapper) {
        this.postService = postService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/panel")
    public String adminPanel() {
        return "AdminPanelView";
    }

    @GetMapping("/users")
    public String getUsers(UserFilterOptions userFilterOptions, Model model){

        model.addAttribute("users", modelMapper.userListToAdminResponseDtoList(userService.searchUsers(userFilterOptions)));

        return "AdminUsersView";
    }


    @PostMapping("/{targetUser}/block")
    public String blockUser(@PathVariable Long targetUser) {

        userService.blockUser(targetUser);

        return "redirect:/admin/users";
    }

    @PostMapping("/{targetUser}/unblock")
    public String unblockUser(@PathVariable Long targetUser) {

        userService.unBlockUser(targetUser);

        return "redirect:/admin/users";
    }

    @GetMapping("/posts")
    public String getAllPosts(PostFilterOptions postFilterOptions, Model model){

        model.addAttribute("posts", modelMapper.postsToPostsDto(postService.searchPosts(postFilterOptions)));

        return "AdminPostsView";
    }

    @PostMapping("/{targetPost}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deletePost(@PathVariable Long targetPost, @AuthenticationPrincipal User currentUser) {

        postService.deletePost(targetPost, currentUser);

        return "redirect:/admin/posts";
    }

}

