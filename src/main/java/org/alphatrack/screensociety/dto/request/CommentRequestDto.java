package org.alphatrack.screensociety.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDto {

    @NotEmpty(message = "The comment cannot be empty")
    @Size(min = 2, max = 255, message ="The comment length should be between 2 and 255 characters")
    private String content;


}
