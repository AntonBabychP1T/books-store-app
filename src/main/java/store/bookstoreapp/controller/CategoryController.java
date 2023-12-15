package store.bookstoreapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import store.bookstoreapp.dto.book.BookDtoWithoutCategoryIds;
import store.bookstoreapp.dto.category.CategoryDto;
import store.bookstoreapp.dto.category.CategoryRequestDto;
import store.bookstoreapp.service.BookService;
import store.bookstoreapp.service.CategoryService;

@Tag(name = "Category management", description = "Endpoints for managing categories")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @Operation(summary = "Create a new category", description = "Create a new category")
    @Parameter(name = "name", description = "name of the new category",
            required = true, example = "New name")
    @Parameter(name = "description", description = "some extra information about category")
    public CategoryDto createCategory(@RequestBody @Valid CategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @GetMapping
    @Operation(
            summary = "Get all categories",
            description = "Get a list of all not deleted categories"
    )
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by id", description = "Get a single category by id key")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Soft delete category from schema")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping
    @Operation(
            summary = "Update category by id",
            description = "Update specific category by id key"
    )
    public CategoryDto updateCategory(Long id, CategoryRequestDto requestDto) {
        return categoryService.update(id, requestDto);
    }

    @GetMapping("/{id}/books")
    @Operation(
            summary = "Get all book by category id",
            description = "Get all book which related with some specified category"
    )
    public List<BookDtoWithoutCategoryIds> getBookByCategoryId(
            @PathVariable Long id,
            Pageable pageable
    ) {
        return bookService.getBookByCategoryId(id, pageable);
    }
}
