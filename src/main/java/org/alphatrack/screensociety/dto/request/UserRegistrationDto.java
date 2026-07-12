package org.alphatrack.screensociety.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter

public class  UserRegistrationDto {

    @NotBlank(message = "First name cannot be empty")
    @Size(message = "The length of First name should be between 4 and 32 characters"
            , min = 4, max = 32)
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Size(message = "The length of Last name should be between 4 and 32 characters"
            , min = 4, max = 32)
    private String lastName;

    @NotBlank(message = "User name cannot be empty")
    @Size(message = "The length of  User name should be between 4 and 32 characters"
            , min = 4, max = 32)
    private String username;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;



}
