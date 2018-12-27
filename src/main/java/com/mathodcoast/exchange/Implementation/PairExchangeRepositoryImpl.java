package com.mathodcoast.exchange.Implementation;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.OrderSide;
import com.binance.api.client.domain.OrderType;
import com.binance.api.client.domain.TimeInForce;
import com.binance.api.client.domain.account.NewOrder;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.account.Order;
import com.binance.api.client.domain.account.request.CancelOrderRequest;
import com.binance.api.client.domain.account.request.OrderStatusRequest;
import com.binance.api.client.domain.general.FilterType;
import com.mathodcoast.exchange.PairDao;
import com.mathodcoast.model.Pair;
import com.mathodcoast.utillities.BinanceBotUtil;
import lombok.Getter;


import java.util.Locale;
import java.util.Map;

import static com.binance.api.client.domain.account.NewOrder.limitBuy;
import static com.binance.api.client.domain.account.NewOrder.limitSell;


@Getter
public class PairExchangeRepositoryImpl implements PairDao {
    private BinanceApiRestClient client;
    private Pair pair;
    private String pairName;
    private int pairPriceLength;
    private int pairQuantityLotLength;

    public PairExchangeRepositoryImpl(Pair pair) {
        client = BinanceBotUtil.getInstance().client;
        this.pair = pair;
        this.pairName = pair.getStringPairName();
        pairQuantityLotLength = setupPairQuantityLotLength();
        pairPriceLength = setupPairPriceLength();
    }

    private int setupPairQuantityLotLength() {
        return getSizeOfNumberAfterPoint(getPairStepSize());
    }

    private String getPairStepSize() {
        return client.getExchangeInfo().getSymbolInfo(pair.getStringPairName()).getSymbolFilter(FilterType.LOT_SIZE).getStepSize();
    }

    private int setupPairPriceLength() {
        String tickSize = getPairTickSize();
        return getSizeOfNumberAfterPoint(tickSize);
    }

    private int getSizeOfNumberAfterPoint(String tickSize) {
        String afterDotString = tickSize.split("\\.")[1];
        int pairPriceLenght = 8;
        for (int i = afterDotString.length() - 1; i > 0; i--) {
            if (afterDotString.toCharArray()[i] == '0') {
                pairPriceLenght = i;
            } else break;
        }
        return pairPriceLenght;
    }

    private String getPairTickSize() {
        return client.getExchangeInfo().getSymbolInfo(pairName).getSymbolFilter(FilterType.PRICE_FILTER).getTickSize();
    }

    @Override
    public Long buyLimitOrder(double price,double quantity) {
        String priceStr, quantityStr;
        priceStr = formatDoubleToBinanceString(price);
        quantityStr = formatDoubleToBinanceStringForQuantity(quantity);

        System.out.println("Creating buy order: " + pairName + " | Price: " + priceStr + " | Quantity: " + quantityStr);

        NewOrderResponse newOrderResponse = client.newOrder(limitBuy(pairName + "",TimeInForce.GTC,quantityStr + "",priceStr + ""));
        return newOrderResponse.getOrderId();
    }

    @Override
    public void cancelOrder(long orderId) {
        client.cancelOrder(new CancelOrderRequest(pairName,orderId));
    }

    @Override
    public Double getLatestPrice() {
        String priceStr = client.get24HrPriceStatistics(pairName).getLastPrice();
        return Double.valueOf(priceStr);
    }

    @Override
    public Long sellLimitOrder(double price,double quantity) {

        String priceStr, quantityStr;
        priceStr = formatDoubleToBinanceString(price);
        quantityStr = formatDoubleToBinanceStringForQuantity(quantity);

        NewOrderResponse newOrderResponse = client.newOrder(limitSell(pairName,TimeInForce.GTC,quantityStr,priceStr));

        System.out.println("Limit Sell order placed: " + pairName + " | Price: " + priceStr + " | Quantity: " + quantity);
        return newOrderResponse.getOrderId();
    }

    @Override
    public Long sellStopLimitOrder(double stop,double price,double quantity) {
        String priceStr, stopStr, quantityStr;
        priceStr = formatDoubleToBinanceString(price);
        stopStr = formatDoubleToBinanceString(stop);
        quantityStr = formatDoubleToBinanceStringForQuantity(quantity);

        NewOrder stopLossOrder = new NewOrder(pairName,OrderSide.SELL,OrderType.STOP_LOSS_LIMIT,TimeInForce.GTC,quantityStr + "",priceStr).stopPrice(stopStr);

        System.out.println("Stop Loss order placed: " + pairName + " | Price: " + priceStr + " | Stop Price: " + stopStr + " | Quantity: " + quantityStr);
        return client.newOrder(stopLossOrder).getOrderId();
    }

    @Override
    public Pair fetchPairInfo() {
        return null;
    }

    @Override
    public Map<String, Pair> fetchAllPairsInfo() {
        return null;
    }

    @Override
    public String getOrderStatus(Long orderId) {
        Order order = client.getOrderStatus(new OrderStatusRequest(pairName,orderId));
        return order.getStatus().toString();
    }

    private String formatDoubleToBinanceString(Double value) {
        return String.format(Locale.US,"%." + pairPriceLength + "f",value);
    }

    private String formatDoubleToBinanceStringForQuantity(Double value) {
        return String.format(Locale.US,"%." + pairQuantityLotLength + "f",value);
    }
}
