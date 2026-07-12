package org.alphatrack.screensociety.repositories.contracts;

import org.alphatrack.screensociety.dto.request.filters.PostFilterOptions;
import org.alphatrack.screensociety.models.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findAll(PostFilterOptions postFilterOptions);

    List<Post> findAll(Long userId, PostFilterOptions postFilterOptions);

    List<Post> findTop10MostCommented();

    List<Post> find10MostRecentPosts();


}
