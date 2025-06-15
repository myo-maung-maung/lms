package lms.com.service;

import lms.com.dtos.EnrollmentDTO;
import lms.com.dtos.PageDTO;

import java.util.List;

public interface EnrollmentService {
    EnrollmentDTO enrollStudent(EnrollmentDTO enrollmentDTO);

    EnrollmentDTO approveEnrollment(Long enrollmentId);

    List<EnrollmentDTO> allEnroll();

    EnrollmentDTO rejectedEnrollment(Long enrollmentId);

    PageDTO<EnrollmentDTO> getPaginationEnroll(int page, int size);
}
