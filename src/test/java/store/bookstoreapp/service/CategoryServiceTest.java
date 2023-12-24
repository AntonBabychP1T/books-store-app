package store.bookstoreapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    private static final Long INVALID_ID = 100L;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CategoryRepository categoryRepository;

    private Category validCategory;
    private CategoryDto validCategoryDto;
    private CategoryRequestDto validCategoryRequestDto;

    @BeforeEach
    void setUp() {
        validCategory = createValidCategory();
        validCategoryDto = createValidCategoryDto(validCategory);
        validCategoryRequestDto = createValidCategoryRequestDto();
    }

    private Category createValidCategory() {
        Category category = new Category();
        category.setId(VALID_ID);
        category.setName(VALID_NAME);
        category.setDescription(VALID_DESCRIPTION);
        return category;
    }

    private CategoryDto createValidCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName(), category.getDescription());
    }

    private CategoryRequestDto createValidCategoryRequestDto() {
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName(VALID_NAME);
        dto.setDescription(VALID_DESCRIPTION);
        return dto;
    }

    @Test
    @DisplayName("save() should return CategoryDto for valid request")
    public void save_WithValidCategoryRequestDto_ShouldReturnCategoryDto() {
        when(categoryMapper.toModel(validCategoryRequestDto)).thenReturn(validCategory);
        when(categoryRepository.save(validCategory)).thenReturn(validCategory);
        when(categoryMapper.toDto(validCategory)).thenReturn(validCategoryDto);

        CategoryDto savedCategory = categoryService.save(validCategoryRequestDto);

        assertThat(savedCategory).isEqualTo(validCategoryDto);
    }

    @Test
    @DisplayName("findAll() should return all categories")
    public void findAll_WithValidPageable_ShouldReturnAllCategories() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(List.of(validCategory), pageable, 1);
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(validCategory)).thenReturn(validCategoryDto);

        List<CategoryDto> categoryDtos = categoryService.findAll(pageable);

        assertThat(categoryDtos).hasSize(1);
        assertThat(categoryDtos.get(0)).isEqualTo(validCategoryDto);
    }

    @Test
    @DisplayName("delete() should remove category with valid ID")
    public void delete_WithValidId_ShouldRemoveCategory() {
        categoryService.delete(VALID_ID);

        verify(categoryRepository).deleteById(VALID_ID);
    }

    @Test
    @DisplayName("getById() should return CategoryDto for valid ID")
    public void getById_WithValidId_ShouldReturnCategoryDto() {
        when(categoryRepository.findById(VALID_ID)).thenReturn(Optional.of(validCategory));
        when(categoryMapper.toDto(validCategory)).thenReturn(validCategoryDto);

        CategoryDto categoryById = categoryService.getById(VALID_ID);

        assertThat(categoryById).isEqualTo(validCategoryDto);
    }

    @Test
    @DisplayName("getById() should throw EntityNotFoundException for invalid ID")
    public void getById_WithInvalidId_ShouldThrowException() {
        when(categoryRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.getById(INVALID_ID)
        );

        Assertions.assertEquals("Can't find category with id "
                + INVALID_ID, exception.getMessage());
    }

    @Test
    @DisplayName("update() should return updated CategoryDto for valid ID")
    public void update_WithValidIdAndCategoryDto_ShouldReturnUpdatedCategoryDto() {
        Category updatedCategory = createValidCategory();
        updatedCategory.setName("New Valid Name");

        when(categoryRepository.findById(VALID_ID)).thenReturn(Optional.of(validCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory))
                .thenReturn(createValidCategoryDto(updatedCategory));

        CategoryDto updatedCategoryDto = categoryService.update(VALID_ID, validCategoryRequestDto);

        verify(categoryMapper).updateCategoryFromDto(validCategoryRequestDto, validCategory);
        assertThat(updatedCategoryDto).isEqualTo(createValidCategoryDto(updatedCategory));
    }

    @Test
    @DisplayName("update() should throw EntityNotFoundException for invalid ID")
    public void update_WithInvalidId_ShouldThrowException() {
        when(categoryRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.update(INVALID_ID, validCategoryRequestDto)
        );

        Assertions.assertEquals("Can't find category with id "
                + INVALID_ID, exception.getMessage());
    }
}
