package com.mathodcoast.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@EqualsAndHashCode
public class TradingConfig {
    private double stopLossCoefficient;
    private double takeProfitCoefficient;
    private double takeProfitForStartCoefficient;
    private double sellToStopCoefficient;
    private double newTakeProfitCoefficient;
    private double noLossActivateCoefficient;
    private final double MARKET_FEE_COEFFICIENT = 0.0015;

    public TradingConfig(double stopLossCoefficient,double takeProfitCoefficient,double takeProfitForStartCoefficient,
                         double sellToStopCoefficient,double newTakeProfitCoefficient,double noLossActivateCoefficient) {
        this.stopLossCoefficient = stopLossCoefficient;
        this.takeProfitCoefficient = takeProfitCoefficient;
        this.takeProfitForStartCoefficient = takeProfitForStartCoefficient;
        this.sellToStopCoefficient = sellToStopCoefficient;
        this.newTakeProfitCoefficient = newTakeProfitCoefficient;
        this.noLossActivateCoefficient = noLossActivateCoefficient;
    }

    public static TradingConfig getInstance(double stopLossCoefficient,double takeProfitCoefficient,
                                            double takeProfitStartCoefficient,double sellToStopCoefficient
                                            ,double newTakeProfitPriceCoefficient,double noLossActivateCoefficient){
        return new TradingConfig(stopLossCoefficient, takeProfitCoefficient, takeProfitStartCoefficient,
                sellToStopCoefficient, newTakeProfitPriceCoefficient,noLossActivateCoefficient);
    }
}
