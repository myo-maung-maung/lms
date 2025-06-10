package lms.com.controller;

import lms.com.dtos.CourseDTO;
import lms.com.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/course")
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/create")
    public ResponseEntity<CourseDTO> create(@RequestBody CourseDTO courseDTO) {
        return ResponseEntity.ok(courseService.createCourse(courseDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CourseDTO>> getAll() {
        return ResponseEntity.ok(courseService.getAllCourse());
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDTO> getById(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getCourse(courseId));
    }

    @PutMapping("/updated/{courseId}")
    public ResponseEntity<CourseDTO> updated(@RequestBody CourseDTO courseDTO, @PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.updateCourse(courseId, courseDTO));
    }

    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<Void> delete(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseDTO>> searchCourses(
            @RequestParam String keyword,
            @RequestParam(required = false) String categoryName
    ) {
        List<CourseDTO> results = courseService.searchCourses(keyword, categoryName);
        return ResponseEntity.ok(results);
    }
}
