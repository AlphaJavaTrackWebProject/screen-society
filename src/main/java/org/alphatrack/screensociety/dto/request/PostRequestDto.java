package org.alphatrack.screensociety.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.alphatrack.screensociety.models.Comment;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequestDto {

    @NotEmpty(message = "The post must have a title")
    @Size(min = 16,max = 64,message = "Title length should be between 16 and 64 characters")
    private String title;

    @NotEmpty(message = "The post must contain content / description")
    @Size(min = 32,max = 8192,message = "description size should be between 32 and 8192 characters")
    private String content;

    @NotBlank(message = "User name cannot be empty")
    @Size(message = "The length of  User name should be between 4 and 32 characters")
    private String authorUsername;



}
