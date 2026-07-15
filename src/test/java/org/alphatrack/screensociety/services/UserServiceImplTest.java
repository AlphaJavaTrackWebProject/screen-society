package org.alphatrack.screensociety.services;


import jakarta.persistence.EntityNotFoundException;
import org.alphatrack.screensociety.dto.request.UserRegistrationDto;
import org.alphatrack.screensociety.dto.request.UserUpdateDto;
import org.alphatrack.screensociety.dto.request.filters.PostFilterOptions;
import org.alphatrack.screensociety.dto.request.filters.UserFilterOptions;
import org.alphatrack.screensociety.models.Post;
import org.alphatrack.screensociety.models.User;
import org.alphatrack.screensociety.models.enums.Role;
import org.alphatrack.screensociety.repositories.contracts.PostRepository;
import org.alphatrack.screensociety.repositories.contracts.UserRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private  UserServiceImpl userService;

    private UserRegistrationDto mockUserDto;
    private User mockUser;

    @BeforeEach
    public void init() {
        mockUserDto = Mockito.mock(UserRegistrationDto.class);
        mockUser = Mockito.mock(User.class);
    }
    @Test
    public void registerUser_Should_Throw_WhenUsernameIsTaken() {

        Mockito.when(userRepository.findUserByUsername(mockUserDto.getUsername()))
                .thenReturn(Optional.of(new User()));

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerUser(mockUserDto));
    }

    @Test
    public void registerUser_Should_Throw_WhenEmailIsTaken() {
        Mockito.when(userRepository.findUserByUsername(mockUserDto.getUsername()))
                .thenReturn(Optional.empty());
        Mockito.when(userRepository.findUserByEmail(mockUserDto.getEmail()))
                .thenReturn(Optional.of(new User()));

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerUser(mockUserDto));
    }

    @Test
    public void registerUser_Should_MapUserAndCallRepository() {
        Mockito.when(userRepository.findUserByUsername(mockUserDto.getUsername()))
                .thenReturn(Optional.empty());
        Mockito.when(userRepository.findUserByEmail(mockUserDto.getEmail()))
                .thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(mockUserDto.getPassword()))
                .thenReturn("SecretEncodedPassword");

        userService.registerUser(mockUserDto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository,Mockito.times(1)).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();

        Assertions.assertEquals(mockUserDto.getUsername(),capturedUser.getUsername());
        Assertions.assertEquals(mockUserDto.getEmail(),capturedUser.getEmail());
        Assertions.assertEquals("SecretEncodedPassword",capturedUser.getPassword());
        Assertions.assertFalse(capturedUser.getIsBlocked());

    }

    @Test
    public void updateProfile_Should_Throw_WhenUserIsNotOwner() {
        Mockito.when(mockUser.getId())
                        .thenReturn(2L);

        Assertions.assertThrows(AccessDeniedException.class,() -> userService.updateProfile(Mockito.mock(UserUpdateDto.class),mockUser,1L));
    }

    @Test
    public void updateProfile_Should_UpdateAndSaveUser_When_UserIsOwner() {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setFirstName("NewFirst");
        userUpdateDto.setLastName("NewLast");

        Mockito.when(mockUser.getId())
                .thenReturn(1L);

        User dbUser = new User();
        dbUser.setFirstName("OldFirst");
        dbUser.setLastName("OldLast");

        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(dbUser));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        userService.updateProfile(userUpdateDto, mockUser, 1L);

        Mockito.verify(userRepository,Mockito.times(1)).save(userCaptor.capture());

        User resultUser = userCaptor.getValue();

        Assertions.assertEquals(resultUser.getFirstName(), userUpdateDto.getFirstName());
        Assertions.assertEquals(resultUser.getLastName(), userUpdateDto.getLastName());

    }

    @Test
    public void searchUsers_Should_ReturnListOfUsers() {
        UserFilterOptions filterOptions = Mockito.mock(UserFilterOptions.class);
        Mockito.when(mockUser.getLastName())
                .thenReturn("ivanov");
        Mockito.when(userRepository.findAll(filterOptions))
                .thenReturn(List.of(mockUser));


        List<User> testUsers = userService.searchUsers(filterOptions);

        Assertions.assertEquals("ivanov", testUsers.get(0).getLastName());
    }


    @Test
    public void blockUser_Should_Throw_WhenUserIsAdmin() {
        Mockito.when(userRepository.findById(1L))
                        .thenReturn(Optional.of(mockUser));
        Mockito.when(mockUser.getRole())
                .thenReturn(Role.ADMIN);

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.blockUser(1L));
    }

    @Test
    public void blockUser_Should_Throw_WhenUserIsNotFound() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.blockUser(1L));
    }

    @Test
    public void blockUser_Should_BlockUser() {
        User user = new User();
        user.setRole(Role.USER);
        Mockito.when(userRepository.findById(1L))
                        .thenReturn(Optional.of(user));

        userService.blockUser(1L);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository, Mockito.times(1)).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        Assertions.assertTrue(capturedUser.getIsBlocked());
    }

    @Test
    public void unBlockUser_Should_Throw_When_UserIsNotFound() {
        Assertions.assertThrows(EntityNotFoundException.class,() -> userService.unBlockUser(1L));
    }

    @Test
    public void unBlockUser_Should_UnBlockUser() {
        User user = new User();
        user.setRole(Role.USER);
        user.setIsBlocked(true);
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        userService.unBlockUser(1L);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository, Mockito.times(1)).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        Assertions.assertFalse(capturedUser.getIsBlocked());
    }

    @Test
    public void promoteToAdmin_Should_Throw_WhenUserIsNotFound() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.promoteToAdmin(1L));
    }

    @Test
    public void promoteToAdmin_Should_PromoteUser() {
        User user = new User();
        user.setRole(Role.USER);
        Mockito.when(userRepository.findById(1L))
                        .thenReturn(Optional.of(user));

        userService.promoteToAdmin(1L);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository,Mockito.times(1)).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();

        Assertions.assertEquals(Role.ADMIN, capturedUser.getRole());
    }



    @Test
    public void removeUser_Should_Throw_When_UserIsNotOwnerAndAdmin() {
        Mockito.when(mockUser.getRole())
                .thenReturn(Role.USER);
        Mockito.when(mockUser.getId())
                .thenReturn(1L);

        Assertions.assertThrows(AccessDeniedException.class, () -> userService.removeUser(2L, mockUser));

    }

    @Test
    public void removeUser_Should_deleteUser_When_UserIsAdmin() {
        Mockito.when(mockUser.getRole())
                .thenReturn(Role.ADMIN);
        User dbUser = new User();
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(dbUser));

        userService.removeUser(1L, mockUser);

        Mockito.verify(userRepository, Mockito.times(1)).delete(dbUser);
    }

    @Test
    public void removeUser_Should_deleteUser_When_UserIsOwner() {
        Mockito.when(mockUser.getRole())
                .thenReturn(Role.USER);
        Mockito.when(mockUser.getId())
                        .thenReturn(1L);
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));

        userService.removeUser(1L, mockUser);

        Mockito.verify(userRepository, Mockito.times(1)).delete(mockUser);
    }

    @Test
    public void getUserPosts_Should_ReturnAllPosts() {
        PostFilterOptions postFilterOptions = Mockito.mock(PostFilterOptions.class);
        Post post = new Post();
        post.setAuthor(mockUser);
        post.setContent("Just some basic content for the test");
        post.setTitle("SettingTestTitle");

        Mockito.when(postRepository.findAll(1L, postFilterOptions))
                .thenReturn(List.of(post));

        List<Post> testPosts = userService.getUserPosts(1L, postFilterOptions);
        String comparedString = testPosts.get(0).getTitle();
        String testPostTitle = "SettingTestTitle";

        Assertions.assertEquals(testPostTitle, comparedString);
    }

    @Test
    public void getUserById_Should_Throw_When_UserIsNotFound() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    public void getUserById_Should_returnUser() {
        Mockito.when(mockUser.getId())
                .thenReturn(1L);

        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));

        User resultUser = userService.getUserById(1L);

        Assertions.assertEquals(mockUser.getId(), resultUser.getId());

    }

    @Test
    public void getUserByUsername_Should_Throw_WhenUserIsNotFound() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getUserByUsername("testUsername"));
    }

    @Test
    public void getUserByUsername_Should_Return_User() {
        Mockito.when(mockUser.getId())
                        .thenReturn(1L);
        Mockito.when(userRepository.findUserByUsername("testUsername"))
                .thenReturn(Optional.of(mockUser));

        User resultUser = userService.getUserByUsername("testUsername");

        Assertions.assertEquals(1L, resultUser.getId());

    }


}
