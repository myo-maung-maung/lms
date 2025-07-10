package lms.com.service;

import lms.com.common.LMSResponse;
import lms.com.dtos.PageDTO;
import lms.com.dtos.UserDTO;

import java.io.IOException;
import java.util.List;

public interface UserService {

    UserDTO addUser(UserDTO userDTO) throws IOException;

    List<UserDTO> allUser();

    List<UserDTO> getInstructors();

    List<UserDTO> getStudents();

    PageDTO<UserDTO> getPaginationUser(int page, int size);

    void deleteUser(Long userId);
}
