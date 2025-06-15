package lms.com.service;

import lms.com.dtos.PageDTO;
import lms.com.dtos.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO addUser(UserDTO userDTO);

    List<UserDTO> allUser();

    List<UserDTO> getInstructors();

    List<UserDTO> getStudents();

    PageDTO<UserDTO> getPaginationUser(int page, int size);
}
