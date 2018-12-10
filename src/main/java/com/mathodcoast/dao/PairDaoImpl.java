package com.mathodcoast.dao;

import com.mathodcoast.model.Pair;
import com.mathodcoast.utillities.BinanceBotUtill;
import net.sealake.binance.api.client.BinanceApiRestClient;
import net.sealake.binance.api.client.domain.OrderSide;
import net.sealake.binance.api.client.domain.OrderType;
import net.sealake.binance.api.client.domain.TimeInForce;
import net.sealake.binance.api.client.domain.account.NewOrder;
import net.sealake.binance.api.client.domain.account.NewOrderResponse;
import net.sealake.binance.api.client.domain.account.Order;
import net.sealake.binance.api.client.domain.account.request.CancelOrderRequest;
import net.sealake.binance.api.client.domain.account.request.OrderStatusRequest;

import java.util.Locale;
import java.util.Map;

import static net.sealake.binance.api.client.domain.account.NewOrder.*;

public class PairDaoImpl implements PairDao {
    private BinanceApiRestClient client;
    private Pair pair;
    private int coinPriceLength;

    public PairDaoImpl(Pair pair) {
        client = BinanceBotUtill.client;
        this.pair = pair;
        coinPriceLength = 8;
    }

    @Override
    public Long buyLimitOrder(Pair pair,double price,double quantity) {

        String priceStr, quantityStr, pairStr;
        pairStr = getPairNameString(pair);
        priceStr = formatDoubleToBinanceString(pair, price);
        quantityStr = formatDoubleToBinanceString(pair, quantity);

        System.out.println("Creating buy order: " + pairStr + " | Price: " + priceStr + " | Quantity: " + quantityStr);

        NewOrderResponse newOrderResponse = client.newOrder(limitBuy(pairStr + "",TimeInForce.GTC, quantityStr + "" ,priceStr + ""));
        return newOrderResponse.getOrderId();
    }

    public String getPairNameString(Pair pair) {
        return pair.getCoin().getName() + pair.getMarket();
    }

    @Override
    public void cancelOrder(Pair pair,long orderId) {
        String pairStr = getPairNameString(pair);
        client.cancelOrder(new CancelOrderRequest(pairStr,orderId));
    }

    @Override
    public Double getLatestPrice(Pair pair) {
        String pairStr = getPairNameString(pair);
        String priceStr = client.get24HrPriceStatistics(pairStr).getLastPrice();
        return Double.valueOf(priceStr);
    }

    private int getPriceVolumeLength(Pair pair) {
        String pairStr = getPairNameString(pair);
        String priceStr = client.get24HrPriceStatistics(pairStr).getLastPrice().trim();
        return priceStr.split("\\.")[1].length();
    }

    @Override
    public Long sellLimitOrder(Pair pair,double price, double quantity) {
        String pairStr = getPairNameString(pair);
        String priceStr, quantityStr;
        priceStr = formatDoubleToBinanceString(pair, price);
        quantityStr = formatDoubleToBinanceString(pair, quantity);


        NewOrderResponse newOrderResponse = client.newOrder(limitSell(pairStr, TimeInForce.GTC, quantityStr, priceStr));

        System.out.println("Limit Sell order placed: " + pairStr + " | Price: " + priceStr  + " | Quantity: " + quantity);
        return newOrderResponse.getOrderId();
    }

    @Override
    public Long sellStopLimitOrder(Pair pair,double stop,double price,double quantity) {
        String priceStr, stopStr;
        priceStr = formatDoubleToBinanceString(pair, price);
        stopStr = formatDoubleToBinanceString(pair, stop);
        String pairStr = getPairNameString(pair);

        NewOrder stopLossOrder = new NewOrder(pairStr,OrderSide.SELL,OrderType.STOP_LOSS_LIMIT,TimeInForce.GTC,quantity + "",priceStr).stopPrice(stopStr);

        System.out.println("Stop Loss order placed: " + pairStr + " | Price: " + priceStr + " | Stop Price: " + stopStr + " | Quantity: " + quantity);
        return client.newOrder(stopLossOrder).getOrderId();
    }

    private String formatDoubleToBinanceString(Pair pair,Double value) {
        return String.format(Locale.US,"%." + coinPriceLength + "f", value);
    }

    @Override
    public Pair fetchPairInfo(String pair) {
        return null;
    }

    @Override
    public Map<String, Pair> fetchAllPairsInfo() {
        return null;
    }

    @Override
    public String getOrderStatus(Pair pair,Long orderId) {
        String pairStr = getPairNameString(pair);
        Order order = client.getOrderStatus(new OrderStatusRequest(pairStr,orderId));
        return order.getStatus().toString();
    }
}
