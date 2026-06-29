package org.alphatrack.screensociety.dto.response;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDto {

    private Long id;

    private String content;

    private String author;

    private Long authorId;
}
