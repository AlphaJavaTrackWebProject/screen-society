package org.alphatrack.screensociety.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostUpdateRequestDto {

    @NotEmpty(message = "The post must have a title")
    @Size(min = 5, max = 30, message = "Title length should be between 5 and 30 characters")
    private String title;

    @NotEmpty(message = "The post must contain content / description")
    @Size(min = 10, max = 255, message = "description size should be between 10 and 255 characters")
    private String content;

    private Set<
            @NotEmpty(message = "Please provide a Tag name")
            @Size(min = 2, max = 10, message = "Tag name should be between 2 and 10 characters") String> tags;

}
