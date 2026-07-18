package org.alphatrack.screensociety.dto.request.filters;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Optional;
@Getter
//@AllArgsConstructor
public class PostFilterOptions {
    private final Optional<String> authorUsername;
    private final Optional<String> title;
    private final Optional<LocalDate> createdBefore;
    private final Optional<LocalDate> createdAfter;
    private final Optional<String> tagName;
    private final Optional<String> sortBy;
    private final Optional<String> sortOrder;

    @Builder
    public PostFilterOptions(String authorUsername,
                             String title,
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdBefore,
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAfter,
                             String tagName,
                             String sortBy,
                             String sortOrder) {
        this.authorUsername = Optional.ofNullable(authorUsername);
        this.title = Optional.ofNullable(title);
        this.createdBefore = Optional.ofNullable(createdBefore);
        this.createdAfter = Optional.ofNullable(createdAfter);
        this.tagName = Optional.ofNullable(tagName);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

}
