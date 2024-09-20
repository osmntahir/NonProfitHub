package com.cmvbilisim.contextmanager.repository;

import com.cmvbilisim.contextmanager.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

     List<Announcement> findByValidityDateGreaterThanEqual(LocalDate today);
}
