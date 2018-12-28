package com.mathodcoast;

import com.mathodcoast.exchange.Implementation.PairExchangeWebSocketImpl;
import com.mathodcoast.exchange.WebSocketDao;
import com.mathodcoast.model.Coin;
import com.mathodcoast.model.Pair;
import com.mathodcoast.model.Pair.Market;
import com.mathodcoast.model.TradingConfig;
import com.mathodcoast.service.TradingOperationWS;
import com.mathodcoast.utillities.BinanceBotUtil;

public class App {
    //public static PairDao pairDao = new PairExchangeRepositoryImpl();
    public static double price;

    static void printInfo(String string) {
        System.out.println(string);
        price = Double.valueOf(string);

        System.out.println("Static price:" + price);
    }

    public static void main(String[] args) {
        BinanceBotUtil botUtil = BinanceBotUtil.getInstance();

        System.out.println(botUtil.getClient().getServerTime());
        System.out.println(System.currentTimeMillis());

        Coin tradingCoin = new Coin();
        tradingCoin.setName("WAVES");
        Pair testPair = new Pair();
        testPair.setMarket(Market.BTC);
        testPair.setCoin(tradingCoin);

        TradingConfig tradingConfig = TradingConfig.getInstance(
                0.01,
                0.2,
                0.003,
                0.0001,
                0.003,
                0.003
        );

        //TradingOperation testBtcTradingOperation = getNewTradingOperation(testPair,tradingConfig,0.02691,0.0012);

        //Thread thread = new Thread(testBtcTradingOperation);
        //thread.start();

        WebSocketDao webSocketDao = new PairExchangeWebSocketImpl(testPair);
       webSocketDao.listenPairPriceAndApply(App::testRunnableMethod);

//        String result = BinanceBotUtil.client.getExchangeInfo().getSymbolInfo("ETHBTC").getSymbolFilter(FilterType.LOT_SIZE).getStepSize();
//        System.out.println(result);

        TradingOperationWS tradingOperationWS = new TradingOperationWS(testPair,tradingConfig,0.00086,0.0012);
        Thread websocketTestThread = new Thread(tradingOperationWS);
        //websocketTestThread.start();



    }

    public static void testRunnableMethod(){
        System.out.println("Supplier Method Body");
    }


}
