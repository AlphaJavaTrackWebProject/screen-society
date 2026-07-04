package org.alphatrack.screensociety.controllers.rest;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.alphatrack.screensociety.dto.request.CommentRequestDto;
import org.alphatrack.screensociety.dto.response.CommentResponseDto;
import org.alphatrack.screensociety.models.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    //TODO inject the dependencies


    public CommentRestController() {
    }

    @Operation(summary = "Edits existing comment, Admin,owner or moderator only")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    @PutMapping("/{commentId}")
    public CommentResponseDto editComment(@PathVariable int commentId, @AuthenticationPrincipal User currentUser,
                                          @Valid @RequestBody CommentRequestDto commentRequestDto) {
        //service.editComment(commentId,currentUser,commentDTO);
        return null;
    }

    @Operation(summary = "Deletes existing comment, Admin,owner or moderator only")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable int commentId, @AuthenticationPrincipal User currentUser) {
        //service.deleteComment(commentId,currentUser);
    }
}
