package com.mathodcoast.service;


import com.mathodcoast.dao.PairDao;
import com.mathodcoast.dao.PairDaoImpl;
import com.mathodcoast.model.Pair;
import lombok.*;

import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TradingOperation implements Runnable{
    private PairDao pairDao;
   // private WebSocketDao webSocketDao;
    private Pair pair;
    private TradingConfig tradingConfig;
    private double buyPrice;
    private double marketCoinQuantity;
    private double coinBuyQuantity;
    private double stopLossPrice;
    private double sellPrice;
    private double takeProfitPrice;
    private double priceDifference;
    private double maxTradePrice;
    private double latestPrice;
    private double newTakeProfitPrice;

    private TradingOperation(Pair pair,TradingConfig tradingConfig,double buyPrice,double marketCoinQuantity) {
        this.pair = pair;
        this.buyPrice = buyPrice;
        this.marketCoinQuantity = marketCoinQuantity;
        this.pairDao = new PairDaoImpl(pair);
        this.tradingConfig = tradingConfig;
    }

    private double calculateStopLossPrice() {
        double stopLoss = buyPrice - buyPrice * tradingConfig.getStopLossCoefficient();
        System.out.println(String.format("Stop Loss price: %.8f", stopLoss));
        return stopLoss;
    }

    public static TradingOperation getNewTradingOperation(Pair pair,TradingConfig tradingConfig,double buyPrice,double marketCoinQuantity){
        return new TradingOperation(pair,tradingConfig,buyPrice,marketCoinQuantity);
    }

    public void run() {
        coinBuyQuantity = Math.round(marketCoinQuantity / buyPrice);

        Long buyOrderId = pairDao.buyLimitOrder(pair, buyPrice, coinBuyQuantity);
        String buyOrderStatus = pairDao.getOrderStatus(pair, buyOrderId);
        stopLossPrice = calculateStopLossPrice();
        takeProfitPrice = buyPrice + buyPrice * tradingConfig.getTakeProfitStartCoefficient();
        maxTradePrice = buyPrice;

        System.out.println(String
                .format(Locale.US,"Buy order status: %s | Stop Loss after Filling: %.8f | Take Profit starts at: %.8f"
                        ,buyOrderStatus , stopLossPrice , takeProfitPrice));

        pairDao.getLatestPrice(pair);
        //webSocketDao.performStopLossAndTakeProfitStrategy(this); // Test

        boolean isBuyOrderFilled = false;
        while(!isBuyOrderFilled){

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

             buyOrderStatus = pairDao.getOrderStatus(pair,buyOrderId);

            if(buyOrderStatus.equals("FILLED")){
                isBuyOrderFilled = true;
                System.out.println( "Buy order status: " + buyOrderStatus);

                sellPrice = stopLossPrice - stopLossPrice * tradingConfig.getSellToStopCoefficient();
                Long sellStopLimitOrder = pairDao.sellStopLimitOrder(pair,stopLossPrice,sellPrice,coinBuyQuantity);

                String stopOrderStatus = "";
                while(!stopOrderStatus.equals("FILLED")) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Increase Take Profit
                    latestPrice = pairDao.getLatestPrice(pair);
                    newTakeProfitPrice = maxTradePrice + maxTradePrice * tradingConfig.getNewTakeProfitCoefficient();
                    if (newTakeProfitPrice < latestPrice) {
                        maxTradePrice = latestPrice;

                        priceDifference = maxTradePrice - buyPrice;
                        takeProfitPrice = maxTradePrice - priceDifference * tradingConfig.getTakeProfitCoefficient();
                        sellPrice = takeProfitPrice - takeProfitPrice * tradingConfig.getSellToStopCoefficient();
                        System.out.println(String.format("Highest prise: %.8f", maxTradePrice));

                        pairDao.cancelOrder(pair,sellStopLimitOrder);
                        sellStopLimitOrder = pairDao.sellStopLimitOrder(pair,takeProfitPrice,sellPrice,coinBuyQuantity);
                    }
                    stopOrderStatus = pairDao.getOrderStatus(pair, sellStopLimitOrder);


                    if((stopOrderStatus.equals("NEW") || stopOrderStatus.equals("PARTIALLY_FILLED")) && latestPrice < takeProfitPrice){
                        System.out.println("IMMEDIATE SALE. Order status: " + stopOrderStatus);
                        pairDao.cancelOrder(pair,sellStopLimitOrder);
                        sellStopLimitOrder = pairDao.sellLimitOrder(pair, latestPrice, coinBuyQuantity);
                    }
                }
                System.out.println("Order status: " + stopOrderStatus);

            } else if (buyOrderStatus.equals("CANCELED")){
                isBuyOrderFilled = true;
                System.out.println( "Buy order status: " + buyOrderStatus);
            }
        }
    }
}
