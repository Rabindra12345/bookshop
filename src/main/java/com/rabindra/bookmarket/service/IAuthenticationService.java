package com.rabindra.bookmarket.service;

import com.rabindra.bookmarket.model.User;

/**
 * @author Rabindra
 * @date 2022
 */
public interface IAuthenticationService
{
    User signInAndReturnJWT(User signInRequest);
}
