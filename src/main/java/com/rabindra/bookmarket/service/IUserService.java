package com.rabindra.bookmarket.service;

import com.rabindra.bookmarket.model.User;

import java.util.Optional;

/**
 * @author Rabindra
 * @date 6.09.2022
 */
public interface IUserService
{
    User saveUser(User user);

    Optional<User> findByUsername(String username);

    void makeAdmin(String username);
}
