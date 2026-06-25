package org.alphatrack.screensociety.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Post {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id",nullable = false,
    foreignKey = @ForeignKey(name = "fk_post_user_id"))
    private User author;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @OneToMany(mappedBy = "post",
    cascade = CascadeType.ALL,
    orphanRemoval = true)
    Builder.Default
    private List<Comment> commentList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_post_id")
    private Post originalPost;

    @ManyToMany
    @JoinTable(name = "post_likes",
    joinColumns = @JoinColumn(name = "post_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likedByUsers = new HashSet<>();

    public boolean isRepost() {
        return this.originalPost != null;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }
}
