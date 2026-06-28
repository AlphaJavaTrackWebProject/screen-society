package org.alphatrack.screensociety.controllers.rest;


import jakarta.validation.Valid;
import org.alphatrack.screensociety.models.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    //TODO inject the dependencies


    public PostRestController() {
    }

    @GetMapping
    public List<Post> getAll(@RequestParam(required = false) String tagToFilter) {
        return service.getAll(tagToFilter);
    }

    @GetMapping("/{id}")
    public Post getById(@PathVariable int id) {
        return service.getById(id);
    }

    @PostMapping
    public void createPost(@Valid @RequestBody PostDTO postDTO, @AuthenticationPrincipal User currentUser) {
        service.create(postDTO, currentUser);
    }

    @PutMapping("/{targetId}")
    public void updatePost(@Valid @RequestBody PostUpdateDTO postUpdateDTO, @PathVariable int targetId, @AuthenticationPrincipal User currentUser) {
        service.update(postUpdateDTO, targetId, currentUser);
    }

    @DeleteMapping("/{targetId}")
    public void deletePost(@PathVariable int targetId, @AuthenticationPrincipal User currentUser) {
        service.delete(targetId, currentUser);
    }

    @PostMapping("/{targetId}/comment")
    public void comment(@PathVariable int targetId, @AuthenticationPrincipal User currentUser, @RequestBody CommentDTO commentDTO) {
        service.comment(targetId, currentUser, commentDTO);
    }

    @PutMapping("/{targetId}/like")
    public void likePost(@PathVariable int targetId, @AuthenticationPrincipal User currentUser) {
        service.likePost(targetId, currentUser);
    }

}
