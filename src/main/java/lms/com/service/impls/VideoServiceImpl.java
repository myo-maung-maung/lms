package lms.com.service.impls;

import lms.com.common.Constant;
import lms.com.common.LMSResponse;
import lms.com.dtos.VideoDTO;
import lms.com.entity.Course;
import lms.com.entity.User;
import lms.com.entity.Video;
import lms.com.exceptions.BadRequestException;
import lms.com.exceptions.EntityNotFoundException;
import lms.com.mapper.VideoMapper;
import lms.com.repository.CourseRepository;
import lms.com.repository.UserRepository;
import lms.com.repository.VideoRepository;
import lms.com.service.VideoService;
import lms.com.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    @Value("${video.file.path.absolutePath}")
    private String absolutePath;

    @Value("${video.file.path.relativePath}")
    private String relativePath;

    private final VideoRepository videoRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final FileUtil fileUtil;

    @Override
    @Transactional
    public LMSResponse uploadVideo(VideoDTO videoDTO) throws IOException, InterruptedException {

        Course course = courseRepository.findById(videoDTO.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        User instructor = userRepository.findById(videoDTO.getInstructorId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Long userId = instructor.getId();
        List<String> videos = new ArrayList<>();

        List<MultipartFile> videoFiles = videoDTO.getVideo();

        if (videoFiles.size() > 5) {
            throw new BadRequestException(Constant.VIDEO_SIZE);
        }

        long totalSize = videoFiles.stream().mapToLong(MultipartFile::getSize).sum();
        if (totalSize > 300L * 1024 * 1024) {
            throw new BadRequestException("Total video size must not exceed 300MB");
        }

        for (MultipartFile videoFile : videoFiles) {
            String videoPath = fileUtil.writeVideoFile(videoFile, absolutePath, relativePath, userId);
            videos.add(videoPath);
        }
        videoDTO.setVideoPath(videos);

        Video video = VideoMapper.dtoToEntity(videoDTO, course, instructor);
        Video savedVideo = videoRepository.save(video);
        return LMSResponse.success(Constant.UPLOAD_VIDEO, VideoMapper.entityToDto(savedVideo));
    }
}
