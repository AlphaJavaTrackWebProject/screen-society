package org.alphatrack.screensociety.services.contracts;

import org.alphatrack.screensociety.dto.request.UserRegistrationDto;
import org.alphatrack.screensociety.dto.request.UserUpdateDto;
import org.alphatrack.screensociety.dto.request.filters.PostFilterOptions;
import org.alphatrack.screensociety.dto.request.filters.UserFilterOptions;
import org.alphatrack.screensociety.models.Post;
import org.alphatrack.screensociety.models.User;

import java.util.List;

public interface UserService {
    User registerUser(UserRegistrationDto userRegistrationDto);

    User updateProfile(UserUpdateDto userUpdateDto, User currentUser,Long userId);

    List<Post> getUserPosts(Long userId, PostFilterOptions filterOptions);

    List<User> searchUsers(UserFilterOptions options);

    void blockUser(Long userId);

    void unBlockUser(Long userId);

    void promoteToAdmin(Long userId);

    User getUserById(Long id);

    void removeUser(Long id, User currentUser);

    User getUserByUsername(String username);
}
