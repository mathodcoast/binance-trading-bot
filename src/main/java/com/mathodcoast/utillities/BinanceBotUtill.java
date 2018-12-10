package com.mathodcoast.utillities;

import net.sealake.binance.api.client.BinanceApiClientFactory;
import net.sealake.binance.api.client.BinanceApiRestClient;
import net.sealake.binance.api.client.BinanceApiWebSocketClient;

public class BinanceBotUtill {

    private static final String API_KEY ="jqjm6mS2FNmJaOGkUjWwKk7FlaEMaY24rpa06c6Jw5gXYBQkpI5xEN2BUEWJgcMM";
    private static final String SECRET_API_KEY ="7jEaAcasA6j32kUx52ENQ7wlavXNrrdau6tIgroBjNBI98X4UouN02c8Zhcl0QMl";

    private static BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(API_KEY, SECRET_API_KEY);
    public static BinanceApiRestClient client = factory.newRestClient();
    public static BinanceApiWebSocketClient webSocketClient = factory.newWebSocketClient();


}
