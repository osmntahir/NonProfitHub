package com.cmvbilisim.contextmanager.controller;

import com.cmvbilisim.contextmanager.model.Announcement;
import com.cmvbilisim.contextmanager.service.AnnouncementService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping
    public ResponseEntity<List<Announcement>> getAllAnnouncements() {
        return ResponseEntity.ok(announcementService.getAllAnnouncements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Announcement> getAnnouncementById(@PathVariable Long id) {
        return announcementService.getAnnouncementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Announcement> addAnnouncement(
            @RequestPart("announcement") Announcement announcement,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        try {
            Announcement savedAnnouncement = announcementService.saveAnnouncementWithImage(announcement, image);
            return ResponseEntity.ok(savedAnnouncement);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Announcement> updateAnnouncement(
            @PathVariable Long id,
            @RequestPart("announcement") Announcement announcement,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        try {
            Announcement updatedAnnouncement = announcementService.updateAnnouncementWithImage(id, announcement, image);
            return ResponseEntity.ok(updatedAnnouncement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable Long id) {
        try {
            announcementService.deleteAnnouncement(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).build();
        }
    }

}
