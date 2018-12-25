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

    public TradingConfig(double stopLossCoefficient,double takeProfitCoefficient,double takeProfitForStartCoefficient,double sellToStopCoefficient,double newTakeProfitCoefficient) {
        this.stopLossCoefficient = stopLossCoefficient;
        this.takeProfitCoefficient = takeProfitCoefficient;
        this.takeProfitForStartCoefficient = takeProfitForStartCoefficient;
        this.sellToStopCoefficient = sellToStopCoefficient;
        this.newTakeProfitCoefficient = newTakeProfitCoefficient;
    }

    public static TradingConfig getInstance(double stopLossCoefficient,double takeProfitCoefficient,double takeProfitStartCoefficient,double sellToStopCoefficient, double newTakeProfitPriceCoefficient){
        return new TradingConfig(stopLossCoefficient, takeProfitCoefficient, takeProfitStartCoefficient, sellToStopCoefficient, newTakeProfitPriceCoefficient);
    }
}
