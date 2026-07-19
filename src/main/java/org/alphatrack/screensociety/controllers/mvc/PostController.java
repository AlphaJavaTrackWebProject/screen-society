package org.alphatrack.screensociety.controllers.mvc;

import jakarta.validation.Valid;
import org.alphatrack.screensociety.dto.request.CommentRequestDto;
import org.alphatrack.screensociety.dto.request.PostRequestDto;
import org.alphatrack.screensociety.dto.request.PostUpdateRequestDto;
import org.alphatrack.screensociety.dto.request.filters.PostFilterOptions;
import org.alphatrack.screensociety.exceptions.AuthorizationFailureException;
import org.alphatrack.screensociety.models.Post;
import org.alphatrack.screensociety.models.User;
import org.alphatrack.screensociety.security.CustomUserDetails;
import org.alphatrack.screensociety.services.contracts.PostService;
import org.alphatrack.screensociety.utils.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


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
    public String getAllPosts(Model model, @ModelAttribute("filterOptions") PostFilterOptions filterOptions) {
        model.addAttribute("posts", postService.searchPosts(filterOptions));
        return "postsView";
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
    public String createNewPost(@Valid @ModelAttribute("post") PostRequestDto postRequestDto, BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails currentUser) {

        if (bindingResult.hasErrors()) {
            return "postCreate";
        }

        postService.createPost(postRequestDto, currentUser.getUser());
        return "redirect:/posts";
    }

    @GetMapping("/{id}/update")
    public String postEditView(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails currentUser, Model model, RedirectAttributes redirectAttributes) {
        try {
            Post targetPost = postService.getPostForUpdate(id, currentUser.getUser());
            PostUpdateRequestDto requestDto = modelMapper.postToPostUpdateDto(targetPost);
            model.addAttribute("postUpdate", requestDto);
            model.addAttribute("postUpdateId", id);
            return "postUpdateView";
        } catch (AuthorizationFailureException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/posts/" + id;
        }
    }

    @PostMapping("/{id}/update")
    public String postSaveChanges(@PathVariable Long id, @Valid @ModelAttribute("postUpdate") PostUpdateRequestDto requestDto,
                                  BindingResult bindingResult, Model model,@AuthenticationPrincipal CustomUserDetails currentUser) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("postUpdateId", id);
            return "postUpdateView";
        }

        postService.updatePost(id, requestDto, currentUser.getUser());

        return "redirect:/posts/" + id;

    }

    @PostMapping("/{id}/delete")
    public String postDelete(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails currentUser) {

        postService.deletePost(id, currentUser.getUser());

        return "redirect:/posts";
    }

    @PostMapping("/{postId}/comment")
    public String addComment(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails currentUser,
                             @Valid @ModelAttribute("comment") CommentRequestDto commentRequestDto, BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("post", postService.getByPostId(postId));
            return "PostView";
        }
        postService.addCommentOnPost(commentRequestDto, postId, currentUser.getUser());

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{postId}/like")
    public String addLike(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails currentUser) {

        postService.addLikesOnPost(postId, currentUser.getUser());

        return "redirect:/posts/" + postId;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // This custom editor splits the string by comma and trims whitespace
        binder.registerCustomEditor(Set.class, "tags", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.isBlank()) {
                    setValue(new HashSet<>());
                } else {
                    Set<String> tags = Arrays.stream(text.split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.toSet());
                    setValue(tags);
                }
            }
        });
    }
}
