# BOOKS SUGGESTION SERVICE 

## Notes / Log By Ernesto (Developer) to Interviewer

At first, I implemented each question one by one, and added some test to check for common edge cases and defensive programming.

When I finished, I reviewed `BookSuggestionService.java` and noticed there was a enough of repeated logic to consider a refactor.

To reduce duplication and simplify testing, I first merged logic of `suggestBooks(Reader reader, int rating)` and `suggestBooks(Reader reader)` in a single book-filtering 
method, since these methods only differed by the rating criteria.

Then I decided to the `suggestBooks(Reader reader, Author author)` logic to the same method, since the only difference was the author criteria. 

- **Note**: I understand that  `Optional` as a parameter type, is generally discouraged, and it shows a warning in some linting systems.... 
I think that with a bit more time I could figure a way to avoid duplication and the warning.

## Introduction

The service suggests books to readers based on the book's ratings, reader's favourite genres, book's author and the 
preferences of other readers of the same age.

Your job is to fulfill all requirements, which are described below. There are several unit tests that will help you to verify your solution.
Please feel free to use any external dependencies by including them in `pom.xml` descriptor.

Add any additonal test that you deem appropriate.

## Problem Statement

Your job is to correctly implement all the methods in the `BookSuggestionService` class, so that they meet the following requirements:

The method `Set<String> suggestBooks(Reader reader)` should return titles of all the  books that meet all the following criteria:
 1. Have a rating of four or higher
 2. Belong to one of the reader's favourite genres
 3. Belong to favourite books' list of at least one different reader of the same age

 The method `Set<String> suggestBooks(Reader reader, Author author)` should return titles of all the  books that meet all the following criteria:
  1. Have a rating of four or higher
  2. Belong to one of the reader's favourite genres
  3. Belong to favourite books' list of at least one different reader of the same age
  4. Have been written by the author specified in the method argument
  
 The method `Set<String> suggestBooks(Reader reader, int rating)` should return titles of all the  books that meet all the following criteria:
   1. Have exactly the rating specified in the method argument
   2. Belong to one of the reader's favourite genres
   3. Belong to favourite books' list of at least one different reader of the same age
 
 ## Build instructions
 You can build the project locally. Simply execute the following commands:

`mvn clean test`
 
