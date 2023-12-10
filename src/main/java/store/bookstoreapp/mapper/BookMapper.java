package store.bookstoreapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import store.bookstoreapp.config.MapperConfig;
import store.bookstoreapp.dto.book.BookDto;
import store.bookstoreapp.dto.book.CreateBookRequestDto;
import store.bookstoreapp.model.Book;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto bookRequestDto);

    void updateBookFromDto(CreateBookRequestDto requestDto, @MappingTarget Book book);
}
