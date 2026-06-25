package org.alphatrack.screensociety.controllers.rest;


import jakarta.validation.Valid;
import org.alphatrack.screensociety.models.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    //TODO inject the dependencies


    public CommentRestController() {
    }

    @PutMapping("/{commentId}")
    public void editComment (@PathVariable int commentId, @AuthenticationPrincipal User currentUser, @Valid @RequestBody CommentDTO commentDTO){
        service.editComment(commentId,currentUser,commentDTO);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment (@PathVariable int commentId, @AuthenticationPrincipal User currentUser){
        service.deleteComment(commentId,currentUser);
    }
}
