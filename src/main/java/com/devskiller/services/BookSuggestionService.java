package com.devskiller.services;

import com.devskiller.model.Author;
import com.devskiller.model.Book;
import com.devskiller.model.Reader;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class BookSuggestionService {

	private final Set<Book> books;
	private final Set<Reader> readers;

	public BookSuggestionService(Set<Book> books, Set<Reader> readers) {
		this.books = books;
		this.readers = readers;
	}

	// To be reused by suggestBooks(Reader reader) and suggestBooks(Reader reader, int rating), since they share the same logic, except for the rating filter.
	private Set<String> filterBooks(Reader reader, Optional<Integer> rating, Optional<Author> author) {
		return books.stream()
				.filter(book -> rating.map(r -> book.rating() == r).orElse(book.rating() >= 4)) // Filter by rating
				.filter(book -> reader.favouriteGenres().contains(book.genre())) // Filter by favorite genres
				.filter(book -> readers.stream()
						.filter(r -> r != reader) // Different reader
						.filter(r -> r.age() == reader.age()) // Same age
						.anyMatch(r -> r.favouriteBooks().contains(book))) // Has book in favorites
				.filter(book -> author.map(a -> book.author().equals(a)).orElse(true)) // Filter by author if present
				.map(Book::title)
				.collect(Collectors.toSet());
	}

	Set<String> suggestBooks(Reader reader) {
		return filterBooks(reader, Optional.empty(), Optional.empty());
	}

	Set<String> suggestBooks(Reader reader, int rating) {
		return filterBooks(reader, Optional.of(rating), Optional.empty());
	}

	Set<String> suggestBooks(Reader reader, Author author) {
		return filterBooks(reader, Optional.empty(), Optional.of(author));
	}

}
