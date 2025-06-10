package lms.com.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseCreateDTO {
    private String title;
    private Long instructorId;
    private Long categoryId;
}
