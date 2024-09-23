
package com.cmvbilisim.contextmanager.service;

import com.cmvbilisim.contextmanager.dto.NewsDTO;

import java.util.List;
import java.util.Optional;

public interface NewsService {
    List<NewsDTO> getAllNews();
    Optional<NewsDTO> getNewsById(Long id);
    NewsDTO saveNews(NewsDTO newsDTO);
    NewsDTO updateNews(Long id, NewsDTO newsDTO);
    void deleteNews(Long id);
    List<NewsDTO> getValidNews();
}
