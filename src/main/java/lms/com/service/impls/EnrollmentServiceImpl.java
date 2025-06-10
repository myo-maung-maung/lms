package lms.com.service.impls;

import lms.com.dtos.EnrollmentDTO;
import lms.com.entity.Course;
import lms.com.entity.Enrollment;
import lms.com.entity.User;
import lms.com.entity.enums.EnrollmentStatus;
import lms.com.mapper.EnrollmentMapper;
import lms.com.repository.CourseRepository;
import lms.com.repository.EnrollmentRepository;
import lms.com.repository.UserRepository;
import lms.com.service.EnrollmentService;
import lms.com.utils.CertificateGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public EnrollmentDTO enrollStudent(EnrollmentDTO enrollmentDTO) {
        User student = userRepository.findById(enrollmentDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(enrollmentDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Enrollment enrollment = EnrollmentMapper.dtoToEntity(enrollmentDTO, student, course);
        enrollment.setUser(student);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDate.now());
        enrollment.setStatus(EnrollmentStatus.PENDING);
        return EnrollmentMapper.entityToDto(enrollmentRepository.save(enrollment));
    }

    @Override
    public EnrollmentDTO approveEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        enrollment.setStatus(EnrollmentStatus.APPROVED);
        return EnrollmentMapper.entityToDto(enrollmentRepository.save(enrollment));
    }
}
