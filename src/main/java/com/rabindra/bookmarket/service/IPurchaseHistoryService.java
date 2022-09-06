package com.rabindra.bookmarket.service;

import com.rabindra.bookmarket.model.PurchaseHistory;
import com.rabindra.bookmarket.repository.projection.IPurchaseItem;

import java.util.List;

/**
 * @author Rabindra
 * @date 6.09.2022
 */
public interface IPurchaseHistoryService
{
    PurchaseHistory savePurchaseHistory(PurchaseHistory purchaseHistory);

    List<IPurchaseItem> findPurchasedItemsOfUser(Long userId);
}
