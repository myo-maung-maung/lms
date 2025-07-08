package lms.com.service.impls;

import lms.com.common.Constant;
import lms.com.common.LMSResponse;
import lms.com.dtos.EnrollmentDTO;
import lms.com.dtos.PageDTO;
import lms.com.entity.Course;
import lms.com.entity.Enrollment;
import lms.com.entity.User;
import lms.com.entity.enums.EnrollmentStatus;
import lms.com.entity.enums.Role;
import lms.com.mapper.EnrollmentMapper;
import lms.com.repository.CourseRepository;
import lms.com.repository.EnrollmentRepository;
import lms.com.repository.UserRepository;
import lms.com.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public LMSResponse enrollStudent(EnrollmentDTO enrollmentDTO) {
        User student = userRepository.findById(enrollmentDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(enrollmentDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Enrollment enrollment = EnrollmentMapper.dtoToEntity(enrollmentDTO, student, course);
        enrollment.setUser(student);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDate.now());
        enrollment.setStatus(EnrollmentStatus.PENDING);

        Enrollment savedEnroll = enrollmentRepository.save(enrollment);
        return LMSResponse.success(Constant.ENROLL, EnrollmentMapper.entityToDto(savedEnroll));
    }

    @Override
    @Transactional
    public LMSResponse approveEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        enrollment.setStatus(EnrollmentStatus.APPROVED);

        Enrollment savedEnroll = enrollmentRepository.save(enrollment);
        return LMSResponse.success(Constant.APPROVED_ENROLL, EnrollmentMapper.entityToDto(savedEnroll));
    }

    @Override
    public List<EnrollmentDTO> allEnroll() {
        return enrollmentRepository.findAll().stream()
                .map(EnrollmentMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LMSResponse rejectedEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        enrollment.setStatus(EnrollmentStatus.REJECT);

        Enrollment rejected = enrollmentRepository.save(enrollment);
        return LMSResponse.success(Constant.REJECTED_ENROLL, EnrollmentMapper.entityToDto(rejected));
    }

    @Override
    public PageDTO<EnrollmentDTO> getPaginationEnroll(int page, int size) {
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // login user role
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(Constant.USER_NOT_FOUND));

        Page<Enrollment> enrollPage;
        if (user.getUserRole() == Role.STUDENT) {
            // student - only their own enrollments
            enrollPage = enrollmentRepository.findByUserId(user.getId(), pageable);
        } else if (user.getUserRole() == Role.INSTRUCTOR) {
            // instructor - only enrollments for their courses
            enrollPage = enrollmentRepository.findByInstructorId(user.getId(), pageable);
        } else {
            // admin or others - show all
            enrollPage = enrollmentRepository.findAll(pageable);
        }
//        Page<Enrollment> enrollPage = enrollmentRepository.findAll(pageable);

        Page<EnrollmentDTO> dtoPage = enrollPage.map(EnrollmentMapper::entityToDto);

        return PageDTO.of(dtoPage);
    }
}
