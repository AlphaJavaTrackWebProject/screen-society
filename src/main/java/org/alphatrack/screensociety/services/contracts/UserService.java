package org.alphatrack.screensociety.services.contracts;

import org.alphatrack.screensociety.dto.request.UserRegistrationDto;
import org.alphatrack.screensociety.dto.request.UserUpdateDto;
import org.alphatrack.screensociety.dto.request.filters.UserFilterOptions;
import org.alphatrack.screensociety.models.User;

import java.util.List;

public interface UserService {
    User registerUser(UserRegistrationDto userRegistrationDto);

    User updateProfile(UserUpdateDto userUpdateDto, User currentUser);

    List<User> searchUsers(UserFilterOptions options);

    void blockUser(Integer userId);
    void unBlockUser(Integer userId);
    void promoteToAdmin(Integer userId);

    User getUserById(Integer id);
    User getUserByUsername(String username);
}
