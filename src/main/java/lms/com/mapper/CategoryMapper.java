package lms.com.mapper;

import lms.com.dtos.CategoryDTO;
import lms.com.entity.Category;
public class CategoryMapper {

    public static Category dtoToEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }

        return Category.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }

    public static CategoryDTO entityToDto(Category entity) {
        if (entity == null) {
            return null;
        }

        return CategoryDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
