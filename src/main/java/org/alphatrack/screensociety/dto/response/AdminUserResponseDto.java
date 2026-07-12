package org.alphatrack.screensociety.dto.response;


import lombok.*;
import org.alphatrack.screensociety.models.enums.Role;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminUserResponseDto {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private Role role;

    private boolean isBlocked;

}
