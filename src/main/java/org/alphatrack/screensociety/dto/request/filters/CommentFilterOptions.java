package org.alphatrack.screensociety.dto.request.filters;

import lombok.Getter;

import java.time.OffsetTime;
import java.util.Optional;
@Getter
public class CommentFilterOptions {

    private final Optional<String> content;
    private final Optional<String> authorUsername;
    private final Optional<Integer> minLength;
    private final Optional<Integer> maxLength;
    private final Optional<String> sortBy;
    private final Optional<String> sortOrder;

    public CommentFilterOptions(String content,
                                String authorUsername,
                                Integer minLength,
                                Integer maxLength,
                                String sortBy,
                                String sortOrder) {
        this.content = Optional.ofNullable(content);
        this.authorUsername = Optional.ofNullable(authorUsername);
        this.minLength = Optional.ofNullable(minLength);
        this.maxLength = Optional.ofNullable(maxLength);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }


}
