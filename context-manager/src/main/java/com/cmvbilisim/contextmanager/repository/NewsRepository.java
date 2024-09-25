package com.cmvbilisim.contextmanager.repository;

import com.cmvbilisim.contextmanager.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

     List<News> findByValidityDateGreaterThanEqual(LocalDate today);

     Optional<News> findByIdAndValidityDateGreaterThanEqual(Long id, LocalDate today);
}
