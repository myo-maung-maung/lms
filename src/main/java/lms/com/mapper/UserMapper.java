package lms.com.mapper;

import lms.com.dtos.UserDTO;
import lms.com.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {

    public static User dtoToEntity(UserDTO dto, PasswordEncoder passwordEncoder) {
        if (dto == null) {
            return null;
        }

        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .username(dto.getUserName())
                .userRole(dto.getUserRole())
                .imagePath(dto.getImagePath())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
    }

    public static UserDTO entityToDto(User entity) {
        if (entity == null) {
            return null;
        }

        return UserDTO.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .userName(entity.getUsername())
                .userRole(entity.getUserRole())
                .password("*****")
                .imagePath(entity.getImagePath())
                .build();
    }
}
