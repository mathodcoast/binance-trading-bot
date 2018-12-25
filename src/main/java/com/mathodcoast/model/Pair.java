package com.mathodcoast.model;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode

public class Pair {
    private Coin coin;
    private Market market;
    private double bid;
    private double ask;
    private double price;

    public String getStringPairName(){
        return coin.getName() + market.getName();
    }

    @Getter
    public enum Market{
        USDT("USDT"), ETH("ETH"), BTC("BTC"), BNB("BNB");
        private String name;

        Market(String name) {
            this.name = name;
        }
    }
}
