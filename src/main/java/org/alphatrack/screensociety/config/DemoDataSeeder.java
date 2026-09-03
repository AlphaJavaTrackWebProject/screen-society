package org.alphatrack.screensociety.config;

import lombok.RequiredArgsConstructor;
import org.alphatrack.screensociety.models.Comment;
import org.alphatrack.screensociety.models.Post;
import org.alphatrack.screensociety.models.Tag;
import org.alphatrack.screensociety.models.User;
import org.alphatrack.screensociety.models.enums.Role;
import org.alphatrack.screensociety.repositories.contracts.PostRepository;
import org.alphatrack.screensociety.repositories.contracts.TagRepository;
import org.alphatrack.screensociety.repositories.contracts.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;


@Component
@Profile("demo")
@RequiredArgsConstructor
public class DemoDataSeeder implements CommandLineRunner {

    private static final String DEMO_PASSWORD = "Password123!";

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        String encodedPassword = passwordEncoder.encode(DEMO_PASSWORD);

        User admin = userRepository.save(User.builder()
                .username("admin")
                .email("admin@screensociety.io")
                .firstName("Alex")
                .lastName("Admin")
                .password(encodedPassword)
                .role(Role.ADMIN)
                .isBlocked(false)
                .isEnabled(true)
                .build());

        User moderator = userRepository.save(User.builder()
                .username("moderator")
                .email("moderator@screensociety.io")
                .firstName("Mira")
                .lastName("Moderator")
                .password(encodedPassword)
                .role(Role.MODERATOR)
                .isBlocked(false)
                .isEnabled(true)
                .build());

        User demoUser = userRepository.save(User.builder()
                .username("demo")
                .email("demo@screensociety.io")
                .firstName("Demo")
                .lastName("User")
                .password(encodedPassword)
                .role(Role.USER)
                .isBlocked(false)
                .isEnabled(true)
                .build());

        User jane = userRepository.save(User.builder()
                .username("jane")
                .email("jane@screensociety.io")
                .firstName("Jane")
                .lastName("Doe")
                .password(encodedPassword)
                .role(Role.USER)
                .isBlocked(false)
                .isEnabled(true)
                .build());

        Tag scifi = tagRepository.save(Tag.builder().name("sci-fi").build());
        Tag drama = tagRepository.save(Tag.builder().name("drama").build());
        Tag comedy = tagRepository.save(Tag.builder().name("comedy").build());
        Tag documentary = tagRepository.save(Tag.builder().name("documentary").build());

        Post post1 = postRepository.save(Post.builder()
                .author(demoUser)
                .title("The Last of Us Season 2 is finally here")
                .content("Just finished the premiere — the pacing feels tighter than season one. Curious what everyone else thinks about the changes from the games.")
                .createdAt(LocalDateTime.now().minusDays(2))
                .tags(Set.of(scifi, drama))
                .build());

        Post post2 = postRepository.save(Post.builder()
                .author(jane)
                .title("Underrated comedy series you probably missed")
                .content("Been rewatching some late-2010s sitcoms that never got the audience they deserved. Starting a thread — drop your picks below.")
                .createdAt(LocalDateTime.now().minusDays(1))
                .tags(Set.of(comedy))
                .build());

        Post post3 = postRepository.save(Post.builder()
                .author(admin)
                .title("Best documentary releases this year so far")
                .content("Compiling a running list of the strongest documentary releases this year. Suggestions welcome.")
                .createdAt(LocalDateTime.now().minusHours(6))
                .tags(Set.of(documentary))
                .build());

        postRepository.save(Post.builder()
                .author(demoUser)
                .title(post2.getTitle())
                .content(post2.getContent())
                .createdAt(LocalDateTime.now().minusHours(3))
                .originalPost(post2)
                .tags(Set.of(comedy))
                .build());

        post1.addComment(Comment.builder()
                .author(jane)
                .content("Agreed, the pacing change works a lot better on screen than I expected.")
                .build());

        post3.addComment(Comment.builder()
                .author(demoUser)
                .content("Solid writeup, subscribing to this thread.")
                .build());

        post1.addLike(jane);
        post1.addLike(admin);
        post2.addLike(demoUser);

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
    }
}
