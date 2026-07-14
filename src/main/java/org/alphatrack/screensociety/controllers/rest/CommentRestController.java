package org.alphatrack.screensociety.controllers.rest;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.alphatrack.screensociety.dto.request.CommentRequestDto;
import org.alphatrack.screensociety.dto.response.CommentResponseDto;
import org.alphatrack.screensociety.models.Comment;
import org.alphatrack.screensociety.models.User;
import org.alphatrack.screensociety.security.CustomUserDetails;
import org.alphatrack.screensociety.services.contracts.CommentService;
import org.alphatrack.screensociety.utils.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    private final CommentService commentService;
    private final ModelMapper modelMapper;


    public CommentRestController(CommentService commentService, ModelMapper modelMapper) {
        this.commentService = commentService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Edits existing comment, Admin,owner or moderator only")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    @PutMapping("/{commentId}")
    public CommentResponseDto editComment(@PathVariable Long commentId, @AuthenticationPrincipal CustomUserDetails currentUser,
                                          @Valid @RequestBody CommentRequestDto commentRequestDto) {

       Comment updatedComment =  commentService.updateComment(commentId,commentRequestDto,currentUser.getUser());

        return modelMapper.commentToResponseDto(updatedComment);
    }

    @Operation(summary = "Deletes existing comment, Admin,owner or moderator only")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal CustomUserDetails currentUser) {

        commentService.deleteComment(commentId, currentUser.getUser());
    }
}
