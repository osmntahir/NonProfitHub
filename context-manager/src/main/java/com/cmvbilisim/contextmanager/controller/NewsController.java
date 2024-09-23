package com.cmvbilisim.contextmanager.controller;

import com.cmvbilisim.contextmanager.model.News;
import com.cmvbilisim.contextmanager.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<News>> getAllNews() {

        return ResponseEntity.ok(newsService.getAllNews());
    }

    @GetMapping("/valid")
    public ResponseEntity<List<News>> getValidNews() {
        List<News> news = newsService.getValidNews();
        return ResponseEntity.ok(news);
    }
    // anonymus
    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id) {
        return newsService.getNewsById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    //admin
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<News> createNews(@RequestBody News news) {
        return ResponseEntity.ok(newsService.saveNews(news));
    }
    //admin
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<News> updateNews(@PathVariable Long id, @RequestBody News news) {
        return ResponseEntity.ok(newsService.updateNews(id, news));
    }
    //admin
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }
}
