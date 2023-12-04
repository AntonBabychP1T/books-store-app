package store.bookstoreapp.search;

public record BookSearchParameters(String[] titles, String[] authors, String[] isbns) {
}
