package com.cmvbilisim.contextmanager.service;

import com.cmvbilisim.contextmanager.model.News;
import com.cmvbilisim.contextmanager.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return newsRepository.save(news);
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
