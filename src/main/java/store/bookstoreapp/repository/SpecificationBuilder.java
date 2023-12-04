package store.bookstoreapp.repository;

import org.springframework.data.jpa.domain.Specification;
import store.bookstoreapp.search.BookSearchParameters;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParameters searchParametrs);
}
