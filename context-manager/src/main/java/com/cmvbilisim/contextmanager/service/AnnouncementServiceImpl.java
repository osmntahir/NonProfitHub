package com.cmvbilisim.contextmanager.service;


import com.cmvbilisim.contextmanager.model.Announcement;
import com.cmvbilisim.contextmanager.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import java.time.LocalDate;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    @Autowired
    public AnnouncementServiceImpl(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    @Override
    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    @Override
    public Announcement saveAnnouncementWithImage(Announcement announcement, MultipartFile image) {
        validateAnnouncement(announcement);

        try {
            if (image != null && !image.isEmpty()) {
                String fileName = image.getOriginalFilename();
                String fileExtension = getFileExtension(fileName);

                if (!isValidImageExtension(fileExtension)) {
                    throw new RuntimeException("Invalid file type. Only image files (JPG, PNG, JPEG) are allowed.");
                }

                Path uploadPath = Paths.get("uploads/images/");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                announcement.setImagePath(filePath.toString());
            }

            return announcementRepository.save(announcement);

        } catch (IOException e) {
            throw new RuntimeException("Error while saving the image", e);
        }
    }
    @Override
    public Announcement updateAnnouncementWithImage(Long id, Announcement announcement, MultipartFile image) {
        Optional<Announcement> existingAnnouncementOpt = announcementRepository.findById(id);

        if (existingAnnouncementOpt.isEmpty()) {
            throw new IllegalArgumentException("Announcement not found with id: " + id);
        }
        validateAnnouncement(announcement);

        Announcement existingAnnouncement = existingAnnouncementOpt.get();

        // Update the existing announcement's properties
        existingAnnouncement.setSubject(announcement.getSubject());
        existingAnnouncement.setContent(announcement.getContent());
        existingAnnouncement.setValidityDate(announcement.getValidityDate());

        try {
            if (image != null && !image.isEmpty()) {
                String fileName = image.getOriginalFilename();
                String fileExtension = getFileExtension(fileName);

                if (!isValidImageExtension(fileExtension)) {
                    throw new RuntimeException("Invalid file type. Only image files (JPG, PNG, JPEG) are allowed.");
                }

                Path uploadPath = Paths.get("uploads/images/");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Update the image path
                existingAnnouncement.setImagePath(filePath.toString());
            }

            return announcementRepository.save(existingAnnouncement);

        } catch (IOException e) {
            throw new RuntimeException("Error while saving the image during update", e);
        }
    }


    private void validateAnnouncement(Announcement announcement) {
        if (announcement.getSubject() == null || announcement.getSubject().isEmpty()) {
            throw new ConstraintViolationException("Konu gereklidir", null);
        }
        if (announcement.getContent() == null || announcement.getContent().isEmpty()) {
            throw new ConstraintViolationException("İçerik gereklidir", null);
        }
        if (announcement.getValidityDate() == null || announcement.getValidityDate().before(java.sql.Date.valueOf(LocalDate.now()))) {
            throw new ConstraintViolationException("Geçerlilik Tarihi gereklidir ve gelecekte bir tarih olmalıdır", null);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex > 0) ? fileName.substring(dotIndex + 1) : "";
    }

    private boolean isValidImageExtension(String extension) {
        return extension.equalsIgnoreCase("jpg") ||
                extension.equalsIgnoreCase("jpeg") ||
                extension.equalsIgnoreCase("png");
    }

    @Override
    public Optional<Announcement> getAnnouncementById(Long id) {
        return announcementRepository.findById(id);
    }


    @Override
    public void deleteAnnouncement(Long id) {

        Optional<Announcement> announcementOpt = announcementRepository.findById(id);
        if (announcementOpt.isEmpty()) {
            throw new IllegalArgumentException("Announcement not found with id: " + id);
        }

        Announcement announcement = announcementOpt.get();


        if (announcement.getImagePath() != null && !announcement.getImagePath().isEmpty()) {
            Path imagePath = Paths.get(announcement.getImagePath());
            try {
                Files.deleteIfExists(imagePath);
            } catch (IOException e) {
                throw new RuntimeException("Error while deleting the image file: " + imagePath.toString(), e);
            }
        }


        announcementRepository.deleteById(id);
    }

}
