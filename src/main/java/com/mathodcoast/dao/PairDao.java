package com.mathodcoast.dao;

import com.mathodcoast.model.Pair;

import java.util.Map;

public interface PairDao {


    Long buyLimitOrder(Pair pair,double price,double quantity);

    Long sellLimitOrder(Pair pair,double price, double quantity);

    Long sellStopLimitOrder(Pair pair, double stop, double price, double quantity);

    Pair fetchPairInfo(String pair);

    Map<String, Pair> fetchAllPairsInfo();

    String getOrderStatus(Pair pair, Long orderID);

    String getPairNameString(Pair pair);

    void cancelOrder(Pair pair, long stopLossId);

    Double getLatestPrice (Pair pair);

}
