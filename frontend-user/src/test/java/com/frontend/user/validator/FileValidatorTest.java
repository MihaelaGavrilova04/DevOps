package com.frontend.user.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;

class FileValidatorTest {

    private FileValidator fileValidator;

    @BeforeEach
    void setUp() {
        fileValidator = new FileValidator();
    }

    @Test
    void validateFileContent_validFile_doesNotThrow() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "content".getBytes()
        );

        assertDoesNotThrow(() -> fileValidator.validateFileContent(file));
    }

    @Test
    void validateFileContent_nullFile_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> fileValidator.validateFileContent(null));
    }

    @Test
    void validateFileContent_emptyFile_throwsException() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                new byte[0]
        );

        assertThrows(IllegalArgumentException.class,
                () -> fileValidator.validateFileContent(file));
    }

    @Test
    void validateFileContent_invalidExtension_throwsException() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.exe",
                "application/octet-stream",
                "content".getBytes()
        );

        assertThrows(IllegalArgumentException.class,
                () -> fileValidator.validateFileContent(file));
    }

    @Test
    void validateFileContent_tooLargeFile_throwsException() {
        byte[] bigContent = new byte[11 * 1024 * 1024]; // 11 MB

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "big.pdf",
                "application/pdf",
                bigContent
        );

        assertThrows(IllegalArgumentException.class,
                () -> fileValidator.validateFileContent(file));
    }
}
