package Enotes_API_Service.Dto;

import Enotes_API_Service.entity.Role;
import lombok.*;


import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {

    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobNo;

    private String password;

    private List<RoleDto> roles;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class RoleDto{
        private Integer id;
        private String name;
    }
}
