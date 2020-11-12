package org.sid.web;

import java.time.Instant;

class Event {
    private Instant instant;
    private Double value;
    private String societeID;

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getSocieteID() {
        return societeID;
    }

    public void setSocieteID(String societeID) {
        this.societeID = societeID;
    }
}
