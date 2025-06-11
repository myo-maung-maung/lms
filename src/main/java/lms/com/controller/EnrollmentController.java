package lms.com.controller;

import lms.com.dtos.EnrollmentDTO;
import lms.com.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/enroll")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/create-enroll")
    public ResponseEntity<EnrollmentDTO> enroll(@RequestBody EnrollmentDTO enrollmentDTO) {
        return ResponseEntity.ok(enrollmentService.enrollStudent(enrollmentDTO));
    }

    @PutMapping("/approve/{enrollmentId}")
    public ResponseEntity<EnrollmentDTO> approve(@PathVariable Long enrollmentId) {
        return ResponseEntity.ok(enrollmentService.approveEnrollment(enrollmentId));
    }

    @GetMapping("/all-enroll")
    public ResponseEntity<List<EnrollmentDTO>> getAll() {
        return ResponseEntity.ok(enrollmentService.allEnroll());
    }

    @PutMapping("/reject/{enrollmentId}")
    public ResponseEntity<EnrollmentDTO> reject(@PathVariable Long enrollmentId) {
        return ResponseEntity.ok(enrollmentService.rejectedEnrollment(enrollmentId));
    }
}
