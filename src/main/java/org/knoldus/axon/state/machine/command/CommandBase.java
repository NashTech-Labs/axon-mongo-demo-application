package org.knoldus.axon.state.machine.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class CommandBase<T> {

    @TargetAggregateIdentifier
    private final T id;

    public CommandBase(T id) {
        this.id = id;
    }

    public T getId() {
        return id;
    }
}
