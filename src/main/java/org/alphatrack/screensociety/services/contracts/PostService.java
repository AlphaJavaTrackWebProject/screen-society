package org.alphatrack.screensociety.services.contracts;

import org.alphatrack.screensociety.dto.request.CommentRequestDto;
import org.alphatrack.screensociety.dto.request.PostRequestDto;
import org.alphatrack.screensociety.dto.request.TagRequestDto;
import org.alphatrack.screensociety.dto.request.filters.PostFilterOptions;
import org.alphatrack.screensociety.models.Post;
import org.alphatrack.screensociety.models.User;


import java.util.List;

public interface PostService {
    Post createPost(PostRequestDto postRequestDto, User currentUser);
    Post addCommentOnPost(CommentRequestDto commentRequestDto, Long postId, User currentUser);
    Post addLikesOnPost(Long postId, User currentUser);
    Post repost(Long postId,User currentUser);
    Post addTags(Long postId, TagRequestDto tagRequestDto, User currentUser);

    List<Post> searchPosts(PostFilterOptions postFilterOptions);
    List<Post> showTop10MostCommented();
    List<Post> show10MostRecentPosts();

    void deletePost(Long postId,User currentUser);

    Post getByPostId(Long id);



}
