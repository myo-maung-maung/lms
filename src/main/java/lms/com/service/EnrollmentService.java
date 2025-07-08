package lms.com.service;

import lms.com.common.LMSResponse;
import lms.com.dtos.EnrollmentDTO;
import lms.com.dtos.PageDTO;

import java.util.List;

public interface EnrollmentService {
    LMSResponse enrollStudent(EnrollmentDTO enrollmentDTO);

    LMSResponse approveEnrollment(Long enrollmentId);

    List<EnrollmentDTO> allEnroll();

    LMSResponse rejectedEnrollment(Long enrollmentId);

    PageDTO<EnrollmentDTO> getPaginationEnroll(int page, int size);
}
