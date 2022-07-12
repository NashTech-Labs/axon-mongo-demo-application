package org.knoldus.axon.state.machine.dto;

import lombok.*;
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
