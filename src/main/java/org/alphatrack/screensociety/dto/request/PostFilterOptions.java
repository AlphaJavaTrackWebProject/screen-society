package org.alphatrack.screensociety.dto.request;

import org.alphatrack.screensociety.models.Comment;
import org.alphatrack.screensociety.models.Tag;
import org.alphatrack.screensociety.models.User;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Optional;

public class PostFilterOptions {
    private Optional<String> authorUsername;
    private Optional<String> title;
    private Optional<LocalDate> createdBefore;
    private Optional<LocalDate> createdAfter;
    private Optional<String> tagName;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

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

    public Optional<String> getAuthorUsername() {
        return authorUsername;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<LocalDate> getCreatedBefore() {
        return createdBefore;
    }

    public Optional<LocalDate> getCreatedAfter() {
        return createdAfter;
    }

    public Optional<String> getTagName() {
        return tagName;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
