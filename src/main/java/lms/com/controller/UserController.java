package lms.com.controller;

import jakarta.validation.Valid;
import lms.com.common.Constant;
import lms.com.common.LMSResponse;
import lms.com.dtos.PageDTO;
import lms.com.dtos.UserDTO;
import lms.com.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/add-user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> addUser(@Valid @ModelAttribute UserDTO userDTO) throws IOException {
        return ResponseEntity.ok(userService.addUser(userDTO));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LMSResponse> allUser() {
        List<UserDTO> users = userService.allUser();
        return ResponseEntity.ok(LMSResponse.success(Constant.GET_ALL, users));
    }

    @GetMapping("/instructors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getInstructors() {
        return ResponseEntity.ok(userService.getInstructors());
    }

    @GetMapping("/students")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getStudents() {
        return ResponseEntity.ok(userService.getStudents());
    }

    @GetMapping("/pagination")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<LMSResponse> getPaginationUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size
    ) {
        PageDTO<UserDTO> pageDto = userService.getPaginationUser(page, size);
        return ResponseEntity.ok(LMSResponse.success(Constant.PAGINATION, pageDto));
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<LMSResponse> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(LMSResponse.success(Constant.USER_DELETE, null));
    }
}
