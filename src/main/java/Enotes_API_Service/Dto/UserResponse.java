package Enotes_API_Service.Dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobNo;

    private StatusDto status;

    private List<UserRequest.RoleDto> roles;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class RoleDto{
        private Integer id;
        private String name;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class StatusDto {
        private Integer id;
        private Boolean isActive;
    }
}
