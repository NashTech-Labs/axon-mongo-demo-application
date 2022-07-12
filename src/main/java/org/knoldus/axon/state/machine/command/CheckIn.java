package org.knoldus.axon.state.machine.command;

import org.knoldus.axon.state.machine.dto.Charge;

public class CheckIn extends CommandBase<String> {

    private final Charge charge;

    public CheckIn(String id, Charge charge) {
        super(id);
        this.charge = charge;
    }

    public Charge getCharge() {
        return charge;
    }
}
