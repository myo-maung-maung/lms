package lms.com.service;

import lms.com.dtos.EnrollmentDTO;

public interface EnrollmentService {
    EnrollmentDTO enrollStudent(EnrollmentDTO enrollmentDTO);

    EnrollmentDTO approveEnrollment(Long enrollmentId);
}
