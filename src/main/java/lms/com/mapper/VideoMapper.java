package lms.com.mapper;

import lms.com.dtos.VideoDTO;
import lms.com.entity.Course;
import lms.com.entity.User;
import lms.com.entity.Video;

public class VideoMapper {

    public static Video dtoToEntity(VideoDTO dto, Course course, User user) {
        if (dto == null) {
            return null;
        }

        return Video.builder()
                .id(dto.getId())
                .videoPath(dto.getVideoPath())
                .description(dto.getDescription())
                .title(dto.getTitle())
                .course(course)
                .user(user)
                .build();
    }

    public static VideoDTO entityToDto(Video entity) {
        if (entity == null) {
            return null;
        }

        return VideoDTO.builder()
                .id(entity.getId())
                .videoPath(entity.getVideoPath())
                .description(entity.getDescription())
                .title(entity.getTitle())
                .courseId(entity.getCourse() != null ? entity.getCourse().getId() : null)
                .instructorId(entity.getUser() != null ? entity.getCourse().getId() : null)
                .build();
    }
}
