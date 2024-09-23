package com.cmvbilisim.contextmanager.service;

import com.cmvbilisim.contextmanager.dto.AnnouncementDTO;
import com.cmvbilisim.contextmanager.mapper.AnnouncementMapper;
import com.cmvbilisim.contextmanager.model.Announcement;
import com.cmvbilisim.contextmanager.repository.AnnouncementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    private static final Logger logger = LoggerFactory.getLogger(AnnouncementServiceImpl.class);

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementMapper announcementMapper;

    @Autowired
    public AnnouncementServiceImpl(AnnouncementRepository announcementRepository, AnnouncementMapper announcementMapper) {
        this.announcementRepository = announcementRepository;
        this.announcementMapper = announcementMapper;
    }

    @Override
    public List<AnnouncementDTO> getAllAnnouncements() {
        logger.info("Fetching all announcements");
        List<AnnouncementDTO> announcements = announcementRepository.findAll().stream()
                .map(announcementMapper::toDTO)
                .collect(Collectors.toList());
        logger.debug("Fetched {} announcements", announcements.size());
        return announcements;
    }

    @Override
    public AnnouncementDTO saveAnnouncementWithImage(AnnouncementDTO announcementDTO, MultipartFile image) {
        logger.info("Saving new announcement: {}", announcementDTO.getSubject());
        validateAnnouncement(announcementDTO);

        try {
            Announcement announcement = announcementMapper.toEntityForCreate(announcementDTO);
            logger.debug("Converted AnnouncementDTO to Announcement entity");

            if (image != null && !image.isEmpty()) {
                String fileName = Paths.get(image.getOriginalFilename()).getFileName().toString();
                String fileExtension = getFileExtension(fileName);

                if (!isValidImageExtension(fileExtension)) {
                    logger.warn("Invalid image file type: {}", fileExtension);
                    throw new RuntimeException("Geçersiz dosya türü. Sadece JPG, PNG, JPEG dosyaları kabul edilir.");
                }

                Path uploadPath = Paths.get("uploads/images/");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                    logger.debug("Created upload directory at {}", uploadPath.toString());
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                announcement.setImagePath(filePath.toString());
                logger.info("Image saved at {}", filePath.toString());
            }

            Announcement savedAnnouncement = announcementRepository.save(announcement);
            logger.info("Announcement saved with ID {}", savedAnnouncement.getId());
            return announcementMapper.toDTO(savedAnnouncement);

        } catch (IOException e) {
            logger.error("Error while saving the image", e);
            throw new RuntimeException("Resim kaydedilirken hata oluştu", e);
        }
    }

    @Override
    public AnnouncementDTO updateAnnouncementWithImage(Long id, AnnouncementDTO announcementDTO, MultipartFile image) {
        logger.info("Updating announcement with ID {}", id);
        Optional<Announcement> existingAnnouncementOpt = announcementRepository.findById(id);

        if (existingAnnouncementOpt.isEmpty()) {
            logger.warn("Announcement not found with ID {}", id);
            throw new IllegalArgumentException("Belirtilen ID ile bir duyuru bulunamadı: " + id);
        }

        validateAnnouncement(announcementDTO);
        Announcement existingAnnouncement = existingAnnouncementOpt.get();
        announcementMapper.updateEntityFromDTO(announcementDTO, existingAnnouncement);
        logger.debug("Updated Announcement entity from AnnouncementDTO");

        try {
            if (image != null && !image.isEmpty()) {
                String fileName = Paths.get(image.getOriginalFilename()).getFileName().toString();
                String fileExtension = getFileExtension(fileName);

                if (!isValidImageExtension(fileExtension)) {
                    logger.warn("Invalid image file type: {}", fileExtension);
                    throw new RuntimeException("Geçersiz dosya türü. Sadece JPG, PNG, JPEG dosyaları kabul edilir.");
                }

                Path uploadPath = Paths.get("uploads/images/");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                    logger.debug("Created upload directory at {}", uploadPath.toString());
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                logger.info("Image saved at {}", filePath.toString());

                // Eski resmi sil
                if (existingAnnouncement.getImagePath() != null && !existingAnnouncement.getImagePath().isEmpty()) {
                    Path oldImagePath = Paths.get(existingAnnouncement.getImagePath());
                    Files.deleteIfExists(oldImagePath);
                    logger.info("Old image deleted at {}", oldImagePath.toString());
                }

                // Yeni resim yolunu güncelle
                existingAnnouncement.setImagePath(filePath.toString());
            }

            Announcement updatedAnnouncement = announcementRepository.save(existingAnnouncement);
            logger.info("Announcement with ID {} updated successfully", id);
            return announcementMapper.toDTO(updatedAnnouncement);

        } catch (IOException e) {
            logger.error("Error while saving the image during update", e);
            throw new RuntimeException("Güncelleme sırasında resim kaydedilirken hata oluştu", e);
        }
    }

    @Override
    public List<AnnouncementDTO> getValidAnnouncements() {
        logger.info("Fetching valid announcements (validity date >= today)");
        LocalDate today = LocalDate.now();
        List<AnnouncementDTO> validAnnouncements = announcementRepository.findByValidityDateGreaterThanEqual(today).stream()
                .map(announcementMapper::toDTO)
                .collect(Collectors.toList());
        logger.debug("Fetched {} valid announcements", validAnnouncements.size());
        return validAnnouncements;
    }

    @Override
    public Optional<AnnouncementDTO> getAnnouncementById(Long id) {
        logger.info("Fetching announcement with ID {}", id);
        Optional<AnnouncementDTO> announcementDTO = announcementRepository.findById(id)
                .map(announcementMapper::toDTO);
        if (announcementDTO.isPresent()) {
            logger.debug("Announcement found: {}", announcementDTO.get().getSubject());
        } else {
            logger.warn("Announcement not found with ID {}", id);
        }
        return announcementDTO;
    }

    @Override
    public void deleteAnnouncement(Long id) {
        logger.info("Deleting announcement with ID {}", id);
        Optional<Announcement> announcementOpt = announcementRepository.findById(id);
        if (announcementOpt.isEmpty()) {
            logger.warn("Announcement not found with ID {}", id);
            throw new IllegalArgumentException("Belirtilen ID ile bir duyuru bulunamadı: " + id);
        }

        Announcement announcement = announcementOpt.get();

        if (announcement.getImagePath() != null && !announcement.getImagePath().isEmpty()) {
            Path imagePath = Paths.get(announcement.getImagePath());
            try {
                Files.deleteIfExists(imagePath);
                logger.info("Image file deleted at {}", imagePath.toString());
            } catch (IOException e) {
                logger.error("Error while deleting the image file: {}", imagePath.toString(), e);
                throw new RuntimeException("Resim dosyası silinirken hata oluştu: " + imagePath.toString(), e);
            }
        }

        announcementRepository.deleteById(id);
        logger.info("Announcement with ID {} deleted successfully", id);
    }

    private void validateAnnouncement(AnnouncementDTO announcementDTO) {
        logger.debug("Validating announcement DTO: {}", announcementDTO.getSubject());
        if (announcementDTO.getSubject() == null || announcementDTO.getSubject().isEmpty()) {
            logger.error("Validation failed: Subject is required");
            throw new ConstraintViolationException("Konu gereklidir", null);
        }
        if (announcementDTO.getContent() == null || announcementDTO.getContent().isEmpty()) {
            logger.error("Validation failed: Content is required");
            throw new ConstraintViolationException("İçerik gereklidir", null);
        }
        if (announcementDTO.getValidityDate() == null || announcementDTO.getValidityDate().isBefore(LocalDate.now())) {
            logger.error("Validation failed: Validity date is required and must be a future date");
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
}
