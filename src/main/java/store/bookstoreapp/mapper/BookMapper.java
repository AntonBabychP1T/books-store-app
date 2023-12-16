package store.bookstoreapp.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import store.bookstoreapp.config.MapperConfig;
import store.bookstoreapp.dto.book.BookDto;
import store.bookstoreapp.dto.book.BookDtoWithoutCategoryIds;
import store.bookstoreapp.dto.book.CreateBookRequestDto;
import store.bookstoreapp.model.Book;
import store.bookstoreapp.model.Category;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    @Mapping(target = "categoryIds", ignore = true)
    BookDto toDto(Book book);

    @Mapping(target = "categories", ignore = true)
    Book toModel(CreateBookRequestDto bookRequestDto);

    @Mapping(target = "categories", ignore = true)
    Book toModel(BookDto bookDto);

    void updateBookFromDto(CreateBookRequestDto requestDto, @MappingTarget Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        Set<Long> categoryIds = book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        bookDto.setCategoryIds(categoryIds);
    }
}
