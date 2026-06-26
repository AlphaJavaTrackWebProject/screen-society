package org.alphatrack.screensociety.models;

import jakarta.persistence.*;
import lombok.*;
import org.alphatrack.screensociety.models.enums.Role;


import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "username", nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "e_mail", nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "author")
    @Builder.Default
    private Set<Post> posts = new HashSet<>();

}