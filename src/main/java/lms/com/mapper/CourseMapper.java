package lms.com.mapper;

import lms.com.dtos.CourseDTO;
import lms.com.entity.Category;
import lms.com.entity.Course;
import lms.com.entity.User;

public class CourseMapper {

    public static Course dtoToEntity(CourseDTO dto, User instructor, Category category) {
        if (dto == null) {
            return null;
        }

        return Course.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .status(dto.getStatus())
                .instructor(instructor)
                .category(category)
                .imagePath(dto.getImagePath())
                .build();
    }

    public static CourseDTO entityToDto(Course entity) {
        if (entity == null) {
            return null;
        }

        return CourseDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .status(entity.getStatus())
                .imagePath(entity.getImagePath())
                .instructorId(entity.getInstructor() != null ? entity.getInstructor().getId() : null)
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
                .build();
    }
}
