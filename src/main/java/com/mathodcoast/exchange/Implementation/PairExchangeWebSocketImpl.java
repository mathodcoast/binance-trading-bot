package com.mathodcoast.exchange.Implementation;

import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.AggTradeEvent;
import com.mathodcoast.exchange.WebSocketDao;
import com.mathodcoast.model.Pair;
import com.mathodcoast.utillities.BinanceBotUtil;
import java.io.Closeable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalUnit;


public class PairExchangeWebSocketImpl implements WebSocketDao {
    private BinanceApiWebSocketClient webSocketClient;
    private Pair pair;
    public String eventType;

    public PairExchangeWebSocketImpl(Pair pair) {
        this.webSocketClient = BinanceBotUtil.getInstance().webSocketClient;
        this.pair = pair;
    }

    @Override
    public Closeable listenPairPriceAndApply(Runnable runnable) {
        return webSocketClient.onAggTradeEvent(pair.getStringPairName().toLowerCase(),new BinanceApiCallback<AggTradeEvent>() {
            @Override
            public void onResponse(AggTradeEvent aggTradeEvent) {
                pair.setPrice(Double.valueOf(aggTradeEvent.getPrice()));
                System.out.println(pair.getPrice());
                runnable.run();
                System.err.println("websocket body");
                System.out.println(aggTradeEvent.getEventType());
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
}
