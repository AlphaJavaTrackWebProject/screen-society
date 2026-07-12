package org.alphatrack.screensociety.services;

import jakarta.persistence.EntityNotFoundException;
import org.alphatrack.screensociety.dto.request.CommentRequestDto;
import org.alphatrack.screensociety.dto.request.filters.CommentFilterOptions;
import org.alphatrack.screensociety.models.Comment;
import org.alphatrack.screensociety.models.User;
import org.alphatrack.screensociety.models.enums.Role;
import org.alphatrack.screensociety.repositories.contracts.CommentRepository;
import org.alphatrack.screensociety.services.contracts.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> getAllComments(CommentFilterOptions commentFilterOptions) {
        return commentRepository.findAll(commentFilterOptions);
    }

    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Comment with id %d does not exist.", commentId)));
    }

    @Transactional
    @Override
    public Comment updateComment(Long commentId, CommentRequestDto dto, User currentUser) {
        Comment comment = getCommentById(commentId);

        boolean isOwner = comment.getAuthor().equals(currentUser);

        if (!isOwner) {
            throw new AccessDeniedException("You can only edit your own comments.");
        }

        comment.setContent(dto.getContent());

        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId, User currentUser) {
        Comment comment = getCommentById(commentId);

        boolean isOwner = comment.getAuthor().equals(currentUser);
        boolean isAdmin = currentUser.getRole().equals(Role.ADMIN);

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Only the author or an Admin can delete this comment");
        }

        comment.getPost().removeComment(comment);

        commentRepository.delete(comment);
    }

    @Override
    public Comment getCommentForEdit(Long commentId, User currentUser) {
        Comment comment = getCommentById(commentId);

        boolean isOwner = comment.getAuthor().equals(currentUser);

        if (!isOwner) {
            throw new AccessDeniedException("Only the author can edit this comment");
        }

        return comment;
    }
}
