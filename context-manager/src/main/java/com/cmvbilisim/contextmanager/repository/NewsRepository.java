package com.cmvbilisim.contextmanager.repository;

import com.cmvbilisim.contextmanager.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

     List<News> findByValidityDateGreaterThanEqual(LocalDate today);
}
