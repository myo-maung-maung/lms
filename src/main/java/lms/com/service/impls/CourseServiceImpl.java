package lms.com.service.impls;

import lms.com.common.Constant;
import lms.com.common.LMSResponse;
import lms.com.dtos.CourseDTO;
import lms.com.dtos.PageDTO;
import lms.com.dtos.VideoDTO;
import lms.com.entity.Category;
import lms.com.entity.Course;
import lms.com.entity.User;
import lms.com.entity.Video;
import lms.com.entity.enums.EnrollmentStatus;
import lms.com.entity.enums.Role;
import lms.com.exceptions.BadRequestException;
import lms.com.exceptions.EntityDeletionException;
import lms.com.exceptions.EntityNotFoundException;
import lms.com.mapper.CourseMapper;
import lms.com.mapper.VideoMapper;
import lms.com.repository.CategoryRepository;
import lms.com.repository.CourseRepository;
import lms.com.repository.UserRepository;
import lms.com.repository.VideoRepository;
import lms.com.service.CourseService;
import lms.com.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    @Value("${course.file.path.absolutePath}")
    private String absolutePath;

    @Value("${course.file.path.relativePath}")
    private String relativePath;

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final FileUtil fileUtil;
    private final VideoRepository videoRepository;

    @Override
    @Transactional
    public LMSResponse createCourse(CourseDTO courseDTO) throws IOException {
        User instructor = userRepository.findById(courseDTO.getInstructorId())
                .orElseThrow(() -> new EntityNotFoundException("Instructor not found"));

        Category category = categoryRepository.findById(courseDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        Long userId = instructor.getId();
        List<String> imagePath = new ArrayList<>();
        for (MultipartFile image: courseDTO.getImages()) {
            if (image.getSize() > 2 * 1024 * 1024) {
                throw new BadRequestException(Constant.IMAGE_VALIDATION);
            }
            String path = fileUtil.writeMediaFile(image, absolutePath, relativePath, userId);
            imagePath.add(path);
        }
        courseDTO.setImagePath(imagePath);
        courseDTO.setStatus(EnrollmentStatus.PENDING);

        Course course = CourseMapper.dtoToEntity(courseDTO, instructor, category);
        Course savedCourse = courseRepository.save(course);
        return LMSResponse.success(Constant.ADD_COURSE, CourseMapper.entityToDto(savedCourse));
    }

    @Override
    public List<CourseDTO> getAllCourse() {
        return courseRepository.findAll().stream()
                .map(CourseMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LMSResponse updateCourse(Long courseId, CourseDTO courseDTO) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        course.setTitle(courseDTO.getTitle());

        if (courseDTO.getInstructorId() != null) {
            User instructor = userRepository.findById(courseDTO.getInstructorId())
                    .orElseThrow(() -> new EntityNotFoundException("Instructor not found"));
            course.setInstructor(instructor);
        }

        if (courseDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(courseDTO.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            course.setCategory(category);
        }
        Course updated = courseRepository.save(course);
        return LMSResponse.success(Constant.COURSE_UPDATE, CourseMapper.entityToDto(updated));
    }

    @Override
    public LMSResponse getCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        CourseDTO dto = CourseMapper.entityToDto(course);
        List<Video> videos = videoRepository.findByCourseId(courseId);
        List<VideoDTO> videoDTOS = videos.stream()
                .map(VideoMapper::entityToDto)
                .collect(Collectors.toList());
        dto.setVideos(videoDTOS);
        return LMSResponse.success(Constant.COURSE_ID, dto);
    }

    @Override
    @Transactional
    public void deleteCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new EntityDeletionException("Course not found");
        }
        courseRepository.deleteById(courseId);
    }

    @Override
    @Transactional
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

    @Override
    public PageDTO<CourseDTO> getPaginationCourse(int page, int size) {
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // login user role
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(Constant.USER_NOT_FOUND));

        boolean isInstructor = user.getUserRole() == Role.INSTRUCTOR;
        boolean isAdmin = user.getUserRole() == Role.ADMIN;
        Page<Course> coursePage;
        if (isInstructor) {
            coursePage = courseRepository.findByInstructorId(user.getId(), pageable);
        } else if (isAdmin) {
            coursePage = courseRepository.findAll(pageable);
        } else {
            coursePage = courseRepository.findByStatus(EnrollmentStatus.APPROVED, pageable);
        }

        Page<CourseDTO> dtoPage = coursePage.map(CourseMapper::entityToDto);

        return PageDTO.of(dtoPage);
    }

    @Override
    @Transactional
    public LMSResponse approveCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException(Constant.COURSE_ID));
        course.setStatus(EnrollmentStatus.APPROVED);

        Course savedCourse  = courseRepository.save(course);
        return LMSResponse.success(Constant.APPROVED, CourseMapper.entityToDto(savedCourse));
    }

    @Override
    @Transactional
    public LMSResponse rejectCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException(Constant.COURSE_ID));
        course.setStatus(EnrollmentStatus.REJECT);
        Course rejected = courseRepository.save(course);
        return LMSResponse.success(Constant.REJECTED, CourseMapper.entityToDto(rejected));
    }
}
