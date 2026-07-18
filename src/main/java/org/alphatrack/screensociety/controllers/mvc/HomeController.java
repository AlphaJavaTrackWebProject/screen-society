package org.alphatrack.screensociety.controllers.mvc;

import org.alphatrack.screensociety.dto.request.filters.PostFilterOptions;
import org.alphatrack.screensociety.dto.request.filters.UserFilterOptions;
import org.alphatrack.screensociety.services.contracts.PostService;
import org.alphatrack.screensociety.services.contracts.UserService;
import org.alphatrack.screensociety.utils.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    private final PostService postService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Autowired
    public HomeController(PostService postService, ModelMapper modelMapper, UserService userService) {
        this.postService = postService;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping
    public String showHomePage(Model model){

        model.addAttribute("recentPosts", modelMapper.postsToPostsDto(postService.show10MostRecentPosts()));
        model.addAttribute("commentedPosts", modelMapper.postsToPostsDto(postService.showTop10MostCommented()));
        model.addAttribute("numberOfPosts",postService.searchPosts(PostFilterOptions.builder().build()).size());
        model.addAttribute("registeredUsers", userService.searchUsers(UserFilterOptions.builder().build()).size());

        return "index";
    }

    @GetMapping("/about")
    public String showAboutPage(){
        return "about";
    }


    @GetMapping("/contact")
    public String showContactPage(){
        return "contact";
    }
}
