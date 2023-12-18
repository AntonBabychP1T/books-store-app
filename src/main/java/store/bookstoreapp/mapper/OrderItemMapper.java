package store.bookstoreapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import store.bookstoreapp.config.MapperConfig;
import store.bookstoreapp.dto.orderitem.OrderItemDto;
import store.bookstoreapp.model.OrderItem;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(OrderItem orderItem);
}
