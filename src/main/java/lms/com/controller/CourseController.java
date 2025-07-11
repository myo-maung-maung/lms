package lms.com.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lms.com.common.Constant;
import lms.com.common.LMSResponse;
import lms.com.dtos.CourseDTO;
import lms.com.dtos.PageDTO;
import lms.com.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/course")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "COURSE", description = "Operations related to courses")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private final CourseService courseService;

    @GetMapping("/test")
    public String test() {
        logger.debug("This is a debug message");
        logger.trace("This is a trace message");
        return "Test successful";
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<LMSResponse> create(@Valid @ModelAttribute CourseDTO courseDTO) throws IOException {
        return ResponseEntity.ok(courseService.createCourse(courseDTO));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN', 'STUDENT')")
    public ResponseEntity<LMSResponse> getAll() {
        List<CourseDTO> course = courseService.getAllCourse();
        return ResponseEntity.ok(LMSResponse.success(Constant.GET_ALL, course));
    }

    @GetMapping("/details/{courseId}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN', 'STUDENT')")
    public ResponseEntity<LMSResponse> getById(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getCourse(courseId));
    }

    @PutMapping("/updated/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<LMSResponse> updated(@RequestBody CourseDTO courseDTO, @PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.updateCourse(courseId, courseDTO));
    }

    @DeleteMapping("/delete/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<LMSResponse> delete(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok(LMSResponse.success(Constant.COURSE_DELETE, null));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseDTO>> searchCourses(
            @RequestParam String keyword,
            @RequestParam(required = false) String categoryName
    ) {
        List<CourseDTO> results = courseService.searchCourses(keyword, categoryName);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/pagination")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN', 'STUDENT')")
    public ResponseEntity<LMSResponse> getPaginationCourse(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size
    ) {
        PageDTO<CourseDTO> pageDto = courseService.getPaginationCourse(page, size);
        return ResponseEntity.ok(LMSResponse.success(Constant.PAGINATION, pageDto));
    }

    @PutMapping("/approve/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LMSResponse> approve(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.approveCourse(courseId));
    }

    @PutMapping("/reject/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LMSResponse> reject(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.rejectCourse(courseId));
    }
}
