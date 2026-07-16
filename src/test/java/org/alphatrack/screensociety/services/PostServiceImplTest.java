package org.alphatrack.screensociety.services;

import jakarta.persistence.EntityNotFoundException;
import org.alphatrack.screensociety.dto.request.CommentRequestDto;
import org.alphatrack.screensociety.dto.request.PostRequestDto;
import org.alphatrack.screensociety.dto.request.PostUpdateRequestDto;
import org.alphatrack.screensociety.dto.request.TagRequestDto;
import org.alphatrack.screensociety.dto.request.filters.PostFilterOptions;
import org.alphatrack.screensociety.models.Post;
import org.alphatrack.screensociety.models.Tag;
import org.alphatrack.screensociety.models.User;
import org.alphatrack.screensociety.models.enums.Role;
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

    @Test
    public void repost_Should_MapAndCallRepository_When_OriginalPost() {
        User user = new User();
        user.setUsername("Peter");
        user.setIsBlocked(false);
        Post originalPost = new Post();
        originalPost.setTitle("Original Title");
        originalPost.setContent("Original Content");
        originalPost.setTags(new HashSet<>());

        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(originalPost));

        postService.repost(1L, user);

        ArgumentCaptor<Post> postArgumentCaptor = ArgumentCaptor.forClass(Post.class);
        Mockito.verify(postRepository, Mockito.times(1)).save(postArgumentCaptor.capture());

        Post capturedPost = postArgumentCaptor.getValue();

        Assertions.assertEquals(originalPost, capturedPost.getOriginalPost());
        Assertions.assertEquals(user, capturedPost.getAuthor());
    }

    @Test
    public void addTags_Should_Throw_When_PostIsNotFound() {
        TagRequestDto tagRequestDto = new TagRequestDto();
        Assertions.assertThrows(EntityNotFoundException.class, () -> postService.addTags(1L, tagRequestDto, mockUser));
    }

    @Test
    public void addTags_Should_Throw_When_UserIsNotOwnerOrAdmin() {
        TagRequestDto tagRequestDto = new TagRequestDto();

        User author = Mockito.mock(User.class);
        Mockito.when(author.getId()).thenReturn(2L);
        Post post = new Post();
        post.setAuthor(author);

        User currentUser = Mockito.mock(User.class);
        Mockito.when(currentUser.getId()).thenReturn(1L);
        Mockito.when(currentUser.getRole()).thenReturn(Role.USER);

        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Assertions.assertThrows(AccessDeniedException.class, () -> postService.addTags(1L, tagRequestDto, currentUser));
    }

    @Test
    public void addTags_Should_AddTagAndSave_When_UserIsOwner() {
        TagRequestDto tagRequestDto = new TagRequestDto();
        tagRequestDto.setName("action");

        User currentUser = Mockito.mock(User.class);
        Mockito.when(currentUser.getId()).thenReturn(1L);
        Mockito.when(currentUser.getRole()).thenReturn(Role.USER);

        Post post = new Post();
        post.setAuthor(currentUser);

        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Tag actionTag = new Tag();
        actionTag.setName("action");
        Mockito.when(tagRepository.findByName("action")).thenReturn(Optional.of(actionTag));

        postService.addTags(1L, tagRequestDto, currentUser);

        ArgumentCaptor<Post> postArgumentCaptor = ArgumentCaptor.forClass(Post.class);
        Mockito.verify(postRepository, Mockito.times(1)).save(postArgumentCaptor.capture());

        Post capturedPost = postArgumentCaptor.getValue();
        Assertions.assertTrue(capturedPost.getTags().contains(actionTag));
    }

    @Test
    public void searchPosts_Should_CallRepository() {
        PostFilterOptions filterOptions = Mockito.mock(PostFilterOptions.class);
        postService.searchPosts(filterOptions);
        Mockito.verify(postRepository, Mockito.times(1)).findAll(filterOptions);
    }

    @Test
    public void showTop10MostCommented_Should_CallRepository() {
        postService.showTop10MostCommented();
        Mockito.verify(postRepository, Mockito.times(1)).findTop10MostCommented();
    }

    @Test
    public void show10MostRecentPosts_Should_CallRepository() {
        postService.show10MostRecentPosts();
        Mockito.verify(postRepository, Mockito.times(1)).find10MostRecentPosts();
    }

    @Test
    public void deletePost_Should_Throw_When_PostIsNotFound() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> postService.deletePost(1L, mockUser));
    }

    @Test
    public void deletePost_Should_Throw_When_UserIsNotOwnerOrAdmin() {
        User author = Mockito.mock(User.class);
        Mockito.when(author.getId()).thenReturn(2L);
        Post post = new Post();
        post.setAuthor(author);

        User currentUser = Mockito.mock(User.class);
        Mockito.when(currentUser.getId()).thenReturn(1L);
        Mockito.when(currentUser.getRole()).thenReturn(Role.USER);

        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Assertions.assertThrows(AccessDeniedException.class, () -> postService.deletePost(1L, currentUser));
    }

    @Test
    public void deletePost_Should_Delete_When_UserIsAdmin() {
        User author = Mockito.mock(User.class);
        Mockito.when(author.getId()).thenReturn(2L);
        Post post = new Post();
        post.setAuthor(author);

        User adminUser = Mockito.mock(User.class);
        Mockito.when(adminUser.getId()).thenReturn(1L);
        Mockito.when(adminUser.getRole()).thenReturn(Role.ADMIN);

        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        postService.deletePost(1L, adminUser);

        Mockito.verify(postRepository, Mockito.times(1)).delete(post);
    }

    @Test
    public void getByPostId_Should_Throw_When_NotFound() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> postService.getByPostId(1L));
    }

    @Test
    public void getByPostId_Should_ReturnPost() {
        Post post = new Post();
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Post result = postService.getByPostId(1L);

        Assertions.assertEquals(post, result);
    }

    @Test
    public void getPostForUpdate_Should_Throw_When_UserIsNotOwner() {
        User author = Mockito.mock(User.class);
        Post post = new Post();
        post.setAuthor(author);

        User currentUser = Mockito.mock(User.class);

        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Assertions.assertThrows(AccessDeniedException.class, () -> postService.getPostForUpdate(1L, currentUser));
    }

    @Test
    public void getPostForUpdate_Should_ReturnPost_When_UserIsOwner() {
        User author = Mockito.mock(User.class);
        Post post = new Post();
        post.setAuthor(author);

        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Post result = postService.getPostForUpdate(1L, author);

        Assertions.assertEquals(post, result);
    }

}
