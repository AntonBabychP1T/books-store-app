package store.bookstoreapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import store.bookstoreapp.config.MapperConfig;
import store.bookstoreapp.dto.order.OrderDto;
import store.bookstoreapp.model.Order;

@Mapper(config = MapperConfig.class, uses = {OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderItems", target = "orderItems")
    OrderDto toDto(Order order);
}
