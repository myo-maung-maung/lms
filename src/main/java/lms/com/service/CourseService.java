package lms.com.service;

import lms.com.dtos.CourseDTO;

import java.util.List;

public interface CourseService {
    CourseDTO createCourse(CourseDTO courseDTO);

   List<CourseDTO> getAllCourse();

    CourseDTO updateCourse(Long courseId, CourseDTO courseDTO);

    CourseDTO getCourse(Long courseId);

    void deleteCourse(Long courseId);

    List<CourseDTO> searchCourses(String keyword, String categoryName);
}
