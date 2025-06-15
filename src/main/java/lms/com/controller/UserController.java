package lms.com.controller;

import lms.com.dtos.PageDTO;
import lms.com.dtos.UserDTO;
import lms.com.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/add-user")
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.addUser(userDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> allUser() {
        return ResponseEntity.ok(userService.allUser());
    }

    @GetMapping("/instructors")
    public ResponseEntity<List<UserDTO>> getInstructors() {
        return ResponseEntity.ok(userService.getInstructors());
    }

    @GetMapping("/students")
    public ResponseEntity<List<UserDTO>> getStudents() {
        return ResponseEntity.ok(userService.getStudents());
    }

    @GetMapping("/pagination")
    public ResponseEntity<PageDTO<UserDTO>> getPaginationUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size
    ) {
        return ResponseEntity.ok(userService.getPaginationUser(page, size));
    }
}
