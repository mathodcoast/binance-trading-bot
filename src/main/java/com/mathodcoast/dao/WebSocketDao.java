package com.mathodcoast.dao;

import com.mathodcoast.model.Pair;
import com.mathodcoast.service.TradingOperation;

public interface WebSocketDao {
    void createStopLossIfOrderFilled(Long orderId);

    void performStopLossAndTakeProfitStrategy(TradingOperation tradingOperation);
}
