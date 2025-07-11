package lms.com.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lms.com.common.LMSResponse;
import lms.com.dtos.VideoDTO;
import lms.com.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/video")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "VIDEO", description = "Operations related to videos")
public class VideoController {

    private final VideoService videoService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<LMSResponse> uploadVideo(@ModelAttribute @Valid VideoDTO videoDTO) throws IOException, InterruptedException {
        return ResponseEntity.ok(videoService.uploadVideo(videoDTO));
    }
}
