package lms.com.service;

import lms.com.common.LMSResponse;
import lms.com.dtos.VideoDTO;

import java.io.IOException;

public interface VideoService {
    LMSResponse uploadVideo(VideoDTO videoDTO) throws IOException, InterruptedException;
}
