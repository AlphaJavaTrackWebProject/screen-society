package org.alphatrack.screensociety.services;

import jakarta.persistence.EntityNotFoundException;
import org.alphatrack.screensociety.dto.request.CommentRequestDto;
import org.alphatrack.screensociety.dto.request.filters.CommentFilterOptions;
import org.alphatrack.screensociety.models.Comment;
import org.alphatrack.screensociety.models.Post;
import org.alphatrack.screensociety.models.User;
import org.alphatrack.screensociety.models.enums.Role;
import org.alphatrack.screensociety.repositories.contracts.CommentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    public void getAllComments_Should_CallRepository() {
        CommentFilterOptions filterOptions = Mockito.mock(CommentFilterOptions.class);

        commentService.getAllComments(filterOptions);

        Mockito.verify(commentRepository, Mockito.times(1)).findAll(filterOptions);
    }

    @Test
    public void getCommentById_Should_Throw_When_NotFound() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> commentService.getCommentById(1L));
    }

    @Test
    public void getCommentById_Should_ReturnComment() {
        Comment comment = new Comment();
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Comment result = commentService.getCommentById(1L);

        Assertions.assertEquals(comment, result);
    }

    @Test
    public void updateComment_Should_Throw_When_UserIsNotOwner() {
        User author = Mockito.mock(User.class);
        Comment comment = new Comment();
        comment.setAuthor(author);

        User currentUser = Mockito.mock(User.class);
        CommentRequestDto dto = new CommentRequestDto();

        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Assertions.assertThrows(AccessDeniedException.class, () -> commentService.updateComment(1L, dto, currentUser));
    }

    @Test
    public void updateComment_Should_UpdateContentAndSave_When_UserIsOwner() {
        User currentUser = Mockito.mock(User.class);
        Comment comment = new Comment();
        comment.setAuthor(currentUser);

        CommentRequestDto dto = new CommentRequestDto();
        dto.setContent("Updated Content");

        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentService.updateComment(1L, dto, currentUser);

        ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
        Mockito.verify(commentRepository, Mockito.times(1)).save(commentCaptor.capture());

        Comment capturedComment = commentCaptor.getValue();
        Assertions.assertEquals("Updated Content", capturedComment.getContent());
    }

    @Test
    public void deleteComment_Should_Throw_When_UserIsNotOwnerOrAdmin() {
        User author = Mockito.mock(User.class);
        Comment comment = new Comment();
        comment.setAuthor(author);

        User currentUser = Mockito.mock(User.class);
        Mockito.when(currentUser.getRole()).thenReturn(Role.USER);

        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Assertions.assertThrows(AccessDeniedException.class, () -> commentService.deleteComment(1L, currentUser));
    }

    @Test
    public void deleteComment_Should_Delete_When_UserIsOwner() {
        User currentUser = Mockito.mock(User.class);
        Mockito.when(currentUser.getRole()).thenReturn(Role.USER);

        Post post = Mockito.mock(Post.class);
        Comment comment = new Comment();
        comment.setAuthor(currentUser);
        comment.setPost(post);

        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentService.deleteComment(1L, currentUser);

        Mockito.verify(post, Mockito.times(1)).removeComment(comment);
        Mockito.verify(commentRepository, Mockito.times(1)).delete(comment);
    }

    @Test
    public void deleteComment_Should_Delete_When_UserIsAdmin() {
        User author = Mockito.mock(User.class);

        User adminUser = Mockito.mock(User.class);
        Mockito.when(adminUser.getRole()).thenReturn(Role.ADMIN);

        Post post = Mockito.mock(Post.class);
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setPost(post);

        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentService.deleteComment(1L, adminUser);

        Mockito.verify(post, Mockito.times(1)).removeComment(comment);
        Mockito.verify(commentRepository, Mockito.times(1)).delete(comment);
    }

    @Test
    public void getCommentForEdit_Should_Throw_When_UserIsNotOwner() {
        User author = Mockito.mock(User.class);
        Comment comment = new Comment();
        comment.setAuthor(author);

        User currentUser = Mockito.mock(User.class);

        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Assertions.assertThrows(AccessDeniedException.class, () -> commentService.getCommentForEdit(1L, currentUser));
    }

    @Test
    public void getCommentForEdit_Should_ReturnComment_When_UserIsOwner() {
        User currentUser = Mockito.mock(User.class);
        Comment comment = new Comment();
        comment.setAuthor(currentUser);

        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Comment result = commentService.getCommentForEdit(1L, currentUser);

        Assertions.assertEquals(comment, result);
    }
}