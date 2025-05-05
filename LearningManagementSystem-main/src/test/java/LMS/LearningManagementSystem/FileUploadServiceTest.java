package LMS.LearningManagementSystem;

import LMS.LearningManagementSystem.service.FileUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileUploadServiceTest {

    private FileUploadService fileUploadService;

    @Mock
    private MultipartFile multipartFile;

    private final String uploadDir = new File("uploads").getAbsolutePath();

    public void cleanUploadDirectory() throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (Files.exists(uploadPath)) {
            Files.walk(uploadPath)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }
    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        fileUploadService = new FileUploadService();
        cleanUploadDirectory(); // Clean the upload directory before each test
    }

    @Test
    void testUploadFileCreatesDirectory() throws IOException {
        when(multipartFile.getOriginalFilename()).thenReturn("testFile.txt");
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("test content".getBytes()));

        // Ensure the uploads directory does not exist before the test
        Path uploadPath = Paths.get(uploadDir);
        if (Files.exists(uploadPath)) {
            Files.delete(uploadPath);
        }

        String result = fileUploadService.uploadFile(multipartFile);

        assertTrue(Files.exists(uploadPath));
        assertTrue(result.startsWith("/uploads"));
    }

    @Test
    void testUploadFileSavesFile() throws IOException {
        String originalFileName = "testFile.txt";
        when(multipartFile.getOriginalFilename()).thenReturn(originalFileName);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("test content".getBytes()));

        String result = fileUploadService.uploadFile(multipartFile);

        Path filePath = Paths.get(uploadDir, result.replace("/uploads/", ""));

        assertTrue(Files.exists(filePath));
        assertEquals("test content", Files.readString(filePath));

        // Clean up the created file
        Files.deleteIfExists(filePath);
    }

    @Test
    void testUploadFileGeneratesUniqueFileName() throws IOException {
        String originalFileName = "testFile.txt";
        when(multipartFile.getOriginalFilename()).thenReturn(originalFileName);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("test content".getBytes()));

        String result1 = fileUploadService.uploadFile(multipartFile);
        String result2 = fileUploadService.uploadFile(multipartFile);

        assertNotEquals(result1, result2);

        // Clean up the created files
        Files.deleteIfExists(Paths.get(uploadDir, result1.replace("/uploads/", "")));
        Files.deleteIfExists(Paths.get(uploadDir, result2.replace("/uploads/", "")));
    }
}
