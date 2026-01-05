//package com.frontend.user.validator;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.web.multipart.MultipartFile;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class FileValidatorTest {
//
//    @Test
//    void validateFileContent_ValidFile() {
//        FileValidator validator = new FileValidator();
//        MultipartFile file = mock(MultipartFile.class);
//
//        when(file.isEmpty()).thenReturn(false);
//        when(file.getOriginalFilename()).thenReturn("test.pdf");
//        when(file.getSize()).thenReturn(1024L);
//
//        assertDoesNotThrow(() -> validator.validateFileContent(file));
//    }
//
//    @Test
//    void validateFileContent_EmptyFile() {
//        FileValidator validator = new FileValidator();
//        MultipartFile file = mock(MultipartFile.class);
//
//        when(file.isEmpty()).thenReturn(true);
//
//        Exception exception = assertThrows(RuntimeException.class,
//                () -> validator.validateFileContent(file));
//
//        assertTrue(exception.getMessage().contains("empty"));
//    }
//
//    @Test
//    void validateFileContent_NullFile() {
//        FileValidator validator = new FileValidator();
//
//        Exception exception = assertThrows(RuntimeException.class,
//                () -> validator.validateFileContent(null));
//
//        assertTrue(exception.getMessage().contains("null"));
//    }
//
//    @Test
//    void validateFileContent_InvalidExtension() {
//        FileValidator validator = new FileValidator();
//        MultipartFile file = mock(MultipartFile.class);
//
//        when(file.isEmpty()).thenReturn(false);
//        when(file.getOriginalFilename()).thenReturn("test.exe");
//
//        Exception exception = assertThrows(RuntimeException.class,
//                () -> validator.validateFileContent(file));
//
//        assertTrue(exception.getMessage().contains("extension"));
//    }
//
//    @Test
//    void validateFileContent_FileTooLarge() {
//        FileValidator validator = new FileValidator();
//        MultipartFile file = mock(MultipartFile.class);
//
//        when(file.isEmpty()).thenReturn(false);
//        when(file.getOriginalFilename()).thenReturn("test.pdf");
//        when(file.getSize()).thenReturn(1024L * 1024 * 11); // 11MB
//
//        Exception exception = assertThrows(RuntimeException.class,
//                () -> validator.validateFileContent(file));
//
//        assertTrue(exception.getMessage().contains("size"));
//    }
//}