package org.knoldus.axon.state.machine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Charge {

    private String id;
    private BigDecimal holdAmount;
    private BigDecimal availableAmount;
    private String flow;
    private String date;
    private String hotel;
    private boolean authorize;
}
