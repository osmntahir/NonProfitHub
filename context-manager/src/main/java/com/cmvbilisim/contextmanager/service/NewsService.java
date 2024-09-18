package com.cmvbilisim.contextmanager.service;

import com.cmvbilisim.contextmanager.model.News;

import java.util.List;
import java.util.Optional;

public interface NewsService {
    List<News> getAllNews();

    Optional<News> getNewsById(Long id);

    News saveNews(News news);

    News updateNews(Long id, News news);

    void deleteNews(Long id);
}
