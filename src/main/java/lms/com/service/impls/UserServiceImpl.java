package lms.com.service.impls;

import lms.com.dtos.UserDTO;
import lms.com.entity.User;
import lms.com.entity.enums.Role;
import lms.com.mapper.UserMapper;
import lms.com.repository.UserRepository;
import lms.com.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO addUser(UserDTO userDTO) {
        User user = UserMapper.dtoToEntity(userDTO, passwordEncoder);
        User savedUser = userRepository.save(user);
        return UserMapper.entityToDto(savedUser);
    }

    @Override
    public List<UserDTO> allUser() {
        return userRepository.findAll().stream()
                .map(UserMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getInstructors() {
        return userRepository.findByUserRole(Role.INSTRUCTOR)
                .stream()
                .map(UserMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
