package com.mathodcoast.exchange.Implementation;

import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.AggTradeEvent;
import com.mathodcoast.exchange.WebSocketDao;
import com.mathodcoast.model.Pair;
import com.mathodcoast.utillities.BinanceBotUtil;
import java.io.Closeable;

public class PairExchangeWebSocketImpl implements WebSocketDao {
    private BinanceBotUtil botUtil;
    private BinanceApiWebSocketClient webSocketClient;
    private Pair pair;

    public PairExchangeWebSocketImpl(Pair pair) {
        this.botUtil = BinanceBotUtil.getInstance();
        this.webSocketClient = botUtil.getWebSocketClient();
        this.pair = pair;
    }

    @Override
    public Closeable listenPairPriceAndApply(Runnable runnable) {
        return webSocketClient.onAggTradeEvent(pair.getStringPairName().toLowerCase(),new BinanceApiCallback<AggTradeEvent>() {
            @Override
            public void onResponse(AggTradeEvent aggTradeEvent) {
                pair.setPrice(Double.valueOf(aggTradeEvent.getPrice()));
                runnable.run();
            }

            @Override
            public void onFailure(Throwable cause) {
                System.err.println("Web socket failed (TEST)");
                cause.printStackTrace(System.err);
            }
        });
    }

    @Override
    public Closeable listeningAndCashingOfPairPrice() {
        return webSocketClient.onAggTradeEvent(pair.getStringPairName().toLowerCase(),
                aggTradeEvent -> {
            pair.setPrice(Double.valueOf(aggTradeEvent.getPrice()));
            pair.setEventType(aggTradeEvent.getEventType());
                });
    }

    @Override
    public Closeable listenUserAccountChanges() {
        return webSocketClient.onUserDataUpdateEvent(botUtil.getListenKey(), userDataUpdateEvent -> {
            System.out.println(userDataUpdateEvent.getOrderTradeUpdateEvent());
        });
    }
}
