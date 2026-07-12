package org.alphatrack.screensociety.controllers.mvc;

import org.alphatrack.screensociety.services.contracts.PostService;
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

    @Autowired
    public HomeController(PostService postService, ModelMapper modelMapper) {
        this.postService = postService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public String showHomePage(Model model){

        model.addAttribute("recentPosts", modelMapper.postsToPostsDto(postService.show10MostRecentPosts()));
        model.addAttribute("commentedPosts", modelMapper.postsToPostsDto(postService.showTop10MostCommented()));

        return "HomeView";
    }

    @GetMapping("/about")
    public String showAboutPage(){
        return "AboutView";
    }

}
