package org.alphatrack.screensociety.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false,length = 8192)
    private String content;

    @Column(name = "time_created")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id",
    nullable = false,
    foreignKey = @ForeignKey(name = "fk_comment_user_id"))
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id",
    nullable = false,
    foreignKey = @ForeignKey(name = "fk_comment_post_id"))
    private Post post;
}
