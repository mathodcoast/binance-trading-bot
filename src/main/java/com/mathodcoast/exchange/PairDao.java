package com.mathodcoast.exchange;

import com.mathodcoast.model.Pair;

import java.util.Map;

public interface PairDao {

    Long buyLimitOrder(double price,double quantity);

    Long sellLimitOrder(double price, double quantity);

    Long sellStopLimitOrder(double stop, double price, double quantity);

    Pair fetchPairInfo();

    Map<String, Pair> fetchAllPairsInfo();

    String getOrderStatus(Long orderID);

    void cancelOrder(long stopLossId);

    Double getLatestPrice();

}
