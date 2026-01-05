package com.frontend.documents.config;

import io.minio.MinioClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinioClientConfigTest {

    @Test
    void getMinioClient_ReturnsMinioClient() throws Exception {
        MinioClientConfig config = new MinioClientConfig();

        setField(config, "endpoint", "http://localhost:9000");
        setField(config, "accessKey", "minioadmin");
        setField(config, "secretKey", "minioadmin");

        try (MockedConstruction<MinioClient.Builder> mocked = mockConstruction(MinioClient.Builder.class,
                (mock, context) -> {
                    MinioClient mockClient = mock(MinioClient.class);
                    when(mock.build()).thenReturn(mockClient);
                    when(mock.endpoint(anyString())).thenReturn(mock);
                    when(mock.credentials(anyString(), anyString())).thenReturn(mock);
                })) {

            MinioClient client = config.getMinioClient();

            assertNotNull(client);
            assertFalse(mocked.constructed().isEmpty());

            MinioClient.Builder builder = mocked.constructed().get(0);
            verify(builder).endpoint("http://localhost:9000");
            verify(builder).credentials("minioadmin", "minioadmin");
            verify(builder).build();
        }
    }

    @Test
    void getMinioClient_WithDifferentEndpoint() throws Exception {
        MinioClientConfig config = new MinioClientConfig();

        setField(config, "endpoint", "http://minio:9000");
        setField(config, "accessKey", "user");
        setField(config, "secretKey", "password");

        try (MockedConstruction<MinioClient.Builder> mocked = mockConstruction(MinioClient.Builder.class,
                (mock, context) -> {
                    when(mock.build()).thenReturn(mock(MinioClient.class));
                    when(mock.endpoint(anyString())).thenReturn(mock);
                    when(mock.credentials(anyString(), anyString())).thenReturn(mock);
                })) {

            MinioClient client = config.getMinioClient();

            assertNotNull(client);

            MinioClient.Builder builder = mocked.constructed().get(0);
            verify(builder).endpoint("http://minio:9000");
            verify(builder).credentials("user", "password");
        }
    }

    @Test
    void getMinioClient_WithHttpsEndpoint() {
        MinioClientConfig config = new MinioClientConfig();
        ReflectionTestUtils.setField(config, "endpoint", "https://secure-minio:9000");
        ReflectionTestUtils.setField(config, "accessKey", "secure");
        ReflectionTestUtils.setField(config, "secretKey", "secure123");

        MinioClient client = config.getMinioClient();

        assertNotNull(client);
    }

    @Test
    void getMinioClient_WithSpecialCharacters() throws Exception {
        MinioClientConfig config = new MinioClientConfig();
        setPrivateField(config, "endpoint", "http://localhost:9000");
        setPrivateField(config, "accessKey", "user@email");
        setPrivateField(config, "secretKey", "pass#word!123");

        MinioClient client = config.getMinioClient();

        assertNotNull(client);
    }

    private void setPrivateField(Object object, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    @Test
    void getMinioClient_EmptyEndpoint_ThrowsException() throws Exception {
        MinioClientConfig config = new MinioClientConfig();

        setField(config, "endpoint", "");
        setField(config, "accessKey", "key");
        setField(config, "secretKey", "secret");

        assertThrows(Exception.class, () -> config.getMinioClient());
    }

    @Test
    void getMinioClient_InvalidEndpointFormat() throws Exception {
        MinioClientConfig config = new MinioClientConfig();

        setField(config, "endpoint", "invalid-url");
        setField(config, "accessKey", "key");
        setField(config, "secretKey", "secret");

        try (MockedConstruction<MinioClient.Builder> mocked = mockConstruction(MinioClient.Builder.class)) {
            assertThrows(Exception.class, () -> config.getMinioClient());
        }
    }

    private void setField(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
}