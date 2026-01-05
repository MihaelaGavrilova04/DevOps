package com.frontend.documents.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentDownloadDTOTest {

    @Test
    void constructor_SetsAllFields() {
        byte[] content = "test content".getBytes();
        DocumentDownloadDTO dto = new DocumentDownloadDTO(content, "test.pdf", "application/pdf");

        assertArrayEquals(content, dto.getFileContent());
        assertEquals("test.pdf", dto.getOriginalName());
        assertEquals("application/pdf", dto.getContentType());
    }

    @Test
    void constructor_NullContent() {
        DocumentDownloadDTO dto = new DocumentDownloadDTO(null, "test.pdf", "application/pdf");

        assertNull(dto.getFileContent());
        assertEquals("test.pdf", dto.getOriginalName());
        assertEquals("application/pdf", dto.getContentType());
    }

    @Test
    void constructor_EmptyFileName() {
        byte[] content = new byte[0];
        DocumentDownloadDTO dto = new DocumentDownloadDTO(content, "", "application/pdf");

        assertArrayEquals(content, dto.getFileContent());
        assertEquals("", dto.getOriginalName());
        assertEquals("application/pdf", dto.getContentType());
    }

    @Test
    void constructor_NullContentType() {
        byte[] content = "content".getBytes();
        DocumentDownloadDTO dto = new DocumentDownloadDTO(content, "test.pdf", null);

        assertArrayEquals(content, dto.getFileContent());
        assertEquals("test.pdf", dto.getOriginalName());
        assertNull(dto.getContentType());
    }

    @Test
    void constructor_WithSpecialCharactersInName() {
        byte[] content = "content".getBytes();
        DocumentDownloadDTO dto = new DocumentDownloadDTO(content, "тест-файл.pdf", "application/pdf");

        assertEquals("тест-файл.pdf", dto.getOriginalName());
        assertEquals("application/pdf", dto.getContentType());
    }

    @Test
    void setters_WorkCorrectly() {
        DocumentDownloadDTO dto = new DocumentDownloadDTO(new byte[0], "", "");

        byte[] newContent = "new content".getBytes();
        dto.setFileContent(newContent);
        dto.setOriginalName("new.pdf");
        dto.setContentType("text/plain");

        assertArrayEquals(newContent, dto.getFileContent());
        assertEquals("new.pdf", dto.getOriginalName());
        assertEquals("text/plain", dto.getContentType());
    }

    @Test
    void equalsAndHashCode() {
        byte[] content1 = "content".getBytes();
        DocumentDownloadDTO dto1 = new DocumentDownloadDTO(content1, "test.pdf", "application/pdf");
        DocumentDownloadDTO dto2 = new DocumentDownloadDTO(content1, "test.pdf", "application/pdf");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_ContainsFields() {
        byte[] content = "content".getBytes();
        DocumentDownloadDTO dto = new DocumentDownloadDTO(content, "test.pdf", "application/pdf");

        String toString = dto.toString();

        assertTrue(toString.contains("test.pdf"));
        assertTrue(toString.contains("application/pdf"));
        assertTrue(toString.contains("fileContent"));
    }

    @Test
    void constructor_LargeFileContent() {
        byte[] largeContent = new byte[1024 * 1024]; // 1MB
        for (int i = 0; i < largeContent.length; i++) {
            largeContent[i] = (byte) (i % 256);
        }

        DocumentDownloadDTO dto = new DocumentDownloadDTO(largeContent, "large.bin", "application/octet-stream");

        assertEquals(largeContent.length, dto.getFileContent().length);
        assertEquals("large.bin", dto.getOriginalName());
        assertEquals("application/octet-stream", dto.getContentType());
    }

    @Test
    void constructor_EmptyFile() {
        byte[] emptyContent = new byte[0];
        DocumentDownloadDTO dto = new DocumentDownloadDTO(emptyContent, "empty.txt", "text/plain");

        assertEquals(0, dto.getFileContent().length);
        assertEquals("empty.txt", dto.getOriginalName());
        assertEquals("text/plain", dto.getContentType());
    }
}