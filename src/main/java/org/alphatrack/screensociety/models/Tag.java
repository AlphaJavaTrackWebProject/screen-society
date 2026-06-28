package org.alphatrack.screensociety.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tags",
indexes = {
        @Index(name = "idx_tag_name",columnList = "name")
})
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

}
