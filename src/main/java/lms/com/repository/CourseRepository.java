package lms.com.repository;

import lms.com.entity.Course;
import lms.com.entity.enums.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTitleContainingIgnoreCase(String keyword);

    List<Course> findByTitleContainingIgnoreCaseAndCategory_NameIgnoreCase(String keyword, String categoryName);

    Page<Course> findByInstructorId(Long id, Pageable pageable);

    Page<Course> findByStatusNot(EnrollmentStatus status, Pageable pageable);

    Page<Course> findByStatus(EnrollmentStatus enrollmentStatus, Pageable pageable);
}
