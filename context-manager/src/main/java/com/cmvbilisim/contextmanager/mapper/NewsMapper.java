
package com.cmvbilisim.contextmanager.mapper;

import com.cmvbilisim.contextmanager.dto.NewsDTO;
import com.cmvbilisim.contextmanager.model.News;
import org.springframework.stereotype.Component;

@Component
public class NewsMapper {

    public NewsDTO toDTO(News news) {
        if (news == null) {
            return null;
        }
        NewsDTO dto = new NewsDTO();
        dto.setId(news.getId());
        dto.setSubject(news.getSubject());
        dto.setContent(news.getContent());
        dto.setValidityDate(news.getValidityDate());
        dto.setNewsLink(news.getNewsLink());
        return dto;
    }

    public News toEntity(NewsDTO dto) {
        if (dto == null) {
            return null;
        }
        News news = new News();
        news.setId(dto.getId());
        news.setSubject(dto.getSubject());
        news.setContent(dto.getContent());
        news.setValidityDate(dto.getValidityDate());
        news.setNewsLink(dto.getNewsLink());
        return news;
    }


    public News toEntityForCreate(NewsDTO dto) {
        if (dto == null) {
            return null;
        }
        News news = new News();
        news.setSubject(dto.getSubject());
        news.setContent(dto.getContent());
        news.setValidityDate(dto.getValidityDate());

        return news;
    }


    public void updateEntityFromDTO(NewsDTO dto, News news) {
        if (dto != null && news != null) {
            news.setSubject(dto.getSubject());
            news.setContent(dto.getContent());
            news.setValidityDate(dto.getValidityDate());

        }
    }
}
