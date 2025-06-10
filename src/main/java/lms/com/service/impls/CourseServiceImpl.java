package lms.com.service.impls;

import lms.com.dtos.CourseDTO;
import lms.com.entity.Category;
import lms.com.entity.Course;
import lms.com.entity.User;
import lms.com.mapper.CourseMapper;
import lms.com.repository.CategoryRepository;
import lms.com.repository.CourseRepository;
import lms.com.repository.UserRepository;
import lms.com.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

//    public CourseServiceImpl(
//            CourseRepository courseRepository,
//            CategoryRepository categoryRepository,
//            UserRepository userRepository
//    ) {
//        this.courseRepository = courseRepository;
//        this.categoryRepository = categoryRepository;
//        this.userRepository = userRepository;
//    }

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        User instructor = userRepository.findById(courseDTO.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        Category category = categoryRepository.findById(courseDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Course course = CourseMapper.dtoToEntity(courseDTO, instructor, category);
        return CourseMapper.entityToDto(courseRepository.save(course));
    }

    @Override
    public List<CourseDTO> getAllCourse() {
        return courseRepository.findAll().stream()
                .map(CourseMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDTO updateCourse(Long courseId, CourseDTO courseDTO) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setTitle(courseDTO.getTitle());

        if (courseDTO.getInstructorId() != null) {
            User instructor = userRepository.findById(courseDTO.getInstructorId())
                    .orElseThrow(() -> new RuntimeException("Instructor not found"));
            course.setInstructor(instructor);
        }

        if (courseDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(courseDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            course.setCategory(category);
        }
        return CourseMapper.entityToDto(courseRepository.save(course));
    }

    @Override
    public CourseDTO getCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return CourseMapper.entityToDto(course);
    }

    @Override
    public void deleteCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new RuntimeException("Course not found");
        }
        courseRepository.deleteById(courseId);
    }

    @Override
    public List<CourseDTO> searchCourses(String keyword, String categoryName) {
        List<Course> courses;

        if (categoryName == null || categoryName.isBlank()) {
            courses = courseRepository.findByTitleContainingIgnoreCase(keyword);
        } else {
            courses = courseRepository.findByTitleContainingIgnoreCaseAndCategory_NameIgnoreCase(keyword, categoryName);
        }

        return courses.stream()
                .map(CourseMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
