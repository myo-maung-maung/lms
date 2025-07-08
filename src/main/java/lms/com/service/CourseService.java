package lms.com.service;

import lms.com.common.LMSResponse;
import lms.com.dtos.CourseDTO;
import lms.com.dtos.PageDTO;

import java.io.IOException;
import java.util.List;

public interface CourseService {
    LMSResponse createCourse(CourseDTO courseDTO) throws IOException;

   List<CourseDTO> getAllCourse();

    LMSResponse updateCourse(Long courseId, CourseDTO courseDTO);

    LMSResponse getCourse(Long courseId);

    void deleteCourse(Long courseId);

    List<CourseDTO> searchCourses(String keyword, String categoryName);

    PageDTO<CourseDTO> getPaginationCourse(int page, int size);

    LMSResponse approveCourse(Long courseId);

    LMSResponse rejectCourse(Long courseId);
}
