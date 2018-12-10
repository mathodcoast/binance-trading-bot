package com.mathodcoast.service;

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
    private double takeProfitStartCoefficient;
    private double sellToStopCoefficient;
    private double newTakeProfitCoefficient;

    public TradingConfig(double stopLossCoefficient,double takeProfitCoefficient,double takeProfitStartCoefficient,double sellToStopCoefficient,double newTakeProfitCoefficient) {
        this.stopLossCoefficient = stopLossCoefficient;
        this.takeProfitCoefficient = takeProfitCoefficient;
        this.takeProfitStartCoefficient = takeProfitStartCoefficient;
        this.sellToStopCoefficient = sellToStopCoefficient;
        this.newTakeProfitCoefficient = newTakeProfitCoefficient;
    }

    public static TradingConfig getInstance(double stopLossCoefficient,double takeProfitCoefficient,double takeProfitStartCoefficient,double sellToStopCoefficient, double changeTakeProfitCoefficient){
        return new TradingConfig(stopLossCoefficient, takeProfitCoefficient, takeProfitStartCoefficient, sellToStopCoefficient, changeTakeProfitCoefficient);
    }
}
