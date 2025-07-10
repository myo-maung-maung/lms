package lms.com.service.impls;

import jakarta.annotation.PostConstruct;
import lms.com.common.Constant;
import lms.com.dtos.PageDTO;
import lms.com.dtos.UserDTO;
import lms.com.entity.User;
import lms.com.entity.enums.Role;
import lms.com.exceptions.DuplicateException;
import lms.com.exceptions.EntityNotFoundException;
import lms.com.mapper.UserMapper;
import lms.com.repository.UserRepository;
import lms.com.service.UserService;
import lms.com.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${user.file.path.absolutePath}")
    private String absolutePath;

    @Value(("${user.file.path.relativePath}"))
    private String relativePath;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileUtil fileUtil;

    @PostConstruct
    public void CreateAdminAccount() {
        Optional<User> optionalAdmin = userRepository.findByUserRole(Role.ADMIN);
        if (optionalAdmin.isEmpty()) {
            User admin = new User();
            admin.setUsername("Admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setUserRole(Role.ADMIN);
            userRepository.save(admin);

            System.out.println("Admin account created successfully");
        } else {
            System.out.println("Admin account already exits");
        }
    }

    @Override
    @Transactional
    public UserDTO addUser(UserDTO userDTO) throws IOException {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateException("Email already exists");
        }

        Long userId = userDTO.getId();
        String imagePath = null;
        if (userDTO.getImage() != null && !userDTO.getImage().isEmpty()) {
            imagePath = fileUtil.writeMediaFile(userDTO.getImage(), absolutePath, relativePath, userId);
            userDTO.setImagePath(imagePath);
        }
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

    @Override
    public List<UserDTO> getStudents() {
        return userRepository.findByUserRole(Role.STUDENT)
                .stream()
                .map(UserMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PageDTO<UserDTO> getPaginationUser(int page, int size) {
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> userPage = userRepository.findAll(pageable);

        Page<UserDTO> dtoPage = userPage.map(UserMapper::entityToDto);

        return PageDTO.of(dtoPage);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(Constant.USER_NOT_FOUND);
        }
        userRepository.deleteById(userId);
    }

}
