package lms.com.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> {
    private List<T> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;

    public static <T> PageDTO<T> of(Page<T> page) {
        PageDTO<T> dto = new PageDTO<>();
        dto.setContent(page.getContent());
        dto.setCurrentPage(page.getNumber());
        dto.setTotalPages(page.getTotalPages());
        dto.setTotalElements(page.getTotalElements());
        return dto;
    }
}
