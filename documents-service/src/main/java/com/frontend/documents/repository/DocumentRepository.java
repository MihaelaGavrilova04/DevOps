package com.frontend.documents.repository;

import com.frontend.documents.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findAllByUserId(Long userId);
    Optional<Document> findByUserIdAndId(Long userId, Long id);
}
