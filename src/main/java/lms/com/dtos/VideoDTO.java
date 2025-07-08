package lms.com.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class VideoDTO {
    private Long id;
    private List<String> videoPath;
    private String description;
    private String title;
    @NotNull(message = "Video is required")
    @Size(max = 5, message = "You can upload to up to 5 videos")
    private List<MultipartFile> video;
    private Long courseId;
    private Long instructorId;
}
