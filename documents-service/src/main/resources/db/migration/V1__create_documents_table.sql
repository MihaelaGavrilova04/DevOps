CREATE TABLE documents (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    object_name VARCHAR(255) NOT NULL UNIQUE,
    file_size BIGINT,
    content_type VARCHAR(100),
    upload_time TIMESTAMP
);

-- Индекс за по-бързо търсене по user_id
CREATE INDEX idx_documents_user_id ON documents(user_id);

-- Индекс за търсене по upload_time
CREATE INDEX idx_documents_upload_time ON documents(upload_time);
