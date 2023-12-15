package store.bookstoreapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import store.bookstoreapp.config.MapperConfig;
import store.bookstoreapp.dto.category.CategoryDto;
import store.bookstoreapp.dto.category.CategoryRequestDto;
import store.bookstoreapp.model.Category;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    void updateCategoryFromDto(CategoryRequestDto requestDto, @MappingTarget Category category);

    Category toModel(CategoryRequestDto requestDto);
}
