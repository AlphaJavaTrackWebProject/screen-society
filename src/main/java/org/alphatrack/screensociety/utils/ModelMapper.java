package org.alphatrack.screensociety.utils;

import org.alphatrack.screensociety.dto.request.CommentRequestDto;
import org.alphatrack.screensociety.dto.request.PostUpdateRequestDto;
import org.alphatrack.screensociety.dto.request.UserUpdateDto;
import org.alphatrack.screensociety.dto.response.*;
import org.alphatrack.screensociety.models.Comment;
import org.alphatrack.screensociety.models.Post;
import org.alphatrack.screensociety.models.Tag;
import org.alphatrack.screensociety.models.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ModelMapper {

    public List<PostResponseDto> postsToPostsDto (List<Post> list){
        return list.stream()
                .map(post -> postToPostResponseDto(post))
                .toList();
    }

    public PostResponseDto postToPostResponseDto(Post post){

        return PostResponseDto.builder()
                .id(post.getId())
                .authorId(post.getAuthor().getId())
                .author(post.getAuthor().getUsername())
                .title(post.getTitle())
                .content(post.getContent())
                .commentList(commentsToCommentsDto(post.getCommentList()))
                .tags(tagsToTagsDto(post.getTags()))
                .build();
    }

    public List<CommentResponseDto> commentsToCommentsDto(List<Comment> comments){
        return comments.stream()
                .map(comment -> commentToCommentDto(comment))
                .toList();
    }

    public CommentResponseDto commentToCommentDto(Comment comment){

        return CommentResponseDto.builder()
                .content(comment.getContent())
                .author(comment.getAuthor().getUsername())
                .id(comment.getId())
                .authorId(comment.getAuthor().getId())
                .build();
    }

    public CommentRequestDto commentToCommentRequestDto(Comment comment){

        return CommentRequestDto.builder()
                .authorUsername(comment.getAuthor().getUsername())
                .content(comment.getContent())
                .build();
    }

    public List<TagResponseDto> tagsToTagsDto(Set<Tag> tagSet){

       return tagSet.stream()
                .map(tag -> tagToTagDto(tag))
                .toList();

    }

    public TagResponseDto tagToTagDto(Tag tag){
        return TagResponseDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

    public PostUpdateRequestDto postToPostUpdateDto (Post post){
        return PostUpdateRequestDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .tags(tagsToString(post.getTags()))
                .build();
    }

    public Set<String> tagsToString(Set<Tag> tagSet){
        return tagSet.stream()
                .map(tag -> tag.getName())
                .collect(Collectors.toSet());
    }

    public UserResponseDto userToUserDto(User user){
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public List<UserResponseDto> usersListToResponseDtoList (List<User> users){
        return users.stream()
                .map(user -> userToUserDto(user))
                .toList();
    }

    public UserUpdateDto userToUserUpdateDto(User user){
        return UserUpdateDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    public AdminUserResponseDto userToAdminUserResponseDto(User user){
        return AdminUserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .isBlocked(user.getIsBlocked())
                .build();
    }

    public List<AdminUserResponseDto> userListToAdminResponseDtoList(List<User> users){
        return users.stream()
                .map(user -> userToAdminUserResponseDto(user))
                .toList();
    }

    public CommentResponseDto commentToResponseDto(Comment comment){
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .author(comment.getAuthor().getUsername())
                .authorId(comment.getAuthor().getId())
                .build();
    }


}
