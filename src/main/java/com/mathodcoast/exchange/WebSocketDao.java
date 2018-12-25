package com.mathodcoast.exchange;

import java.io.Closeable;

public interface WebSocketDao {

    Closeable listenPairPriceAndApply(Runnable runnable);

    Closeable listeningAndCashingOfPairPrice();
}
