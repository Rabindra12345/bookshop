package com.rabindra.bookmarket.repository.projection;

import java.time.LocalDateTime;

/**
 * @author Rabindra
 * @date 6.09.2022
 */
public interface IPurchaseItem
{
    String getTitle();
    Double getPrice();
    LocalDateTime getPurchaseTime();
}
