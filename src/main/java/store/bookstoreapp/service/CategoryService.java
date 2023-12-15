package store.bookstoreapp.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import store.bookstoreapp.dto.category.CategoryDto;
import store.bookstoreapp.dto.category.CategoryRequestDto;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryRequestDto requestDto);

    CategoryDto update(Long id, CategoryRequestDto categoryDto);

    void delete(Long id);

}
