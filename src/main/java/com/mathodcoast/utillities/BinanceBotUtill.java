package com.mathodcoast.utillities;


import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.general.ExchangeInfo;

public class BinanceBotUtill {

    private static final String API_KEY ="";
    private static final String SECRET_API_KEY ="";

    private static BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(API_KEY, SECRET_API_KEY);
    public static BinanceApiRestClient client = factory.newRestClient();

   // private static BinanceApiClientFactory noApiFactory = BinanceApiClientFactory.newInstance();
   // public static BinanceApiRestClient noApiClient = noApiFactory.newRestClient();
    public static BinanceApiWebSocketClient webSocketClient = factory.newWebSocketClient();

   // public static ExchangeInfo exchangeInfo = noApiClient.getExchangeInfo();

    public static double increaseValueOnCoefficient(double value,double coefficient){
        return value + value * coefficient;
    }
}
