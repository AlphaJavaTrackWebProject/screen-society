package org.alphatrack.screensociety.controllers.mvc;

import jakarta.validation.Valid;
import org.alphatrack.screensociety.dto.request.CommentRequestDto;
import org.alphatrack.screensociety.models.Comment;
import org.alphatrack.screensociety.models.User;
import org.alphatrack.screensociety.security.CustomUserDetails;
import org.alphatrack.screensociety.services.contracts.CommentService;
import org.alphatrack.screensociety.utils.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentController(CommentService commentService,ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.commentService = commentService;
    }

    @GetMapping("/{id}/update")
    public String editCommentView(@PathVariable Long id,
                                  @AuthenticationPrincipal CustomUserDetails currentUser,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            Comment comment = commentService.getCommentForEdit(id, currentUser.getUser());
            model.addAttribute("comment", modelMapper.commentToCommentRequestDto(comment));
            model.addAttribute("id", id);
            return "CommentEditView";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not allowed to edit this comment.");
            return "redirect:/posts";
        }
    }

    @PostMapping("/{id}/update")
    public String editComment(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails currentUser,
                              @Valid @ModelAttribute("comment") CommentRequestDto commentRequestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "CommentEditView";
        }

        Long postId = commentService.getCommentById(id).getPost().getId();

        commentService.updateComment(id, commentRequestDto, currentUser.getUser());

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{id}/delete")
    public String deleteComment(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails currentUser) {

        Long postId = commentService.getCommentById(id).getPost().getId();

        commentService.deleteComment(id, currentUser.getUser());

        return "redirect:/posts/" + postId;
    }

}
