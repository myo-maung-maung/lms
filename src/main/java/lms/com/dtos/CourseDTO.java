package lms.com.dtos;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lms.com.entity.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDTO {
    private Long id;
    @NotBlank(message = "Course title is required")
    private String title;
    private Long instructorId;
    private Long categoryId;
    private List<String> imagePath;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    @NotNull(message = "Image is required")
    @Size(max = 5, message = "Yoc can upload up to 5 images")
    private List<MultipartFile> images;

    private List<VideoDTO> videos;
}
