package org.alphatrack.screensociety.controllers.rest;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.alphatrack.screensociety.dto.request.CommentRequestDto;
import org.alphatrack.screensociety.dto.request.PostRequestDto;
import org.alphatrack.screensociety.dto.request.PostUpdateRequestDto;
import org.alphatrack.screensociety.dto.response.PostResponseDto;
import org.alphatrack.screensociety.models.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    //TODO inject the dependencies


    public PostRestController() {
    }

    @Operation(summary = "Retrieves all posts with option to filter by passed tag")
    @GetMapping
    public List<PostResponseDto> getAll(@RequestParam(required = false) String tagToFilter) {
        return null;/*service.getAll(tagToFilter);*/
    }

    @Operation(summary = "Retrieves a specific post by id")
    @GetMapping("/{id}")
    public PostResponseDto getById(@PathVariable int id) {
        return null; /*service.getById(id);*/
    }

    @Operation(summary = "Creates new post")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    @PostMapping
    public PostResponseDto createPost(@Valid @RequestBody PostRequestDto postDTO, @AuthenticationPrincipal User currentUser) {
        return null;/*service.create(postDTO, currentUser);*/
    }

    @Operation(summary = "Edit existing post with option to add tag")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PutMapping("/{targetId}")
    public PostResponseDto updatePost(@Valid @RequestBody PostUpdateRequestDto postUpdateDTO,
                                      @PathVariable int targetId, @AuthenticationPrincipal User currentUser) {
        return null;//service.update(postUpdateDTO, targetId, currentUser);
    }

    @Operation(summary = "Deletes a post, Admin,owner or moderator only")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    @DeleteMapping("/{targetId}")
    public void deletePost(@PathVariable int targetId, @AuthenticationPrincipal User currentUser) {
        //service.delete(targetId, currentUser);
    }

    @Operation(summary = "Adds a comment to specific post")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    @PostMapping("/{targetId}/comment")
    public void comment(@PathVariable int targetId, @AuthenticationPrincipal User currentUser, @RequestBody CommentRequestDto commentDTO) {
        //service.comment(targetId, currentUser, commentDTO);
    }
    @Operation(summary = "Adds a like to a specific post")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    @PutMapping("/{targetId}/like")
    public void likePost(@PathVariable int targetId, @AuthenticationPrincipal User currentUser) {
        // service.likePost(targetId, currentUser);
    }

}
