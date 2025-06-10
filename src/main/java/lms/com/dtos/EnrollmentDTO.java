package lms.com.dtos;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lms.com.entity.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentDTO {
    private Long id;
    private LocalDate enrolledAt;
    private Long userId;
    private Long courseId;
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;
}
