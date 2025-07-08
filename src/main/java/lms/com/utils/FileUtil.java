package lms.com.utils;

import lms.com.common.Constant;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

@Component
public class FileUtil {

    public String writeMediaFile(MultipartFile multipartFile,
                                 String absolutePath,
                                 String relativePath,
                                 Long userId) throws IOException {
        ImageIO.scanForPlugins();

        String originalFileName = multipartFile.getOriginalFilename();
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            throw new RuntimeException(Constant.INVALID_IMAGE);
        }

        String extension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1).toLowerCase();
        String baseName = originalFileName.contains(".")
                ? originalFileName.substring(0, originalFileName.lastIndexOf('.'))
                : originalFileName;

        File userDir = new File(absolutePath, String.valueOf(userId));
        if (!userDir.exists()) {
            userDir.mkdirs();
        }

        String outputFileName = baseName.replaceAll(" ", "_") + ".jpg";
        File outputFile = new File(userDir, outputFileName);

        long startMs = System.currentTimeMillis();
        System.out.println("Image covert time is at: " + startMs + "s");

        try {
            BufferedImage originalImage = ImageIO.read(multipartFile.getInputStream());
            if (originalImage != null) {
                // Convert using ImageIO
                BufferedImage rgbImage = new BufferedImage(
                        originalImage.getWidth(),
                        originalImage.getHeight(),
                        BufferedImage.TYPE_INT_RGB
                );

                Graphics2D g = rgbImage.createGraphics();
                g.drawImage(originalImage, 0, 0, Color.WHITE, null);
                g.dispose();

                ImageIO.write(rgbImage, "jpg", outputFile);
            } else {
                // Unreadable by ImageIO → fallback to ffmpeg
                File tempInputFile = File.createTempFile("upload_", "_" + originalFileName);
                multipartFile.transferTo(tempInputFile);

                File convertedFile = convertWithFfmpeg(tempInputFile, outputFileName, userDir);
                tempInputFile.delete();
            }
        } catch (Exception e) {
            throw new RuntimeException("Image conversion failed: " + e.getMessage(), e);
        }

        long endMs   = System.currentTimeMillis();
        long durationSec = (endMs - startMs) / 1000;
        System.out.println("Image convert end time is at: " + endMs + "s");
        System.out.println("Total convert time is at: " + durationSec + "s");

        return relativePath + userId + "/" + outputFileName;
    }

    public String writeVideoFile(MultipartFile multipartFile,
                                 String absolutePath,
                                 String relativePath,
                                 Long userId) throws IOException, InterruptedException {
        File userDir = new File(absolutePath, String.valueOf(userId));
        if (!userDir.exists()) {
            userDir.mkdirs();
        }

        String originalFileName = multipartFile.getOriginalFilename();
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            throw new RuntimeException("Invalid video file");
        }

        String baseName = originalFileName.contains(".")
                ? originalFileName.substring(0, originalFileName.lastIndexOf('.'))
                : originalFileName;

        String outputFileName = baseName.replaceAll(" ", "_") + ".mp4";
        File outputFile = new File(userDir, outputFileName);

        File tempInputFile = File.createTempFile("vid_", "_" + originalFileName);
        multipartFile.transferTo(tempInputFile);

        ProcessBuilder builder = new ProcessBuilder(
                "C:\\ffmpeg\\bin\\ffmpeg.exe",
                "-y",
                "-i", tempInputFile.getAbsolutePath(),
                "-c:v", "libx264",
                "-preset", "fast",
                "-crf", "23",
                "-c:a", "aac",
                "-b:a", "128k",
                "-movflags", "+faststart",
                "-pix_fmt", "yuv420p",
                outputFile.getAbsolutePath()
        );

        builder.redirectErrorStream(true);
        Process process = builder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
//                System.out.println("[FFMPEG] " + line);
            }
        }

        int exitCode = process.waitFor();

        tempInputFile.delete();

        if (exitCode != 0 || !outputFile.exists()) {
            throw new RuntimeException("Failed to convert video to MP4.");
        }

        return relativePath + userId + "/" + outputFileName;
    }

    private File convertWithFfmpeg(File inputFile, String outputFileName, File outputDir) throws IOException, InterruptedException {
        File outputFile = new File(outputDir, outputFileName);

        ProcessBuilder builder = new ProcessBuilder(
                "C:\\ffmpeg\\bin\\ffmpeg.exe", // ffmpeg path
                "-y", // overwrite
                "-i", inputFile.getAbsolutePath(),
                outputFile.getAbsolutePath()
        );

        builder.redirectErrorStream(true);
        Process process = builder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // System.out.println("[FFMPEG] " + line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0 || !outputFile.exists()) {
            throw new RuntimeException("FFMPEG failed to convert image to JPG");
        }

        return outputFile;
    }

}
