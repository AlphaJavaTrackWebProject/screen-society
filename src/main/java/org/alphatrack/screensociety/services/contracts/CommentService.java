package org.alphatrack.screensociety.services.contracts;

import org.alphatrack.screensociety.dto.request.CommentRequestDto;
import org.alphatrack.screensociety.dto.request.filters.CommentFilterOptions;
import org.alphatrack.screensociety.models.Comment;
import org.alphatrack.screensociety.models.User;

import java.util.List;

public interface CommentService {
    List<Comment> getAllComments(CommentFilterOptions commentFilterOptions);

    Comment getCommentById(Long commentId);
    Comment updateComment(Long commentId, CommentRequestDto commentRequestDto, User currentUser);

    void deleteComment(Long commentId, User currentUser);


}
