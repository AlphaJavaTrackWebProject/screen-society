package org.alphatrack.screensociety.controllers.mvc;

import jakarta.validation.Valid;
import org.alphatrack.screensociety.dto.request.CommentRequestDto;
import org.alphatrack.screensociety.dto.request.PostRequestDto;
import org.alphatrack.screensociety.dto.request.PostUpdateRequestDto;
import org.alphatrack.screensociety.dto.request.filters.PostFilterOptions;
import org.alphatrack.screensociety.models.Post;
import org.alphatrack.screensociety.models.User;
import org.alphatrack.screensociety.services.contracts.PostService;
import org.alphatrack.screensociety.utils.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/posts")
public class PostController {
    private final ModelMapper modelMapper;
    private final PostService postService;

    @Autowired
    public PostController(ModelMapper modelMapper, PostService postService) {
        this.modelMapper = modelMapper;
        this.postService = postService;
    }

    @GetMapping
    public String getAllPosts(Model model, @ModelAttribute PostFilterOptions filterOptions) {
        model.addAttribute("posts", postService.searchPosts(filterOptions));
        return "PostsView";
    }

    @GetMapping("/{id}")
    public String getPostById(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.getByPostId(id));
        model.addAttribute("comment", new CommentRequestDto());
        return "PostView";
    }

    @GetMapping("/new")
    public String postCreateView(Model model) {
        model.addAttribute("post", new PostRequestDto());
        return "postCreate";
    }

    @PostMapping("/new")
    public String createNewPost(@Valid @ModelAttribute PostRequestDto postRequestDto, BindingResult bindingResult, @AuthenticationPrincipal User currentUser) {

        if (bindingResult.hasErrors()) {
            return "PostCreateView";
        }

        postService.createPost(postRequestDto, currentUser);
        return "redirect:/posts";
    }

    @GetMapping("/{id}/update")
    public String postEditView (@PathVariable Long id, @AuthenticationPrincipal User currentUser, Model model){

        Post targetPost = postService.getPostForUpdate(id,currentUser);

        PostUpdateRequestDto requestDto = modelMapper.postToPostUpdateDto(targetPost);
        model.addAttribute("postUpdate", requestDto);

        return "PostUpdateView";
    }

    @PostMapping("/{id}/update")
    public String postSaveChanges(@PathVariable Long id,@Valid @ModelAttribute("postUpdate") PostUpdateRequestDto requestDto,
                                  BindingResult bindingResult, @AuthenticationPrincipal User currentUser){

        if (bindingResult.hasErrors()){
            return "PostUpdateView";
        }

        postService.updatePost(id,requestDto, currentUser);

        return "redirect:/posts";
    }

    @PostMapping("/{id}/delete")
    public String postDelete (@PathVariable Long id,@AuthenticationPrincipal User currentUser){

        postService.deletePost(id, currentUser);

        return "redirect:/posts";
    }

    @PostMapping("/{postId}/comment")
    public String addComment(@PathVariable Long postId, @AuthenticationPrincipal User currentUser,
                             @Valid @ModelAttribute CommentRequestDto commentRequestDto,BindingResult bindingResult,
                             Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("post", postService.getByPostId(postId));
            return "PostView";
        }
        postService.addCommentOnPost(commentRequestDto, postId, currentUser);

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{postId}/like")
    public String addLike(@PathVariable Long postId,@AuthenticationPrincipal User currentUser){

        postService.addLikesOnPost(postId, currentUser);

        return "redirect:/posts/" + postId;
    }
}
