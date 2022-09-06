package com.rabindra.bookmarket.service;

import com.rabindra.bookmarket.model.Book;

import java.util.List;

/**
 * @author Rabindra
 * @date 2022
 */
public interface IBookService
{
    Book saveBook(Book book);

    void deleteBook(Long id);

    List<Book> findAllBooks();
}
