package lms.com.controller;

import lms.com.common.Constant;
import lms.com.common.LMSResponse;
import lms.com.dtos.CourseDTO;
import lms.com.dtos.EnrollmentDTO;
import lms.com.dtos.PageDTO;
import lms.com.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/enroll")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/create-enroll")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<LMSResponse> enroll(
            @RequestBody EnrollmentDTO enrollmentDTO
    ) {
        return ResponseEntity.ok(enrollmentService.enrollStudent(enrollmentDTO));
    }

    @PutMapping("/approve/{enrollmentId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<LMSResponse> approve(@PathVariable Long enrollmentId) {
        return ResponseEntity.ok(enrollmentService.approveEnrollment(enrollmentId));
    }

    @GetMapping("/all-enroll")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'STUDENT', 'ADMIN')")
    public ResponseEntity<LMSResponse> getAll() {
        List<EnrollmentDTO> enroll = enrollmentService.allEnroll();
        return ResponseEntity.ok(LMSResponse.success(Constant.GET_ALL, enroll));
    }

    @PutMapping("/reject/{enrollmentId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<LMSResponse> reject(@PathVariable Long enrollmentId) {
        return ResponseEntity.ok(enrollmentService.rejectedEnrollment(enrollmentId));
    }

    @GetMapping("/pagination")
    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR')")
    public ResponseEntity<LMSResponse> getPaginationEnroll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size
    ) {
        PageDTO<EnrollmentDTO> pageDto = enrollmentService.getPaginationEnroll(page, size);
        return ResponseEntity.ok(LMSResponse.success(Constant.PAGINATION, pageDto));
    }
}
