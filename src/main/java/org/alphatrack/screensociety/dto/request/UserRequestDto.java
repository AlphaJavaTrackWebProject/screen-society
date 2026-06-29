package org.alphatrack.screensociety.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {

    @NotNull(message = "First name cannot be empty")
    @Size(message = "The length of First name should be between 2 and 20 characters"
            , min = 2, max = 20)
    private String firstName;

    @NotNull(message = "Last name cannot be empty")
    @Size(message = "The length of Last name should be between 2 and 20 characters"
            , min = 2, max = 20)
    private String lastName;

    @NotNull(message = "User name cannot be empty")
    @Size(message = "The length of  User name should be between 2 and 20 characters"
            , min = 2, max = 20)

    private String userName;

    @NotNull(message = "Email cannot be empty")
    @Email(message = "Invalid email")
    private String email;



}
