package com.mathodcoast.exchange.Implementation;

import com.binance.api.client.BinanceApiWebSocketClient;
import com.mathodcoast.exchange.WebSocketDao;
import com.mathodcoast.model.Pair;
import com.mathodcoast.utillities.BinanceBotUtill;
import java.io.Closeable;


public class PairExchangeWebSocketImpl implements WebSocketDao {
    private BinanceApiWebSocketClient webSocketClient;
    private Pair pair;

    public PairExchangeWebSocketImpl(Pair pair) {
        this.webSocketClient = BinanceBotUtill.getInstance().webSocketClient;
        this.pair = pair;
    }

    @Override
    public Closeable listenPairPriceAndApply(Runnable runnable) {
        return webSocketClient.onAggTradeEvent(pair.getStringPairName().toLowerCase(),aggTradeEvent -> {
            pair.setPrice(Double.valueOf(aggTradeEvent.getPrice()));
            System.out.println(pair.getPrice());
            runnable.run();
        });
    }

    @Override
    public Closeable listeningAndCashingOfPairPrice() {
        return webSocketClient.onAggTradeEvent(pair.getStringPairName().toLowerCase(),
                aggTradeEvent -> pair.setPrice(Double.valueOf(aggTradeEvent.getPrice())));
    }
}
