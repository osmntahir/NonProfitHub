
package com.cmvbilisim.contextmanager.service;

import com.cmvbilisim.contextmanager.dto.AnnouncementDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface AnnouncementService {
    List<AnnouncementDTO> getAllAnnouncements();
    AnnouncementDTO saveAnnouncementWithImage(AnnouncementDTO announcementDTO, MultipartFile image);
    AnnouncementDTO updateAnnouncementWithImage(Long id, AnnouncementDTO announcementDTO, MultipartFile image);
    List<AnnouncementDTO> getValidAnnouncements();
    Optional<AnnouncementDTO> getAnnouncementById(Long id);
    void deleteAnnouncement(Long id);
}
