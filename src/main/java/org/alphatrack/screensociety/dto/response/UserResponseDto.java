package org.alphatrack.screensociety.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.alphatrack.screensociety.models.enums.Role;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private Long id;

    private String firstName;

    private String lastName;

    /*TODO discuss*/
    private String userName;

    private String email;

    private Role role;

}
