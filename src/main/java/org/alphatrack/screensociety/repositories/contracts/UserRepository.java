package org.alphatrack.screensociety.repositories.contracts;

import org.alphatrack.screensociety.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {
    User findByEmail(String email);

    User findByUsername(String username);

}
