package lms.com.repository;

import lms.com.dtos.CourseDTO;
import lms.com.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTitleContainingIgnoreCase(String keyword);

    List<Course> findByTitleContainingIgnoreCaseAndCategory_NameIgnoreCase(String keyword, String categoryName);
}
