package com.mathodcoast.model;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Coin {
    private String name;
    private double balance;
    private double btcValueBalance;
}
