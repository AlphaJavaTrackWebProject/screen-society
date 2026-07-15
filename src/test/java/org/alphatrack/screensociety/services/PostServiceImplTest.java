package org.alphatrack.screensociety.services;

import jakarta.persistence.EntityNotFoundException;
import org.alphatrack.screensociety.dto.request.CommentRequestDto;
import org.alphatrack.screensociety.dto.request.PostRequestDto;
import org.alphatrack.screensociety.dto.request.PostUpdateRequestDto;
import org.alphatrack.screensociety.models.Post;
import org.alphatrack.screensociety.models.Tag;
import org.alphatrack.screensociety.models.User;
import org.alphatrack.screensociety.models.enums.Role;
import org.alphatrack.screensociety.repositories.contracts.CommentRepository;
import org.alphatrack.screensociety.repositories.contracts.PostRepository;
import org.alphatrack.screensociety.repositories.contracts.TagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private PostServiceImpl postService;

    private User mockUser;

    @BeforeEach
    public void init() {
        mockUser = Mockito.mock(User.class);
    }

    @Test
    public void createPost_Should_Throw_When_UserIsBlocked() {
        PostRequestDto postRequestDto = Mockito.mock(PostRequestDto.class);
        Mockito.when(mockUser.getIsBlocked())
                .thenReturn(true);

        Assertions.assertThrows(IllegalStateException.class, () -> postService.createPost(postRequestDto, mockUser));
    }

    @Test
    public void createPost_Should_MapAndCallRepository() {
        User user = new User();
        user.setUsername("username");
        user.setIsBlocked(false);
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle("CurrentTestingTitle");
        postRequestDto.setContent("Current Testing Content for request Dto");

        postService.createPost(postRequestDto, user);

        ArgumentCaptor<Post> postArgumentCaptor = ArgumentCaptor.forClass(Post.class);

        Mockito.verify(postRepository,Mockito.times(1)).save(postArgumentCaptor.capture());

        Post capturedPost = postArgumentCaptor.getValue();

        Assertions.assertEquals(postRequestDto.getTitle(), capturedPost.getTitle());
        Assertions.assertEquals(postRequestDto.getContent(), capturedPost.getContent());

    }

    @Test
    public void updatePost_Should_Throw_When_PostNotFound() {
        PostUpdateRequestDto postUpdateRequestDto = Mockito.mock(PostUpdateRequestDto.class);
        Assertions.assertThrows(EntityNotFoundException.class,() -> postService.updatePost(1L, postUpdateRequestDto, mockUser ));
    }

    @Test
    public void updatePost_Should_Throw_When_UserIsNotOwnerOrAdmin() {
        PostUpdateRequestDto postUpdateRequestDto = Mockito.mock(PostUpdateRequestDto.class);
        mockUser.setRole(Role.USER);
        mockUser.setUsername("Ivan");
        Post post = new Post();
        User user = new User();
        user.setUsername("Peter");
        post.setAuthor(user);
        Mockito.when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        Assertions.assertThrows(AccessDeniedException.class, () -> postService.updatePost(1L, postUpdateRequestDto, mockUser));

    }

    @Test
    public void updatePost_Should_MapAndAddTagAndUpdateContent() {
        mockUser.setUsername("Peter");
        Set<String> tags = new HashSet<>();
        tags.add("horror");
        tags.add("sci-fi");
        PostUpdateRequestDto postUpdateRequestDto = new PostUpdateRequestDto();
        postUpdateRequestDto.setContent("Setting testing content for the dto");
        postUpdateRequestDto.setTags(tags);
        Post post = new Post();
        post.setAuthor(mockUser);
        Mockito.when(postRepository.findById(1L))
                        .thenReturn(Optional.of(post));

        Tag horrorTag = new Tag();
        horrorTag.setName("horror");

        Tag scifiTag = new Tag();
        scifiTag.setName("sci-fi");

        Mockito.when(tagRepository.findByName("horror")).thenReturn(Optional.of(horrorTag));
        Mockito.when(tagRepository.findByName("sci-fi")).thenReturn(Optional.of(scifiTag));

        postService.updatePost(1L, postUpdateRequestDto, mockUser);

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        Mockito.verify(postRepository, Mockito.times(1)).save(postCaptor.capture());

        Post capturedPost = postCaptor.getValue();

        Assertions.assertEquals(postUpdateRequestDto.getTags().size(), capturedPost.getTags().size());
        Assertions.assertEquals(postUpdateRequestDto.getContent(), capturedPost.getContent());

    }

    @Test
    public void addCommentOnPost_Should_Throw_When_UserIsBlocked() {
        User user = new User();
        user.setIsBlocked(true);
        CommentRequestDto commentRequestDto = new CommentRequestDto();

        Assertions.assertThrows(IllegalStateException.class, () -> postService.addCommentOnPost(commentRequestDto, 1L, user));
    }

    @Test
    public void addCommentOnPost_Should_Throw_When_PostIsNotFound() {
        CommentRequestDto commentRequestDto = new CommentRequestDto();

        Assertions.assertThrows(EntityNotFoundException.class, () -> postService.addCommentOnPost(commentRequestDto, 1L, mockUser));
    }

    @Test
    public void addCommentOnPost_Should_MapAndAddCommentAndCallRepository() {
        User user = new User();
        user.setUsername("Ivan");
        user.setIsBlocked(false);
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setContent("Testing the content feature for comment");
        Post post = new Post();
        Mockito.when(postRepository.findById(1L))
                        .thenReturn(Optional.of(post));

        postService.addCommentOnPost(commentRequestDto,1L, user);

        ArgumentCaptor<Post> postArgumentCaptor = ArgumentCaptor.forClass(Post.class);
        Mockito.verify(postRepository,Mockito.times(1)).save(postArgumentCaptor.capture());

        Post capturedPost = postArgumentCaptor.getValue();

        Assertions.assertEquals(capturedPost.getCommentList().get(0).getAuthor().getUsername(), user.getUsername());
    }

    @Test
    public void addLikesOnPost_Should_Throw_When_UserIsBlocked() {
        User user = new User();
        user.setIsBlocked(true);
        Assertions.assertThrows(IllegalStateException.class, () -> postService.addLikesOnPost(1L, user));
    }

    @Test
    public void addLikesOnPost_Should_Throw_When_PostIsNotFound() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> postService.addLikesOnPost(1L, mockUser));
    }

    @Test
    public void addLikesOnPost_Should_AddLike() {
        Post post = new Post();
        User user = new User();
        user.setUsername("Peter");
        user.setIsBlocked(false);
        Mockito.when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        postService.addLikesOnPost(1L, user);

        Assertions.assertEquals(1, post.getLikedByUsers().size());

    }

    @Test
    public void addLikesOnPost_Should_RemoveLike() {
        Post post = new Post();
        User user = new User();
        user.setUsername("Peter");
        user.setIsBlocked(false);
        post.addLike(user);
        Mockito.when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        postService.addLikesOnPost(1L, user);

        Assertions.assertEquals(0, post.getLikedByUsers().size());
    }

    @Test
    public void repost_Should_Throw_WhenUserIsBlocked() {
        User user = new User();
        user.setIsBlocked(true);

        Assertions.assertThrows(IllegalStateException.class, () -> postService.repost(1L, user));
    }

    @Test
    public void repost_Should_Throw_When_PostIsNotFound() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> postService.repost(1L, mockUser));
    }

}
