package org.alphatrack.screensociety.repositories.contracts;

import org.alphatrack.screensociety.models.Post;
import org.alphatrack.screensociety.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

}
