package org.alphatrack.screensociety.models;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false,length = 8192)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id",
    nullable = false,
    foreignKey = @ForeignKey(name = "fk_comment_user_id"))
    private User author;

    private Post post
}
