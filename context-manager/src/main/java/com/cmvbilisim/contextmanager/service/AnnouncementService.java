package com.cmvbilisim.contextmanager.service;


import com.cmvbilisim.contextmanager.model.Announcement;

import java.util.List;
import java.util.Optional;

public interface AnnouncementService {
    
    Announcement saveAnnouncement(Announcement announcement);

    Announcement updateAnnouncement(Long id, Announcement announcement);

    void deleteAnnouncement(Long id);


    Optional<Announcement> getAnnouncementById(Long id);

    List<Announcement> getAllAnnouncements();
}
