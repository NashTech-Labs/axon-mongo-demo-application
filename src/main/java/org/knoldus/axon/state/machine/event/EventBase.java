package org.knoldus.axon.state.machine.event;

public class EventBase<T> {

    private final T id;

    public EventBase(T id) {
        this.id = id;
    }

    public T getId() {
        return id;
    }
}
