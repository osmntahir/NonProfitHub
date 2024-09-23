
package com.cmvbilisim.contextmanager.mapper;

import com.cmvbilisim.contextmanager.dto.AnnouncementDTO;
import com.cmvbilisim.contextmanager.model.Announcement;
import org.springframework.stereotype.Component;

@Component
public class AnnouncementMapper {

    public AnnouncementDTO toDTO(Announcement announcement) {
        if (announcement == null) {
            return null;
        }
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setId(announcement.getId());
        dto.setSubject(announcement.getSubject());
        dto.setContent(announcement.getContent());
        dto.setValidityDate(announcement.getValidityDate());
        dto.setImagePath(announcement.getImagePath());
        return dto;
    }

    public Announcement toEntity(AnnouncementDTO dto) {
        if (dto == null) {
            return null;
        }
        Announcement announcement = new Announcement();
        announcement.setId(dto.getId());
        announcement.setSubject(dto.getSubject());
        announcement.setContent(dto.getContent());
        announcement.setValidityDate(dto.getValidityDate());
        announcement.setImagePath(dto.getImagePath());
        return announcement;
    }


    public Announcement toEntityForCreate(AnnouncementDTO dto) {
        if (dto == null) {
            return null;
        }
        Announcement announcement = new Announcement();
        announcement.setSubject(dto.getSubject());
        announcement.setContent(dto.getContent());
        announcement.setValidityDate(dto.getValidityDate());
        announcement.setImagePath(dto.getImagePath());
        return announcement;
    }


    public void updateEntityFromDTO(AnnouncementDTO dto, Announcement announcement) {
        if (dto != null && announcement != null) {
            announcement.setSubject(dto.getSubject());
            announcement.setContent(dto.getContent());
            announcement.setValidityDate(dto.getValidityDate());

        }
    }
}
