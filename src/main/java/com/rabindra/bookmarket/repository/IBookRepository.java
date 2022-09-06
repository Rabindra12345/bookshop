package com.rabindra.bookmarket.repository;

import com.rabindra.bookmarket.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rabindra
 * @date 6.09.2022
 */
public interface IBookRepository extends JpaRepository<Book, Long>
{
}
