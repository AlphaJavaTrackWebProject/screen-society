package org.alphatrack.screensociety.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequestDto {

    @NotEmpty(message = "The post must have a title")
    @Size(min = 5,max = 30,message = "Title length should be between 5 and 30 characters")
    private String title;

    @NotEmpty(message = "The post must contain content / description")
    @Size(min = 10,max = 255,message = "description size should be between 10 and 255 characters")
    private String content;

}
