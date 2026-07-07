package org.alphatrack.screensociety.repositories.contracts;

import org.alphatrack.screensociety.dto.request.filters.CommentFilterOptions;
import org.alphatrack.screensociety.models.Comment;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findAll(CommentFilterOptions commentFilterOptions);
}
