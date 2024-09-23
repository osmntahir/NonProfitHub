
package com.cmvbilisim.contextmanager.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AnnouncementDTO {
    private Long id;
    private String subject;
    private String content;
    private LocalDate validityDate;
    private String imagePath;
}