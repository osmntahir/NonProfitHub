package com.cmvbilisim.contextmanager.service;

import com.cmvbilisim.contextmanager.dto.NewsDTO;
import com.cmvbilisim.contextmanager.mapper.NewsMapper;
import com.cmvbilisim.contextmanager.model.News;
import com.cmvbilisim.contextmanager.repository.NewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {

    private static final Logger logger = LoggerFactory.getLogger(NewsServiceImpl.class);

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository, NewsMapper newsMapper) {
        this.newsRepository = newsRepository;
        this.newsMapper = newsMapper;
    }

    @Override
    public List<NewsDTO> getAllNews() {
        logger.info("Fetching all news");
        List<NewsDTO> newsList = newsRepository.findAll().stream()
                .map(newsMapper::toDTO)
                .collect(Collectors.toList());
        logger.debug("Fetched {} news items", newsList.size());
        return newsList;
    }

    @Override
    public Optional<NewsDTO> getNewsById(Long id) {
        logger.info("Fetching news with ID {}", id);
        Optional<NewsDTO> newsDTO = newsRepository.findById(id)
                .map(newsMapper::toDTO);
        if (newsDTO.isPresent()) {
            logger.debug("News found: {}", newsDTO.get().getSubject());
        } else {
            logger.warn("News not found with ID {}", id);
        }
        return newsDTO;
    }

    @Override
    public NewsDTO saveNews(NewsDTO newsDTO) {
        logger.info("Saving new news: {}", newsDTO.getSubject());
        validateNews(newsDTO);

        News news = newsMapper.toEntityForCreate(newsDTO);
        logger.debug("Converted NewsDTO to News entity");

        News savedNews = newsRepository.save(news);
        logger.info("News saved with ID {}", savedNews.getId());

        savedNews.setNewsLink("/news/" + savedNews.getId());
        News updatedNews = newsRepository.save(savedNews);
        logger.info("News link updated for ID {}", updatedNews.getId());

        return newsMapper.toDTO(updatedNews);
    }

    @Override
    public NewsDTO updateNews(Long id, NewsDTO newsDTO) {
        logger.info("Updating news with ID {}", id);
        News existingNews = newsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("News not found with ID {}", id);
                    return new IllegalArgumentException("Belirtilen ID ile bir haber bulunamadı: " + id);
                });

        validateNews(newsDTO);
        newsMapper.updateEntityFromDTO(newsDTO, existingNews);
        logger.debug("Updated News entity from NewsDTO");

        existingNews.setNewsLink("/news/" + existingNews.getId());
        logger.debug("News link set to {}", existingNews.getNewsLink());

        News updatedNews = newsRepository.save(existingNews);
        logger.info("News with ID {} updated successfully", id);
        return newsMapper.toDTO(updatedNews);
    }

    @Override
    public void deleteNews(Long id) {
        logger.info("Deleting news with ID {}", id);
        if (!newsRepository.existsById(id)) {
            logger.warn("News not found with ID {}", id);
            throw new IllegalArgumentException("Belirtilen ID ile bir haber bulunamadı: " + id);
        }
        newsRepository.deleteById(id);
        logger.info("News with ID {} deleted successfully", id);
    }

    @Override
    public List<NewsDTO> getValidNews() {
        logger.info("Fetching valid news (validity date >= today)");
        LocalDate today = LocalDate.now();
        List<NewsDTO> validNews = newsRepository.findByValidityDateGreaterThanEqual(today).stream()
                .map(newsMapper::toDTO)
                .collect(Collectors.toList());
        logger.debug("Fetched {} valid news items", validNews.size());
        return validNews;
    }

    private void validateNews(NewsDTO newsDTO) {
        logger.debug("Validating news DTO: {}", newsDTO.getSubject());
        if (newsDTO.getSubject() == null || newsDTO.getSubject().isEmpty()) {
            logger.error("Validation failed: Subject is required");
            throw new ConstraintViolationException("Konu gereklidir", null);
        }
        if (newsDTO.getContent() == null || newsDTO.getContent().isEmpty()) {
            logger.error("Validation failed: Content is required");
            throw new ConstraintViolationException("İçerik gereklidir", null);
        }
        if (newsDTO.getValidityDate() == null || newsDTO.getValidityDate().isBefore(LocalDate.now())) {
            logger.error("Validation failed: Validity date is required and must be today or a future date");
            throw new ConstraintViolationException("Geçerlilik tarihi bugün veya gelecekteki bir gün olmalıdır", null);
        }
    }
}
