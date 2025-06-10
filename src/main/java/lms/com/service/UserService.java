package lms.com.service;

import lms.com.dtos.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO addUser(UserDTO userDTO);

    List<UserDTO> allUser();

    List<UserDTO> getInstructors();
}
