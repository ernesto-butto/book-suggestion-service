package com.devskiller.services;

import com.devskiller.model.Author;
import com.devskiller.model.Book;
import com.devskiller.model.Reader;

import java.util.Set;
import java.util.stream.Collectors;

class BookSuggestionService {

	private final Set<Book> books;
	private final Set<Reader> readers;

	public BookSuggestionService(Set<Book> books, Set<Reader> readers) {
		this.books = books;
		this.readers = readers;
	}

	Set<String> suggestBooks(Reader reader) {
		return books.stream()
				.filter(book -> book.rating() >= 4) // Filter books with rating >= 4
				.filter(book -> reader.favouriteGenres().contains(book.genre())) // Filter books that contains reader's favorite genres
				.filter(book -> readers.stream()
						.filter(r -> r != reader) // Different reader
						.filter(r -> r.age() == reader.age()) // Same age
						.anyMatch(r -> r.favouriteBooks().contains(book))) // Has book in favorites
				.map(Book::title)
				.collect(Collectors.toSet());
	}

	Set<String> suggestBooks(Reader reader, int rating) {
		throw new UnsupportedOperationException(/*TODO*/);
	}

	Set<String> suggestBooks(Reader reader, Author author) {
		return books.stream()
				.filter(book -> book.rating() >= 4) // Filter books with rating >= 4
				.filter(book -> reader.favouriteGenres().contains(book.genre())) // Filter books matching reader's favorite genres
				.filter(book -> readers.stream()
						.filter(r -> r != reader) // Different reader
						.filter(r -> r.age() == reader.age()) // Same age
						.anyMatch(r -> r.favouriteBooks().contains(book))) // Has book in favorites
				.filter(book -> book.author().equals(author)) // Filter books by the specified author
				.map(Book::title)
				.collect(Collectors.toSet());
	}

}
