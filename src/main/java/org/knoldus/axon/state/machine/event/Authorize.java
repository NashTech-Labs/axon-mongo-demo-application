package org.knoldus.axon.state.machine.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.knoldus.axon.state.machine.dto.Charge;

public class Authorize extends EventBase<String> {

    private final Charge charge;
    @JsonCreator
    public Authorize(@JsonProperty("id") String id,
                     @JsonProperty("charge") Charge charge) {
        super(id);
        this.charge = charge;
    }

    public Charge getCharge() {
        return charge;
    }
}
