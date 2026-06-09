package com.epfcore.epfcore.document.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.epfcore.epfcore.document.entity.Document;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    List<Document> findByUserId(Integer userId);
    List<Document> findByStatus(Document status);
    List<Document> findByUserIdAndStatus(Integer userId, Document status);
}
