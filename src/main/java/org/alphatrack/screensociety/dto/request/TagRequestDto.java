package org.alphatrack.screensociety.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagRequestDto {

    @NotEmpty(message = "Please provide a Tag name")
    @Size(min = 2,max = 10,message = "Tag name should be between 2 and 10 characters")
    private String name;

}
