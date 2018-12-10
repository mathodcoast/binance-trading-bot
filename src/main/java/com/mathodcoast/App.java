package com.mathodcoast;

import com.mathodcoast.dao.PairDao;
import com.mathodcoast.dao.PairDaoImpl;
import com.mathodcoast.model.Coin;
import com.mathodcoast.model.Pair;
import com.mathodcoast.model.Pair.Market;
import com.mathodcoast.service.TradingConfig;
import com.mathodcoast.service.TradingOperation;
import com.mathodcoast.utillities.BinanceBotUtill;
import net.sealake.binance.api.client.BinanceApiClientFactory;
import net.sealake.binance.api.client.BinanceApiWebSocketClient;
import net.sealake.binance.api.client.domain.account.Account;
import net.sealake.binance.api.client.domain.account.AssetBalance;
import net.sealake.binance.api.client.domain.event.AggTradeEvent;
import net.sealake.binance.api.client.domain.event.DepthEvent;
import net.sealake.binance.api.client.domain.market.TickerStatistics;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import static com.mathodcoast.service.TradingOperation.getNewTradingOperation;

public class App {
    //public static PairDao pairDao = new PairDaoImpl();
    public static double price;

    static void printInfo(String string){
        System.out.println(string);
        price = Double.valueOf(string);

        System.out.println("Static price:" + price);
    }

    public static void main(String[] args) {

        System.out.println(BinanceBotUtill.client.getServerTime());
        System.out.println(System.currentTimeMillis());

//        Account account = BinanceBotUtill.client.getAccount(6000000L,System.currentTimeMillis());
//        List<AssetBalance> balances = account.getBalances();
//        balances.forEach(System.out::println);
//
//        List<TickerStatistics> tickerStatistics = BinanceBotUtill.client.getAll24HrPriceStatistics();
//        tickerStatistics.forEach(System.out::println);
        //BinanceApiWebSocketClient webSocketClient = BinanceApiClientFactory.newInstance().newWebSocketClient();

//        Closeable ws = webSocketClient.onAggTradeEvent("ethbtc", (AggTradeEvent response) -> {
//            for (int i = 0; i < 100; i++) {
//                System.out.println(response.getPrice());
//                System.out.println(response.getQuantity());
//                System.out.println("---------------");
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        try {
//            ws.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Closeable ws = webSocketClient.onAggTradeEvent("ethbtc",response -> {
//
//            printInfo(response.getPrice());
//
//            if (Double.valueOf(response.getPrice()) > 0.028320) {
//                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                webSocketClient.close();
//            }
//        });
//
//
//
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            ws.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("After Web Socket");
//        System.out.println(Thread.currentThread());

        Coin tradingCoin = new Coin();
        tradingCoin.setName("OST");

        Pair testPair = new Pair();
        testPair.setMarket(Market.BTC);
        testPair.setCoin(tradingCoin);

        TradingConfig tradingConfig = TradingConfig.getInstance(
                0.01,
                0.1,
                0.005,
                0.0005,
                0.002
        );

        TradingOperation testBtcTradingOperation = getNewTradingOperation(testPair, tradingConfig, 0.00000681, 0.0011);

        Thread thread = new Thread(testBtcTradingOperation);
        thread.start();

        //pairDao.sellStopLimitOrder(testPair,0.00766123, 0.00766123,258);

//        String test = "0.00006580";
//        System.out.println(test.trim().split("\\.")[1].length());
//
//        String string = BinanceBotUtill.client.get24HrPriceStatistics("BNBBTC").getOpenPrice();
//        System.out.println(string);
    }
}
