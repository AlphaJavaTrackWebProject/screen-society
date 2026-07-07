package org.alphatrack.screensociety.repositories.contracts;

import org.alphatrack.screensociety.dto.request.UserFilterOptions;
import org.alphatrack.screensociety.models.User;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> findAll(UserFilterOptions userFilterOptions);


}
