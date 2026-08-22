package org.alphatrack.screensociety.dto.request.filters;

import lombok.Builder;

import java.util.Optional;

public class UserFilterOptions {
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String sortBy;
    private final String sortOrder;
    private final Boolean includeDisabled;

    @Builder(toBuilder = true)
    public UserFilterOptions(String username,
                             String firstName,
                             String lastName,
                             String email,
                             String sortBy,
                             String sortOrder,
                             Boolean includeDisabled) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
        this.includeDisabled = includeDisabled;
    }

    public Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }

    public Optional<String> getFirstName() {
        return Optional.ofNullable(firstName);
    }

    public Optional<String> getLastName() {
        return Optional.ofNullable(lastName);
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public Optional<String> getSortBy() {
        return Optional.ofNullable(sortBy);
    }

    public Optional<String> getSortOrder() {
        return Optional.ofNullable(sortOrder);
    }

    public boolean isIncludeDisabled() {
        return Optional.ofNullable(includeDisabled).orElse(false);
    }
}