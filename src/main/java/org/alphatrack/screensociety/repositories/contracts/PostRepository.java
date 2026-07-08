package org.alphatrack.screensociety.repositories.contracts;

import org.alphatrack.screensociety.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Post findPostsByAuthor_Username(String authorUsername);
}
