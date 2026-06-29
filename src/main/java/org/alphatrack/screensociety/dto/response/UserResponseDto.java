package org.alphatrack.screensociety.dto.response;


import lombok.*;
import org.alphatrack.screensociety.models.enums.Role;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Role role;

}
