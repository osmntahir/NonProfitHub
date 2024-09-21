package com.cmvbilisim.contextmanager.service;

import com.cmvbilisim.contextmanager.model.News;
import com.cmvbilisim.contextmanager.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    @Override
    public Optional<News> getNewsById(Long id) {
        return newsRepository.findById(id);
    }

    @Override
    public News saveNews(News news) {
        News savedNews = newsRepository.save(news);
        String newsLink = "/news/" + savedNews.getId();
        savedNews.setNewsLink(newsLink);
        return newsRepository.save(news);
    }

    @Override
    public News updateNews(Long id, News news) {

        News existingNews = newsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("News not found with id: " + id));


        validateNews(news);

        // Update the properties of the existing news
        existingNews.setSubject(news.getSubject());
        existingNews.setContent(news.getContent());
        existingNews.setValidityDate(news.getValidityDate());


        return newsRepository.save(existingNews);
    }

    private void validateNews(News news) {
        if (news.getSubject() == null || news.getSubject().isEmpty()) {
            throw new ConstraintViolationException("Konu gereklidir", null);
        }
        if (news.getContent() == null || news.getContent().isEmpty()) {
            throw new ConstraintViolationException("Icerik gereklidir", null);
        }
        if (news.getValidityDate() == null || news.getValidityDate().isBefore(LocalDate.now())) {
            throw new ConstraintViolationException("Gecerlilik tarihi bugun veya gelecekteki bir gun olmalidir", null);
        }
    }
    @Override
    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }

    @Override
    public List<News> getValidNews() {
        LocalDate today = LocalDate.now();
        return newsRepository.findByValidityDateGreaterThanEqual(today);
    }
}
