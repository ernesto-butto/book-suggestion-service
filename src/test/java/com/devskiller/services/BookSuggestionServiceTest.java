package com.devskiller.services;

import com.devskiller.model.Author;
import com.devskiller.model.Book;
import com.devskiller.model.Reader;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static com.devskiller.model.Genre.*;
import static com.google.common.collect.Sets.newHashSet;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;

public class BookSuggestionServiceTest {

	private Author author1 = new Author(randomAlphabetic(8), randomAlphabetic(10));
	private Author author2 = new Author(randomAlphabetic(8), randomAlphabetic(10));
	private Author author3 = new Author(randomAlphabetic(8), randomAlphabetic(10));
	private Author author4 = new Author(randomAlphabetic(8), randomAlphabetic(10));
	private Author author5 = new Author(randomAlphabetic(8), randomAlphabetic(10));
	private Author author6 = new Author(randomAlphabetic(8), randomAlphabetic(10));
	private Book book1 = new Book(author1, randomAlphabetic(15), randomAlphabetic(10), HORROR, 5);
	private Book book2 = new Book(author1, randomAlphabetic(15), randomAlphabetic(10), HORROR, 4);
	private Book book3 = new Book(author2, randomAlphabetic(15), randomAlphabetic(10), HORROR, 3);
	private Book book4 = new Book(author3, randomAlphabetic(15), randomAlphabetic(10), ROMANTIC, 5);
	private Book book5 = new Book(author4, randomAlphabetic(15), randomAlphabetic(10), HORROR, 5);
	private Book book6 = new Book(author5, randomAlphabetic(15), randomAlphabetic(10), DRAMA, 5);
	private Book book7 = new Book(author1, randomAlphabetic(15), randomAlphabetic(10), HORROR, 3);
	private Book book8 = new Book(author1, randomAlphabetic(15), randomAlphabetic(10), FICTION, 4);
	private Book book9 = new Book(author1, randomAlphabetic(15), randomAlphabetic(10), HORROR, 4);
	private Book book10 = new Book(author6, randomAlphabetic(15), randomAlphabetic(10), FICTION, 4);
	private int randomAge1 = nextInt(0, 120);
	private int randomAge2 = nextInt(0, 120);
	private Reader reader1 = new Reader(randomAge1);
	private Reader reader2 = new Reader(randomAge1);
	private Reader reader3 = new Reader(randomAge2);
	private BookSuggestionService suggestionService;

	@Before
	public void setUp() {
		reader1.addToFavourites(HORROR);
		reader1.addToFavourites(ROMANTIC);
		reader2.addToFavourites(book2);
		reader2.addToFavourites(book1);
		reader2.addToFavourites(book3);
		reader2.addToFavourites(book4);
		reader2.addToFavourites(book7);
		reader2.addToFavourites(book8);
		reader2.addToFavourites(book10);
		reader3.addToFavourites(book5);
		Set<Book> books = newHashSet(book1, book2, book3, book4, book5, book6, book7, book8, book9, book10);
		Set<Reader> readers = newHashSet(reader1, reader2, reader3);
		suggestionService = new BookSuggestionService(books, readers);
	}

	// QUESTION 1 TESTS: suggestBooks(Reader reader)
	@Test
	public void shouldSuggestBookTitlesWithDefaultRatingOfFourOrHigher() {
		// when:
		Set<String> suggestedBooks = suggestionService.suggestBooks(reader1);

		// then:
		assertThat(suggestedBooks).isEqualTo(
				newHashSet(
						book1.title(),
						book2.title(),
						book4.title()
					)
				);

		// Notes for interview:
		// Requirements:
		// 1. Have a rating of four or higher
		// 2. Belong to one of the reader's favourite genres
		// 3. Belong to favourite books' list of at least one different reader of the same age

		// Explanation
		// Reader 1 and 2 are of the same age, reader 3 is of different age
		// Reader 2 has books 1, 2 and 4 in favorites, they are rating 5, 4 and 5 respectively, above or equal to 4

	}

	// Note for interview: Important because it tests the case when the reader has no favorite books, tests common edge case, and defensive programming practices
	@Test
	public void shouldNotSuggestBooksWhenNoFavoriteGenres() {
		// given:
		Reader newReader = new Reader(randomAge1);
		// No genres added to favorites

		// when:
		Set<String> suggestedBooks = suggestionService.suggestBooks(newReader);

		// then:
		assertThat(suggestedBooks).isEmpty();
	}

	@Test
	public void shouldNotSuggestBooksWithLowRating() {
		// given:
		Reader newReader = new Reader(randomAge1);
		newReader.addToFavourites(HORROR);

		// when:
		Set<String> suggestedBooks = suggestionService.suggestBooks(newReader);

		// then:
		assertThat(suggestedBooks).doesNotContain(book3.title(), book7.title());  // both are HORROR books with rating 3
	}

	@Test
	public void shouldNotSuggestBooksNotInOtherReadersFavorites() {
		// given:
		Reader newReader = new Reader(randomAge1);
		newReader.addToFavourites(HORROR);

		// when:
		Set<String> suggestedBooks = suggestionService.suggestBooks(newReader);

		// then:
		assertThat(suggestedBooks).doesNotContain(book9.title());  // HORROR book with rating 4 but not in other readers' favorites
	}

	// QUESTION 2 TESTS: suggestBooks(Reader reader, Author author)
	@Test
	public void shouldOnlySuggestBookTitlesOfGivenAuthor() {
		// when:
		Set<String> suggestedBooks = suggestionService.suggestBooks(reader1, author1);

		// then:
		assertThat(suggestedBooks).isEqualTo(newHashSet(book1.title(), book2.title()));
	}

	@Test
	public void shouldReturnEmptySetWhenAuthorHasNoBooksInGenre() {
		// given:
		Reader reader = new Reader(randomAge1);
		reader.addToFavourites(ROMANTIC); // author1 has no ROMANTIC books with rating >= 4

		// when:
		Set<String> suggestedBooks = suggestionService.suggestBooks(reader, author1);

		// then:
		assertThat(suggestedBooks)
				.as("Should return empty set when author has no books in reader's favorite genre")
				.isEmpty();
	}

	// QUESTION 3 TESTS: suggestBooks(Reader reader, int rating)
	@Test
	public void shouldSuggestBookTitlesWithCorrectRating() {
		// when:
		Set<String> suggestedBooks = suggestionService.suggestBooks(reader1, 4);

		// then:
		assertThat(suggestedBooks).isEqualTo(newHashSet(book2.title()));
	}

}
