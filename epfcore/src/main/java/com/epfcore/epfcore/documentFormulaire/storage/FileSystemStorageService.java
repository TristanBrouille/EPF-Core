package com.epfcore.epfcore.documentFormulaire.storage;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    public FileSystemStorageService(StorageProperties properties) {
        if (properties.getLocation().trim().isEmpty()) {
            throw new StorageException("File upload location can not be Empty.");
        }
        Path configured = Paths.get(properties.getLocation());
        Path resolved;
        if (configured.isAbsolute()) {
            resolved = configured;
        } else {
            // Résolution relative à target/classes pour être indépendant du répertoire de travail IntelliJ
            Path fallback = configured.toAbsolutePath();
            try {
                URL source = getClass().getProtectionDomain().getCodeSource().getLocation();
                Path sourcePath = Paths.get(source.toURI());
                Path base = Files.isDirectory(sourcePath)
                        ? sourcePath.getParent().getParent()  // target/classes → module root
                        : sourcePath.getParent();              // dossier contenant le jar en prod
                resolved = base.resolve(configured).normalize();
            } catch (Exception e) {
                resolved = fallback;
            }
        }
        this.rootLocation = resolved;
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public void store(MultipartFile file, String filename) {
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file.");
        }

        String cleanedFilename = StringUtils.cleanPath(filename);
        if (cleanedFilename.contains("..")) {
            throw new StorageException("Cannot store file with relative path outside current directory " + cleanedFilename);
        }

        try {
            Path destinationFile = rootLocation.resolve(cleanedFilename).normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
                throw new StorageException("Cannot store file outside current directory.");
            }
            try (var inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(StringUtils.cleanPath(filename)).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            Path file = rootLocation.resolve(StringUtils.cleanPath(filename)).normalize();
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new StorageException("Failed to delete file: " + filename, e);
        }
    }
}