package org.alphatrack.screensociety.controllers.rest;


import jakarta.validation.Valid;
import org.alphatrack.screensociety.dto.request.CommentRequestDto;
import org.alphatrack.screensociety.dto.request.PostRequestDto;
import org.alphatrack.screensociety.dto.request.PostUpdateRequestDto;
import org.alphatrack.screensociety.dto.response.PostResponseDto;
import org.alphatrack.screensociety.models.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    //TODO inject the dependencies


    public PostRestController() {
    }

    @GetMapping
    public List<PostResponseDto> getAll(@RequestParam(required = false) String tagToFilter) {
        return null;/*service.getAll(tagToFilter);*/
    }

    @GetMapping("/{id}")
    public PostResponseDto getById(@PathVariable int id) {
        return null; /*service.getById(id);*/
    }

    @PostMapping
    public PostResponseDto createPost(@Valid @RequestBody PostRequestDto postDTO, @AuthenticationPrincipal User currentUser) {
        return null;/*service.create(postDTO, currentUser);*/
    }

    @PutMapping("/{targetId}")
    public PostResponseDto updatePost(@Valid @RequestBody PostUpdateRequestDto postUpdateDTO,
                                      @PathVariable int targetId, @AuthenticationPrincipal User currentUser) {
        return null;//service.update(postUpdateDTO, targetId, currentUser);
    }

    @DeleteMapping("/{targetId}")
    public void deletePost(@PathVariable int targetId, @AuthenticationPrincipal User currentUser) {
        //service.delete(targetId, currentUser);
    }

    @PostMapping("/{targetId}/comment")
    public void comment(@PathVariable int targetId, @AuthenticationPrincipal User currentUser, @RequestBody CommentRequestDto commentDTO) {
        //service.comment(targetId, currentUser, commentDTO);
    }

    @PutMapping("/{targetId}/like")
    public void likePost(@PathVariable int targetId, @AuthenticationPrincipal User currentUser) {
        // service.likePost(targetId, currentUser);
    }

}
