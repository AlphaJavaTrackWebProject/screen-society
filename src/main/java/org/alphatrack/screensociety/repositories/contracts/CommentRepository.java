package org.alphatrack.screensociety.repositories.contracts;

import org.alphatrack.screensociety.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>,CommentRepositoryCustom {

}
