package store.bookstoreapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import store.bookstoreapp.dto.category.CategoryDto;
import store.bookstoreapp.dto.category.CategoryRequestDto;
import store.bookstoreapp.exception.EntityNotFoundException;
import store.bookstoreapp.mapper.CategoryMapper;
import store.bookstoreapp.model.Category;
import store.bookstoreapp.repository.category.CategoryRepository;
import store.bookstoreapp.service.impl.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    private static final String VALID_NAME = "Valid name";
    private static final String VALID_DESCRIPTION = "Valid description";
    private static final Long VALID_ID = 1L;
    private static final Long NOT_VALID_ID = 100L;

    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Verify save() method works")
    public void save_ValidCategoryRequestDto_ReturnCategoryDto() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName(VALID_NAME);
        categoryRequestDto.setDescription(VALID_DESCRIPTION);
        Category category = new Category();
        category.setName(categoryRequestDto.getName());
        category.setDescription(categoryRequestDto.getDescription());
        category.setId(VALID_ID);
        CategoryDto categoryDto = new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
        when(categoryMapper.toModel(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto savedCategory = categoryService.save(categoryRequestDto);

        assertThat(savedCategory).isEqualTo(categoryDto);
    }

    @Test
    @DisplayName("Verify findAll() method works")
    public void findAll_ValidPageable_ReturnAllCategories() {
        Category category = new Category();
        category.setName(VALID_NAME);
        category.setDescription(VALID_DESCRIPTION);
        category.setId(VALID_ID);

        CategoryDto categoryDto = new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );

        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = List.of(category);
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, categories.size());
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        List<CategoryDto> categoryDtos = categoryService.findAll(pageable);

        assertThat(categoryDtos).hasSize(1);
        assertThat(categoryDtos.get(0)).isEqualTo(categoryDto);
    }

    @Test
    @DisplayName("Verify deleteById method works")
    public void delete_ValidId_NotValueInList() {
        categoryService.delete(VALID_ID);
        verify(categoryRepository).deleteById(VALID_ID);
    }

    @Test
    @DisplayName("Verify cetById() method works")
    public void getById_ValidId_ReturnCategoryDto() {
        Category category = new Category();
        category.setName(VALID_NAME);
        category.setDescription(VALID_DESCRIPTION);
        category.setId(VALID_ID);

        CategoryDto categoryDto = new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );

        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        when(categoryRepository.findById(VALID_ID)).thenReturn(Optional.of(category));

        CategoryDto categoryById = categoryService.getById(VALID_ID);

        assertThat(categoryById).isEqualTo(categoryDto);

    }

    @Test
    @DisplayName("Verify findBookById() method works")
    public void getById_NotValidId_ThrowException() {
        when(categoryRepository.findById(NOT_VALID_ID)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(NOT_VALID_ID));

        String expectedMessage = "Can't find category with id " + NOT_VALID_ID;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    @DisplayName("Verify update() method works")
    public void update_ValidIdAndCategoryDto_ReturnCategoryDto() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName(VALID_NAME);
        categoryRequestDto.setDescription(VALID_DESCRIPTION);
        Category category = new Category();
        category.setName("New Valid Name");
        category.setDescription(categoryRequestDto.getDescription());
        category.setId(VALID_ID);
        CategoryDto categoryDto = new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
        when(categoryRepository.findById(VALID_ID)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto updateCategory = categoryService.update(VALID_ID, categoryRequestDto);

        verify(categoryMapper).updateCategoryFromDto(categoryRequestDto, category);
        assertThat(updateCategory).isEqualTo(categoryDto);
    }

    @Test
    @DisplayName("Verify update() method works")
    public void update_NotValidId_ThrowException() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName(VALID_NAME);
        categoryRequestDto.setDescription(VALID_DESCRIPTION);

        when(categoryRepository.findById(NOT_VALID_ID)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(NOT_VALID_ID, categoryRequestDto));

        String expectedMessage = "Can't find category with id " + NOT_VALID_ID;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
