package lms.com.mapper;

import lms.com.dtos.EnrollmentDTO;
import lms.com.entity.Course;
import lms.com.entity.Enrollment;
import lms.com.entity.User;

public class EnrollmentMapper {

    public static Enrollment dtoToEntity(EnrollmentDTO dto, User user, Course course) {
        if (dto == null) {
            return null;
        }

        return Enrollment.builder()
                .id(dto.getId())
                .enrolledAt(dto.getEnrolledAt())
                .status(dto.getStatus())
                .user(user)
                .course(course)
                .build();
    }

    public static EnrollmentDTO entityToDto(Enrollment enrollment) {
        EnrollmentDTO entity = new EnrollmentDTO();
        entity.setId(enrollment.getId());
        entity.setCourseId(enrollment.getCourse().getId());
        entity.setUserId(enrollment.getUser().getId());
        entity.setEnrolledAt(enrollment.getEnrolledAt());
        entity.setStatus(enrollment.getStatus());
        return entity;
    }
}
