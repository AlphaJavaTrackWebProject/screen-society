package org.alphatrack.screensociety.dto.response;

import lombok.*;


import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDto {

    private Long id;

    private Long authorId;

    private String author;

    private String title;

    private String content;

    private List<TagResponseDto> tags;

    private List<CommentResponseDto> commentList;


}
