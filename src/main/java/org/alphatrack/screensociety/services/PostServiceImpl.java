package org.alphatrack.screensociety.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.alphatrack.screensociety.dto.request.CommentRequestDto;
import org.alphatrack.screensociety.dto.request.PostRequestDto;
import org.alphatrack.screensociety.dto.request.PostUpdateRequestDto;
import org.alphatrack.screensociety.dto.request.TagRequestDto;
import org.alphatrack.screensociety.dto.request.filters.PostFilterOptions;
import org.alphatrack.screensociety.models.Comment;
import org.alphatrack.screensociety.models.Post;
import org.alphatrack.screensociety.models.Tag;
import org.alphatrack.screensociety.models.User;
import org.alphatrack.screensociety.models.enums.Role;
import org.alphatrack.screensociety.repositories.contracts.PostRepository;
import org.alphatrack.screensociety.repositories.contracts.TagRepository;
import org.alphatrack.screensociety.repositories.contracts.UserRepository;
import org.alphatrack.screensociety.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;


    @Autowired
    public PostServiceImpl(PostRepository postRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;

    }


    @Transactional
    @Override
    public Post createPost(PostRequestDto postRequestDto, User currentUser) {

        if (currentUser.isBlocked()) {
            throw new IllegalStateException("You are blocked and unable to create post");
        }

        Post newPost = Post.builder()
                .author(currentUser)
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        return postRepository.save(newPost);
    }

    @Transactional
    @Override
    public Post updatePost(Long postId, PostUpdateRequestDto postUpdateRequestDto, User currentUser) {

        Post post = getByPostId(postId);

        boolean isOwner = post.getAuthor().equals(currentUser);

        if (!isOwner){
            throw new AccessDeniedException("Only author / owner can edit it's post");
        }

        Set<Tag> tags = new HashSet<>();

       for (String tagName : postUpdateRequestDto.getTags()){

           String formattedTagName = tagName.toLowerCase().trim();

           Tag tag = tagRepository.findByName(formattedTagName).orElseGet(() -> {

               Tag newTag = Tag.builder()
                       .name(formattedTagName)
                       .build();

               return tagRepository.save(newTag);
           });

           tags.add(tag);
       }

        post.setContent(postUpdateRequestDto.getContent());
        post.setTags(tags);

        postRepository.save(post);
        return post;
    }

    @Transactional
    @Override
    public Post addCommentOnPost(CommentRequestDto commentRequestDto, Long postId, User currentUser) {

        if (currentUser.isBlocked()) {
            throw new IllegalStateException("You are blocked and unable to make comments");
        }

        Post post = getByPostId(postId);

        Comment comment = Comment.builder()
                .author(currentUser)
                .content(commentRequestDto.getContent())
                .build();

        post.addComment(comment);

        return postRepository.save(post);
    }

    @Transactional
    @Override
    public Post addLikesOnPost(Long postId, User currentUser) {

        if (currentUser.isBlocked()) {
            throw new IllegalStateException("You are blocked and unable to make comments");
        }

        Post post = getByPostId(postId);

        if (post.getLikedByUsers().contains(currentUser)) {
            post.removeLike(currentUser);
        } else {
            post.addLike(currentUser);
        }
        return postRepository.save(post);
    }

    @Transactional
    @Override
    public Post repost(Long postId, User currentUser) {

        if (currentUser.isBlocked()) {
            throw new IllegalStateException("You are blocked and unable to repost");
        }

        Post postToBeReposted = getByPostId(postId);

        Post rootPost = postToBeReposted.isRepost() ? postToBeReposted.getOriginalPost() : postToBeReposted;

        Post newPost = Post.builder()
                .content(postToBeReposted.getContent())
                .author(currentUser)
                .title(postToBeReposted.getTitle())
                .createdAt(LocalDateTime.now())
                .originalPost(rootPost)
                .tags(new HashSet<>(rootPost.getTags()))
                .build();

        return postRepository.save(newPost);
    }

    @Transactional
    @Override
    public Post addTags(Long postId, TagRequestDto tagRequestDto, User currentUser) {
        Post post = getByPostId(postId);

        boolean isOwner = post.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole().equals(Role.ADMIN);

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Only the author or an admin can tag this post.");
        }

        String formattedTagName = tagRequestDto.getName().toLowerCase().trim();

        Tag tag = tagRepository.findByName(formattedTagName).orElseGet(() -> {
            Tag newTag = Tag.builder()
                    .name(formattedTagName)
                    .build();
            return tagRepository.save(newTag);
        });

        post.addTag(tag);

        return postRepository.save(post);
    }


    @Override
    public List<Post> searchPosts(PostFilterOptions postFilterOptions) {
        return postRepository.findAll(postFilterOptions);
    }


    @Override
    public List<Post> showTop10MostCommented() {
        return postRepository.findTop10MostCommented();
    }


    @Override
    public List<Post> show10MostRecentPosts() {
        return postRepository.find10MostRecentPosts();
    }

    @Transactional
    @Override
    public void deletePost(Long postId, User currentUser) {
        Post post = getByPostId(postId);
        boolean isOwner = post.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole().equals(Role.ADMIN);

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Only the author or an admin can delete a post");
        }

        postRepository.delete(post);
    }

    @Override
    public Post getByPostId(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Post with id %d does not exist", id)));
    }

    @Override
    public Post getPostForUpdate(Long postId, User currentUser) {
        Post post = getByPostId(postId);

        boolean isOwner = post.getAuthor().equals(currentUser);

        if (!isOwner){
            throw new AccessDeniedException("Only author can edit its post/s");
        }

        return post;
    }

}
