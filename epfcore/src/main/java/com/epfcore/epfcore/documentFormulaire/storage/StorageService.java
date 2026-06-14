package com.epfcore.epfcore.documentFormulaire.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void init();

    void store(MultipartFile file, String filename);

    Resource loadAsResource(String filename);

    void delete(String filename);
}