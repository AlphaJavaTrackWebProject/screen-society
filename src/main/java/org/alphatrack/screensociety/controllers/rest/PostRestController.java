package org.alphatrack.screensociety.controllers.rest;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.alphatrack.screensociety.dto.request.CommentRequestDto;
import org.alphatrack.screensociety.dto.request.PostRequestDto;
import org.alphatrack.screensociety.dto.request.PostUpdateRequestDto;
import org.alphatrack.screensociety.dto.request.filters.PostFilterOptions;
import org.alphatrack.screensociety.dto.response.PostResponseDto;
import org.alphatrack.screensociety.models.Post;
import org.alphatrack.screensociety.models.User;
import org.alphatrack.screensociety.security.CustomUserDetails;
import org.alphatrack.screensociety.services.contracts.PostService;
import org.alphatrack.screensociety.utils.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService postService;
    private final ModelMapper modelMapper;

    @Autowired
    public PostRestController(PostService postService, ModelMapper modelMapper) {
        this.postService = postService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Retrieves all posts with option to filter by passed tag")
    @GetMapping
    public List<PostResponseDto> getAll(@RequestParam(required = false) String tagToFilter) {

        PostFilterOptions postFilterOptions = PostFilterOptions.builder()
                .tagName(tagToFilter)
                .build();

        return modelMapper.postsToPostsDto(postService.searchPosts(postFilterOptions));
    }

    @Operation(summary = "Retrieves a specific post by id")
    @GetMapping("/{id}")
    public PostResponseDto getById(@PathVariable Long id) {

        return modelMapper.postToPostResponseDto(postService.getByPostId(id));
    }

    @Operation(summary = "Creates new post")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    @PostMapping
    public PostResponseDto createPost(@Valid @RequestBody PostRequestDto postDTO, @AuthenticationPrincipal CustomUserDetails currentUser) {

        Post newPost = postService.createPost(postDTO, currentUser.getUser());

        return modelMapper.postToPostResponseDto(newPost);

    }

    @Operation(summary = "Edit existing post with option to add tag")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PutMapping("/{targetId}")
    public PostResponseDto updatePost(@Valid @RequestBody PostUpdateRequestDto postUpdateDTO,
                                      @PathVariable Long targetId, @AuthenticationPrincipal CustomUserDetails currentUser) {
        Post updatedPost = postService.updatePost(targetId,postUpdateDTO,currentUser.getUser());

        return modelMapper.postToPostResponseDto(updatedPost);

    }

    @Operation(summary = "Deletes a post, Admin,owner or moderator only")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    @DeleteMapping("/{targetId}")
    public void deletePost(@PathVariable Long targetId, @AuthenticationPrincipal CustomUserDetails currentUser) {

        postService.deletePost(targetId, currentUser.getUser());
    }

    @Operation(summary = "Adds a comment to specific post")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    @PostMapping("/{targetId}/comment")
    public void comment(@PathVariable Long targetId, @AuthenticationPrincipal CustomUserDetails currentUser, @RequestBody CommentRequestDto commentDTO) {

        postService.addCommentOnPost(commentDTO, targetId, currentUser.getUser());

    }

    @Operation(summary = "Adds a like to a specific post")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    @PutMapping("/{targetId}/like")
    public void likePost(@PathVariable Long targetId, @AuthenticationPrincipal CustomUserDetails currentUser) {
        postService.addLikesOnPost(targetId, currentUser.getUser());
    }

}
