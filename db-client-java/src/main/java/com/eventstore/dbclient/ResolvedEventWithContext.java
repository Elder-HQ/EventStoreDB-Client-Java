package com.eventstore.dbclient;

public class ResolvedEventWithContext {
    private final long firstStreamPosition;
    private final long lastStreamPosition;
    private final Position lastAllStreamPosition;
    private final ResolvedEvent event;

    public ResolvedEventWithContext(long firstStreamPosition, long lastStreamPosition, Position lastAllStreamPosition, ResolvedEvent event) {
        this.firstStreamPosition = firstStreamPosition;
        this.lastStreamPosition = lastStreamPosition;
        this.lastAllStreamPosition = lastAllStreamPosition;
        this.event = event;
    }

    public long getFirstStreamPosition() {
        return firstStreamPosition;
    }

    public long getLastStreamPosition() {
        return lastStreamPosition;
    }

    public Position getLastAllStreamPosition() {
        return lastAllStreamPosition;
    }

    public ResolvedEvent getEvent() {
        return event;
    }
}
