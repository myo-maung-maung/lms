package lms.com.dtos;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lms.com.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String userName;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role userRole;
}
