package lms.com.repository;

import lms.com.entity.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Page<Enrollment> findByUserId(Long id, Pageable pageable);
    @Query("SELECT e FROM Enrollment e WHERE e.course.instructor.id = :instructorId")
    Page<Enrollment> findByInstructorId(@Param("instructorId") Long instructorId, Pageable pageable);
}
