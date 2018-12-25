package com.mathodcoast.service;

import com.binance.api.client.exception.BinanceApiException;
import com.mathodcoast.exchange.Implementation.PairExchangeRepositoryImpl;
import com.mathodcoast.exchange.Implementation.PairExchangeWebSocketImpl;
import com.mathodcoast.exchange.PairDao;
import com.mathodcoast.exchange.WebSocketDao;
import com.mathodcoast.model.Pair;
import com.mathodcoast.model.TradingConfig;
import com.mathodcoast.utillities.BinanceBotUtill;

import java.io.Closeable;
import java.io.IOException;
import java.util.Locale;
import java.util.function.Supplier;


public class TradingOperationWS implements Runnable{
    private BinanceBotUtill botUtill;
    private PairDao pairDao;
    private WebSocketDao webSocketDao;
    private Pair pair;
    private TradingConfig tradingConfig;
    private Closeable webSocket;

    private double buyPrice;
    private double marketCoinQuantity;
    private double coinTradeQuantity;
    private double stopLossPrice;
    private double sellPrice;
    private double takeProfitPrice;
    private double newTakeProfitPrice;

    private Long orderId;
    private String orderStatus;

    private int webSocketTestCounter = 0;

    public TradingOperationWS(Pair pair,TradingConfig tradingConfig,double buyPrice,double marketCoinQuantity) {
        botUtill = BinanceBotUtill.getInstance();
        this.pair = pair;
        this.buyPrice = buyPrice;
        this.marketCoinQuantity = marketCoinQuantity;
        this.tradingConfig = tradingConfig;
        this.pairDao = new PairExchangeRepositoryImpl(pair);
        this.webSocketDao = new PairExchangeWebSocketImpl(pair);
    }

    @Override
    public void run(){
        calculateCoinTradeQuantity();
        orderId = pairDao.buyLimitOrder(buyPrice, coinTradeQuantity);
        orderStatus = pairDao.getOrderStatus(orderId);
        printOrderStatus();

        orderStatusCheckingCycle();
        if (orderStatus.equals("FILLED")){
            webSocket = webSocketDao.listeningAndCashingOfPairPrice();

            takeProfitPrice = botUtill.increaseValueOnCoefficient(buyPrice, tradingConfig.getTakeProfitForStartCoefficient());
            calculateStopLossPrice();

            createStopLimitOrder(stopLossPrice);

            while(!orderStatus.equals("FILLED")){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                newTakeProfitPrice = botUtill.increaseValueOnCoefficient(takeProfitPrice, tradingConfig.getNewTakeProfitCoefficient());
                if(pair.getPrice() > newTakeProfitPrice){
                    pairDao.cancelOrder(orderId);

                    createStopLimitOrder(takeProfitPrice);

                    takeProfitPrice = newTakeProfitPrice;
                }
                orderStatus = pairDao.getOrderStatus(orderId);


            }
            printOrderStatus();
        }
        closeWebSocked();
    }

    private void orderStatusCheckingCycle() {
        while(!orderStatus.equals("FILLED") & !orderStatus.equals("CANCELED")){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try{
                orderStatus = pairDao.getOrderStatus(orderId);
            }
            catch (BinanceApiException e){
                System.out.println("Get Order Status error");
                System.out.println(e.getError().getCode());
                System.out.println(e.getError().getMsg());
            }
        }
        printOrderStatus();
    }

    private void createStopLimitOrder(double stopLossPrice) {
        calculateSellPrice(stopLossPrice);
        orderId = pairDao.sellStopLimitOrder(stopLossPrice,sellPrice,coinTradeQuantity);
        orderStatus = pairDao.getOrderStatus(orderId);
        printOrderStatus();
    }

    private void calculateSellPrice(double stopLimitPrice) {
        sellPrice = stopLimitPrice - stopLimitPrice * tradingConfig.getSellToStopCoefficient();
    }

    private void closeWebSocked() {
        try {
            webSocket.close();
            System.out.println("Web Socked closed.");
        } catch (IOException | NullPointerException e) {
            System.out.println("Web socked closing error");
            e.printStackTrace();
        }
    }

    private void printOrderStatus() {
        System.out.println(String
                .format(Locale.US,"Order status: %s",orderStatus));
    }


    private void webSocketClosingCondition(Supplier<Boolean> supplier){
        if (supplier.get()) {
            try {
                webSocket.close();
                System.out.println("Web Socked closed.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void calculateStopLossPrice() {
        stopLossPrice = buyPrice - buyPrice * tradingConfig.getStopLossCoefficient();
        //System.out.println(String.format(Locale.US,"Stop Loss price: %.8f", stopLossPrice));
    }

    private void calculateCoinTradeQuantity(){
        System.out.println(String.format(Locale.US,"Quantity for buying in BTC: %f", marketCoinQuantity));
        coinTradeQuantity = marketCoinQuantity / buyPrice;
    }
}
