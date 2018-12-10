package com.mathodcoast.dao;

import com.mathodcoast.service.TradingOperation;
import com.mathodcoast.utillities.BinanceBotUtill;
import net.sealake.binance.api.client.BinanceApiWebSocketClient;

public class WebSocketDaoImpl implements WebSocketDao {
    private BinanceApiWebSocketClient webSocketClient;
    private PairDao pairDao;

    public WebSocketDaoImpl(PairDao pairDao) {
        this.webSocketClient = BinanceBotUtill.webSocketClient;
        this.pairDao = pairDao;
    }

    @Override
    public void createStopLossIfOrderFilled(Long orderId) {
        webSocketClient.onUserDataUpdateEvent("",response -> {

           response.getOrderTradeUpdateEvent().getOrderStatus();
        });
    }

    @Override
    public void performStopLossAndTakeProfitStrategy(TradingOperation tO) {
        String pairStr = pairDao.getPairNameString(tO.getPair());
//        long stopLossId = pairDao.sellStopLimitOrder(tO.getPair(), tO.getStopLossPrice(), tO.getStopLossPrice(), tO.getCoinBuyQuantity());
//        System.out.println(pairDao.getOrderStatus(tO.getPair(),stopLossId));
        System.out.println("Web socket function: " + pairStr);

        webSocketClient.onAggTradeEvent(pairStr.toLowerCase(), response -> {
            System.out.println(pairStr + " Actual price: " + response.getPrice());
//            double actualPrice = Long.valueOf(response.getPrice());
//
//            tO.setPriceChangeCoef(((actualPrice - tO.getBuyPrice()) / actualPrice) * 100);
//            tO.setTakeProfitCoef(tO.getPriceChangeCoef() / 5);
//            if(actualPrice > tO.getMaxTradePrice()){
//                tO.setMaxTradePrice(actualPrice);
//            }



//            if(actualPrice > tO.getBuyPrice() + tO.getBuyPrice() * 0.005){
//                String stopLossOrderStatus = pairDao.getOrderStatus(tO.getPair(),stopLossId);
//                if(stopLossOrderStatus.equals("NEW")) {
//                    pairDao.cancelOrder(tO.getPair(),stopLossId);
//                    System.out.println(stopLossOrderStatus);
//                }
//
//                Long stopLimitId;
//                stopLimitId = pairDao.sellStopLimitOrder(tO.getPair(),tO.getStopLossPrice(),tO.getStopLossPrice(),tO.getCoinBuyQuantity());
//            }
//            System.out.println(actualPrice);
        });
    }
}
